package gov.nist.csd.pm.policy.exceptions;

public class AssignmentCausesLoopException extends PMException{

    public AssignmentCausesLoopException(String child, String parent) {
        super("a relation between " + child + " and " + parent + " would cause a loop in the graph");
    }

}
