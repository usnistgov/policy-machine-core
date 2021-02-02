package gov.nist.csd.pm.pip.prohibitions.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
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
import java.util.stream.Collectors;

public class MySQLProhibitions implements Prohibitions {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectReader reader2 = new ObjectMapper().readerFor(OperationSet.class);
    private final MySQLConnection conn;


    public MySQLProhibitions(MySQLConnection connection) throws PIPException {
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
            throw new PIPException("prohibitions", ex.getMessage());
        }
    }

    public boolean nodeExists(String subject_name) throws PIPException{

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
            throw new PIPException("prohibitions", ex.getMessage());
        }
    }

    public int getNodeIdFromSubjectName(String subject_name) throws PIPException{

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
            throw new PIPException("prohibitions", ex.getMessage());
        }
    }

    public String getNodeNameFromNodeID(int id) throws PIPException {
        MySQLConnection connection = new MySQLConnection();
        MySQLGraph mySQLGraph = new MySQLGraph(connection);
        return mySQLGraph.getNodeNameFromId(id);
    }

    public boolean existsContainer(int deny_id) throws PIPException{
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
            throw new PIPException("prohibitions", ex.getMessage());
        }
    }

    public int getProhibitionIdByProhibitionName (String prohibitionName) throws PIPException{
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
            throw new PIPException("prohibitions", ex.getMessage());
        }
    }

    public int getTypeByNodeId (int node_id) throws PIPException {
        MySQLGraph graph = new MySQLGraph(new MySQLConnection());

        if (graph.exists(graph.getNodeNameFromId(node_id))) {

            try (
                    Connection con = this.conn.getConnection();
                    PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_NODE_TYPE_FROM_NODE_TYPE)) {

                Node node = graph.getNode(graph.getNodeNameFromId(node_id));
                String node_type_id_name = node.getType().toString();
                ps.setString(1, node_type_id_name);
                ResultSet rs = ps.executeQuery();
                int deny_type_id = 0;
                String description = "";
                while (rs.next()) {
                    description = rs.getString("description");
                }

                switch (description) {
                    case "User":
                        deny_type_id = 1;
                        break;
                    case "User Attribute":
                        deny_type_id = 2;
                        break;
                    default:
                        deny_type_id = 3;
                }

                return deny_type_id;
            } catch (SQLException s) {
                throw new PIPException("prohibitions", s.getMessage());
            }
        }
        return 0;
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
            throw new PIPException("prohibitions", "You cannot create the prohibition. Another prohibition with the name '" + prohibition.getName() + "' already exists");
        }

        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Connection con = this.conn.getConnection();

            ps = con.prepareStatement(MySQLHelper.INSERT_PROHIBITION, Statement.RETURN_GENERATED_KEYS);
            pstmt = con.prepareStatement(MySQLHelper.INSERT_CONTAINERS);

            ps.setString(1, prohibition.getName());
            //no type id yet -> 1
            ps.setInt(2, 1); //type_id
            ps.setString(3, prohibition.getSubject());

            if ( nodeExists(prohibition.getSubject())) {
                ps.setInt(4, getNodeIdFromSubjectName(prohibition.getSubject())); //user_attribute_id
                ps.setString(5, null); //process_id null if node exists
                // deny_type_id = node_type_id if node_id exists
                ps.setInt(2, getTypeByNodeId(getNodeIdFromSubjectName(prohibition.getSubject())));
            } else {
                ps.setString(5, prohibition.getSubject()); //process_id
                ps.setString(4, null);
                ps.setInt(2, 3); // deny_type = process
            }

            ps.setInt(6, prohibition.isIntersection() ? 1 : 0 );

            if (prohibition.getOperations() == null) {
                ps.setString(7, null);
            } else {
                try {
                ps.setString(7, hashSetToJSON(prohibition.getOperations()));
                } catch (JsonProcessingException j) {
                    throw new PIPException("prohibitions", j.getMessage());
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
                //if (graph.exists(graph.getNodeNameFromId(Integer.parseInt(container.getKey())))) {
                if (graph.exists(container.getKey())) {

                    pstmt.setInt(2, getNodeIdFromSubjectName(container.getKey()));
                    pstmt.setInt(3, container.getValue() ? 1 : 0);
                    pstmt.executeUpdate();
                } else {
                    throw new PIPException("prohibitions", "The object attribute "+container.getKey()+" does not exist in the graph");
                }
            }
            con.close();
        } catch (SQLException s) {
            throw new PIPException("prohibitions", s.getMessage());
        } finally {
            try {
                if(rs != null) {rs.close();}
                if(ps != null) {ps.close();}
            } catch (SQLException e) {
                throw new PIPException("prohibitions", e.getMessage());
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
            List<Deny_obj_attr> containers = getAllContainers();

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
                        throw new PIPException("prohibitions", j.getMessage());
                    }
                }

                Prohibition p = new Prohibition.Builder(prohibition_name, subject_name, operations_set)
                        .setIntersection(is_intersection)
                        .build();

                //containers from getAllContainers()
                List<Deny_obj_attr> containers_curr = containers.stream()
                        .filter(container -> container.deny_id == id)
                        .collect(Collectors.toList());

                for (Deny_obj_attr deny_obj_attr : containers_curr) {
                    p.addContainer(getNodeNameFromNodeID(deny_obj_attr.obj_att_id), deny_obj_attr.obj_compl == 1);
                }
                prohibitions.add(p);
            }

            return prohibitions;
        } catch (SQLException s) {
            throw new PIPException("prohibitions", s.getMessage());
        }
    }

    public List<Deny_obj_attr> getAllContainers() throws PIPException {
        try (
                Connection con = this.conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_ALL_CONTAINERS)
        ) {
            List<Deny_obj_attr> containers = new ArrayList<>();
            while (rs.next()) {
                int deny_id = rs.getInt("deny_id");
                int obj_att_id = rs.getInt("object_attribute_id");
                int object_complement = rs.getInt("object_complement");

                Deny_obj_attr container = new Deny_obj_attr(deny_id, obj_att_id, object_complement);
                containers.add(container);
            }
            con.close();
            return containers;

        } catch (SQLException sqlException) {
            throw new PIPException("prohibitions", sqlException.getMessage());
        }
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
        throw new PIPException("prohibitions", "a prohibition does not exist with the name " + prohibitionName);
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
            throw new PIPException("prohibitions", "Another prohibition with the same name (" + prohibition.getName() + ") already exists.");
        }

        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.UPDATE_PROHIBITION);
                PreparedStatement psCont = con.prepareStatement(MySQLHelper.INSERT_CONTAINERS);
                PreparedStatement preparedStatement = con.prepareStatement(MySQLHelper.DELETE_PROHIBITION_CONTAINER)

        ) {

            ps.setString(1, prohibition.getName());
            ps.setString(2, prohibition.getSubject());

            if (nodeExists(prohibition.getSubject())) {
                ps.setInt(3, getNodeIdFromSubjectName(prohibition.getSubject())); //user_attribute_id
                ps.setString(4, null); //process_id null if node exists
                ps.setInt(7, getTypeByNodeId(getNodeIdFromSubjectName(prohibition.getSubject())));

            } else {
                ps.setString(3, null);
                ps.setString(4, prohibition.getSubject()); //process_id
                ps.setInt(7, 3); // type = process
            }

            ps.setInt(5, prohibition.isIntersection() ? 1 : 0 );
            if (prohibition.getOperations() == null) {
                ps.setString(6, null);
            } else {
                try {
                    ps.setString(6, hashSetToJSON(prohibition.getOperations()));
                } catch (JsonProcessingException j) {
                    throw new PIPException("prohibitions", j.getMessage());
                }
            }

            ps.setString(8, prohibitionName);
            ps.executeUpdate();

            //delete container before adding new ones
            preparedStatement.setInt(1, getProhibitionIdByProhibitionName(prohibition.getName()));
            preparedStatement.executeUpdate();

            //adding new container
            for (Map.Entry<String,Boolean> entry : prohibition.getContainers().entrySet()) {
                psCont.setInt(1, getProhibitionIdByProhibitionName(prohibition.getName()));
                MySQLGraph graph = new MySQLGraph(this.conn);
                //if (graph.exists(graph.getNodeNameFromId(Integer.parseInt(container.getKey())))) {
                if (graph.exists(entry.getKey())) {
                    psCont.setInt(2, getNodeIdFromSubjectName(entry.getKey()));
                    psCont.setInt(3, entry.getValue() ? 1 : 0);
                }
                else {
                    throw new PIPException("prohibitions", "The object attribute "+entry.getKey()+" does not exist in the graph");
                }
                psCont.executeUpdate();
            }
        } catch (SQLException s) {
            throw new PIPException("prohibitions", s.getMessage());
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
                throw new PIPException("prohibitions", "a prohibition does not exist with the name "+ prohibitionName);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new PIPException("prohibitions", e.getMessage());
        }
    }

    public class Deny_obj_attr {
        private int deny_id;
        private int obj_att_id;
        private int obj_compl;

        public Deny_obj_attr(int a, int b, int c) {
            this.deny_id = a;
            this.obj_att_id = b;
            this.obj_compl = c;
        }

        public int getDeny_id() {
            return deny_id;
        }

        public void setDeny_id(int deny_id) {
            this.deny_id = deny_id;
        }

        public int getObj_att_id() {
            return obj_att_id;
        }

        public void setObj_att_id(int obj_att_id) {
            this.obj_att_id = obj_att_id;
        }

        public int getObj_compl() {
            return obj_compl;
        }

        public void setObj_compl(int obj_compl) {
            this.obj_compl = obj_compl;
        }
    }
}
