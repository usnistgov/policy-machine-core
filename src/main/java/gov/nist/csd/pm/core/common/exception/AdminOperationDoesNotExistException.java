package gov.nist.csd.pm.core.common.exception;

public class AdminOperationDoesNotExistException extends PMException{
	public AdminOperationDoesNotExistException(String opName) {
		super("PM admin operation " + opName + " does not exist");
	}
}
