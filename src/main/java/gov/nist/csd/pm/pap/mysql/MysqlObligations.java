package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.nist.csd.pm.pap.ObligationsStore;
import gov.nist.csd.pm.policy.Obligations;
import gov.nist.csd.pm.policy.exceptions.*;
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

public class MysqlObligations implements ObligationsStore {

    private MysqlConnection connection;

    public MysqlObligations(MysqlConnection mysqlConnection) {
        this.connection = mysqlConnection;
    }

    @Override
    public void create(UserContext author, String id, Rule... rules)
    throws PMBackendException, ObligationIdExistsException, NodeDoesNotExistException {
        checkCreateInput(new MysqlGraph(connection), author, id, rules);

        String sql = """
                insert into obligation (id, author, rules) values (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, MysqlPolicyStore.objectMapper.writeValueAsString(author));
            ps.setBytes(3, serializeRules(rules));

            ps.executeUpdate();
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void update(UserContext author, String id, Rule... rules)
    throws PMBackendException, ObligationDoesNotExistException, NodeDoesNotExistException {
        checkUpdateInput(new MysqlGraph(connection), author, id, rules);

        connection.beginTx();

        try {
            delete(id);

            try {
                create(author, id, rules);
            } catch (ObligationIdExistsException e) {
                throw new PMBackendException(e);
            }

            connection.commit();
        } catch (MysqlPolicyException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public void delete(String id) throws MysqlPolicyException {
        String sql = """
                delete from obligation where id = ?
                """;

        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public List<Obligation> getAll() throws MysqlPolicyException {
        List<Obligation> obligations = new ArrayList<>();

        String sql = """
                select id, author, rules from obligation;
                """;

        try(Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString(1);
                UserContext author = MysqlPolicyStore.userCtxReader.readValue(rs.getString(2));
                Rule[] rules = deserializeRules(rs.getBlob(3).getBinaryStream().readAllBytes());

                obligations.add(new Obligation(author, id, List.of(rules)));
            }

            return obligations;
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public boolean exists(String id) throws MysqlPolicyException {
        try {
            get(id);
            return true;
        } catch (ObligationDoesNotExistException e) {
            return false;
        }
    }

    @Override
    public Obligation get(String id) throws ObligationDoesNotExistException, MysqlPolicyException {
        String sql = """
                select author, rules from obligation where id = ?
                """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new ObligationDoesNotExistException(id);
            }

            UserContext author = MysqlPolicyStore.userCtxReader.readValue(rs.getString(1));
            Rule[] rules = deserializeRules(rs.getBlob(2).getBinaryStream().readAllBytes());

            return new Obligation(author, id, List.of(rules));
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
