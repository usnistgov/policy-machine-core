package gov.nist.csd.pm.pip.tx.memory;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.tx.memory.cmd.TxCmd;
import gov.nist.csd.pm.pip.tx.memory.cmd.obligations.AddObligationTxCmd;

import java.util.ArrayList;
import java.util.List;

public class TxObligations implements Obligations {

    private Obligations targetObligations;
    private List<TxCmd> txCmds;

    public TxObligations(Obligations obligations) {
        this.targetObligations = obligations;
        this.txCmds = new ArrayList<>();
    }

    @Override
    public void add(Obligation obligation, boolean enable) throws PMException {
        this.txCmds.add(new AddObligationTxCmd(targetObligations, obligation, enable));
    }

    @Override
    public Obligation get(String label) throws PMException {
        targetObligations.get
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        return null;
    }

    @Override
    public void update(String label, Obligation obligation) throws PMException {

    }

    @Override
    public void delete(String label) throws PMException {

    }

    @Override
    public void setEnable(String label, boolean enabled) throws PMException {

    }

    @Override
    public List<Obligation> getEnabled() throws PMException {
        return null;
    }

    public void commit() {

    }
}
