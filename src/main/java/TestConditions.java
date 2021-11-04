import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestConditions {

    public static void main(String[] args) throws IOException {
        List<String> attributelist = new ArrayList<>();
        List<String> danishList = new ArrayList();
        try {
            FhirContext ctx = FhirContext.forR4();
            String serverBase = "http://hapi.fhir.org/baseR4";
            IGenericClient client = ctx.newRestfulGenericClient(serverBase);

            //csv file containing data
//                        String strFile = filename;
//                        CSVReader reader = new CSVReader(new FileReader(strFile));
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            BufferedReader br = Files.newBufferedReader(Paths.get("C:/Users/kirst/Projects/TestdataFKI/hjemmesygeplejesct.csv"),  StandardCharsets.UTF_8);
            CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build();
            String [] nextLine;
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {

                Condition condition = new Condition();
                // ..populate the Condition object..
               CodeableConcept c = new CodeableConcept().setText("Tryksår i stadium 2, siddende på venstre lår. 4cmx5cm, dybde 3mm, ingen infektion");
               List<Coding> cList = new ArrayList<Coding>();
               cList.add(new Coding().setCode("I4.4").setDisplay("Problemer med tryksår").setSystem("http://kl.dk/fhir/common/caresocial/CodeSystem/FSIII"));
               cList.add(new Coding().setCode("420324007").setDisplay("Pressure ulcer stage 2").setSystem("http://snomed.info/sct"));
               c.setCoding(cList);
               condition.setCode(c);
               condition.setSubject(new Reference().setReference("Patient/1932669"));
               condition.setRecordedDate(new Date());

                CodeableConcept c1 = new CodeableConcept();
               List<Coding> cList1 = new ArrayList<Coding>();
                cList1.add(new Coding().setCode("active").setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical"));
                c1.setCoding(cList1);
               condition.setClinicalStatus(c1);
                /* patient.addIdentifier().setSystem("urn:oid:1.2.208.176.1.2").setValue(nextLine[0]).setUse(Identifier.IdentifierUse.OFFICIAL);
                String patientFirstName = nextLine[4];
                patient.addName().setFamily(nextLine[3]).setUse(HumanName.NameUse.OFFICIAL);
                String str[] = patientFirstName.split(" ");
                for (int i = 0; i<str.length;i++){
                    patient.getNameFirstRep().addGiven(str[i]);
                }
                if(nextLine[2].equals("M")){
                    patient.setGender(Enumerations.AdministrativeGender.MALE);

                }
                if(nextLine[2].equals("K")){
                    patient.setGender(Enumerations.AdministrativeGender.FEMALE);

                }



                System.out.println(patient.getIdentifierFirstRep().getValue());
                System.out.println(patient.getNameFirstRep().getNameAsSingleString());
                System.out.println(patient.getGender().getDisplay());
*/
                MethodOutcome outcome = client.create()
                        .resource(condition)
                        .prettyPrint()
                        .encodedJson()
                        .execute();

                IIdType id = outcome.getId();
                System.out.println("Got ID: " + id.getValue());


            }


        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (
                CsvValidationException e) {
            e.printStackTrace();
        }
    }
}


