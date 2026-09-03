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

package gov.nist.ngac.pm.core.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import gov.nist.ngac.pm.core.pap.modification.ObligationsModification;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import java.util.Collection;

/**
 * The persistence layer for obligations.
 */
public interface ObligationsStore extends ObligationsModification, Transactional {

    boolean obligationExists(String name) throws PMException;

    /**
     * The persisted PML text for an obligation, along with its name and author.
     *
     * @param name the obligation's name
     * @return the obligation's name, PML text, and author, or null if it doesn't exist
     * @throws PMException if the lookup fails
     */
    ObligationPml getObligationPml(String name) throws PMException;

    /**
     * @return the name, PML text, and author for every persisted obligation
     * @throws PMException if the lookup fails
     */
    Collection<ObligationPml> getObligationPmls() throws PMException;

    /**
     * The names of every persisted obligation authored by the given author.
     *
     * @param author the author to match
     * @return the names of every obligation with this author
     * @throws PMException if the lookup fails
     */
    Collection<String> getObligationNamesWithAuthor(NodeUserContext author) throws PMException;

    record ObligationPml(String name, String pmlText, NodeUserContext author) {
    }

}
