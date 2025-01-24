package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.common.exception.ProhibitionContainerDoesNotExistException;
import gov.nist.csd.pm.common.exception.ProhibitionExistsException;
import gov.nist.csd.pm.common.exception.ProhibitionSubjectDoesNotExistException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.ArrayList;
import java.util.Collection;

import static gov.nist.csd.pm.pap.GraphModifier.checkAccessRightsValid;

public class ProhibitionsModifier extends Modifier implements ProhibitionsModification {

    public ProhibitionsModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void createProhibition(String name,
                                  long subjectId,
                                  ProhibitionSubjectType subjectType, AccessRightSet accessRightSet,
                                  boolean intersection,
                                  Collection<ContainerCondition> containerConditions) throws PMException {
        checkCreateInput(name, subjectId, accessRightSet, new ArrayList<>(containerConditions));

        store.prohibitions().createProhibition(name, subjectId, accessRightSet, intersection, new ArrayList<>(containerConditions));
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        if(!checkDeleteInput(name)) {
            return;
        }

        store.prohibitions().deleteProhibition(name);
    }

    /**
     * Check the prohibition being created.
     *
     * @param name                The name of the prohibition.
     * @param subject             The subject of the prohibition.
     * @param accessRightSet      The denied access rights.
     * @param containerConditions The prohibition container conditions.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkCreateInput(String name, ProhibitionSubject subject, AccessRightSet accessRightSet,
                                    Collection<ContainerCondition> containerConditions) throws PMException {
        if (store.prohibitions().prohibitionExists(name)) {
            throw new ProhibitionExistsException(name);
        }

        // check the prohibition parameters are valid
        checkAccessRightsValid(store.operations().getResourceOperations(), accessRightSet);
        checkProhibitionSubjectExists(subject);
        checkProhibitionContainersExist(containerConditions);
    }

    /**
     * Check if the prohibition exists. If it doesn't, return false to indicate to the caller that execution should not
     * proceed.
     *
     * @param name The name of the prohibition.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkDeleteInput(String name) throws PMException {
        if (!store.prohibitions().prohibitionExists(name)) {
            return false;
        }

        return true;
    }

    protected void checkProhibitionSubjectExists(ProhibitionSubject subject)
            throws PMException {
        if (subject.getType() != ProhibitionSubject.Type.PROCESS) {
            if (!store.graph().nodeExists(subject.getName())) {
                throw new ProhibitionSubjectDoesNotExistException(subject.getName());
            }
        }
    }

    protected void checkProhibitionContainersExist(Collection<ContainerCondition> containerConditions)
            throws PMException {
        for (ContainerCondition container : containerConditions) {
            if (!store.graph().nodeExists(container.getId())) {
                throw new ProhibitionContainerDoesNotExistException(container.getId());
            }
        }
    }
}
