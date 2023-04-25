package gov.nist.csd.pm.pap.mysql;

import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MysqlUserDefinedPML implements UserDefinedPML {

    private MysqlConnection connection;

    public MysqlUserDefinedPML(MysqlConnection connection) {
        this.connection = connection;
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws MysqlPolicyException {
        String sql = """
                insert into pml_function (name, bytes) values (?,?)
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
                delete from pml_function where name=?
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, functionName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        String sql = """
                select bytes from pml_function
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
    public FunctionDefinitionStatement getFunction(String name) throws PMException {
        String sql = """
                select bytes from pml_function where name = ?
                """;

        Map<String, FunctionDefinitionStatement> functionDefinitionStatements = new HashMap<>();
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);

            FunctionDefinitionStatement funcDef = null;
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                funcDef = SerializationUtils.deserialize(resultSet.getBlob(1).getBinaryStream().readAllBytes());
                functionDefinitionStatements.put(funcDef.getFunctionName(), funcDef);
            }

            resultSet.close();

            return funcDef;
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void addConstant(String constantName, Value constantValue) throws MysqlPolicyException {
        String sql = """
                insert into pml_constant (name, value) values (?,?)
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
                delete from pml_constant where name=?
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, constName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        String sql = """
                select name, value from pml_constant
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
    public Value getConstant(String name) throws PMException {
        String sql = """
                select value from pml_constant where name=?
                """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);

            ResultSet resultSet = ps.executeQuery();
            Value value = null;
            if (resultSet.next()) {
                value = SerializationUtils.deserialize(resultSet.getBlob(1).getBinaryStream().readAllBytes());
            }

            resultSet.close();

            return value;
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

}
