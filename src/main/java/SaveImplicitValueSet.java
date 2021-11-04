import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ValueSet;

import java.io.IOException;

public class SaveImplicitValueSet {
    public static void main(String[] args) throws IOException {
        ValueSet v = new ValueSet();
        ValueSet.ValueSetComposeComponent component =  new ValueSet.ValueSetComposeComponent();

        component.addInclude().setSystem("http://kl.dk/fhir/common/caresocial/CodeSystem/KLCommonCareSocialCodes");
        component.getInclude().get(0).addFilter(new ValueSet.ConceptSetFilterComponent().setOp(ValueSet.FilterOperator.DESCENDENTOF).setValue("940f37e6-8a3d-483b-adac-be8af3268a5b").setProperty("descendants"));
        //ISA 7ac451dc-773b-4877-baaf-1e6b1d5c8e28
        //System.out.println(component.getInclude().get(0).getConcept().get(0).getCode());
        v.setCompose(component);
        v.setUrl("http://kl.dk/fhir/common/caresocial/ValueSet/KLExampleOfFilteredContent");
        //v.setId("KLCommonEvaluationTypeCodes");

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
