package gov.nist.csd.pm.pip.memory.tx.cmd.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class DeleteProhibitionTxCmd implements TxCmd {

    private Prohibitions prohibitions;
    private String prohibitionName;

    public DeleteProhibitionTxCmd(Prohibitions prohibitions, String prohibitionName) {
        this.prohibitions = prohibitions;
        this.prohibitionName = prohibitionName;
    }

    @Override
    public void commit() throws PMException {
        prohibitions.delete(prohibitionName);
    }
}
