package gov.nist.csd.pm.pap.memory;

import com.google.gson.Gson;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.exceptions.ObligationDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MemoryObligations implements Obligations, Serializable {

    protected MemoryTx tx;
    private List<Obligation> obligations;

    public MemoryObligations() {
        this.obligations = new ArrayList<>();
        this.tx = new MemoryTx(false, 0, null);
    }

    public MemoryObligations(List<Obligation> obligations) {
        this.obligations = obligations;
        this.tx = new MemoryTx(false, 0, null);
    }

    public MemoryObligations(Obligations obligations) throws PMException {
        this.obligations = obligations.getObligations();
        this.tx = new MemoryTx(false, 0, null);
    }

    @Override
    public void createObligation(UserContext author, String label, Rule... rules) {
        if (tx.active()) {
            tx.policyStore().createObligation(author, label, rules);
        }

        obligations.add(new Obligation(author, label, Arrays.asList(rules)));
    }

    @Override
    public void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
        if (tx.active()) {
            tx.policyStore().updateObligation(author, label, rules);
        }

        for (Obligation o : obligations) {
            if (o.getLabel().equals(label)) {
                o.setAuthor(author);
                o.setLabel(label);
                o.setRules(List.of(rules));
            }
        }
    }

    @Override
    public void deleteObligation(String label) throws PMException {
        if (tx.active()) {
            tx.policyStore().deleteObligation(label);
        }

        this.obligations.removeIf(o -> o.getLabel().equals(label));
    }

    @Override
    public List<Obligation> getObligations() {
        return new ArrayList<>(obligations);
    }

    @Override
    public boolean obligationExists(String label) throws PMException {
        for (Obligation o : obligations) {
            if (o.getLabel().equals(label)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Obligation getObligation(String label) throws PMException {
        for (Obligation obligation : obligations) {
            if (obligation.getLabel().equals(label)) {
                return obligation.clone();
            }
        }

        throw new ObligationDoesNotExistException(label);
    }

    public void fromJson(String json) {
        this.obligations = new Gson().fromJson(json, List.class);
    }
}
