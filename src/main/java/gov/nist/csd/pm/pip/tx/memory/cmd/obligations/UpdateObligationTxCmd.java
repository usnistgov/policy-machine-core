package gov.nist.csd.pm.pip.tx.memory.cmd.obligations;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.tx.memory.cmd.TxCmd;

public class UpdateObligationTxCmd implements TxCmd {

    private Obligations obligations;
    private String label;
    private Obligation obligation;
    private Obligation oldObligation;

    public UpdateObligationTxCmd(Obligations obligations, String label, Obligation obligation) {
        this.obligations = obligations;
        this.label = label;
        this.obligation = obligation;
    }

    @Override
    public void commit() throws PMException {
        this.oldObligation = this.obligations.get(label);
        this.obligations.update(label, obligation);
    }

    @Override
    public void rollback() throws PMException {
        this.obligations.update(label, oldObligation);
    }
}