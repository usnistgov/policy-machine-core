package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.pap.store.ProhibitionsStore;

import java.util.*;

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
        List<Prohibition> existingPros = new ArrayList<>(policy.prohibitions.getOrDefault(
                subject.getName(),
                new ArrayList<>()
        ));

        Prohibition p = new Prohibition(
                name,
                subject,
                accessRightSet,
                intersection,
                containerConditions.stream().toList()
        );

        existingPros.add(p);

        policy.prohibitions.put(subject.getName(), existingPros);

        txCmdTracker.trackOp(tx, new TxCmd.CreateProhibitionTxCmd(p));
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        Prohibition old = getProhibition(name);

        for (String subject : policy.prohibitions.keySet()) {
            Collection<Prohibition> ps = policy.prohibitions.getOrDefault(subject, new ArrayList<>());
            ps.removeIf(p -> p.getName().equals(name));

            if (ps.isEmpty()) {
                policy.prohibitions.remove(subject);
            } else {
                policy.prohibitions.put(subject, ps);
            }
        }

        if(old != null) {
            txCmdTracker.trackOp(tx, new TxCmd.DeleteProhibitionTxCmd(old));
        }
    }

    @Override
    public Map<String, Collection<Prohibition>> getProhibitions() throws PMException {
        return policy.prohibitions;
    }

    @Override
    public Prohibition getProhibition(String name) throws PMException {
        for (String subject : policy.prohibitions.keySet()) {
            Collection<Prohibition> subjectPros = policy.prohibitions.getOrDefault(subject, new ArrayList<>());
            for (Prohibition p : subjectPros) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
        }

        throw new ProhibitionDoesNotExistException(name);
    }

    @Override
    public boolean prohibitionExists(String name) throws PMException {
        for (Map.Entry<String, Collection<Prohibition>> e : policy.prohibitions.entrySet()) {
            for (Prohibition p : e.getValue()) {
                if (p.getName().equals(name)) {
                    return true;
                }
            }
        }

        return false;
    }
}