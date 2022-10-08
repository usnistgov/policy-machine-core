package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class EventSpecificResponseStatement extends PALStatement{

    private final String event;
    private final String alias;
    private final CreateRuleStatement.ResponseBlock responseBlock;

    public EventSpecificResponseStatement(String event, String alias, CreateRuleStatement.ResponseBlock responseBlock) {
        this.event = event;
        this.alias = alias;
        this.responseBlock = responseBlock;
    }

    public String getEvent() {
        return event;
    }

    public String getAlias() {
        return alias;
    }

    public boolean hasAlias() {
        return !(alias == null || alias.isEmpty());
    }

    public CreateRuleStatement.ResponseBlock getResponseBlock() {
        return responseBlock;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
