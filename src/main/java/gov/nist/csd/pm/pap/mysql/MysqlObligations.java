package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.exceptions.ObligationDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlObligations implements Obligations {

    private MysqlConnection connection;

    public MysqlObligations(MysqlConnection mysqlConnection) {
        this.connection = mysqlConnection;
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
    public void update(UserContext author, String label, Rule... rules) throws MysqlPolicyException {
        connection.beginTx();

        try {
            delete(label);
            create(author, label, rules);
            connection.commit();
        } catch (MysqlPolicyException e) {
            connection.rollback();
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
    public boolean exists(String label) throws PMException {
        try {
            get(label);
            return true;
        } catch (ObligationDoesNotExistException e) {
            return false;
        }
    }

    @Override
    public Obligation get(String label) throws PMException {
        String sql = """
                select author, rules from obligation where label = ?
                """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ResultSet rs = ps.executeQuery();
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



}
