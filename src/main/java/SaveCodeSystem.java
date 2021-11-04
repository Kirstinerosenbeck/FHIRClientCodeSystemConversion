import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;

public class SaveCodeSystem {
//Download code-system i formatet excel 11+n. Gem det i csv-utf8 fra excel. Læg i mappen C:/CodeSystems/ og kør scriptet
//find json-filen her: C:/Users/kirst/Projects/fsh-kl/KL_ig/ig-data/input/vocabulary/, konverter til utf-8, og sæt id.
       //CodeSystem variables FFB

//       static String localFilename="C:/CodeSystems/FFB.csv";
//       static String version="20200908";
//       static String url="http://kl.dk/fhir/common/caresocial/CodeSystem/FFB";
//       static String OID="urn:oid:1.2.208.176.2.22";
//       static String title="FFB";
//       static String name ="FFB";
//       static String description="Codes from FFB";
//      static String fileLocalSave = "C:/Users/kirst/Projects/fsh-kl/KL_ig/ig-data/input/vocabulary/CodeSystem-FFB.json";


        //CodeSystem variables FSIII

//        static String localFilename="C:/CodeSystems/FSIII.csv";
//        static String version="20200908";
//        static String url="http://kl.dk/fhir/common/caresocial/CodeSystem/FSIII";
//        static String OID="urn:oid:1.2.208.176.2.21";
//        static String title="FSIII";
//        static String name ="FSIII";
//        static String description="Codes from FSIII";
//        static String fileLocalSave = "C:/Users/kirst/Projects/fsh-kl/KL_ig/ig-data/input/vocabulary/CodeSystem-FSIII.json";

        //CodeSystem variables KLCommonCareSocialCodes

      static String localFilename="C:/CodeSystems/KLCommonCareSocialCodes.csv";
        static String version="20200908";
        static String url="http://kl.dk/fhir/common/caresocial/CodeSystem/KLCommonCareSocialCodes";
        static String OID="";
        static String title="KLCommonCareSocialCodes";
        static String name ="KLCommonCareSocialCodes";
        static String description="Administrative/technical codes in Local Govenment Denmark (KL), associated with KLCommonCareSocial";
        static String fileLocalSave = "C:/Users/kirst/Projects/fsh-kl/input/vocabulary/CodeSystem-KLCommonCareSocialCodes.json";

          //static String mode="print";
          //static String mode="save";
          static String mode="write";

        public static void main(String[] args) throws IOException {
        CodeSystem cs = new CodeSystem();
        cs = getConceptsFromFile(localFilename, cs);
               // CodeSystem.ConceptDefinitionComponent concept = new CodeSystem.ConceptDefinitionComponent().addConcept().setCode("a").setDisplay("a").setDefinition("a");
               // cs = setParent(cs,concept,"7b41185e-eeb4-437d-8120-5d51bbd27a79");
     //   cs = addSubConceptsFromFile("C:/CodeSystems/KLCommonCareSocialCodes.csv", cs);


        //CodeSystem.ConceptDefinitionComponent concept = new CodeSystem.ConceptDefinitionComponent().addConcept().setCode("b").setDisplay("b-listed patients").setDefinition("trala");
        //cs.addConcept().setCode("a").setDisplay("all patient").setDefinition("all patients are included");
        //cs = setParent(cs, concept, "a")
         //       printCodeSystem(cs);
                cs.setVersion(version);

                cs.setUrl(url);
                if(!OID.equals("")){
                        Identifier i = new Identifier().setSystem("urn:oid").setValue(OID);
                        cs.addIdentifier(i);
                }


                cs.setTitle(title);
                cs.setName(name);
                cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
                cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
                cs.setDescription(description);
                cs.setHierarchyMeaning(CodeSystem.CodeSystemHierarchyMeaning.ISA);
                //cs.addFilter(new CodeSystem.CodeSystemFilterComponent().setCode("concept").setDescription("all descendants excluding the concept").addOperator(CodeSystem.FilterOperator.DESCENDENTOF));

                FhirContext ctx = FhirContext.forR4();
                if (mode.equals("save")) {

                String serverBase = "http://80.197.59.44/hapi-fhir-jpaserver/fhir/";

                IGenericClient client = ctx.newRestfulGenericClient(serverBase);

                MethodOutcome outcome = client.create()
                        .resource(cs)
                        .prettyPrint()
                        .encodedJson()
                        .execute();
                IIdType id = outcome.getId();
                System.out.println("Got ID: " + id.getValue());
                }
                if(mode.equals("print")) {
                        IParser parser = ctx.newJsonParser();

// Indent the output
                        parser.setPrettyPrint(true);

// Serialize it
                        String serialized = parser.encodeResourceToString(cs);
                        System.out.println(serialized);



                }
                if(mode.equals("write")){
                        IParser parser = ctx.newJsonParser();

// Indent the output
                        parser.setPrettyPrint(true);

// Serialize it
                        String serialized = parser.encodeResourceToString(cs);
                        try (PrintWriter out = new PrintWriter(fileLocalSave)) {
                                out.println(serialized);
                        }


                }


        }


