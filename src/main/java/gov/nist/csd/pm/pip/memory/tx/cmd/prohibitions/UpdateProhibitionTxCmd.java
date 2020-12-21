package gov.nist.csd.pm.pip.memory.tx.cmd.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

public class UpdateProhibitionTxCmd implements TxCmd {

    private Prohibitions prohibitions;
    private String prohibitionName;
    private Prohibition prohibition;

    public UpdateProhibitionTxCmd(Prohibitions prohibitions, String prohibitionName, Prohibition prohibition) {
        this.prohibitions = prohibitions;
        this.prohibitionName = prohibitionName;
        this.prohibition = prohibition;
    }

    @Override
    public void commit() throws PMException {
        prohibitions.update(prohibitionName, prohibition);
    }
}
