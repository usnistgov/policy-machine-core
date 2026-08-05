package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.modification.ObligationsModification;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;
import java.util.Collection;

public interface ObligationsStore extends ObligationsModification, Transactional {

    boolean obligationExists(String name) throws PMException;

    /**
     * The persisted PML text for an obligation plus its name and author, for {@code ObligationsQuerier} to
     * recompile into an {@link gov.nist.csd.pm.core.pap.obligation.Obligation}. A store that holds a compiled
     * Obligation natively (e.g. the in-memory store) renders it on the fly via {@code Obligation#toString()}.
     * @param name The name of the obligation.
     * @return The obligation's name, PML text, and author, or {@code null} if no obligation with this name exists.
     * @throws PMException If there is an error in the PM.
     */
    ObligationPml getObligationPml(String name) throws PMException;

    /**
     * @return The name, PML text, and author for every persisted obligation.
     * @throws PMException If there is an error in the PM.
     */
    Collection<ObligationPml> getObligationPmls() throws PMException;

    /**
     * The names of every persisted obligation authored by {@code author}. A cheap, PML-text-free lookup --
     * callers that only need to know "which obligations did this author create" (e.g. a delete-guard check)
     * shouldn't have to pay for a PML read/recompile of every obligation just to inspect its author field.
     * @param author The author to match.
     * @return The names of every obligation with this author.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getObligationNamesWithAuthor(NodeUserContext author) throws PMException;

    record ObligationPml(String name, String pmlText, NodeUserContext author) {
    }

}