        private static CodeSystem getConceptsFromFile(String filename, CodeSystem cs) {

                List<CodeSystem.ConceptDefinitionComponent> conceptList = new ArrayList<>();
                List<String> parentList = new ArrayList();
                List<String> codeList = new ArrayList();
                int superNumber=0;

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
                                if(lineNumber==0){

                                }
                                else {
                                        boolean dublet=false;
                                        int index=0;
                                        for(int k=0; k<codeList.size(); k++){
                                                if(nextLine[0].equals(codeList.get(k))){
                                                        //dublet fundet
                                                        dublet=true;
                                                        index=k;
                                                }

                                        }
                                        if(dublet==false){
                                                codeList.add(nextLine[0]);
                                                conceptList.add(new CodeSystem.ConceptDefinitionComponent().setCode(nextLine[0]).setDisplay(nextLine[3]).setDefinition(nextLine[11]));
                                                //System.out.println(conceptList.get(conceptList.size()-1).getDefinition());
                                        if (nextLine[1].isEmpty()) {
                                               // System.out.println("foundSuperConcept");
                                                parentList.add("super");
                                                superNumber = superNumber + 1;
                                                //cs.addConcept(conceptList.get(lineNumber));
                                        } else {
                                                parentList.add(nextLine[1]);
                                        }
                                }
                                        if(dublet==true){
                                                //System.out.println("opfanges dubletter");
                                                if((conceptList.get(index).hasDisplay())==false){
                                                conceptList.get(index).setDisplay(nextLine[3]);
                                                        }
                                                if((conceptList.get(index).hasDefinition())==false){
                                                        System.out.println("ever here");
                                                        System.out.println("sætter definition til "+nextLine[12]);
                                                        conceptList.get(index).setDefinition(nextLine[12]);

                                                }
                                                if((parentList.get(index)).equals("super")){
                                                        if (nextLine[1].isEmpty()) {

                                                        }
                                                        else{
                                                                parentList.set(index,nextLine[1]);
                                                                superNumber=superNumber-1;
                                                        }
                                                }





                                        }
                                }
                                lineNumber++;

                        }


                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                } catch (CsvValidationException e) {
                        e.printStackTrace();
                }
                for(int i=0;i< parentList.size();i++){
                        System.out.println("parent: "+parentList.get(i));
                        System.out.println("child: "+conceptList.get(i).getCode());
                }


                while(parentList.size()>superNumber) {


                        List<Integer> childIndexes = identifyLeaves(conceptList, parentList);
                        System.out.println(parentList.size()+" supernummer: "+superNumber+ " i denne kørsel fjernes:" +childIndexes.size());

                        for (int p = 0; p < childIndexes.size(); p++) {
                                //this is a leave concept
                                CodeSystem.ConceptDefinitionComponent child = conceptList.get(childIndexes.get(p));
                                //this is the leave concepts parent
                                String parent = parentList.get(childIndexes.get(p));
                                //add leave to its parent
                                for (int r = 0; r < conceptList.size(); r++) {
                                        if ((conceptList.get(r).getCode()).equals(parent)) {
                                                //System.out.println(parent+" har barn "+child.getCode());
                                                conceptList.get(r).addConcept(child);
                                        }
                                }

                        }
                        //now we are finished with the leaves, delete from lists
                        for (int q = childIndexes.size()-1; q > -1; q=q-1) {
                               // System.out.println("I will now remove concept: "+childIndexes.get(q));
                                conceptList.remove((childIndexes.get(q)).intValue());
                                parentList.remove((childIndexes.get(q)).intValue());


                        }
                }

//conceptList skulle nu gerne udelukkende indeholde super-begreber, med indeholdte underbegreber
                                for (int l=0; l<conceptList.size();l++){
                                        //System.out.println(conceptList.get(l).getConcept().get(1).getCode());
                                        cs.addConcept(conceptList.get(l));
                                }


                return cs;
        }

        private static List<Integer> identifyLeaves(List<CodeSystem.ConceptDefinitionComponent> conceptList, List<String> parentList) {
                List<Integer> indexes = new ArrayList<>();
                for(int i=0;i< conceptList.size();i++){
                        boolean identifiedParent=false;
                        for(int j=0;j<parentList.size();j++){
                                //System.out.println("tester  "+conceptList.get(i).getCode()+ "er det samme som "+parentList.get(j));
                                if((conceptList.get(i).getCode()).equals(parentList.get(j))){
                                       // System.out.println("Det er sandt at  "+conceptList.get(i).getCode()+ " er det samme som "+parentList.get(j)+"dvs. index "+i+"er forælder");
                                        identifiedParent=true;
                                }

                                }
                        //System.out.println(identifiedParent);
                        if(identifiedParent==false){
                                if(parentList.get(i).equals("super")){

                                }else{
                                        indexes.add(i);
                                }


                        }
                }

                return indexes;
        }

        public static void printCodeSystem(CodeSystem cs) {

                String s = new String();
                for (int i=0;i<cs.getConcept().size();i++){
                        System.out.println("this is a superConcept: "+cs.getConcept().get(i).getDisplay()+" my index is "+i);
                for (int j=0;j<cs.getConcept().get(i).getConcept().size();j++){
                        System.out.println("this is a subConcept: "+cs.getConcept().get(i).getConcept().get(j).getDisplay()+" my index is "+i+" "+j);
                }

                }


        }





}
