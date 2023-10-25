package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.subject.AnyUserSubject;
import gov.nist.csd.pm.policy.model.obligation.event.subject.ProcessesSubject;
import gov.nist.csd.pm.policy.model.obligation.event.subject.Subject;
import gov.nist.csd.pm.policy.model.obligation.event.target.AnyTarget;
import gov.nist.csd.pm.policy.model.obligation.event.target.Target;

import java.util.List;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public class AdjudicatorObligations implements Obligations {
    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    public AdjudicatorObligations(UserContext userCtx, PAP pap, AccessRightChecker accessRightChecker) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = accessRightChecker;
    }

    @Override
    public void create(UserContext author, String name, Rule... rules) throws PMException {
        for (Rule rule : rules) {
            Subject subject = rule.getEventPattern().getSubject();
            checkSubject(subject, CREATE_OBLIGATION);

            Target target = rule.getEventPattern().getTarget();
            checkTarget(target, CREATE_OBLIGATION);
        }
    }

    private void checkTarget(Target target, String accessRight) throws PMException {
        if (target instanceof AnyTarget) {
            accessRightChecker.check(userCtx, AdminPolicyNode.OBLIGATIONS_TARGET.nodeName(), accessRight);
        }

        for (String tar : target.getTargets()) {
            accessRightChecker.check(userCtx, tar, accessRight);
        }
    }

    private void checkSubject(Subject subject, String accessRight) throws PMException {
        if (subject instanceof AnyUserSubject ||
                subject instanceof ProcessesSubject) {
            accessRightChecker.check(userCtx, AdminPolicyNode.OBLIGATIONS_TARGET.nodeName(), accessRight);

        }

        for (String user : subject.getSubjects()) {
            accessRightChecker.check(userCtx, user, accessRight);
        }
    }

    @Override
    public void update(UserContext author, String name, Rule... rules) throws PMException {
        create(author, name, rules);
    }

    @Override
    public void delete(String name) throws PMException {
        Obligation obligation = pap.obligations().get(name);
        for (Rule rule : obligation.getRules()) {
            Subject subject = rule.getEventPattern().getSubject();
            checkSubject(subject, DELETE_OBLIGATION);

            Target target = rule.getEventPattern().getTarget();
            checkTarget(target, DELETE_OBLIGATION);
        }
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        List<Obligation> obligations = pap.obligations().getAll();
        obligations.removeIf(obligation -> {
            try {
                for (Rule rule : obligation.getRules()) {
                    Subject subject = rule.getEventPattern().getSubject();
                    checkSubject(subject, GET_OBLIGATION);

                    Target target = rule.getEventPattern().getTarget();
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
    public boolean exists(String name) throws PMException {
        boolean exists = pap.obligations().exists(name);
        if (!exists) {
            return false;
        }

        try {
            get(name);
        } catch (UnauthorizedException e) {
            return false;
        }

        return true;
    }

    @Override
    public Obligation get(String name) throws PMException {
        Obligation obligation = pap.obligations().get(name);
        for (Rule rule : obligation.getRules()) {
            Subject subject = rule.getEventPattern().getSubject();
            checkSubject(subject, GET_OBLIGATION);

            Target target = rule.getEventPattern().getTarget();
            checkTarget(target, GET_OBLIGATION);
        }

        return obligation;
    }
}
