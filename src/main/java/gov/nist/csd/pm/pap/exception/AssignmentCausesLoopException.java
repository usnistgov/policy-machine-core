package gov.nist.csd.pm.pap.exception;

public class AssignmentCausesLoopException extends PMException {

    public AssignmentCausesLoopException(String ascendant, String descendant) {
        super("a relation between " + ascendant + " and " + descendant + " would cause a loop in the graph");
    }

}
