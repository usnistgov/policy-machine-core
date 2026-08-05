package gov.nist.ngac.pm.core.common.exception;

public class NodeIdExistsException extends PMException {
	public NodeIdExistsException(long id) {
		super("node node " + id + " already exists");
	}
}
