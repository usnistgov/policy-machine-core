package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.policy.exceptions.PMException;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class MysqlTestEnv {

    public static final String EXECUTION_PROPERTY = "execution";
    public static final String CONNECTION_PROPERTY = "connectionUrl";
    public static final String USER_PROPERTY = "user";
    public static final String PASSWORD_PROPERTY = "password";
    public static final String EXTERNAL_EXECUTION = "external";
    public static final String CONTAINER_EXECUTION = "container";
    public static final DockerImageName MYSQL_8_IMAGE = DockerImageName.parse("mysql:8.0.24");

    @Container
    private MySQLContainer<?> container;

    private String connectionUrl;
    private String user;
    private String password;

    public MysqlTestEnv() throws IOException, PMException {
        Properties properties = new Properties();
        try(InputStream is = getClass().getResourceAsStream("/sql/db.properties")) {
            properties.load(is);
        }

        if (!properties.containsKey(EXECUTION_PROPERTY)
                || !(properties.get(EXECUTION_PROPERTY).equals(EXTERNAL_EXECUTION)
                || properties.get(EXECUTION_PROPERTY).equals(CONTAINER_EXECUTION))) {
            throw new PMException("mysql tests expect the 'execution' property to be set " +
                    "in /resources/sql/db/properties to 'external' or 'container'");
        }

        if (properties.get(EXECUTION_PROPERTY).equals(EXTERNAL_EXECUTION)) {
            if (!(properties.containsKey(CONNECTION_PROPERTY)
                    && properties.containsKey(USER_PROPERTY)
                    && properties.containsKey(PASSWORD_PROPERTY))) {
                throw new PMException("using an external database for mysql tests expects " +
                        "the 'connectionUrl', 'user', and 'password' properties to be set in " +
                        "/resources/sql/db/properties");
            }

            this.connectionUrl = properties.getProperty(CONNECTION_PROPERTY);
            this.user = properties.getProperty(USER_PROPERTY);
            this.password = properties.getProperty(PASSWORD_PROPERTY);
        } else {
            this.container = new MySQLContainer<>(MYSQL_8_IMAGE)
                    .withDatabaseName("pm_core")
                    .withUsername("test")
                    .withPassword("test")
                    .withEnv("MYSQL_ROOT_PASSWORD", "test");
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        if (container == null) {
            return DriverManager.getConnection(connectionUrl, user, password);
        } else {
            return DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        }
    }

    public synchronized void start() {
        if (container != null) {
            container.start();
        }

        try(InputStream is = getClass().getResourceAsStream("/sql/pm_core.sql")) {
            String sql = new String(is.readAllBytes());
            String[] split = sql.split(";");
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            for (String s : split) {
                stmt.execute(s);
            }

            conn.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void stop() {
        if (container == null) {
            return;
        }

        container.stop();
    }

    public synchronized void reset() throws SQLException {
        Connection connection = getConnection();
        List<String> sequence = PolicyResetSequence.getSequence();
        try (Statement stmt = connection.createStatement()) {
            for (String s : sequence) {
                stmt.executeUpdate(s);
            }
        }
        connection.close();
    }

    public String getConnectionUrl() {
        return container == null ? connectionUrl : container.getJdbcUrl();
    }

    public String getUser() {
        return container == null ? user : container.getUsername();
    }

    public String getPassword() {
        return container == null ? password : container.getPassword();
    }

}
