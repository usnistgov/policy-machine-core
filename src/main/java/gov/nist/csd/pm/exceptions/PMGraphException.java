package gov.nist.csd.pm.exceptions;

/**
 * PMGraphException covers all exceptions that can occur when interacting with an NGAC graph.  This includes invalid node types,
 * invalid assignments, invalid associations, etc.  Details of the exception are provided in the message.
 */
public class PMGraphException extends PMException {
    public PMGraphException(String msg) {
        super(msg);
    }
}
