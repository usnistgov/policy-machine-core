package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject.Type.*;
import static gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject.Type.USER;

public class MysqlProhibitions implements Prohibitions {

    private MysqlConnection connection;

    public MysqlProhibitions(MysqlConnection mysqlConnection) {
        this.connection = mysqlConnection;
    }

    @Override
    public void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws MysqlPolicyException {
        connection.beginTx();

        String sql;
        if (subject.type() == ProhibitionSubject.Type.PROCESS) {
            sql =
                    """
                    insert into prohibition (label, process_id, subject_type, access_rights, is_intersection) values (?,?,?,?,?)
                    """;
        } else {
            sql =
                    """
                    insert into prohibition (label, node_id, subject_type, access_rights, is_intersection) values (?,(select id from node where name = ?),?,?,?)
                    """;
        }

        int prohibitionID;
        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, label);
            ps.setString(2, subject.name());
            ps.setInt(3, getProhibitionSubjectTypeId(subject.type()));
            ps.setString(4, MysqlPolicyStore.arsetToJson(accessRightSet));
            ps.setBoolean(5, intersection);

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                prohibitionID = generatedKeys.getInt(1);
            } else {
                throw new MysqlPolicyException("could not retrieve generated prohibition id after insert");
            }
        } catch (SQLException | JsonProcessingException e) {
            connection.rollback();
            throw new MysqlPolicyException(e.getMessage());
        }

        sql = """
        insert into prohibition_container (prohibition_id, container_id, is_complement) values (?, (select id from node where name = ?), ?)
        """;
        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            for (ContainerCondition containerCondition : containerConditions) {
                ps.setInt(1, prohibitionID);
                ps.setString(2, containerCondition.name());
                ps.setInt(3, containerCondition.complement() ? 1 : 0);

                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            connection.rollback();
            throw new MysqlPolicyException(e.getMessage());
        }

        connection.commit();
    }

    private int getProhibitionSubjectTypeId(ProhibitionSubject.Type type) {
        switch (type) {
            case USER -> {
                return 1;
            }
            case USER_ATTRIBUTE -> {
                return 2;
            }
            case PROCESS -> {
                return 3;
            }
        }

        return 0;
    }

    private ProhibitionSubject.Type getProhibitionSubjectTypeFromId(int id) {
        switch (id) {
            case 1 -> {
                return USER;
            }
            case 2 -> {
                return USER_ATTRIBUTE;
            }
            case 3 -> {
                return PROCESS;
            }
        }

        return USER;
    }

    @Override
    public void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws MysqlPolicyException {
        connection.beginTx();

        try {
            deleteProhibition(label);
            createProhibition(label, subject, accessRightSet, intersection, containerConditions);
            connection.commit();
        } catch (MysqlPolicyException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public void deleteProhibition(String label) throws MysqlPolicyException {
        String sql = """
                delete from prohibition where label = ?
                """;

        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Map<String, List<Prohibition>> getProhibitions() throws MysqlPolicyException {
        String sql = """
                select id, label, (select name from node where node.id=prohibition.node_id) as node, process_id, subject_type, access_rights, is_intersection from prohibition
                """;

        try(Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            List<Prohibition> prohibitions = getProhibitionsFromResultSet(rs);

            rs.close();

            Map<String, List<Prohibition>> prohibitionsMap = new HashMap<>();
            for (Prohibition p : prohibitions) {
                List<Prohibition> subjPros = prohibitionsMap.getOrDefault(p.getSubject().name(), new ArrayList<>());
                subjPros.add(p);
                prohibitionsMap.put(p.getSubject().name(), subjPros);
            }

            return prohibitionsMap;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public boolean prohibitionExists(String label) throws PMException {
        try {
            getProhibition(label);
            return true;
        } catch (ProhibitionDoesNotExistException e) {
            return false;
        }
    }

    private List<ContainerCondition> getContainerConditions(int id) throws SQLException {
        List<ContainerCondition> containers = new ArrayList<>();
        String containerSql = """
                        select (select name from node where node.id = prohibition_container.container_id) as container, is_complement 
                        from prohibition_container 
                        where prohibition_id=?
                        """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(containerSql)) {
            ps.setInt(1, id);
            ResultSet containerRs = ps.executeQuery();
            while (containerRs.next()) {
                String container = containerRs.getString(1);
                boolean isComplement = containerRs.getBoolean(2);
                containers.add(new ContainerCondition(container, isComplement));
            }

            containerRs.close();
        }

        return containers;
    }

    @Override
    public List<Prohibition> getProhibitionsWithSubject(String subject) throws MysqlPolicyException {
        String sql = """
                select id, label, (select name from node where node.id=prohibition.node_id) as node, process_id, subject_type, access_rights, is_intersection 
                from prohibition 
                where node_id = (select id from node where name = ?) || process_id = ?
                """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, subject);
            ps.setString(2, subject);
            ResultSet rs = ps.executeQuery();

            List<Prohibition> prohibitions = getProhibitionsFromResultSet(rs);

            rs.close();

            return prohibitions;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Prohibition getProhibition(String label) throws PMException {
        String sql = """
                select id, label, (select name from node where node.id=prohibition.node_id) as node, process_id, subject_type, access_rights, is_intersection from prohibition where label = ?
                """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ResultSet rs = ps.executeQuery();

            List<Prohibition> prohibitions = getProhibitionsFromResultSet(rs);
            if (prohibitions.isEmpty()) {
                throw new ProhibitionDoesNotExistException(label);
            }

            rs.close();

            return prohibitions.get(0);
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private List<Prohibition> getProhibitionsFromResultSet(ResultSet rs) throws MysqlPolicyException {
        List<Prohibition> prohibitions = new ArrayList<>();

        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String label = rs.getString(2);
                String node = rs.getString(3);
                String process = rs.getString(4);
                ProhibitionSubject.Type type = getProhibitionSubjectTypeFromId(rs.getInt(5));
                AccessRightSet arset = MysqlPolicyStore.arsetReader.readValue(rs.getString(6));
                boolean isIntersection = rs.getBoolean(7);

                List<ContainerCondition> containers = getContainerConditions(id);

                prohibitions.add(new Prohibition(label, new ProhibitionSubject(type == PROCESS ? process : node, type), arset, isIntersection, containers));
            }

            return prohibitions;
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

}
