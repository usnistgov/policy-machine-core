package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.ProhibitionsStore;
import gov.nist.csd.pm.policy.exceptions.TransactionNotStartedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.*;

import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;

class MemoryProhibitionsStore extends ProhibitionsStore {

    private Map<String, List<Prohibition>> prohibitions;
    private TxHandler<Map<String, List<Prohibition>>> txHandler;

    MemoryProhibitionsStore() {
        this.prohibitions = new HashMap<>();
        this.txHandler = new TxHandler<>();
    }

    MemoryProhibitionsStore(Map<String, List<Prohibition>> prohibitions) {
        this.prohibitions = copyProhibitions(prohibitions);
        this.txHandler = new TxHandler<>();
    }

    Map<String, List<Prohibition>> copyProhibitions(Map<String, List<Prohibition>> toCopy) {
        HashMap<String, List<Prohibition>> prohibitions = new HashMap<>();
        for (String subject : toCopy.keySet()) {
            List<Prohibition> copyPros = new ArrayList<>();
            for (Prohibition p : toCopy.get(subject)) {
                copyPros.add(new Prohibition(p));
            }

            prohibitions.put(subject, copyPros);
        }

        return prohibitions;
    }

    @Override
    public synchronized void create(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) {
        List<Prohibition> existingPros = prohibitions.getOrDefault(subject.name(), new ArrayList<>());
        existingPros.add(new Prohibition(label, subject, accessRightSet, intersection, Arrays.asList(containerConditions)));
        prohibitions.put(subject.name(), existingPros);
    }

    @Override
    public synchronized void update(String label, ProhibitionSubject subject, AccessRightSet accessRightSet,
                                    boolean intersection, ContainerCondition... containerConditions) throws PMException {
        runTx(this, () -> {
            delete(label);
            create(label, subject, accessRightSet, intersection, containerConditions);
        });
    }

    @Override
    public synchronized void delete(String label) {
        for(String subject : prohibitions.keySet()) {
            List<Prohibition> ps = prohibitions.get(subject);
            Iterator<Prohibition> iterator = ps.iterator();
            while (iterator.hasNext()) {
                Prohibition p = iterator.next();
                if(p.getLabel().equals(label)) {
                    iterator.remove();
                    prohibitions.put(subject, ps);
                }
            }
        }
    }

    @Override
    public synchronized Map<String, List<Prohibition>> getAll() {
        Map<String, List<Prohibition>> retProhibitions = new HashMap<>();
        for (String subject : prohibitions.keySet()) {
            retProhibitions.put(subject, prohibitions.get(subject));
        }

        return retProhibitions;
    }
    @Override
    public synchronized List<Prohibition> getWithSubject(String subject) {
        List<Prohibition> subjectPros = prohibitions.get(subject);
        if (subjectPros == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(subjectPros);
    }

    @Override
    public synchronized Prohibition get(String label) throws ProhibitionDoesNotExistException {
        for (String subject : prohibitions.keySet()) {
            List<Prohibition> subjectPros = prohibitions.get(subject);
            for (Prohibition p : subjectPros) {
                if (p.getLabel().equals(label)) {
                    return p;
                }
            }
        }

        throw new ProhibitionDoesNotExistException(label);
    }

    @Override
    public synchronized void beginTx() {
        if (!txHandler.isInTx()) {
            txHandler.setState(copyProhibitions(prohibitions));
        }

        txHandler.beginTx();
    }

    @Override
    public synchronized void commit() throws TransactionNotStartedException {
        if (!txHandler.isInTx()) {
            throw new TransactionNotStartedException();
        }

        txHandler.commit();
    }

    @Override
    public synchronized void rollback() throws TransactionNotStartedException {
        if (!txHandler.isInTx()) {
            return;
        }

        prohibitions = txHandler.getState();
        txHandler.rollback();
    }
}
