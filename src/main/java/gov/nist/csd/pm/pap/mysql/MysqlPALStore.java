package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.pap.store.PALStore;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class MysqlPALStore extends PALStore {

    private MysqlConnection connection;

    public MysqlPALStore(MysqlConnection connection) {
        this.connection = connection;
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws MysqlPolicyException {
        String sql = """
                insert into pal_function (name, bytes) values (?,?)
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, functionDefinitionStatement.getFunctionName());
            ps.setBytes(2, SerializationUtils.serialize(functionDefinitionStatement));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void removeFunction(String functionName) throws MysqlPolicyException {
        String sql = """
                delete from pal_function where name=?
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, functionName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws MysqlPolicyException {
        String sql = """
                select bytes from pal_function
                """;

        Map<String, FunctionDefinitionStatement> functionDefinitionStatements = new HashMap<>();
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                FunctionDefinitionStatement funcDef = SerializationUtils.deserialize(resultSet.getBlob(1).getBinaryStream().readAllBytes());
                functionDefinitionStatements.put(funcDef.getFunctionName(), funcDef);
            }
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return functionDefinitionStatements;
    }

    @Override
    public void addConstant(String constantName, Value constantValue) throws MysqlPolicyException {
        String sql = """
                insert into pal_constant (name, value) values (?,?)
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, constantName);
            ps.setBytes(2, SerializationUtils.serialize(constantValue));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void removeConstant(String constName) throws MysqlPolicyException {
        String sql = """
                delete from pal_constant where name=?
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, constName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Map<String, Value> getConstants() throws MysqlPolicyException {
        String sql = """
                select name, value from pal_constant
                """;

        Map<String, Value> constants = new HashMap<>();
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                String key = resultSet.getString(1);
                Value value = SerializationUtils.deserialize(resultSet.getBlob(2).getBinaryStream().readAllBytes());
                constants.put(key, value);
            }
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return constants;
    }

    @Override
    public PALContext getContext() throws MysqlPolicyException {
        return new PALContext(getFunctions(), getConstants());
    }

    @Override
    public void beginTx() throws MysqlPolicyException {
        connection.beginTx();
    }

    @Override
    public void commit() throws PMException {
        connection.commit();
    }

    @Override
    public void rollback() throws MysqlPolicyException {
        connection.rollback();
    }
}
