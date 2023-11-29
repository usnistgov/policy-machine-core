package gov.nist.csd.pm.pap.memory.unmodifiable;

import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.target.AnyInIntersectionTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UnmodifiableObligation extends Obligation {

    public UnmodifiableObligation(UserContext author, String name,
                                  List<Rule> rules) {
        super(author, name, Collections.unmodifiableList(rules));
    }

    @Override
    public Obligation addRule(String name, EventPattern eventPattern, Response response) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteRule(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAuthor(UserContext userCtx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRules(List<Rule> rules) {
        throw new UnsupportedOperationException();
    }
}
