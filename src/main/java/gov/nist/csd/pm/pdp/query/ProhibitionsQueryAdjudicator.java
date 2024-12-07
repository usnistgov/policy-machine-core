package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.AdminAccessRights;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.ProhibitionsQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pdp.Adjudicator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.GET_PROCESS_PROHIBITIONS;
import static gov.nist.csd.pm.pap.AdminAccessRights.GET_PROHIBITIONS;

public class ProhibitionsQueryAdjudicator extends Adjudicator implements ProhibitionsQuery {

    private final UserContext userCtx;
    private final PAP pap;
    private final PrivilegeChecker privilegeChecker;

    public ProhibitionsQueryAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public Map<String, Collection<Prohibition>> getProhibitions() throws PMException {
        Map<String, Collection<Prohibition>> prohibitions = pap.query().prohibitions().getProhibitions();
        Map<String, Collection<Prohibition>> retProhibitions = new HashMap<>();
        for (String subject : prohibitions.keySet()) {
            Collection<Prohibition> subjectPros = filterProhibitions(prohibitions.get(subject));
            retProhibitions.put(subject, subjectPros);
        }

        return retProhibitions;
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithSubject(String subject) throws PMException {
        return filterProhibitions(pap.query().prohibitions().getProhibitionsWithSubject(subject));
    }

    @Override
    public Prohibition getProhibition(String name) throws PMException {
        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        // check user has access to subject
        if (prohibition.getSubject().getType() == ProhibitionSubject.Type.PROCESS) {
            privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), GET_PROCESS_PROHIBITIONS);
        } else {
            privilegeChecker.check(userCtx, prohibition.getSubject().getName(), GET_PROHIBITIONS);
        }

        // check user has access to each container condition
        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            privilegeChecker.check(userCtx, containerCondition.getName(), GET_PROHIBITIONS);
        }

        return prohibition;
    }

    @Override
    public Collection<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException {
        privilegeChecker.check(this.userCtx, subject, AdminAccessRights.REVIEW_POLICY);

        return pap.query().prohibitions().getInheritedProhibitionsFor(subject);
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithContainer(String container) throws PMException {
        privilegeChecker.check(this.userCtx, container, AdminAccessRights.REVIEW_POLICY);

        return pap.query().prohibitions().getProhibitionsWithContainer(container);
    }



    private Collection<Prohibition> filterProhibitions(Collection<Prohibition> prohibitions) {
        prohibitions.removeIf(prohibition -> {
            try {
                // check user has access to subject prohibitions
                if (prohibition.getSubject().getType() == ProhibitionSubject.Type.PROCESS) {
                    privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(),
                            GET_PROCESS_PROHIBITIONS);
                } else {
                    privilegeChecker.check(userCtx, prohibition.getSubject().getName(), GET_PROHIBITIONS);
                }

                // check user has access to each target prohibitions
                for (ContainerCondition containerCondition : prohibition.getContainers()) {
                    privilegeChecker.check(userCtx, containerCondition.getName(), GET_PROHIBITIONS);
                }

                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return prohibitions;
    }
}
