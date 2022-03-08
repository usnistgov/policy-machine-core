package gov.nist.csd.pm.pip.mysql;

import gov.nist.csd.pm.exceptions.PIPException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_SCHEMA = "policydb_core";

    private String url;
    private String user;
    private String pass;


    public MySQLConnection(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public Connection getConnection() throws PIPException {
        Connection con;
        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(url, user, pass);
            return con;
        } catch (SQLException | ClassNotFoundException ex) {
            throw new PIPException(ex.getMessage());
        }
    }
}
