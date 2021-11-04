import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.ValueSet;

import java.io.IOException;

public class SavePatient {

    public static void main(String[] args) throws IOException {
// We're connecting to a R4 compliant server in this example
        FhirContext ctx = FhirContext.forR4();
        String serverBase = "http://hapi.fhir.org/baseR4";

        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        Patient patient = new Patient();
// ..populate the patient object..
        patient.addIdentifier().setSystem("system:cpr").setValue("12345");
        patient.addName().setFamily("Rose").addGiven("Kirse");

// Invoke the server create method (and send pretty-printed JSON
// encoding to the server
// instead of the default which is non-pretty printed XML)
        MethodOutcome outcome = client.create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();

        IIdType id = outcome.getId();
        System.out.println("Got ID: " + id.getValue());
    }
}