package gov.nist.csd.pm.exceptions;

/**
 * The purpose of this exception is to provide a generic exception for all database implementations of Policy Machine
 * interfaces. For example, in the Neo4j implementation of the Graph interface, the JDBC connector throws a SQLException
 * if the connection fails.  Instead of throwing the SQLException, catch it, and throw a PMDBException with the exception
 * message.
 */
public class PMDBException extends PMException {
    public PMDBException(String msg) {
        super(msg);
    }
}
