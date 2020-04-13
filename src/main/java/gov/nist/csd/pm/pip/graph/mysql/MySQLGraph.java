package gov.nist.csd.pm.pip.graph.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;
import gov.nist.csd.pm.pip.graph.mysql.MySQLHelper;
import gov.nist.csd.pm.pip.graph.mysql.MySQLConnection;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;

public class MySQLGraph implements Graph {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectReader reader = new ObjectMapper().readerFor(HashMap.class);
    private static final ObjectReader reader2 = new ObjectMapper().readerFor(OperationSet.class);
    private static final String NODE_NOT_FOUND_MSG = "node %s does not exist";
    private final MySQLConnection conn;

    public MySQLGraph(MySQLConnection connection) {
        this.conn = connection;
    }


    public static String toJSON(Map<String, String> map) throws JsonProcessingException {
        return objectMapper.writeValueAsString(map);
    }

    public static String hashSetToJSON(Set<String> set) throws JsonProcessingException {
        return objectMapper.writeValueAsString(set);
    }

    public boolean nameExists(String name) throws PMException{
        Collection<Node> all_nodes = getNodes();
        List<Node> nodes;
        try {
            nodes = all_nodes.stream()
                    .filter(node_k -> node_k.getName().equals(name))
                    .collect(Collectors.toList());
            if (nodes.size() >= 1) {
                return true;
            }
        } catch (Exception p) {
            throw new PMException("No nodes in the mysql graph");
        }
        return false;
    }

    public long getNodeIdFromName (String name) throws PMException {

        Collection<Node> all_nodes = getNodes();
        List<Node> nodes;
        try {
            nodes = all_nodes.stream()
                    .filter(node_k -> node_k.getName().equals(name))
                    .collect(Collectors.toList());
            nodes.forEach(System.out::println);
            if (nodes.size() >= 1) {
                return nodes.get(0).getId();
            }
        } catch (Exception p) {
            throw new PMException("No nodes in the mysql graph");
        }
        throw new PMException("There are no nodes with that name.");
    }

    public String getNodeNameFromId(long id) throws PMException{
        Collection<Node> all_nodes = getNodes();
        List<Node> nodes;
        try {
            nodes = all_nodes.stream()
                    .filter(node_k -> node_k.getId() == id)
                    .collect(Collectors.toList());
            nodes.forEach(System.out::println);
            if (nodes.size() >= 1) {
                return nodes.get(0).getName();
            }
        } catch (Exception p) {
            throw new PMException("No nodes in the mysql graph");
        }
        throw new PMException("There are no nodes with that name.");
    }

    @Override
    public Node createPolicyClass(String name, Map<String, String> properties) throws PMException {
        //check for null values

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a policy class in the mysql graph");
        }
        else if (exists(name)) {
            throw new PMException("You cannot create a policy class with that name, another node with that name already exists.");
        }

        else if (nameExists(name)){
            throw new PMException("a node with the name '" + name + "' already exists");
        }

