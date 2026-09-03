/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.modification;

import static gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightValidator.validateAccessRights;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.exception.ProhibitionExistsException;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.Set;

/**
 * {@link ProhibitionsModification} implementation, validating a prohibition's access rights and
 * referenced nodes before delegating to the backend
 * {@link gov.nist.ngac.pm.core.pap.store.ProhibitionsStore}.
 */
public class ProhibitionsModifier extends Modifier implements ProhibitionsModification {

    public ProhibitionsModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void createNodeProhibition(String name,
                                      long nodeId,
                                      AccessRightSet accessRightSet,
                                      Set<Long> inclusionSet,
                                      Set<Long> exclusionSet,
                                      boolean isConjunctive) throws PMException {
        checkCreateInput(name, nodeId, accessRightSet, inclusionSet, exclusionSet);

        policyStore.prohibitions().createNodeProhibition(name, nodeId, accessRightSet, inclusionSet, exclusionSet, isConjunctive);
    }

    @Override
    public void createProcessProhibition(String name,
                                         long userId,
                                         String process,
                                         AccessRightSet accessRightSet,
                                         Set<Long> inclusionSet,
                                         Set<Long> exclusionSet,
                                         boolean isConjunctive) throws PMException {
        checkCreateInput(name, userId, accessRightSet, inclusionSet, exclusionSet);

        policyStore.prohibitions().createProcessProhibition(name, userId, process, accessRightSet, inclusionSet, exclusionSet, isConjunctive);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        if(!checkDeleteInput(name)) {
            return;
        }

        policyStore.prohibitions().deleteProhibition(name);
    }

    /**
     * Validate create prohibition inputs.
     * @param name the name of the prohibition.
     * @param nodeId the id of the subject node.
     * @param accessRightSet the access right set.
     * @param inclusionSet the set of inclusion attributes.
     * @param exclusionSet the set of exclusion attributes.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkCreateInput(String name, long nodeId, AccessRightSet accessRightSet,
                                    Set<Long> inclusionSet, Set<Long> exclusionSet) throws PMException {
        if (policyStore.prohibitions().prohibitionExists(name)) {
            throw new ProhibitionExistsException(name);
        }

        // check the prohibition parameters are valid
        validateAccessRights(policyStore.operations().getResourceAccessRights(), accessRightSet);
        checkProhibitionSubjectExists(nodeId);
        checkProhibitionContainersExist(inclusionSet, exclusionSet);
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

    protected void checkProhibitionSubjectExists(long nodeId)
            throws PMException {
            if (!policyStore.graph().nodeExists(nodeId)) {
                throw new NodeDoesNotExistException(nodeId);
            }
    }

    /**
     * Checks that every node referenced in the inclusion or exclusion set exists.
     *
     * @throws PMException if any referenced node does not exist
     */
    protected void checkProhibitionContainersExist(Set<Long> inclusionSet, Set<Long> exclusionSet) throws PMException {
        for (long inc : inclusionSet) {
            if (!policyStore.graph().nodeExists(inc)) {
                throw new NodeDoesNotExistException(inc);
            }
        }

        for (long exc : exclusionSet) {
            if (!policyStore.graph().nodeExists(exc)) {
                throw new NodeDoesNotExistException(exc);
            }
        }
    }
}
