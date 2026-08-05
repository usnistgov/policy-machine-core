package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.modification.ObligationsModification;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;
import java.util.Collection;

public interface ObligationsStore extends ObligationsModification, Transactional {

    boolean obligationExists(String name) throws PMException;

    /**
     * The persisted PML text for an obligation plus its name and author.
     * @param name The name of the obligation.
     * @return The obligation's name, PML text, and author, or null if no obligation with this name exists.
     * @throws PMException If there is an error in the PM.
     */
    ObligationPml getObligationPml(String name) throws PMException;

    /**
     * @return The name, PML text, and author for every persisted obligation.
     * @throws PMException If there is an error in the PM.
     */
    Collection<ObligationPml> getObligationPmls() throws PMException;

    /**
     * The names of every persisted obligation authored by the provided author.
     * @param author The author to match.
     * @return The names of every obligation with this author.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getObligationNamesWithAuthor(NodeUserContext author) throws PMException;

    record ObligationPml(String name, String pmlText, NodeUserContext author) {
    }

}
