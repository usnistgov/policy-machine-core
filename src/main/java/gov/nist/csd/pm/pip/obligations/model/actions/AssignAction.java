package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.EvrNode;

import java.util.ArrayList;
import java.util.List;

public class AssignAction extends Action {

    private List<Assignment> assignments;

    public AssignAction() {
        assignments = new ArrayList<>();
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }

    public static class Assignment {
        private EvrNode what;
        private EvrNode where;

        public Assignment(EvrNode what, EvrNode where) {
            this.what = what;
            this.where = where;
        }

        public EvrNode getWhat() {
            return what;
        }

        public void setWhat(EvrNode what) {
            this.what = what;
        }

        public EvrNode getWhere() {
            return where;
        }

        public void setWhere(EvrNode where) {
            this.where = where;
        }
    }
}
