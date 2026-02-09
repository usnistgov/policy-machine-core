package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.store.ProhibitionsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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