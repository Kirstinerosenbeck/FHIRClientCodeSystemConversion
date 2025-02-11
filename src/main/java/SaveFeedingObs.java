import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;

import java.io.IOException;

public class SaveFeedingObs {
    public static void main(String[] args) throws IOException {
        FhirContext ctx = FhirContext.forR4();
        String serverBase = "http://hapi.fhir.org/baseR4";

        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        Observation feedObs = new Observation();

        feedObs.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://snomed.info/sct").setCode("169740003")));



        MethodOutcome outcome = client.create()
                .resource(feedObs)
                .prettyPrint()
                .encodedJson()
                .execute();

        IIdType id = outcome.getId();
        System.out.println("Got ID: " + id.getValue());
    }
}
