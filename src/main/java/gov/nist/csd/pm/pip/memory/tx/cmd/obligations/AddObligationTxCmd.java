package gov.nist.csd.pm.pip.memory.tx.cmd.obligations;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;

public class AddObligationTxCmd implements TxCmd {

    private Obligations obligations;
    private Obligation obligation;
    private boolean enabled;

    public AddObligationTxCmd(Obligations obligations, Obligation obligation, boolean enabled) {
        this.obligations = obligations;
        this.obligation = obligation;
        this.enabled = enabled;
    }

    @Override
    public void commit() throws PMException {
        this.obligations.add(obligation, enabled);
    }
}
