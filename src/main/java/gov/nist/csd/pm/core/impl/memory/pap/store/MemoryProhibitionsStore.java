package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.store.ProhibitionsStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MemoryProhibitionsStore extends MemoryStore implements ProhibitionsStore {

    public MemoryProhibitionsStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void createProhibition(String name,
                                  ProhibitionSubject subject,
                                  AccessRightSet accessRightSet,
                                  boolean intersection,
                                  Collection<ContainerCondition> containerConditions) {
        Prohibition p = new Prohibition(
                name,
                subject,
                accessRightSet,
                intersection,
                containerConditions.stream().toList()
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
    public Map<Long, Collection<Prohibition>> getNodeProhibitions() throws PMException {
        return policy.nodeProhibitions;
    }

    @Override
    public Map<String, Collection<Prohibition>> getProcessProhibitions() throws PMException {
        return policy.processProhibitions;
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

    @Override
    public Collection<Prohibition> getProhibitionsWithNode(long subject) throws PMException {
        return policy.nodeProhibitions.getOrDefault(subject, new ArrayList<>());
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithProcess(String subject) throws PMException {
        return policy.processProhibitions.getOrDefault(subject, new ArrayList<>());
    }
}