        ResultSet rs_type = null;
        PreparedStatement pstmt = null;
        PreparedStatement ps = null;
        try {
            Connection con = this.conn.getConnection();
            //====================  NodeType parser : Retrieve node_type_id ====================
            pstmt = con.prepareStatement(MySQLHelper.SELECT_NODE_TYPE_ID_FROM_NODE_TYPE);
            pstmt.setString(1, "PC");
            rs_type = pstmt.executeQuery();
            int node_type_id = 0;
            while (rs_type.next()){
                node_type_id = rs_type.getInt("node_type_id");
            }
            //==================== create the node from (id, node_type_id, name, properties) ====================
            ps = con.prepareStatement(MySQLHelper.INSERT_NODE, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, node_type_id);
            ps.setString(2,name);
            //Json serialization using Jackson
            if ( properties == null) {
                ps.setString(3, null);
            } else {
                try {
                    ps.setString(3, toJSON(properties));
                } catch (JsonProcessingException j) {
                    j.printStackTrace();
                }
            }

            ps.executeUpdate();

            Node node = new Node(name, PC, properties);
            con.close();
            return node;
        } catch (SQLException s) {
            s.printStackTrace();
        }
        finally {
            try {
                //We can also use DbUtils to clean up: DbUtils.closeQuietly(rs);
                if(rs_type != null) {rs_type.close();}
                if(ps != null) {ps.close();}
                if(pstmt != null) {pstmt.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Create a node in the mysql graph.
     *
     * @return the ID that was passed as part of the node parameter.
     * @throws IllegalArgumentException When the provided node is null.
     * @throws IllegalArgumentException When the provided node has a null or empty name.
     * @throws IllegalArgumentException When the provided node has a null type.
     * @throws PMException When the provided name already exists in the mysql graph
     */
    @Override
    public Node createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String ... additionalParents) throws PMException  {
        //check for null values

        if (type == PC) {
            throw new PMException("use createPolicyClass to create a policy class node");
        }
        else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a node in the mysql graph");
        }
        else if (type == null) {
            throw new IllegalArgumentException("a null type was provided to the mysql graph when creating a node");
        }
        else if (exists(name)) {
            throw new PMException("You cannot create a policy class with that name, another node with that name already exists.");
        }
        else if (initialParent.equals("0")) {
            throw new IllegalArgumentException("must specify an initial parent ID when creating a non policy class node");
        }
        else if (nameExists(name)){
            throw new PMException("a node with the name '" + name + "' already exists");
        }

        ResultSet rs_type = null;
        PreparedStatement pstmt = null;
        PreparedStatement ps = null;
        try {
            Connection con = this.conn.getConnection();
            //====================  NodeType parser : Retrieve node_type_id ====================
            pstmt = con.prepareStatement(MySQLHelper.SELECT_NODE_TYPE_ID_FROM_NODE_TYPE);
            pstmt.setString(1, type.toString());
            rs_type = pstmt.executeQuery();
            int node_type_id;
            int id = 0;
            while (rs_type.next()) {
                node_type_id = rs_type.getInt("node_type_id");
                //==================== create the node from (id, node_type_id, name, properties) ====================
                ps = con.prepareStatement(MySQLHelper.INSERT_NODE, Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1, node_type_id);
                ps.setString(2, name);
                //Json serialization using Jackson
                if (properties == null) {
                    ps.setString(3, null);
                } else {
                    try {
                        ps.setString(3, toJSON(properties));
                    } catch (JsonProcessingException j) {
                        j.printStackTrace();
                    }
                }
            }
            ps.executeUpdate();

            ResultSet resultSet = ps.getGeneratedKeys();
            if(resultSet.next())
            {
                id = resultSet.getInt(1);
            }
            Node node = new Node(id, name, type, properties);
            //assign the new nodes to given parent nodes
            assign(name, initialParent);
            for (String parent : additionalParents) {
                assign(name, parent);
            }
            con.close();
            return node;
        } catch (SQLException s) {
            s.printStackTrace();
        }
        finally {
            try {
                if(rs_type != null) {rs_type.close();}
                if(ps != null) {ps.close();}
                if(pstmt != null) {pstmt.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Update a node with the given node context. Only the name and properties can be updated. If the name of the context
     * is null, then the node will not be updated.  The properties provided in the context will overwrite any existing
     * properties.  If the properties are null, they will be skipped. However, if the properties are an empty map, the
     * empty map will be set as the node's new properties.
     * @throws IllegalArgumentException When the provided node does not exist in the mysql graph
     * @throws IllegalArgumentException When the provided name is null
     * @throws PMException if there is an error updating the node
     */

    @Override
    public void updateNode (String name, Map<String, String> properties) throws PMException {
        //method not stable
    }


    public void updateNode(long id, String name, Map<String, String> properties) throws PMException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when updating the node in the mysql graph");
        }
        Node node = getNode(name);
        if (node == null) {
            throw new PMException(String.format("node with the name %s could not be found to update", name));
        }

        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.UPDATE_NODE)
        ){
            ps.setString(1, name);
            try {
                ps.setString(2, toJSON(properties));
            } catch (JsonProcessingException j) {
                j.printStackTrace();
            }
            ps.setLong(3, id);
            ps.executeUpdate();

        } catch (SQLException s) {
            s.printStackTrace();
            throw new PMException("Cannot connect to the database");
        }
    }

    /**
     * Delete the node with the given name from the graph. No error handled if nothing happens while deleting a node that does not exists.
     *
     * @param name the name of the node to delete.
     * @throws PMException If there was an error deleting the node
     */
    @Override
    public void deleteNode(String name) throws PMException {
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.DELETE_NODE)
        ){

            ps.setString(1, name);
            ps.executeUpdate();
            //TODO : Handle the case if no node got deleted
 /*         int i = ps.executeUpdate();
            if (i == 0) {
                throw new PMException("The node you want to delete does not exist");
            }*/
        } catch (SQLException e) {
            throw new PMException(e.getMessage());
        }
    }

    /**
     * Check that a node with the given name exists in the graph.
     *
     * @param name of the node to check for.
     * @return true or False if a node with the given name exists or not.
     * @throws PMException if there is an error checking if the node exists in the graph.
     */
    @Override
    public boolean exists(String name) throws PMException {
        try (Connection con = this.conn.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT node_id, name from node where name=?")
        ){
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            List<Node> nodes = new ArrayList<>();
            while (rs.next()) {
                String              name_p = rs.getString("name");
                Node cur_node = new Node(name_p, null, null);
                nodes.add(cur_node);
            }
            return nodes.size() != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);
        }
    }

    /**
     * Get the set of policy classes. The returned set is just the names of each policy class.
     *
     * @return the set of policy class names.
     * @throws PMException if there is an error retrieving the names of the policy classes.
     */
    @Override
    public Set<String> getPolicyClasses() throws PMException {
        Set<Node> nodes = new HashSet<>(getNodes());
        Set<String> namesPolicyClasses = new HashSet<>();
        for (Node node : nodes){
            if (node.getType().equals(NodeType.toNodeType("PC"))) {
                namesPolicyClasses.add(node.getName());
            }
        }

        if (namesPolicyClasses.isEmpty()) {
            throw new PMException("There are no Policies in the current database");
        }
        return namesPolicyClasses;
    }

    /**
     * Retrieve the set of all nodes in the graph.
     *
     * @return a Set of all the nodes in the graph.
     * @throws PMException if there is an error retrieving all nodes in the graph.
     */
    @Override
    public Set<Node> getNodes() throws PMException {
        ResultSet rs_type = null;
        PreparedStatement pstmt = null;
        Node node = null;
        Set<Node> nodes = new HashSet<>();
        HashMap<Node, Long> nodesHashmap = new HashMap<>();

        try (
                Connection con = this.conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_ALL_FROM_NODE)

        ){
            // return node_type_id instead of numeric value of the node
            long node_type =  0;
            while (rs.next()) {
                long                id = rs.getInt("node_id");
                String              name = rs.getString("name");
                                    node_type = rs.getInt("node_type_id");
                String properties_string = rs.getString("node_property");
                Map<String, String> properties = null;

                if (properties_string != null) {
                    try {
                        properties = reader.readValue(properties_string);
                    } catch (JsonProcessingException j) {
                        j.printStackTrace();
                    }
                }

                NodeType type = null;
                node = new Node(id, name, type, properties);
                nodesHashmap.put(node, node_type);
                nodes.add(node);
            }
            //retrieve all nodes
            for (Map.Entry<Node, Long> node_k: nodesHashmap.entrySet()) {
                pstmt = con.prepareStatement(MySQLHelper.SELECT_NODE_TYPE_NAME_FROM_NODE_TYPE);
                pstmt.setLong(1, node_k.getValue());
                rs_type = pstmt.executeQuery();
                String name_type = "";
                while (rs_type.next()){
                    name_type = rs_type.getString("name");
                }
                node_k.getKey().setType(NodeType.toNodeType(name_type));
            }
            nodes = nodesHashmap.keySet();
            con.close();
            return nodes;
/*            if (nodes.size() == 0 ) {
                throw new PMException("There are no nodes.");
            }else {
                return nodes;
            }*/
        } catch (SQLException s) {
            s.printStackTrace();
        }
        finally {
            try {

                if(pstmt != null) {pstmt.close();}
                if(rs_type != null) {rs_type.close();}

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nodes;
    }

    @Override
    public Node getNode(NodeType type, Map<String, String> properties) throws PMException {
        Set<Node> search = search(type, properties);
        if (search.isEmpty()) {
            throw new PMException(String.format("a node matching the criteria (%s, %s) does not exist", type, properties));
        }
        return search.iterator().next();
    }

    @Override
    public Node getNode(String name) throws PMException {

        Collection<Node> nodes = getNodes();
        Node node;
        try {
            node = nodes.stream()
                    .filter(node_k -> node_k.getName().equals(name))
                    .iterator().next();
        } catch (Exception p) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, name));
        }
        return node;
    }

    /**
     * Search the graph for nodes matching the given parameters. A node must
     * contain all properties provided to be returned.
     * To get all the nodes that have a specific property key with any value use "*" as the value in the parameter.
     * (i.e. {key=*})
     *
     * @param type       the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return a set of nodes that match the given search criteria.
     * @throws PMException if there is an error searching the graph.
     */
    @Override
    public Set<Node> search(NodeType type, Map<String, String> properties) throws PMException {

        MemGraph graph = new MemGraph();
        return graph.search(type, properties);
    }

    /**
     * Get the set of nodes that are assigned to the node with the given name.
     *
     * @param name of the node to get the children of.
     * @return the Set of NGACNodes that are assigned to the node with the given name.
     * @throws PMException if there is an error retrieving the children of the node.
     */
    @Override
    public Set<String> getChildren(String name) throws PMException {
        if (!exists(name)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, name));
        }

        Set<Long> sources = new HashSet<>();
        Set<String> sources_p = new HashSet<>();
        long nodeID = getNodeIdFromName(name);

        try (
                Connection con = this.conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_START_NODE_ID + nodeID)

        ){

            while (rs.next()) {
                long              start_node_id = rs.getInt("start_node_id");
                sources.add(start_node_id);

            }
            for (long id : sources) {
                sources_p.add(getNodeNameFromId(id));
            }
            return sources_p;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        }
    }

    /**
     * Get the set of nodes that the node with the given name is assigned to.
     *
     * @param name of the node to get the parents of.
     * @return the Set of NGACNodes that are assigned to the node with the given ID.
     * @throws PMException if there is an error retrieving the parents of the node.
     */
    @Override
    public Set<String> getParents(String name) throws PMException {
        if (!exists(name)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, name));
        }

        Set<String> targets_p = new HashSet<>();
        Set<Long> targets = new HashSet<>();
        long nodeID = getNodeIdFromName(name);

        try (
                Connection con = this.conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_END_NODE_ID + nodeID)
        ) {

            while (rs.next()) {
                long              end_node_id = rs.getInt("end_node_id");
                targets.add(end_node_id);
            }
            for (long id : targets) {
                targets_p.add(getNodeNameFromId(id));
            }
            return targets_p;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        }
    }

    /**
     * Assign the child node to the parent node. Both nodes must exist and both types must make a valid assignment.
     *
     * @throws IllegalArgumentException if the child node context is null or does not exist in the mysql graph.
     * @throws IllegalArgumentException if the parent node context is null or does not exist in the mysql graph.
     * @throws PMException if the two types do not make a valid assignment.
     */
    @Override
    public void assign(String child, String parent) throws PMException {
        Node childNode = getNode(child);
        Node parentNode = getNode(parent);
        if (!exists(child)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, child));
        }
        else if (!exists(parent)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, parent));
        }

        Assignment.checkAssignment(childNode.getType(), parentNode.getType());

        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.INSERT_ASSIGNMENT)
        ) {
            ps.setLong(1, childNode.getId());
            ps.setLong(2, parentNode.getId());
            ps.executeUpdate();

        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    /**
     * Deassign the child node from the parent node. If the 2 nodes are assigned several times, it delete all assignment.
     *
     * @throws IllegalArgumentException if the child node context is null.
     * @throws IllegalArgumentException if the parent node context is null.
     * @throws PMException if the nodes do not exist
     */
    @Override
    public void deassign(String child, String parent) throws PMException {

        if (!exists(child)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, child));
        }
        else if (!exists(parent)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, parent));
        }

        Node childNode = getNode(child);
        Node parentNode = getNode(parent);
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.DELETE_ASSIGNMENT)
        ) {

            ps.setLong(1, childNode.getId());
            ps.setLong(2, parentNode.getId());
            int i = ps.executeUpdate();
            if (i == 0) {
                throw new IllegalArgumentException("The assignment you want to delete does not exist");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);
        }
    }

    /**
     * Returns true if the child is assigned to the parent.
     *
     * @param child the name of the child node
     * @param parent the name of the parent node
     * @return true if the child is assigned to the parent, false otherwise
     * @throws PMException if there is an error checking if the child is assigned to the parent
     */
    @Override
    public boolean isAssigned(String child, String parent) throws PMException {

        Node childNode = getNode(child);
        Node parentNode = getNode(parent);
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_ASSIGNMENT_ID)
        ) {

            ps.setLong(1, childNode.getId());
            ps.setLong(2, parentNode.getId());
            ResultSet rs = ps.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (rs.next()){
                ids.add(rs.getInt("assignment_id"));
            }
            return ids.size() != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);
        }
    }

    public boolean isAssociated(String child, String parent) throws PMException {
        Node childNode = getNode(child);
        Node parentNode = getNode(parent);

        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_ASSOCIATION_ID)
        ) {

            ps.setLong(1, childNode.getId());
            ps.setLong(2, parentNode.getId());
            ResultSet rs = ps.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (rs.next()){
                ids.add(rs.getInt("association_id"));
            }
            return ids.size() != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);
        }
    }

    /**
     * Create an Association between the user attribute and the Target node with the provided operations. If an association
     * already exists between these two nodes, overwrite the existing operations with the ones provided.  Associations
     * can only begin at a user attribute but can point to either an Object or user attribute
     *
     * @param ua The name of the user attribute.
     * @param target The name of the target attribute.
     * @param operations A Set of operations to add to the association.
     * @throws PMException if there is an error associating the two nodes.
     */
    @Override
    public void associate(String ua, String target, OperationSet operations) throws PMException {
        //throw PMException if nodes does not exists
        if (!exists(ua)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, ua));
        }
        else if (!exists(target)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, target));
        }

        Node uaNode = getNode(ua);
        Node targetNode = getNode(target);

        // check that the association is valid
        Association.checkAssociation(uaNode.getType(), targetNode.getType());

        if (!isAssociated(ua, target)) {
            // no edge exists between ua and target -> create a new association

            try (
                    Connection con = this.conn.getConnection();
                    PreparedStatement ps = con.prepareStatement(MySQLHelper.INSERT_ASSOCIATION)
            ) {

                ps.setLong(1, uaNode.getId());
                ps.setLong(2, targetNode.getId());

                //Json serialization using Jackson
                if (operations == null) {
                    ps.setString(3, null);
                } else {
                    try {
                        ps.setString(3, hashSetToJSON(operations));
                    } catch (JsonProcessingException j) {
                        j.printStackTrace();
                    }
                }
                int i = ps.executeUpdate();
                if (i == 0) {
                    throw new IllegalArgumentException("Something went wrong associating the two nodes.");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new PMException("Cannot connect to the Database" + ex);
            }
        } else {
            //if an association exists update it
            try (
                    Connection con = this.conn.getConnection();
                    PreparedStatement ps = con.prepareStatement(MySQLHelper.UPDATE_ASSOCIATION)
            ) {

                ps.setLong(2, uaNode.getId());
                ps.setLong(3, targetNode.getId());

                //Json serialization using Jackson
                if (operations == null) {
                    ps.setString(1, null);
                } else {
                    try {
                        ps.setString(1, hashSetToJSON(operations));
                    } catch (JsonProcessingException j) {
                        j.printStackTrace();
                    }
                }
                int i = ps.executeUpdate();
                if (i == 0) {
                    throw new IllegalArgumentException("Something went wrong associating the two nodes.");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new PMException("Cannot connect to the Database" + ex);
            }
        }
    }

    /**
     * Delete the Association between the user attribute and Target node.
     *
     * @param ua     the name of the user attribute.
     * @param target the name of the target attribute.
     * @throws PMException if there is an error dissociating the two nodes.
     */
    @Override
    public void dissociate(String ua, String target) throws PMException {

        Node uaNode = getNode(ua);
        Node targetNode = getNode(target);
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.DELETE_ASSOCIATION)

        ) {
            ps.setLong(1, uaNode.getId());
            ps.setLong(2, targetNode.getId());
            int i = ps.executeUpdate();
            if (i == 0) {
                throw new IllegalArgumentException("The association you want to delete does not exist");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);
        }
    }

    /**
     * Retrieve the associations the given node is the source of.  The source node of an association is always a
     * user attribute and this method will throw an exception if an invalid node is provided.  The returned Map will
     * contain the target and operations of each association.
     *
     * @param source the name of the source node.
     * @return a map of the target node names and the operations for each association.
     * @throws PMException if there is an retrieving the associations of the source node from the graph.
     */
    @Override
    public Map<String, OperationSet> getSourceAssociations(String source) throws PMException {
        if (!exists(source)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, source));
        }

        Node ua = getNode(source);
        if (ua.getType() != NodeType.UA) {
            throw new PMException("The source node must be an user attribute.");
        }
        Map<String, OperationSet> sourcesAssoc = new HashMap<>();
        OperationSet operations_set = new OperationSet();

        try (
                Connection con = this.conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_END_NODE_ID_OPERATION + ua.getId())
        ) {

            while (rs.next()) {
                long              end_node_id = rs.getInt("end_node_id");
                String            operations = rs.getString("operation_set");

                if (operations != null) {
                    try {
                        operations_set = reader2.readValue(operations);
                    } catch (JsonProcessingException j) {
                        j.printStackTrace();
                    }
                }
                sourcesAssoc.put(getNodeNameFromId(end_node_id), operations_set);

            }
            return sourcesAssoc;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        }
    }

    /**
     * Retrieve the associations the given node is the target of.  The target node can be an Object Attribute or a User
     * Attribute. This method will throw an exception if a node of any other type is provided.  The returned Map will
     * contain the source node IDs and the operations of each association.
     *
     * @param target the name of the target node.
     * @return a Map of the source Ids and the operations for each association.
     * @throws PMException if there is an retrieving the associations of the target node from the graph.
     */
    @Override
    public Map<String, OperationSet> getTargetAssociations(String target) throws PMException {

        if (!exists(target)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, target));
        }

        Node ua = getNode(target);

        if (ua.getType() != NodeType.UA && ua.getType() != NodeType.OA) {
            throw new PMException("The target node must be an user attribute or an object attribute.");
        }
        Map<String, OperationSet> targetsAssoc = new HashMap<>();
        OperationSet operations_set = new OperationSet();

        try (
                Connection con = this.conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_START_NODE_ID_OPERATION + ua.getId())
        ) {
            while (rs.next()) {
                long              start_node_id =rs.getInt("start_node_id");
                String            operations = rs.getString("operation_set");

                if (operations != null) {
                    try {
                        operations_set = reader2.readValue(operations);
                    } catch (JsonProcessingException j) {
                        j.printStackTrace();
                    }
                }
                targetsAssoc.put(getNodeNameFromId(start_node_id), operations_set);

            }
            return targetsAssoc;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);
        }
    }
}
