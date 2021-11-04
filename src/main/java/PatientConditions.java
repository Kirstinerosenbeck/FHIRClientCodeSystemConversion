import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PatientConditions {


    public static FhirContext ctx = FhirContext.forR4();

        public static void main(String[] args) throws IOException {
            //String serverBase = "https://fhir.medcom.dk/fhir";
            //String serverBase = "http://hapi.fhir.org/baseR4";
            //FhirContext ctx = FhirContext.forR4();

           //IGenericClient client = ctx.newRestfulGenericClient(serverBase);

//Upload et sæt af patienter til server
           //ArrayList<String> patientList = uploadAndGetReferenceToPatients("C:/Users/kirst/Projects/TestdataFKI/TestPatienter.csv", client);

           // Giv patienterne tilstande, og gem på disk
           //ArrayList<String> patientList = simplePatientList(1948498,1948531);
           //GivepatientsConditions(patientList);


            //Upload tilstande til serveren
            //uploadConditionsToServer("C:/Users/kirst/Projects/TestdataFKI/Tilstande/");


            //conditionChangeTest();

// søg efter kognitionsproblemer

        //    searchforproblems();
//simplesearch();


        }





    private static void uploadConditionsToServer(String folder) {
        File dir = new File(folder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String serverBase = "http://hapi.fhir.org/baseR4";
                FhirContext ctx = FhirContext.forR4();
                IParser parser = ctx.newJsonParser();
                IGenericClient client = ctx.newRestfulGenericClient(serverBase);
                String conString = getJsonObject(child.getAbsolutePath());
                Condition con = parser.parseResource(Condition.class, conString);
                MethodOutcome outcome = client.create()
                        .resource(con)
                        .prettyPrint()
                        .encodedJson()
                        .execute();


            }
        }
    }

    private static void GivepatientsConditions(ArrayList<String> patientList) {
        ArrayList<CodeableConcept> conditionList = generateRandomConditions(1,"C:/Users/kirst/Projects/TestdataFKI/traeningSCT.csv");
        saveConditionsToFile(conditionList, patientList, "t");
        ArrayList<CodeableConcept> conditionList1 = generateRandomConditions(1,"C:/Users/kirst/Projects/TestdataFKI/hjemmeplejesct.csv");
        saveConditionsToFile(conditionList1, patientList, "homecare");
        ArrayList<CodeableConcept> conditionList2 = generateRandomConditions(1,"C:/Users/kirst/Projects/TestdataFKI/hjemmesygeplejesct.csv");
        saveConditionsToFile(conditionList2, patientList, "t");
        ArrayList<CodeableConcept> conditionList3 = generateRandomConditions(1,"C:/Users/kirst/Projects/TestdataFKI/p119SCT.csv");
        saveConditionsToFile(conditionList3, patientList, "t");
        ArrayList<CodeableConcept> conditionList4 = generateRandomConditions(1,"C:/Users/kirst/Projects/TestdataFKI/hjemmeplejesct.csv");
        saveConditionsToFile(conditionList4, patientList, "homecare");
        ArrayList<CodeableConcept> conditionList5 = generateRandomConditions(1,"C:/Users/kirst/Projects/TestdataFKI/hjemmesygeplejesct.csv");
        saveConditionsToFile(conditionList5, patientList, "t");


        }


    private static void conditionChangeTest() {
       String serverBase = "http://hapi.fhir.org/baseR4";
        FhirContext ctx = FhirContext.forR4();
        //String serverBase = "http://hapi.fhir.org/baseR4";
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        ArrayList<String> patientList = uploadAndGetReferenceToPatients("C:/Users/kirst/Projects/TestdataFKI/ConditionChange/TestPatienterEinar.csv", client);
        ArrayList<String> practitionerList = uploadAndGetReferenceToPractitioners(client);
        String resourceDir="C:/Users/kirst/Projects/TestdataFKI/ConditionChange/";
        conditionChange(resourceDir, patientList,practitionerList,client);



    }

    private static void conditionChange(String resourceDir, ArrayList<String> patientList, ArrayList<String> practitionerList, IGenericClient client) {

       Reference patientRef = new Reference().setReference(patientList.get(0));
       //Reference pracRef = new Reference().setReference(practitionerList.get(0));

        //Læg området på serveren
        String Obs1234 = getJsonObject(resourceDir+"/resources/Observation-Observation1234.json");
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        Observation obs = parser.parseResource(Observation.class, Obs1234);
        obs.setSubject(patientRef);
        obs.getPerformerFirstRep().setReference(practitionerList.get(0));
        //System.out.println(parser.encodeResourceToString(obs));
        MethodOutcome outcome = client.create()
                .resource(obs)
                .prettyPrint()
                .encodedJson()
                .execute();

        String Obs1234Reference = outcome.getId().toUnqualifiedVersionless().getValue();

        //Læg årsagen på serveren
        String conString444 = getJsonObject(resourceDir+"/resources/Condition-Condition444.json");
        Condition con444 = parser.parseResource(Condition.class, conString444);
        con444.setSubject(patientRef);
        con444.getRecorder().setReference(practitionerList.get(0));
        con444.getAsserter().setReference(practitionerList.get(0));
        //System.out.println(parser.encodeResourceToString(obs));
        outcome = client.create()
                .resource(con444)
                .prettyPrint()
                .encodedJson()
                .execute();

        String con444Reference = outcome.getId().toUnqualifiedVersionless().getValue();

        //læg opfølgningsdatoen på serveren
        String encounterString333 = getJsonObject(resourceDir+"/resources/Encounter-Encounter333.json");
        Encounter enc333 = parser.parseResource(Encounter.class, encounterString333);
        enc333.setSubject(patientRef);
        outcome = client.create()
                .resource(enc333)
                .prettyPrint()
                .encodedJson()
                .execute();

        String enc333Reference = outcome.getId().toUnqualifiedVersionless().getValue();

        //læg tilstandenv1 på serveren
        String conString111 = getJsonObject(resourceDir+"/resources/Condition-Condition111.json");
        String correctedConString111= conString111.replace("Encounter\\/Encounter333", enc333Reference);
        correctedConString111=correctedConString111.replace("Observation\\/Observation1234", Obs1234Reference);
        correctedConString111=correctedConString111.replace("Condition\\/Condition444", con444Reference);
        //System.out.println(correctedConString111);

        Condition con111 = parser.parseResource(Condition.class, correctedConString111);
        con111.setSubject(patientRef);
        con111.getAsserter().setReference(practitionerList.get(0));
        //System.out.println(parser.encodeResourceToString(con111));
        outcome = client.create()
                .resource(con111)
                .prettyPrint()
                .encodedJson()
                .execute();

        String con111Reference = outcome.getId().toUnqualified().getValue();
        System.out.println(con111Reference);

        //læg provenanceV1 på server
        String provString111 = getJsonObject(resourceDir+"/Provenance-ProvenanceCondition111.json");
        String correctedProvString111= provString111.replace("Practitioner\\/7777777", practitionerList.get(0));
        correctedProvString111=correctedProvString111.replace("Condition\\/Condition111", con111Reference);
        System.out.println(correctedProvString111);
        parser.setStripVersionsFromReferences(false);
        Provenance prov111 = parser.parseResource(Provenance.class, correctedProvString111);
        System.out.println(parser.encodeResourceToString(prov111));
        outcome = client.create()
                .resource(prov111)
                .prettyPrint()
                .encodedJson()
                .execute();

        String prov111Reference = outcome.getId().toUnqualifiedVersionless().getValue();

        //læg tilstand v2 på server
        String conString111v2 = getJsonObject(resourceDir+"/resources/Condition-Condition111v2.json");
        String correctedConString111v2= conString111v2.replace("Encounter\\/Encounter333", enc333Reference);
        correctedConString111v2=correctedConString111v2.replace("Observation\\/Observation1234", Obs1234Reference);
        correctedConString111v2=correctedConString111v2.replace("Condition\\/Condition444", con444Reference);
        correctedConString111v2=correctedConString111v2.replace("Provenance\\/ProvenanceCondition111", prov111Reference);
        correctedConString111v2=correctedConString111v2.replace("Practitioner\\/7777777", practitionerList.get(0));
        //System.out.println(correctedConString111);

        Condition con111v2 = parser.parseResource(Condition.class, correctedConString111v2);
        con111v2.setSubject(patientRef);
        con111v2.setId(con111Reference);
        //con111.getAsserter().setReference(practitionerList.get(0));
        //System.out.println(parser.encodeResourceToString(con111));

        outcome = client.update()
                .resource(con111v2)
                .prettyPrint()
                .encodedJson()
                .execute();

        String con111v2Reference = outcome.getId().toUnqualified().getValue();
        System.out.println(con111v2Reference);


        //læg provenanceV2 på server
        String provString111v2 = getJsonObject(resourceDir+"/Provenance-ProvenanceCondition111v2.json");
        String correctedProvString111v2= provString111v2.replace("Practitioner\\/7777777", practitionerList.get(0));
        correctedProvString111v2=correctedProvString111v2.replace("Condition\\/Condition111v2", con111v2Reference);
        parser.setStripVersionsFromReferences(false);
        Provenance prov111v2 = parser.parseResource(Provenance.class, correctedProvString111v2);
        outcome = client.create()
                .resource(prov111v2)
                .prettyPrint()
                .encodedJson()
                .execute();

        String prov111v2Reference = outcome.getId().toUnqualifiedVersionless().getValue();

//læg den eksekverede opfølgning på serveren
        String encounterString333v2 = getJsonObject(resourceDir+"/resources/Encounter-Encounter333v2.json");
        Encounter enc333v2 = parser.parseResource(Encounter.class, encounterString333v2);
        enc333v2.setSubject(patientRef);
        enc333v2.setId(enc333Reference);
        outcome = client.create()
                .resource(enc333v2)
                .prettyPrint()
                .encodedJson()
                .execute();

        String enc333v2Reference = outcome.getId().toUnqualifiedVersionless().getValue();

// læg opfølgningsresultatet på serveren

        String Obs22 = getJsonObject(resourceDir+"/resources/Observation-Observation22.json");
        Observation obs22 = parser.parseResource(Observation.class, Obs22);
        obs22.setSubject(patientRef);
        obs22.getPerformerFirstRep().setReference(practitionerList.get(0));
        //System.out.println(parser.encodeResourceToString(obs));
        outcome = client.create()
                .resource(obs22)
                .prettyPrint()
                .encodedJson()
                .execute();

        String obs22Reference = outcome.getId().toUnqualifiedVersionless().getValue();

//læg tilstand version 3 på serveren
        String conString111v3 = getJsonObject(resourceDir+"/resources/Condition-Condition111v3.json");
        String correctedConString111v3= conString111v3.replace("Encounter\\/Encounter333", enc333Reference);
        correctedConString111v3=correctedConString111v3.replace("Observation\\/Observation1234", Obs1234Reference);
        correctedConString111v3=correctedConString111v3.replace("Condition\\/Condition444", con444Reference);
        correctedConString111v3=correctedConString111v3.replace("Provenance\\/ProvenanceCondition111v2", prov111v2Reference);
        correctedConString111v3=correctedConString111v3.replace("Provenance\\/ProvenanceCondition111", prov111Reference);
        //correctedConString111v3=correctedConString111v3.replace("Provenance\\/ProvenanceCondition111v2", prov111v2Reference);
        correctedConString111v3=correctedConString111v3.replace("Practitioner\\/7777777", practitionerList.get(0));
        correctedConString111v3=correctedConString111v3.replace("Observation\\/Observation22", obs22Reference);
        //System.out.println(correctedConString111);

        Condition con111v3 = parser.parseResource(Condition.class, correctedConString111v3);
        con111v3.setSubject(patientRef);
        con111v3.setId(con111v2Reference);
        //con111.getAsserter().setReference(practitionerList.get(0));
        //System.out.println(parser.encodeResourceToString(con111));

        outcome = client.update()
                .resource(con111v3)
                .prettyPrint()
                .encodedJson()
                .execute();

        String con111v3Reference = outcome.getId().toUnqualified().getValue();

        // Læg provenance version 3 på serveren
        String provString111v3 = getJsonObject(resourceDir+"/Provenance-ProvenanceCondition111v3.json");
        String correctedProvString111v3= provString111v3.replace("Practitioner\\/7777777", practitionerList.get(0));
        correctedProvString111v3=correctedProvString111v3.replace("Condition\\/Condition111v3", con111v3Reference);
        parser.setStripVersionsFromReferences(false);
        Provenance prov111v3 = parser.parseResource(Provenance.class, correctedProvString111v3);
        outcome = client.create()
                .resource(prov111v3)
                .prettyPrint()
                .encodedJson()
                .execute();




        }
    //public <T extends IBaseResource> T parseResource(Class<T> theResourceType, String theMessageString)
    private static <T extends IBaseResource> String LoadResourceToServer(String filename, Class<T> theResourceType) {
//        FhirContext ctx = FhirContext.forR4();
//        IParser parser = ctx.newJsonParser();
//        String s = getJsonObject(filename);
//        Encounter enc333 = parser.parseResource(theResourceType, s);
//        enc333.setSubject(patientRef);
//        outcome = client.create()
//                .resource(obs)
//                .prettyPrint()
//                .encodedJson()
//                .execute();
//
//        String enc333Reference = outcome.getId().toUnqualifiedVersionless().getValue();
//
           return null;
    }

    private static String getJsonObject(String filename) {
        File f = new File(filename);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject=new JSONObject();

        try {
            Object obj = parser.parse(new FileReader(f));

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            jsonObject = (JSONObject) obj;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    private static ArrayList<String> uploadAndGetReferenceToPractitioners(IGenericClient client) {
        ArrayList<String> practitionerList = new ArrayList<String>();




                Practitioner practitioner = new Practitioner();
                practitioner.addName().setFamily("Hansen").setUse(HumanName.NameUse.OFFICIAL).addGiven("Kristina");

                MethodOutcome outcome = client.create()
                        .resource(practitioner)
                        .prettyPrint()
                        .encodedJson()
                        .execute();

                IIdType id = outcome.getId();
                System.out.println("Got ID: " + id.getValue());
                // id.getBaseUrl()

                practitionerList.add(id.toUnqualifiedVersionless().getValue());


        return practitionerList;
    }

    private static ArrayList<String> simplePatientList(int i, int i1) {
        ArrayList<String> patientList = new ArrayList<String>();

        for(int l=i; l<(i1+1);l++){
            patientList.add("Patient/"+l);
        }

            return patientList;
    }

    private static void saveConditionsToFile(ArrayList<CodeableConcept> conditions, ArrayList<String> patientlist, String problemType) {
        for(int i=0; i<conditions.size(); i++){

            KLCondition condition = new KLCondition();
            // ..populate the Condition object..
            condition.setCode(conditions.get(i));
            int rand = randBetween(0,patientlist.size()-1);
            condition.setSubject(new Reference().setReference(patientlist.get((rand))));

            //Create a date
            GregorianCalendar gc = new GregorianCalendar();

            int year = randBetween(2019, 2020);
            gc.set(gc.YEAR, year);
            int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
            gc.set(gc.DAY_OF_YEAR, dayOfYear);
            Date newDate=gc.getTime();
            //Apply date to Recorded date
            condition.setRecordedDate(newDate);

            //Set clinical status active
            CodeableConcept c1 = new CodeableConcept();
            List<Coding> cList1 = new ArrayList<Coding>();
            cList1.add(new Coding().setCode("active").setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical"));
            c1.setCoding(cList1);
            condition.setClinicalStatus(c1);

            if(problemType.equals("homecare")){
                CodeableConcept c2 = new CodeableConcept();
                List<Coding> cList2 = new ArrayList<Coding>();
                List<String> FSIIIseverities = new ArrayList<String>();

                FSIIIseverities.add("B2");
                FSIIIseverities.add("B3");
                FSIIIseverities.add("B4");
                FSIIIseverities.add("B5");


                List<String> FSIIIseveritiesDisplay = new ArrayList<String>();

                FSIIIseveritiesDisplay.add("Lette begrænsninger");
                FSIIIseveritiesDisplay.add("Moderate begrænsninger");
                FSIIIseveritiesDisplay.add("Svære begrænsninger");
                FSIIIseveritiesDisplay.add("Totale begrænsninger");

                cList2.add(new Coding().setCode(FSIIIseverities.get(i%4)).setSystem("http://kl.dk/fhir/common/caresocial/CodeSystem/FSIII").setDisplay(FSIIIseveritiesDisplay.get(i%4)));
                c2.setCoding(cList2);

                condition.setSeverity(c2);
            }


            //Serialiser tilstanden og print den ud som fil
            ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
            IParser parser = ctx.newJsonParser();

// Indent the output
            parser.setPrettyPrint(true);
//String s1=(conditions.get(i).getCoding().get(1).getDisplay()).replace(" ","");

// Serialize it
            String serialized = parser.encodeResourceToString(condition);
            try (PrintWriter out = new PrintWriter("C:/Users/kirst/Projects/TestdataFKI/Tilstande/condition"+conditions.get(i).getCoding().get(1).getDisplay().replace(" ","")+".json")) {
                out.println(serialized);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    public static ArrayList<String> uploadAndGetReferenceToPatients(String filename, IGenericClient client){
            ArrayList<String> patientList = new ArrayList<String>();

            try {

                //csv file containing data
//                        String strFile = filename;
//                        CSVReader reader = new CSVReader(new FileReader(strFile));
                CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
                BufferedReader br = Files.newBufferedReader(Paths.get(filename),  StandardCharsets.UTF_8);
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


                    MethodOutcome outcome = client.create()
                            .resource(patient)
                            .prettyPrint()
                            .encodedJson()
                            .execute();

                    IIdType id = outcome.getId();
                    System.out.println("Got ID: " + id.getValue());
                    // id.getBaseUrl()

                    patientList.add(id.toUnqualifiedVersionless().getValue());
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

            return patientList;
        }
            public static ArrayList<CodeableConcept> generateRandomConditions(int antal, String filename){

                ArrayList<CodeableConcept> conditionList = new ArrayList<CodeableConcept>();
                try {

                    CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
                    BufferedReader br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8);
                    CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build();

                    for (int i = 0; i < antal; i++) {
                        String[] nextLine;
                        int lineNumber = 0;
                        while ((nextLine = reader.readNext()) != null) {
                            List<Coding> conditionCoding = new ArrayList<Coding>();
                            conditionCoding.add(new Coding().setSystem("http://kl.dk/fhir/common/caresocial/CodeSystem/FSIII").setCode(nextLine[5]).setDisplay(nextLine[4]));

                            Coding c = GetAMoreDetailedSCTCode(nextLine[8]);

                            conditionCoding.add(c);
                            conditionList.add(new CodeableConcept().setCoding(conditionCoding).setText("Der er " + nextLine[4].toLowerCase() + ", " + c.getDisplay()));


                        }
                    }

                    } catch(
                            FileNotFoundException e){
                        e.printStackTrace();
                    } catch(IOException e){
                        e.printStackTrace();
                    } catch(
                            CsvValidationException e){
                        e.printStackTrace();
                    }

return conditionList;
            }




    public static Coding GetAMoreDetailedSCTCode(String code) throws IOException {
            URL urlForGetRequest = new URL("http://localhost:8080/concepts?ecQuery=<<"+code+"%20MINUS%20(<<"+code+":363713009=371150009)");
Coding c= new Coding();
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
        int responseCode = conection.getResponseCode();


        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in. readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result

            //GetAndPost.POSTRequest(response.toString());

            JSONParser parser = new JSONParser();
            JSONObject json=new JSONObject();
            try {
                json = (JSONObject) parser.parse(response.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            JSONArray items = (JSONArray) json.get("items");
            JSONObject item;
            if (items.size()>1) {
                Random rand = new Random();
                int randInt = rand.nextInt(items.size() - 1);
                item = (JSONObject) items.get(randInt);
            }
            else{
                item = (JSONObject) items.get(0);
            }

c.setCode(item.get("id").toString()).setDisplay(item.get("fsn").toString()).setSystem("http://snomed.info/sct");


        } else {
            System.out.println("GET NOT WORKED"+code);
        }
return c;
    }


    }





