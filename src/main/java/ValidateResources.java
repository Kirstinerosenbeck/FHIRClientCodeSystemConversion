import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class ValidateResources {



    public static void ValidateResources(String folder) {

        File dir = new File(folder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {

                //System.out.println(child.getAbsolutePath());
                String childProfile = "";

                JSONParser parser = new JSONParser();

                try {
                    Object obj = parser.parse(new FileReader(child));

                    // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONObject meta = (JSONObject) jsonObject.get("meta");
                    JSONArray profile = (JSONArray) meta.get("profile");
                    childProfile = profile.get(0).toString();
                    //System.out.println(childProfile);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
String s= "java -jar C:/Users/kirst/Projects/validation/validator_cli.jar "+child.getAbsolutePath()+" -ig dk.fhir.ig.kl.common.caresocial -profile "+childProfile;
//System.out.println(s);
                    Process proc = Runtime.getRuntime().exec("java -jar C:/Users/kirst/Projects/validation/validator_cli.jar "+child.getAbsolutePath()+" -ig dk.fhir.ig.kl.common.caresocial#current -profile "+childProfile);
                    System.out.println(s);
                    // InputStream in = proc.getInputStream();
                   // InputStream err = proc.getErrorStream();
                    BufferedReader output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    String line;
                    while ((line = output.readLine()) != null) {
                        System.out.println(line);
                        if(line.contains("*FAILURE*")) {
                            System.out.println(line);
                            //System.out.println(s);
                        }
                        if(line.contains("Success")){
                            System.out.println(line);
                        }
                    }
                   // System.out.println(output.);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } else {



        }



    }

    public static void validateResource(String file) {
String childProfile="";
        File f = new File(file);


                JSONParser parser = new JSONParser();

                try {
                    Object obj = parser.parse(new FileReader(f));

                    // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONObject meta = (JSONObject) jsonObject.get("meta");
                    JSONArray profile = (JSONArray) meta.get("profile");
                    childProfile = profile.get(0).toString();
                    //System.out.println(childProfile);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String s= "java -jar C:/Users/kirst/Projects/validation/validator_cli.jar "+f.getAbsolutePath()+" -ig dk.fhir.ig.kl.common.caresocial -profile "+childProfile;
//System.out.println(s);
                    Process proc = Runtime.getRuntime().exec("java -jar C:/Users/kirst/Projects/validation/validator_cli.jar "+f.getAbsolutePath()+" -ig dk.fhir.ig.kl.common.caresocial#current -profile "+childProfile);
                    // InputStream in = proc.getInputStream();
                    // InputStream err = proc.getErrorStream();
                    BufferedReader output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    String line;
                    while ((line = output.readLine()) != null) {
                        System.out.println(line);
                        if(line.contains("*FAILURE*")) {
                            System.out.println(line);
                            //System.out.println(s);
                        }
                        if(line.contains("Success")){
                            System.out.println(line);
                        }
                    }
                    // System.out.println(output.);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }






    public static void main(String[] args) throws IOException {



validateResource("C:/Users/kirst/Projects/TestdataFKI/Validation/PlannedIntervention/CarePlan-PressureUlcerIntervention.json");


    }
}
