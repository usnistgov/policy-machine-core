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

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Set;

/**
 * NGAC prohibition methods.
 */
public interface ProhibitionsModification {

    /**
     * Create a new prohibition for a node. If isConjuctive is true, the prohibition is applied when all conditions of
     * the inclusion and exclusion sets are met. If it is false, the prohibition is applied if only one condition of the
     * sets is met.
     *   - Inclusion condition is satisfied if the target node is or is an ascendant of the inclusion node.
     *   - Exclusion condition is satisfied if the target node is not and is not an ascendant of the exclusion node.
     *
     * @param name           the identifier of the prohibition.
     * @param nodeId         ths subject of the prohibition (user, user attribute, or process).
     * @param accessRightSet the access rights to deny.
     * @param inclusionSet   the set of attributes to include in the prohibition.
     * @param exclusionSet   the set of attributes to exclude from the prohibition.
     * @param isConjunctive  indicates if the prohibition is applied to the conjunction of the inclusion and exclusion
     *                       sets or the disjunction.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void createNodeProhibition(String name, long nodeId, AccessRightSet accessRightSet, Set<Long> inclusionSet, Set<Long> exclusionSet, boolean isConjunctive) throws PMException;

    /**
     * Create a new prohibition for a process executing on behalf of a user. If isConjuctive is true, the prohibition is
     * applied when all conditions of the inclusion and exclusion sets are met. If it is false, the prohibition is
     * applied if only one condition of the sets is met.
     *   - Inclusion condition is satisfied if the target node is or is an ascendant of the inclusion node.
     *   - Exclusion condition is satisfied if the target node is not and is not an ascendant of the exclusion node.
     *
     * @param name the identifier of the prohibition.
     * @param userId the id of the user node represented by this process.
     * @param process the process id.
     * @param accessRightSet the set of access rights to deny.
     * @param inclusionSet   the set of attributes to include in the prohibition.
     * @param exclusionSet   the set of attributes to exclude from the prohibition.
     * @param isConjunctive  indicates if the prohibition is applied to the conjunction of the inclusion and exclusion
     *                       sets or the disjunction.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void createProcessProhibition(String name, long userId, String process, AccessRightSet accessRightSet, Set<Long> inclusionSet, Set<Long> exclusionSet, boolean isConjunctive) throws PMException;

    /**
     * Delete the prohibition with the given name. No exception will be thrown if the prohibition does not exist.
     *
     * @param name The name of the prohibition to delete.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void deleteProhibition(String name) throws PMException;

}
