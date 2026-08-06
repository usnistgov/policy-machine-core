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
