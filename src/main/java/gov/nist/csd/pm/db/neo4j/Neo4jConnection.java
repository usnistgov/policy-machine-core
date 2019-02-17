package gov.nist.csd.pm.db.neo4j;

import gov.nist.csd.pm.exceptions.PMDBException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Object that stores a connection to a neo4j database.
 */
public class Neo4jConnection {

    private String host;
    private int port;
    private String username;
    private String password;

    /**
     * Establishes a new connection to a Neo4j database.
     * @param host the hostname of the Neo4j instance.
     * @param port the port the Neo4j instance is running on.
     * @param username the name of the Neo4j user.
     * @param password the password of the Neo4j user.
     * @throws PMDBException if there is an error connecting to the Neo4j instance.
     */
    public Neo4jConnection(String host, int port, String username, String password) throws PMDBException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        try {
            Driver driver = new org.neo4j.jdbc.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * @return the connection to the Neo4j instance.
     * @throws PMDBException if there is an error establishing the connection.
     */
    public Connection getConnection() throws PMDBException {
        try {
            return DriverManager.getConnection("jdbc:neo4j:bolt://" + host + ":" + port + "", username, password);
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }
}
