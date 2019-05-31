package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.EvrNode;
import gov.nist.csd.pm.pip.obligations.model.Rule;

import java.util.ArrayList;
import java.util.List;

public class CreateAction extends Action {
    private List<EvrNode> what;
    private List<EvrNode> where;
    private Rule          rule;

    public CreateAction(Rule rule) {
        this.rule = rule;
    }

    public CreateAction() {
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

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
