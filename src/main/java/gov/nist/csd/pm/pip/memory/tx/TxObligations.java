package gov.nist.csd.pm.pip.memory.tx;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.memory.tx.cmd.obligations.DeleteObligationTxCmd;
import gov.nist.csd.pm.pip.memory.tx.cmd.obligations.SetEnableTxCmd;
import gov.nist.csd.pm.pip.memory.tx.cmd.obligations.UpdateObligationTxCmd;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;
import gov.nist.csd.pm.pip.memory.tx.cmd.obligations.AddObligationTxCmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TxObligations implements Obligations {

    private Obligations targetObligations;
    private List<TxCmd> cmds;
    private Map<String, Obligation> txObligations;

    public TxObligations(Obligations obligations) {
        this.targetObligations = obligations;
        this.cmds = new ArrayList<>();
        this.txObligations = new HashMap<>();
    }

    @Override
    public void add(Obligation obligation, boolean enable) throws PMException {
        if (targetObligations.get(obligation.getLabel()) != null) {
            throw new PMException("obligation already exists with label " + obligation.getLabel());
        }

        this.cmds.add(new AddObligationTxCmd(targetObligations, obligation, enable));
        this.txObligations.put(obligation.getLabel(), obligation);
    }

    @Override
    public Obligation get(String label) throws PMException {
        Obligation obligation = targetObligations.get(label);
        if (obligation == null) {
            obligation = txObligations.get(label);
        }
        return obligation;
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        List<Obligation> all = new ArrayList<>(targetObligations.getAll());
        all.addAll(new ArrayList<>(txObligations.values()));
        return all;
    }

    @Override
    public void update(String label, Obligation obligation) throws PMException {
        cmds.add(new UpdateObligationTxCmd(targetObligations, label, obligation));
        txObligations.put(label, obligation);
    }

    @Override
    public void delete(String label) throws PMException {
        cmds.add(new DeleteObligationTxCmd(targetObligations, label));
        txObligations.remove(label);
    }

    @Override
    public void setEnable(String label, boolean enabled) throws PMException {
        cmds.add(new SetEnableTxCmd(targetObligations, label, enabled));
    }

    @Override
    public List<Obligation> getEnabled() throws PMException {
        List<Obligation> enabled = targetObligations.getEnabled();
        for (Obligation o : txObligations.values()) {
            if (o.isEnabled()) {
                enabled.add(o);
            }
        }
        return enabled;
    }

    public void commit() throws PMException {
        for (TxCmd txCmd : cmds) {
            txCmd.commit();
        }
    }
}
