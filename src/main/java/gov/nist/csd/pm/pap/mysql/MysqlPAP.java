package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MysqlPAP extends PAP {

    private Connection connection;

    public MysqlPAP(Connection connection) throws PMException {
        super(new MysqlConnection(connection), true);
        this.connection = connection;
    }

    private MysqlPAP(Connection connection, boolean verifySuperPolicy) throws PMException {
        super(new MysqlConnection(connection), verifySuperPolicy);
        this.connection = connection;
    }

    @Override
    public void fromPAL(UserContext author, String input, FunctionDefinitionStatement... customFunctions) throws PMException {
        resetPolicy();
        PALExecutor.execute(this, author, input, customFunctions);
        this.init(policyStore, true);
    }

    private void resetPolicy() throws PMException {
        List<String> sequence = PolicyResetSequence.getSequence();
        try (Statement stmt = connection.createStatement()) {
            for (String s : sequence) {
                stmt.executeUpdate(s);
            }
        } catch (SQLException e) {
            throw new PMException(e.getMessage());
        }
    }
}
