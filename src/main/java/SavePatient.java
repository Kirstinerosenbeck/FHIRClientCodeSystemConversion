import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.ValueSet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SavePatient {

    public static void main(String[] args) throws IOException {
// We're connecting to a R4 compliant server in this example
        FhirContext ctx = FhirContext.forR4();
        String serverBase = "http://hapi.fhir.org/baseR4";

        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        Patient patient = new Patient();
// ..populate the patient object..
        patient.getMeta().addProfile("http://fhir.kl.dk/children/StructureDefinition/klgateway-children-citizen");
        patient.addIdentifier().setSystem("urn:oid:1.2.208.176.1.2").setValue("0505209996").setUse(Identifier.IdentifierUse.OFFICIAL);
        patient.getManagingOrganization().setIdentifier(new Identifier().setSystem("urn:oid:1.2.208.176.1.1").setValue("123456789012345").setUse(Identifier.IdentifierUse.OFFICIAL));


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

        IParser parser = ctx.newJsonParser();

// Indent the output
        parser.setPrettyPrint(true);

// Serialize it

        String serialized = parser.encodeResourceToString(patient);

        try (PrintWriter out = new PrintWriter("C:/CodeSystems/Patient-rikke.json")) {
            out.println(serialized);
        }
    }
}