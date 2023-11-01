package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.subject.AnyUserSubject;
import gov.nist.csd.pm.policy.model.obligation.event.subject.Subject;
import gov.nist.csd.pm.policy.model.obligation.event.target.AnyTarget;
import gov.nist.csd.pm.policy.model.obligation.event.target.Target;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.review.ObligationsReview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObligationsReviewer implements ObligationsReview {

    private final Policy policy;
    private final GraphReviewer graphReviewer;

    public ObligationsReviewer(Policy policy, GraphReviewer graphReviewer) {
        this.policy = policy;
        this.graphReviewer = graphReviewer;
    }

    @Override
    public List<Obligation> getObligationsWithAuthor(UserContext userCtx) throws PMException {
        List<Obligation> obls = new ArrayList<>();
        for (Obligation obligation : policy.obligations().getAll()) {
            if (obligation.getAuthor().equals(userCtx)) {
                obls.add(obligation);
            }
        }

        return obls;
    }

    @Override
    public Map<String, List<Rule>> getRulesWithEventSubject(String attribute) throws PMException {
        Map<String, List<Rule>> rulesMap = new HashMap<>();
        for (Obligation obligation : policy.obligations().getAll()) {
            List<Rule> rules = obligation.getRules();
            for (Rule rule : rules) {
                Subject subject = rule.getEventPattern().getSubject();

                if (subject instanceof AnyUserSubject ||
                        subject.matches(new UserContext(attribute), graphReviewer)) {
                    List<Rule> matchingRules = rulesMap.getOrDefault(obligation.getName(), new ArrayList<>());
                    matchingRules.add(rule);
                    rulesMap.put(obligation.getName(), matchingRules);
                }
            }
        }

        return rulesMap;
    }

    @Override
    public Map<String, List<Rule>> getRulesWithEventTarget(String attribute) throws PMException {
        Map<String, List<Rule>> rulesMap = new HashMap<>();
        for (Obligation obligation : policy.obligations().getAll()) {
            List<Rule> rules = obligation.getRules();
            for (Rule rule : rules) {
                Target target = rule.getEventPattern().getTarget();

                if (target instanceof AnyTarget || target.matches(attribute, graphReviewer)) {
                    List<Rule> matchingRules = rulesMap.getOrDefault(obligation.getName(), new ArrayList<>());
                    matchingRules.add(rule);
                    rulesMap.put(obligation.getName(), matchingRules);
                }
            }
        }

        return rulesMap;
    }

    @Override
    public List<Response> getMatchingEventResponses(EventContext evt) throws PMException {
        List<Response> responses = new ArrayList<>();
        for (Obligation obligation : policy.obligations().getAll()) {
            for (Rule rule : obligation.getRules()) {
                if (evt.matchesPattern(rule.getEventPattern(), graphReviewer)) {
                    responses.add(rule.getResponse());
                }
            }
        }

        return responses;
    }

}
