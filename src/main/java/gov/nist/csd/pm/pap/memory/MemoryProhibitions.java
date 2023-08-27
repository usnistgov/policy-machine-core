package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.ProhibitionsStore;
import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.*;


class MemoryProhibitions extends MemoryStore<TxProhibitions> implements ProhibitionsStore, Transactional, BaseMemoryTx {

    protected MemoryTx<TxProhibitions> tx;
    private Map<String, List<Prohibition>> prohibitions;
    private MemoryGraph graph;

    public MemoryProhibitions() {
        this.prohibitions = new HashMap<>();
    }

    public MemoryProhibitions(Map<String, List<Prohibition>> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public MemoryProhibitions(Prohibitions prohibitions) throws PMException {
        this.prohibitions = prohibitions.getAll();
    }

    public void setMemoryGraph(MemoryGraph graph) {
        this.graph = graph;
    }

    public void clear() {
        this.prohibitions.clear();
    }

    @Override
    public void beginTx() throws PMException {
        if (tx == null) {
            tx = new MemoryTx<>(false, 0, new TxProhibitions(new TxPolicyEventTracker(), this));
        }

        tx.beginTx();
    }

    @Override
    public void commit() throws PMException {
        tx.commit();
    }

    @Override
    public void rollback() throws PMException {
        tx.getStore().rollback();

        tx.rollback();
    }

    @Override
    public void create(String id, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws ProhibitionExistsException, UnknownAccessRightException, ProhibitionSubjectDoesNotExistException, ProhibitionContainerDoesNotExistException, PMBackendException {
        checkCreateInput(graph, id, subject, accessRightSet, intersection, containerConditions);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.create(id, subject, accessRightSet, intersection, containerConditions));

        // add the prohibition to the data structure
        createInternal(id, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void update(String id, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws UnknownAccessRightException, ProhibitionSubjectDoesNotExistException, ProhibitionContainerDoesNotExistException, PMBackendException, ProhibitionDoesNotExistException {
        checkUpdateInput(graph, id, subject, accessRightSet, intersection, containerConditions);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.update(id, subject, accessRightSet, intersection, containerConditions));

        deleteInternal(id);
        createInternal(id, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void delete(String id) throws PMBackendException {
        if (!checkDeleteInput(id)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.delete(id));

        deleteInternal(id);
    }

    @Override
    public Map<String, List<Prohibition>> getAll() {
        Map<String, List<Prohibition>> retProhibitions = new HashMap<>();
        for (String subject : prohibitions.keySet()) {
            retProhibitions.put(subject, prohibitions.get(subject));
        }

        return retProhibitions;
    }

    @Override
    public boolean exists(String id) {
        for (Map.Entry<String, List<Prohibition>> e : prohibitions.entrySet()) {
            for (Prohibition p : e.getValue()) {
                if (p.getId().equals(id)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) {
        List<Prohibition> subjectPros = prohibitions.get(subject);
        if (subjectPros == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(subjectPros);
    }

    @Override
    public Prohibition get(String id) throws ProhibitionDoesNotExistException, PMBackendException {
        checkGetInput(id);

        for (String subject : prohibitions.keySet()) {
            List<Prohibition> subjectPros = prohibitions.get(subject);
            for (Prohibition p : subjectPros) {
                if (p.getId().equals(id)) {
                    return p;
                }
            }
        }

        throw new ProhibitionDoesNotExistException(id);
    }

    private void createInternal(String id, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) {
        List<Prohibition> existingPros = prohibitions.getOrDefault(subject.getName(), new ArrayList<>());
        existingPros.add(new Prohibition(id, subject, accessRightSet, intersection, Arrays.asList(containerConditions)));
        prohibitions.put(subject.getName(), existingPros);
    }

    private void deleteInternal(String id) {
        for(String subject : prohibitions.keySet()) {
            List<Prohibition> ps = prohibitions.get(subject);
            Iterator<Prohibition> iterator = ps.iterator();
            while (iterator.hasNext()) {
                Prohibition p = iterator.next();
                if(p.getId().equals(id)) {
                    iterator.remove();
                    prohibitions.put(subject, ps);
                }
            }
        }
    }
}
