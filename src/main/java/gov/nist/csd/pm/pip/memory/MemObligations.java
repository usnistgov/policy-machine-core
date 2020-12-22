package gov.nist.csd.pm.pip.memory;

import gov.nist.csd.pm.pip.obligations.Obligations;
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
        obligations.put(obligation.getLabel(), new Obligation(obligation));
    }

    @Override
    public Obligation get(String label) {
        Obligation obligation = obligations.get(label);
        return obligation != null ? new Obligation(obligation) : null;
    }

    @Override
    public List<Obligation> getAll() {
        ArrayList<Obligation> all = new ArrayList<>();
        for (String label : obligations.keySet()) {
            all.add(get(label));
        }
        return all;
    }

    @Override
    public void update(String label, Obligation obligation) {
        String updatedLabel = obligation.getLabel();
        if (updatedLabel != null && !updatedLabel.equals(label)) {
            obligations.remove(label);
        } else if (updatedLabel == null) {
            // update the obligations label with the provided label if it's not set
            obligation.setLabel(label);
        }

        obligations.put(obligation.getLabel(), new Obligation(obligation));
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
