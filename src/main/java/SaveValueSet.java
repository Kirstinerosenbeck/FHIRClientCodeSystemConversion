import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class SaveValueSet {
    public static void main(String[] args) throws IOException {

        File dir = new File("/Users/kirst/Projects/fsh-kl/KL_ig/build/input/vocabulary/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Do something with child


                JSONParser parser = new JSONParser();
                String jsonString = "";

                try {
                    //FileReader fr=new FileReader(child);
                    BufferedReader fr = Files.newBufferedReader(Paths.get(String.valueOf(child)),  StandardCharsets.UTF_8);
                    Object obj = parser.parse(fr);
                    //Object obj = parser.parse(new FileReader(child));

                    // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
                    JSONObject jsonObject = (JSONObject) obj;

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
                    jsonObject.writeJSONString(writer);
                    writer.flush();

                  jsonString = new String(out.toByteArray(),"utf-8");

                    //jsonString = jsonObject.toJSONString();
                   // System.out.println(jsonString);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                FhirContext ctx = FhirContext.forR4();
                IParser FHIRparser = ctx.newJsonParser();

// Parse it
                ValueSet parsed = FHIRparser.parseResource(ValueSet.class, jsonString);

                System.out.println(parsed.getName());

                String serverBase = "http://80.197.59.44/hapi-fhir-jpaserver/fhir/";

                IGenericClient client = ctx.newRestfulGenericClient(serverBase);

                MethodOutcome outcome = client.create()
                        .resource(parsed)
                        .prettyPrint()
                        .encodedJson()
                        .execute();
                IIdType id = outcome.getId();
                System.out.println("Got ID: " + id.getValue());
            }
        }


    }

    private static void saveEvaluationTypeValueSet(){
        ValueSet v = new ValueSet();
        ValueSet.ValueSetComposeComponent component =  new ValueSet.ValueSetComposeComponent();

        component.addInclude().setSystem("http://kl.dk/fhir/common/caresocial/CodeSystem/KLCommonCareSocialCodes");
        component.getInclude().get(0).addConcept(new ValueSet.ConceptReferenceComponent().setCode("54c4ffea-7caf-4edc-8aa9-ef6e0be26c4c"));
        component.getInclude().get(0).addConcept(new ValueSet.ConceptReferenceComponent().setCode("053a301d-1bb8-4cc4-b781-87825ecf0ef8"));
        component.getInclude().get(0).addConcept(new ValueSet.ConceptReferenceComponent().setCode("effe55c7-572c-4a99-8fb4-2a9dda2f6572"));
        System.out.println(component.getInclude().get(0).getConcept().get(0).getCode());
        v.setCompose(component);
        v.setUrl("http://kl.dk/fhir/common/caresocial/ValueSet/KLCommonEvaluationTypeCodes");
        v.setId("KLCommonEvaluationTypeCodes");

        FhirContext ctx = FhirContext.forR4();
        String serverBase = "http://80.197.59.44/hapi-fhir-jpaserver/fhir/";

        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        MethodOutcome outcome = client.create()
                .resource(v)
                .prettyPrint()
                .encodedJson()
                .execute();
        IIdType id = outcome.getId();
        System.out.println("Got ID: " + id.getValue());
    }
}
