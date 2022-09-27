package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.author.ObligationsAuthor;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;

import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_OBJECT;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.GET_OBLIGATION;


class Obligations implements ObligationsAuthor {

    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    public Obligations(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = new AccessRightChecker(pap, policyReviewer);
    }

    @Override
    public void create(UserContext author, String label, Rule... rules) throws PMException {
        for (Rule rule : rules) {
            EventSubject subject = rule.getEvent().getSubject();
            checkSubject(subject, CREATE_OBLIGATION);

            Target target = rule.getEvent().getTarget();
            checkTarget(target, CREATE_OBLIGATION);
        }
    }

    private void checkTarget(Target target, String accessRight) throws PMException {
        if (target.getType() == Target.Type.POLICY_ELEMENT) {
            accessRightChecker.check(userCtx, target.policyElement(), accessRight);
        } else if (target.getType() == Target.Type.ANY_POLICY_ELEMENT) {
            accessRightChecker.check(userCtx, SUPER_OBJECT, accessRight);
        } else if (target.getType() == Target.Type.ANY_CONTAINED_IN) {
            accessRightChecker.check(userCtx, target.anyContainedIn(), accessRight);
        } else if (target.getType() == Target.Type.ANY_OF_SET) {
            for (String policyElement : target.anyOfSet()) {
                accessRightChecker.check(userCtx, policyElement, accessRight);
            }
        }
    }

    private void checkSubject(EventSubject subject, String accessRight) throws PMException {
        if (subject.getType() == EventSubject.Type.ANY_USER) {
            accessRightChecker.check(userCtx, SUPER_OBJECT, accessRight);
        } else if (subject.getType() == EventSubject.Type.ANY_USER_WITH_ATTRIBUTE) {
            accessRightChecker.check(userCtx, subject.anyUserWithAttribute(), accessRight);
        } else if (subject.getType() == EventSubject.Type.PROCESS) {
            // need permissions on super object create a process obligation
            accessRightChecker.check(userCtx, SUPER_OBJECT, accessRight);
        } else if (subject.getType() == EventSubject.Type.USERS) {
            for (String user : subject.users()) {
                accessRightChecker.check(userCtx, user, accessRight);
            }
        }
    }

    @Override
    public void update(UserContext author, String label, Rule... rules) throws PMException {
        create(author, label, rules);
    }

    @Override
    public void delete(String label) throws PMException {
        Obligation obligation = pap.obligations().get(label);
        for (Rule rule : obligation.getRules()) {
            EventSubject subject = rule.getEvent().getSubject();
            checkSubject(subject, DELETE_OBLIGATION);

            Target target = rule.getEvent().getTarget();
            checkTarget(target, DELETE_OBLIGATION);
        }
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        List<Obligation> obligations = pap.obligations().getAll();
        obligations.removeIf(obligation -> {
            try {
                for (Rule rule : obligation.getRules()) {
                    EventSubject subject = rule.getEvent().getSubject();
                    checkSubject(subject, GET_OBLIGATION);

                    Target target = rule.getEvent().getTarget();
                    checkTarget(target, GET_OBLIGATION);
                }
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return obligations;
    }

    @Override
    public Obligation get(String label) throws PMException {
        Obligation obligation = pap.obligations().get(label);
        for (Rule rule : obligation.getRules()) {
            EventSubject subject = rule.getEvent().getSubject();
            checkSubject(subject, GET_OBLIGATION);

            Target target = rule.getEvent().getTarget();
            checkTarget(target, GET_OBLIGATION);
        }

        return obligation;
    }
}
