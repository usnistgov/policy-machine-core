package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.common.exception.ProhibitionContainerDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.ProhibitionExistsException;
import gov.nist.csd.pm.core.common.exception.ProhibitionSubjectDoesNotExistException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;

import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;

import static gov.nist.csd.pm.core.pap.modification.GraphModifier.checkAccessRightsValid;

public class ProhibitionsModifier extends Modifier implements ProhibitionsModification {

    public ProhibitionsModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void createProhibition(String name, ProhibitionSubject subject, AccessRightSet accessRightSet,
                                  boolean intersection, Collection<ContainerCondition> containerConditions) throws PMException {
        checkCreateInput(name, subject, accessRightSet, new ArrayList<>(containerConditions));

        policyStore.prohibitions().createProhibition(name, subject, accessRightSet, intersection, new ArrayList<>(containerConditions));
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        if(!checkDeleteInput(name)) {
            return;
        }

        policyStore.prohibitions().deleteProhibition(name);
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
        if (policyStore.prohibitions().prohibitionExists(name)) {
            throw new ProhibitionExistsException(name);
        }

        // check the prohibition parameters are valid
        checkAccessRightsValid(policyStore.operations().getResourceAccessRights(), accessRightSet);
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
	    return policyStore.prohibitions().prohibitionExists(name);
    }

    protected void checkProhibitionSubjectExists(ProhibitionSubject subject)
            throws PMException {
        if (subject.isNode()) {
            if (!policyStore.graph().nodeExists(subject.getNodeId())) {
                throw new ProhibitionSubjectDoesNotExistException(subject.getNodeId());
            }
        }
    }

    protected void checkProhibitionContainersExist(Collection<ContainerCondition> containerConditions)
            throws PMException {
        for (ContainerCondition container : containerConditions) {
            if (!policyStore.graph().nodeExists(container.getId())) {
                throw new ProhibitionContainerDoesNotExistException(container.getId());
            }
        }
    }
}
