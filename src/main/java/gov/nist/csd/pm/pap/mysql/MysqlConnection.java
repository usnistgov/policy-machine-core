package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.policy.tx.Transactional;

import java.sql.Connection;
import java.sql.SQLException;

class MysqlConnection implements Transactional {
    private Connection connection;
    private int txCounter;

    public MysqlConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void beginTx() throws MysqlPolicyException {
        try {
            connection.setAutoCommit(false);

            txCounter++;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void commit() throws MysqlPolicyException {
        if (txCounter != 1) {
            txCounter--;
            return;
        }

        try {
            connection.setAutoCommit(true);
            txCounter--;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new MysqlPolicyException(rollbackEx.getMessage());
            }

            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void rollback() throws MysqlPolicyException {
        try {
            if (!connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
            txCounter = 0;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }
}
