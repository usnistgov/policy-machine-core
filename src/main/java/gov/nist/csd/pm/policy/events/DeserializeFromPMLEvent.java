package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

public class DeserializeFromPMLEvent implements PolicyEvent{

    private final UserContext author;
    private final String pml;
    private final FunctionDefinitionStatement[] customFunctions;

    public DeserializeFromPMLEvent(UserContext author, String pml, FunctionDefinitionStatement ... customFunctions) {
        this.author = author;
        this.pml = pml;
        this.customFunctions = customFunctions;
    }

    @Override
    public String getEventName() {
        return "deserialize_from_pml";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.deserialize().fromPML(author, pml, customFunctions);
    }
}
