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

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.store.ProhibitionsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A {@link ProhibitionsStore} implementation backed by in-memory prohibition indexes.
 */
public class MemoryProhibitionsStore extends MemoryStore implements ProhibitionsStore {

    public MemoryProhibitionsStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void createNodeProhibition(String name,
                                      long nodeId,
                                      AccessRightSet accessRightSet,
                                      Set<Long> inclusionSet,
                                      Set<Long> exclusionSet,
                                      boolean isConjunctive) throws PMException {
        Prohibition p = new NodeProhibition(
            name,
            nodeId, accessRightSet, inclusionSet, exclusionSet, isConjunctive
        );

        policy.addProhibition(p);

        txCmdTracker.trackOp(tx, new TxCmd.CreateProhibitionTxCmd(p));
    }

    @Override
    public void createProcessProhibition(String name,
                                         long userId,
                                         String process,
                                         AccessRightSet accessRightSet,
                                         Set<Long> inclusionSet,
                                         Set<Long> exclusionSet,
                                         boolean isConjunctive) throws PMException {
        Prohibition p = new ProcessProhibition(
            name,
            userId, process, accessRightSet, inclusionSet, exclusionSet, isConjunctive
        );

        policy.addProhibition(p);

        txCmdTracker.trackOp(tx, new TxCmd.CreateProhibitionTxCmd(p));
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        Prohibition old = getProhibition(name);

        policy.deleteProhibition(old);

	    txCmdTracker.trackOp(tx, new TxCmd.DeleteProhibitionTxCmd(old));
    }

    @Override
    public Collection<Prohibition> getAllProhibitions() throws PMException {
        List<Prohibition> all = new ArrayList<>();
        policy.nodeProhibitions.forEach((id, pros) -> all.addAll(pros));
        policy.processProhibitions.forEach((id, pros) -> all.addAll(pros));
        return all;
    }

    @Override
    public Collection<Prohibition> getNodeProhibitions(long nodeId) throws PMException {
        return policy.nodeProhibitions.getOrDefault(nodeId, new ArrayList<>());
    }

    @Override
    public Collection<Prohibition> getProcessProhibitions(String process) throws PMException {
        return policy.processProhibitions.getOrDefault(process, new ArrayList<>());
    }

    @Override
    public Prohibition getProhibition(String name) throws PMException {
        for (long subject : policy.nodeProhibitions.keySet()) {
            Collection<Prohibition> subjectPros = policy.nodeProhibitions.getOrDefault(subject, new ArrayList<>());
            for (Prohibition p : subjectPros) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
        }

        for (String process : policy.processProhibitions.keySet()) {
            Collection<Prohibition> processPros = policy.processProhibitions.getOrDefault(process, new ArrayList<>());
            for (Prohibition p : processPros) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
        }

        throw new ProhibitionDoesNotExistException(name);
    }

    @Override
    public boolean prohibitionExists(String name) throws PMException {
        try {
            getProhibition(name);
            return true;
        } catch (ProhibitionDoesNotExistException e) {
            return false;
        }
    }

}