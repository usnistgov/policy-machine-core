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

package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import java.util.Collection;

/**
 * Interface to query obligations.
 */
public interface ObligationsQuery {

    /**
     * Get all obligations.
     *
     * @return All obligations.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Obligation> getObligations() throws PMException;

    /**
     * Check if an obligation exists with the given name.
     *
     * @param name The obligation to check.
     * @return True if the obligation exists with the given name, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    boolean obligationExists(String name) throws PMException;

    /**
     * Get the obligation associated with the given name.
     *
     * @param name The name of the obligation to get.
     * @return The obligation object associated with the given name.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Obligation getObligation(String name) throws PMException;

    /**
     * Get the obligations created by the given author.
     *
     * @param authorCtx The user representing the author to search for.
     * @return A list of Obligation objects.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Obligation> getObligationsWithAuthor(NodeUserContext authorCtx) throws PMException;
    
}
