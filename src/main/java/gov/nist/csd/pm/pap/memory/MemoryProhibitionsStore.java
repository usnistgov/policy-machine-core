package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.ProhibitionsStore;
import gov.nist.csd.pm.pap.memory.unmodifiable.UnmodifiableProhibition;
import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.*;


class MemoryProhibitionsStore extends MemoryStore<TxProhibitions> implements ProhibitionsStore, Transactional, BaseMemoryTx {

    private Map<String, List<Prohibition>> prohibitions;
    private MemoryGraphStore graph;

    public MemoryProhibitionsStore() {
        this.prohibitions = Collections.unmodifiableMap(new HashMap<>());
    }

    public void setMemoryGraph(MemoryGraphStore graph) {
        this.graph = graph;
    }

    public void clear() {
        this.prohibitions = Collections.unmodifiableMap(new HashMap<>());
    }

    @Override
    public void beginTx() {
        if (tx == null) {
            tx = new MemoryTx<>(false, 0, new TxProhibitions(new TxPolicyEventTracker(), this));
        }

        tx.beginTx();
    }

    @Override
    public void commit() {
        tx.commit();
    }

    @Override
    public void rollback() {
        tx.getStore().rollback();

        tx.rollback();
    }

    @Override
    public void create(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws ProhibitionExistsException, UnknownAccessRightException, ProhibitionSubjectDoesNotExistException, ProhibitionContainerDoesNotExistException, PMBackendException {
        checkCreateInput(graph, name, subject, accessRightSet, intersection, containerConditions);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.create(name, subject, accessRightSet, intersection, containerConditions));

        // add the prohibition to the data structure
        createInternal(name, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void update(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws UnknownAccessRightException, ProhibitionSubjectDoesNotExistException, ProhibitionContainerDoesNotExistException, PMBackendException, ProhibitionDoesNotExistException {
        checkUpdateInput(graph, name, subject, accessRightSet, intersection, containerConditions);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.update(name, subject, accessRightSet, intersection, containerConditions));

        deleteInternal(name);
        createInternal(name, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void delete(String name) throws PMBackendException {
        if (!checkDeleteInput(name)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.delete(name));

        deleteInternal(name);
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
    public boolean exists(String name) {
        for (Map.Entry<String, List<Prohibition>> e : prohibitions.entrySet()) {
            for (Prohibition p : e.getValue()) {
                if (p.getName().equals(name)) {
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

        return subjectPros;
    }

    @Override
    public Prohibition get(String name) throws ProhibitionDoesNotExistException, PMBackendException {
        checkGetInput(name);

        for (String subject : prohibitions.keySet()) {
            List<Prohibition> subjectPros = prohibitions.get(subject);
            for (Prohibition p : subjectPros) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
        }

        throw new ProhibitionDoesNotExistException(name);
    }

    private void createInternal(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) {
        List<Prohibition> existingPros = new ArrayList<>(prohibitions.getOrDefault(subject.getName(), new ArrayList<>()));
        existingPros.add(new UnmodifiableProhibition(name, subject, accessRightSet, intersection, Arrays.asList(containerConditions)));

        HashMap<String, List<Prohibition>> m = new HashMap<>(this.prohibitions);
        m.put(subject.getName(), Collections.unmodifiableList(existingPros));

        this.prohibitions = Collections.unmodifiableMap(m);
    }

    private void deleteInternal(String name) {
        for(String subject : prohibitions.keySet()) {
            List<Prohibition> ps = new ArrayList<>(prohibitions.get(subject));
            if(ps.removeIf(p -> p.getName().equals(name))) {
                HashMap<String, List<Prohibition>> m = new HashMap<>(this.prohibitions);
                m.put(subject, Collections.unmodifiableList(ps));
                this.prohibitions = Collections.unmodifiableMap(m);
            }
        }
    }
}
