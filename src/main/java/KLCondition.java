import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Reference;

@ResourceDef(name="Condition", profile="http://kl.dk/fhir/common/caresocial/StructureDefinition/KLCommonCareSocialCondition")
public class KLCondition extends Condition {
    private static final long serialVersionUID = 1L;

@Child(name="followUpEncounter")
@Extension(url="http://kl.dk/fhir/common/caresocial/StructureDefinition/FollowUpEncounter",definedLocally=false, isModifier=false)
@Description(shortDefinition="")
private Reference followUpEncounter;

  public Reference getFollowUpEncounter() {
      if (followUpEncounter== null) {
          followUpEncounter = new Reference();
      }
      return followUpEncounter;
  }
    public void setFollowUpEncounter(Reference theFollowUpEncounter) {
        followUpEncounter = theFollowUpEncounter;
    }

}
