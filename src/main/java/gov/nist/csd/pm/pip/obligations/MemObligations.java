package gov.nist.csd.pm.pip.obligations;

import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemObligations implements Obligations {

    private Map<String, Obligation> obligations;

    public MemObligations() {
        this.obligations = new HashMap<>();
    }

    @Override
    public void add(Obligation obligation, boolean enable) {
        obligation.setEnabled(true);
        obligations.put(obligation.getLabel(), obligation);
    }

    @Override
    public Obligation get(String label) {
        for(String l : obligations.keySet()) {
            if(l.equals(label)) {
                return obligations.get(l);
            }
        }

        return null;
    }

    @Override
    public List<Obligation> getAll() {
        return new ArrayList<>(obligations.values());
    }

    @Override
    public void update(String label, Obligation obligation) {
        obligations.put(label, obligation);
    }

    @Override
    public void delete(String label) {
        obligations.remove(label);
    }

    @Override
    public void setEnable(String label, boolean enabled) {
        Obligation obligation = obligations.get(label);
        obligation.setEnabled(enabled);
        update(label, obligation);
    }

    @Override
    public List<Obligation> getEnabled() {
        List<Obligation> obligations = getAll();
        obligations.removeIf((obl) -> !obl.isEnabled());
        return obligations;
    }
}
