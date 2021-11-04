import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


    public class TestPatients {

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
                BufferedReader br = Files.newBufferedReader(Paths.get("C:/Users/kirst/Projects/TestdataFKI/TestPatienterShort.csv"),  StandardCharsets.UTF_8);
                CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build();
                String [] nextLine;
                int lineNumber = 0;
                while ((nextLine = reader.readNext()) != null) {

                    Patient patient = new Patient();
                    // ..populate the patient object..
                    patient.addIdentifier().setSystem("urn:oid:1.2.208.176.1.2").setValue(nextLine[0]).setUse(Identifier.IdentifierUse.OFFICIAL);
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

                    MethodOutcome outcome = client.create()
                            .resource(patient)
                            .prettyPrint()
                            .encodedJson()
                            .execute();

                    IIdType id = outcome.getId();
                    System.out.println("Got ID: " + id.toUnqualifiedVersionless().getValueAsString());



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


