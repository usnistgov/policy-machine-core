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

package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.store.ObligationsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An {@link ObligationsStore} implementation backed by an in-memory list of obligations.
 */
public class MemoryObligationsStore extends MemoryStore implements ObligationsStore {

    public MemoryObligationsStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void createObligation(Obligation obligation) throws PMException {
        policy.obligations.add(obligation);

        txCmdTracker.trackOp(tx, new TxCmd.CreateObligationTxCmd(obligation));
    }

    @Override
    public boolean obligationExists(String name) throws PMException {
        for (Obligation o : policy.obligations) {
            if (o.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ObligationPml getObligationPml(String name) throws PMException {
        Obligation obligation = findObligation(name);
        if (obligation == null) {
            return null;
        }

        return new ObligationPml(obligation.getName(), obligation.toString(), obligation.getAuthor());
    }

    @Override
    public Collection<ObligationPml> getObligationPmls() throws PMException {
        List<ObligationPml> rows = new ArrayList<>();
        for (Obligation obligation : policy.obligations) {
            rows.add(new ObligationPml(obligation.getName(), obligation.toString(), obligation.getAuthor()));
        }

        return rows;
    }

    @Override
    public Collection<String> getObligationNamesWithAuthor(NodeUserContext author) throws PMException {
        List<String> names = new ArrayList<>();
        for (Obligation obligation : policy.obligations) {
            if (author.equals(obligation.getAuthor())) {
                names.add(obligation.getName());
            }
        }

        return names;
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation old = requireObligation(name);
        policy.obligations.removeIf(o -> o.getName().equals(name));
        txCmdTracker.trackOp(tx, new TxCmd.DeleteObligationTxCmd(old));
    }

    private Obligation findObligation(String name) {
        for (Obligation obligation : policy.obligations) {
            if (obligation.getName().equals(name)) {
                return obligation;
            }
        }

        return null;
    }

    private Obligation requireObligation(String name) throws ObligationDoesNotExistException {
        Obligation obligation = findObligation(name);
        if (obligation == null) {
            throw new ObligationDoesNotExistException(name);
        }

        return obligation;
    }

}
