package gov.nist.csd.pm.pip.memory.tx.cmd.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

public class AddProhibitionTxCmd implements TxCmd {

    private Prohibitions prohibitions;
    private Prohibition prohibition;

    public AddProhibitionTxCmd(Prohibitions prohibitions, Prohibition prohibition) {
        this.prohibitions = prohibitions;
        this.prohibition = prohibition;
    }

    @Override
    public void commit() throws PMException {
        prohibitions.add(prohibition);
    }
}
