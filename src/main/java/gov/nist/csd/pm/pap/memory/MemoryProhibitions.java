package gov.nist.csd.pm.pap.memory;

import com.google.gson.Gson;
import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.io.Serializable;
import java.util.*;

class MemoryProhibitions implements Prohibitions, Serializable {

    protected MemoryTx tx;
    private Map<String, List<Prohibition>> prohibitions;

    public MemoryProhibitions() {
        this.prohibitions = new HashMap<>();
        this.tx = new MemoryTx(false, 0, null);
    }

    public MemoryProhibitions(Map<String, List<Prohibition>> prohibitions) {
        this.prohibitions = prohibitions;
        this.tx = new MemoryTx(false, 0, null);
    }

    public MemoryProhibitions(Prohibitions prohibitions) throws PMException {
        this.prohibitions = prohibitions.getAll();
        this.tx = new MemoryTx(false, 0, null);
    }

    public void clear() {
        this.prohibitions.clear();
    }

    @Override
    public void create(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().prohibitions().create(label, subject, accessRightSet, intersection, containerConditions);
        }

        List<Prohibition> existingPros = prohibitions.getOrDefault(subject.getName(), new ArrayList<>());
        existingPros.add(new Prohibition(label, subject, accessRightSet, intersection, Arrays.asList(containerConditions)));
        prohibitions.put(subject.getName(), existingPros);
    }

    @Override
    public void update(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().prohibitions().update(label, subject, accessRightSet, intersection, containerConditions);
        }

        delete(label);
        create(label, subject, accessRightSet, intersection, containerConditions);
    }

    @Override
    public void delete(String label) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().prohibitions().delete(label);
        }

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
    public Map<String, List<Prohibition>> getAll() {
        Map<String, List<Prohibition>> retProhibitions = new HashMap<>();
        for (String subject : prohibitions.keySet()) {
            retProhibitions.put(subject, prohibitions.get(subject));
        }

        return retProhibitions;
    }

    @Override
    public boolean exists(String label) throws PMException {
        for (Map.Entry<String, List<Prohibition>> e : prohibitions.entrySet()) {
            for (Prohibition p : e.getValue()) {
                if (p.getLabel().equals(label)) {
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
    public Prohibition get(String label) throws PMException {
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

    public void fromJson(String json) {
        this.prohibitions = new Gson().fromJson(json, Map.class);
    }

}
