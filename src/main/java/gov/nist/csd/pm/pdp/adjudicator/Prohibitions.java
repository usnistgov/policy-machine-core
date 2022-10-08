package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.author.ProhibitionsAuthor;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_OBJECT;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public class Prohibitions extends ProhibitionsAuthor {

    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;
    
    public Prohibitions(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = new AccessRightChecker(pap, policyReviewer);
    }

    @Override
    public void create(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        // check that the user can create a prohibition for the subject
        if (subject.type() == ProhibitionSubject.Type.PROCESS) {
            accessRightChecker.check(userCtx, SUPER_OBJECT, CREATE_PROCESS_PROHIBITION);
        } else {
            accessRightChecker.check(userCtx, subject.name(), CREATE_PROHIBITION);
        }


        // check that the user can create a prohibition for each container in the condition
        for (ContainerCondition contCond : containerConditions) {
            accessRightChecker.check(userCtx, contCond.name(), ADD_CONTAINER_TO_PROHIBITION);

            // there is another access right needed if the condition is a complement
            if (contCond.complement()) {
                accessRightChecker.check(userCtx, SUPER_OBJECT, ADD_CONTAINER_COMPLEMENT_TO_PROHIBITION);
            }
        }
    }

    @Override
    public void update(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        // use the same rights check as createProhibition
        create(label, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void delete(String label) throws PMException {
        Prohibition prohibition = pap.prohibitions().get(label);

        // check that the user can create a prohibition for the subject
        if (prohibition.getSubject().type() == ProhibitionSubject.Type.PROCESS) {
            accessRightChecker.check(userCtx, SUPER_OBJECT, DELETE_PROCESS_PROHIBITION);
        } else {
            accessRightChecker.check(userCtx, prohibition.getSubject().name(), DELETE_PROHIBITION);
        }

        // check that the user can create a prohibition for each container in the condition
        for (ContainerCondition contCond : prohibition.getContainers()) {
            accessRightChecker.check(userCtx, contCond.name(), REMOVE_CONTAINER_FROM_PROHIBITION);

            // there is another access right needed if the condition is a complement
            if (contCond.complement()) {
                accessRightChecker.check(userCtx, SUPER_OBJECT, REMOVE_CONTAINER_COMPLEMENT_FROM_PROHIBITION);
            }
        }
    }

    @Override
    public Map<String, List<Prohibition>> getAll() throws PMException {
        Map<String, List<Prohibition>> prohibitions = pap.prohibitions().getAll();
        Map<String, List<Prohibition>> retProhibitions = new HashMap<>();
        for (String subject : prohibitions.keySet()) {
            List<Prohibition> subjectPros = filterProhibitions(prohibitions.get(subject));
            retProhibitions.put(subject, subjectPros);
        }

        return retProhibitions;
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) throws PMException {
        return filterProhibitions(pap.prohibitions().getWithSubject(subject));
    }

    private List<Prohibition> filterProhibitions(List<Prohibition> prohibitions) {
        prohibitions.removeIf(prohibition -> {
            try {
                // check user has access to subject prohibitions
                if (prohibition.getSubject().type() == ProhibitionSubject.Type.PROCESS) {
                    accessRightChecker.check(userCtx, SUPER_OBJECT, GET_PROCESS_PROHIBITIONS);
                } else {
                    accessRightChecker.check(userCtx, prohibition.getSubject().name(), GET_PROHIBITIONS);
                }

                // check user has access to each target prohibitions
                for (ContainerCondition containerCondition : prohibition.getContainers()) {
                    accessRightChecker.check(userCtx, containerCondition.name(), GET_PROHIBITIONS);
                }

                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return prohibitions;
    }

    @Override
    public Prohibition get(String label) throws PMException {
        Prohibition prohibition = pap.prohibitions().get(label);

        // check user has access to subject prohibitions
        if (prohibition.getSubject().type() == ProhibitionSubject.Type.PROCESS) {
            accessRightChecker.check(userCtx, SUPER_OBJECT, GET_PROCESS_PROHIBITIONS);
        } else {
            accessRightChecker.check(userCtx, prohibition.getSubject().name(), GET_PROHIBITIONS);
        }

        // check user has access to each target prohibitions
        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            accessRightChecker.check(userCtx, containerCondition.name(), GET_PROHIBITIONS);
        }

        return prohibition;
    }
}
