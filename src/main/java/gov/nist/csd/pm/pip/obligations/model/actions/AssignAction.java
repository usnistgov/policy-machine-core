package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.EvrNode;

import java.util.ArrayList;
import java.util.List;

public class AssignAction extends Action {
    private List<EvrNode> what;
    private List<EvrNode> where;

    public AssignAction() {
        where = new ArrayList<>();
    }

    public List<EvrNode> getWhat() {
        return what;
    }

    public void setWhat(List<EvrNode> what) {
        this.what = what;
    }

    public List<EvrNode> getWhere() {
        return where;
    }

    public void setWhere(List<EvrNode> where) {
        this.where = where;
    }

    public void addWhere(EvrNode evrNode) {
        this.where.add(evrNode);
    }
}
