package gov.nist.csd.pm.pip.memory;

import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MemObligations implements Obligations {

    private Map<String, Obligation> obligations;
    public Lock lock;

    public MemObligations() {
        this.obligations = new HashMap<>();

        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.lock = lock.writeLock();
    }

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }

    @Override
    public void add(Obligation obligation, boolean enable) {
        obligation.setEnabled(true);
        obligations.put(obligation.getLabel(), obligation);
    }

    @Override
    public Obligation get(String label) {
        return obligations.get(label);
    }

    @Override
    public List<Obligation> getAll() {
        return new ArrayList<>(obligations.values());
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

        obligations.put(obligation.getLabel(), obligation);
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
