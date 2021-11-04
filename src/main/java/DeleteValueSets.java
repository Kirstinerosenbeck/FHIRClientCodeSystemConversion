import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.IdType;

import java.io.IOException;

public class DeleteValueSets {
    public static void main(String[] args) throws IOException {
        FhirContext ctx = FhirContext.forR4();
        String serverBase = "http://80.197.59.44/hapi-fhir-jpaserver/fhir/";

        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        for(int i=74;i<125;i++) {
            MethodOutcome response = client
                    .delete()
                    .resourceById(new IdType("ValueSet", Integer.toString(i)))
                    .execute();
        }
    }
}
