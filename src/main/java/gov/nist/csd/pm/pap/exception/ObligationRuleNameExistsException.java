package gov.nist.csd.pm.pap.exception;

public class ObligationRuleNameExistsException extends PMException {
    public ObligationRuleNameExistsException(String obligationName, String ruleName) {
        super("A rule with the name " + ruleName + " already exists in the obligation " + obligationName);
    }
}
