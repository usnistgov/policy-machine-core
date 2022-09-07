package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.nist.csd.pm.pap.store.ObligationsStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.ObligationDoesNotExistException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.tx.TxCommitException;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class MysqlObligationsStore extends ObligationsStore {

    private MysqlConnection connection;
    
    public MysqlObligationsStore(MysqlConnection connection) {
        this.connection = connection;
    }

    @Override
    public void create(UserContext author, String label, Rule... rules) throws MysqlPolicyException {
        String sql = """
                insert into obligation (label, author, rules) values (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ps.setString(2, MysqlPolicyStore.objectMapper.writeValueAsString(author));
            ps.setBytes(3, serializeRules(rules));

            ps.executeUpdate();
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void update(UserContext author, String label, Rule... rules) throws MysqlPolicyException, TxCommitException {
        beginTx();

        try {
            delete(label);
            create(author, label, rules);
            commit();
        } catch (MysqlPolicyException e) {
            rollback();
            throw e;
        }
    }

    @Override
    public void delete(String label) throws MysqlPolicyException {
        String sql = """
                delete from obligation where label = ?
                """;

        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public List<Obligation> getAll() throws MysqlPolicyException {
        List<Obligation> obligations = new ArrayList<>();

        String sql = """
                select label, author, rules from obligation;
                """;

        try(Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String label = rs.getString(1);
                UserContext author = MysqlPolicyStore.userCtxReader.readValue(rs.getString(2));
                Rule[] rules = deserializeRules(rs.getBlob(3).getBinaryStream().readAllBytes());

                obligations.add(new Obligation(author, label, List.of(rules)));
            }

            return obligations;
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Obligation get(String label) throws ObligationDoesNotExistException, MysqlPolicyException {
        String sql = """
                select author, rules from obligation where label = label
                """;

        try(Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.next()) {
                throw new ObligationDoesNotExistException(label);
            }

            UserContext author = MysqlPolicyStore.userCtxReader.readValue(rs.getString(1));
            Rule[] rules = deserializeRules(rs.getBlob(2).getBinaryStream().readAllBytes());

            return new Obligation(author, label, List.of(rules));
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private static byte[] serializeRules(Rule[] rules) {
        return SerializationUtils.serialize(rules);
    }

    private static Rule[] deserializeRules(byte[] b) {
        return SerializationUtils.deserialize(b);
    }

    @Override
    public void beginTx() throws MysqlPolicyException {
        connection.beginTx();
    }

    @Override
    public void commit() throws MysqlPolicyException {
        connection.commit();
    }

    @Override
    public void rollback() throws MysqlPolicyException {
        connection.rollback();
    }
}
