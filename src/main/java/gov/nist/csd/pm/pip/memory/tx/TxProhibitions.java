package gov.nist.csd.pm.pip.memory.tx;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.memory.tx.cmd.TxCmd;
import gov.nist.csd.pm.pip.memory.tx.cmd.prohibitions.AddProhibitionTxCmd;
import gov.nist.csd.pm.pip.memory.tx.cmd.prohibitions.DeleteProhibitionTxCmd;
import gov.nist.csd.pm.pip.memory.tx.cmd.prohibitions.UpdateProhibitionTxCmd;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.ArrayList;
import java.util.List;

public class TxProhibitions implements Prohibitions {

    private Prohibitions targetProhibitions;
    private List<Prohibition> prohibitions;
    private List<TxCmd> cmds;

    public TxProhibitions(Prohibitions prohibitions) {
        this.targetProhibitions = prohibitions;
        this.cmds = new ArrayList<>();
        this.prohibitions = new ArrayList<>();
    }

    @Override
    public void add(Prohibition prohibition) throws PMException {
        cmds.add(new AddProhibitionTxCmd(targetProhibitions, prohibition));
        prohibitions.add(prohibition);
    }

    @Override
    public List<Prohibition> getAll() throws PMException {
        List<Prohibition> all = targetProhibitions.getAll();
        for (Prohibition prohibition : prohibitions) {
            all.add(new Prohibition(prohibition));
        }

        return all;
    }

    @Override
    public Prohibition get(String prohibitionName) throws PMException {
        Prohibition prohibition = targetProhibitions.get(prohibitionName);
        if (prohibition == null) {
            for (Prohibition p : prohibitions) {
                if (p.getName().equals(prohibitionName)) {
                    prohibition = p;
                }
            }
        }

        return prohibition;
    }

    @Override
    public List<Prohibition> getProhibitionsFor(String subject) throws PMException {
        List<Prohibition> ret = targetProhibitions.getProhibitionsFor(subject);
        for (Prohibition p : prohibitions) {
            if (p.getSubject().equals(subject)) {
                ret.add(p);
            }
        }

        return ret;
    }

    @Override
    public void update(String prohibitionName, Prohibition prohibition) throws PMException {
        cmds.add(new UpdateProhibitionTxCmd(targetProhibitions, prohibitionName, prohibition));
        for (Prohibition p : prohibitions) {
            if (p.getName().equals(prohibitionName)) {
                p = prohibition;
            }
        }
    }

    @Override
    public void delete(String prohibitionName) throws PMException {
        cmds.add(new DeleteProhibitionTxCmd(targetProhibitions, prohibitionName));
        Prohibition remove = null;
        for (Prohibition p : prohibitions) {
            if (p.getName().equals(prohibitionName)) {
                remove = p;
            }
        }

        if (remove != null) {
            prohibitions.remove(remove);
        }
    }

    public void commit() throws PMException {
        for (TxCmd txCmd : cmds) {
            txCmd.commit();
        }
    }
}
