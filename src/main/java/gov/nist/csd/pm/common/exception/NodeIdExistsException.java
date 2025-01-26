package gov.nist.csd.pm.common.exception;

public class NodeIdExistsException extends PMException {
	public NodeIdExistsException(long id) {
		super("node id " + id + " already exists");
	}
}
