package gov.nist.csd.pm.pip.prohibitions.mysqlProhibition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.mysql.MySQLConnection;
import gov.nist.csd.pm.pip.graph.mysql.MySQLGraph;
import gov.nist.csd.pm.pip.graph.mysql.MySQLHelper;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MySQLProhibitions implements Prohibitions {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectReader reader2 = new ObjectMapper().readerFor(OperationSet.class);
    private final MySQLConnection conn;

    public MySQLProhibitions(MySQLConnection connection) {
        this.conn = connection;
    }

    public static String hashSetToJSON(Set<String> set) throws JsonProcessingException {
        return objectMapper.writeValueAsString(set);
    }

    public boolean exists(String prohibitionName) throws PIPException {
        if (prohibitionName == null || prohibitionName.isEmpty()) {
            throw new IllegalArgumentException("a null or empty name was provided");
        }
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_PROHIBITION_FROM_NAME)
                ){
            ps.setString(1, prohibitionName);
            ResultSet rs = ps.executeQuery();
            List<Integer> l = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("deny_id");
                l.add(id);
            }
            return l.size() != 0 ;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PIPException("Cannot connect to the Database" + ex);
        }
    }

    public boolean existsNode(String subject_name) throws SQLException{

        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_EXISTS_ID_NODE_ID)
        ){
            ps.setString(1, subject_name);
            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("node_id");
            }
            return id != 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Cannot connect to the Database" + ex);
        }
    }

    public int getNodeIdFromSubjectName(String subject_name) throws SQLException{

        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_EXISTS_ID_NODE_ID)
        ){
            ps.setString(1, subject_name);
            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("node_id");
            }
            return id;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Cannot connect to the Database" + ex);
        }
    }

    public boolean existsContainer(int deny_id) throws SQLException{
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_CONTAINER_DENY_ID)
        ){
            ps.setInt(1, deny_id);
            ResultSet rs = ps.executeQuery();
            int obj_att_id = 0;
            if (rs.next()) {
                obj_att_id     = rs.getInt("object_attribute_id");
            }
            return obj_att_id != 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Cannot connect to the Database" + ex);
        }
    }

    public int getProhibitionIdByProhibitionName (String prohibitionName) throws SQLException{
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_PROHIBITION_FROM_NAME)
        ){
            ps.setString(1, prohibitionName);
            ResultSet rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("deny_id");
            }
            return id;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Cannot connect to the Database" + ex);
        }
    }

    /**
     * Create a new prohibition.
     *
     * @param prohibition The prohibition to be created.
     * @throws PIPException if there is an error creating a prohibition.
     */
    @Override
    public void add(Prohibition prohibition) throws PIPException {
        if (prohibition == null) {
            throw new IllegalArgumentException("a null prohibition was received when creating a prohibition");
        }
        else if (prohibition.getName() == null || prohibition.getName().isEmpty()) {
            throw new IllegalArgumentException("a null or empty name was provided when creating a prohibition");
        }
        else if (prohibition.getSubject() == null || prohibition.getSubject().isEmpty()) {
            throw new IllegalArgumentException("a null subject was provided when creating a prohibition");
        }
        else if (exists(prohibition.getName())) {
            throw new PIPException("You cannot create the prohibition. Another prohibition with the name '" + prohibition.getName() + "' already exists");
        }

        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection con = this.conn.getConnection();

            ps = con.prepareStatement(MySQLHelper.INSERT_PROHIBITION, Statement.RETURN_GENERATED_KEYS);
            pstmt = con.prepareStatement(MySQLHelper.INSERT_CONTAINERS);

            ps.setString(1, prohibition.getName());
            //no type id yet -> 2
            ps.setInt(2, 2); //type_id
            ps.setString(3, prohibition.getSubject());

            if ( existsNode(prohibition.getSubject())) {
                ps.setInt(4, getNodeIdFromSubjectName(prohibition.getSubject())); //user_attribute_id
                ps.setString(5, null); //process_id null if node exists
            } else {
                ps.setString(5, prohibition.getSubject()); //process_id
                ps.setString(4, null);
            }

            ps.setInt(6, prohibition.isIntersection() ? 1 : 0 );

            if (prohibition.getOperations() == null) {
                ps.setString(7, null);
            } else {
                try {
                ps.setString(7, hashSetToJSON(prohibition.getOperations()));
                } catch (JsonProcessingException j) {
                    j.printStackTrace();
                }
            }

            ps.executeUpdate();
            ResultSet rs_id = ps.getGeneratedKeys();
            int deny_id = 0;
            if(rs_id.next()) {
                deny_id = rs_id.getInt(1);
            }
            //====================  INSERT NEW CONTAINERS ====================
            for (Map.Entry<String,Boolean> container : prohibition.getContainers().entrySet()) {
                pstmt.setInt(1, deny_id);
                MySQLGraph graph = new MySQLGraph(this.conn);
                if (graph.exists(graph.getNodeNameFromId(Integer.parseInt(container.getKey())))) {
                    pstmt.setInt(2, Integer.parseInt(container.getKey()));
                    pstmt.setInt(3, container.getValue() ? 1 : 0);
                    pstmt.executeUpdate();
                } else {
                    throw new PIPException("The object attribute "+container.getKey()+" does not exist in the graph");
                }
            }
            con.close();
        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            try {
                if(rs != null) {rs.close();}
                if(ps != null) {ps.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Get a list of all prohibitions
     *
     * @return a list of all prohibitions
     * @throws PMException if there is an error getting the prohibitions.
     */
    @Override
    public List<Prohibition> getAll() throws PIPException {
        try (
                Connection con = this.conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_ALL_PROHIBITION)
        ) {
            OperationSet operations_set = new OperationSet();
            List<Prohibition> prohibitions = new ArrayList<>();

            while (rs.next()) {
                int id                  = rs.getInt("deny_id");
                String prohibition_name = rs.getString("deny_name");
                String subject_name     = rs.getString("subject_name");
                boolean is_intersection = rs.getBoolean("is_intersection");
                String deny_operations  = rs.getString("deny_operations");

                if (deny_operations != null) {
                    try {
                        operations_set = reader2.readValue(deny_operations);
                    } catch (JsonProcessingException j) {
                        j.printStackTrace();
                    }
                }

                Prohibition p = new Prohibition.Builder(prohibition_name, subject_name, operations_set)
                        .setIntersection(is_intersection)
                        .build();

                if (existsContainer(id)) {

                    List<Integer> object_attribute_id_c = new ArrayList<>();
                    List<Integer> object_complement_c = new ArrayList<>();

                    int object_attribute_id = 0;
                    int object_complement = 0;
                    try (Statement statement = con.createStatement();
                         ResultSet rs_container = statement.executeQuery(MySQLHelper.SELECT_CONTAINER_DENY_ID_SIMPLE + id)){

                        while (rs_container.next()) {

                            object_complement = rs_container.getInt("object_complement");
                            object_attribute_id = rs_container.getInt("object_attribute_id");
                            object_attribute_id_c.add(object_attribute_id);
                            object_complement_c.add(object_complement);
                        }
                    }

                    for (int i=0; i < object_attribute_id_c.size(); i++) {
                        p.addContainer(String.valueOf(object_attribute_id_c.get(i)), object_complement_c.get(i) ==1);
                    }

                    prohibitions.add(p);

                } else {
                    prohibitions.add(p);
                }
            }

            return prohibitions;
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieve a Prohibition and return the Object representing it.
     *
     * @param prohibitionName The name of the Prohibition to retrieve.
     * @return the Prohibition with the given name.
     * @throws PIPException if there is an error getting the prohibition with the given name.
     */
    @Override
    public Prohibition get(String prohibitionName) throws PIPException {
        List<Prohibition> ps = getAll();
            for(Prohibition p : ps) {
                if(p.getName().equalsIgnoreCase(prohibitionName)) {
                    return p;
                }
            }
        throw new PIPException(String.format("a prohibition does not exist with the name %s", prohibitionName));
    }

    /**
     * Get all of the prohibitions a given entity is the direct subject of.  The subject can be a user, user attribute,
     * or process.
     *
     * @param subject the name of the subject to get the prohibitions for.
     * @return The list of prohibitions the given entity is the subject of.
     */
    @Override
    public List<Prohibition> getProhibitionsFor(String subject) throws PIPException {
        List<Prohibition> ps = getAll();
        List<Prohibition> prohibitionList = new ArrayList<>();
        for(Prohibition p : ps) {
            if(p.getSubject().equalsIgnoreCase(subject)) {
                prohibitionList.add(p);
            }
        }
        return prohibitionList;
    }

    /**
     * Update the prohibition with the given name. Prohibition names cannot be updated.
     *
     * @param prohibitionName the name of the prohibition to update.
     * @param prohibition     The prohibition to update.
     * @throws PIPException if there is an error updating the prohibition.
     */
    @Override
    public void update(String prohibitionName, Prohibition prohibition) throws PIPException {
        if (prohibition == null) {
            throw new IllegalArgumentException("a null prohibition was provided when updating a prohibition");
        } else if (prohibitionName == null || prohibitionName.isEmpty()) {
            throw new IllegalArgumentException("cannot update a prohibition with a null name");
        } else if (exists(prohibition.getName())) {
            throw new PIPException("Another prohibition with the same name (" + prohibition.getName() + ") already exists.");
        }

        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.UPDATE_PROHIBITION);
                PreparedStatement psCont = con.prepareStatement(MySQLHelper.INSERT_CONTAINERS);
                PreparedStatement preparedStatement = con.prepareStatement(MySQLHelper.DELETE_PROHIBITION_CONTAINER)

        ) {

            ps.setString(1, prohibition.getName());
            ps.setString(2, prohibition.getSubject());

            if (existsNode(prohibition.getSubject())) {
                ps.setInt(3, getNodeIdFromSubjectName(prohibition.getSubject())); //user_attribute_id
                ps.setString(4, null); //process_id null if node exists
            } else {
                ps.setString(3, null);
                ps.setString(4, prohibition.getSubject()); //process_id
            }

            ps.setInt(5, prohibition.isIntersection() ? 1 : 0 );
            if (prohibition.getOperations() == null) {
                ps.setString(6, null);
            } else {
                try {
                    ps.setString(6, hashSetToJSON(prohibition.getOperations()));
                } catch (JsonProcessingException j) {
                    j.printStackTrace();
                }
            }

            ps.setString(7, prohibitionName);
            ps.executeUpdate();

            //delete container before adding new ones
            preparedStatement.setInt(1, getProhibitionIdByProhibitionName(prohibition.getName()));
            preparedStatement.executeUpdate();

            //adding new container
            for (Map.Entry<String,Boolean> entry : prohibition.getContainers().entrySet()) {
                psCont.setInt(1, getProhibitionIdByProhibitionName(prohibition.getName()));
                psCont.setString(2, entry.getKey());
                psCont.setBoolean(3, entry.getValue());
                psCont.executeUpdate();
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }
    /**
     * Delete the prohibition, and remove it from the data structure.
     *
     * @param prohibitionName The name of the prohibition to delete.
     * @throws PIPException if there is an error deleting the prohibition.
     */
    @Override
    public void delete(String prohibitionName) throws PIPException {
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement pstmt = con.prepareStatement(MySQLHelper.DELETE_PROHIBITION);
                PreparedStatement ps = con.prepareStatement(MySQLHelper.DELETE_PROHIBITION_CONTAINER)
        ){

            pstmt.setString(1, prohibitionName);
            if (exists(prohibitionName)) {
                ps.setInt(1, getProhibitionIdByProhibitionName(prohibitionName));
                ps.executeUpdate();
            } else {
                throw new PIPException(String.format("a prohibition does not exist with the name %s", prohibitionName));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new PIPException(e.getMessage());
        }
    }
}
