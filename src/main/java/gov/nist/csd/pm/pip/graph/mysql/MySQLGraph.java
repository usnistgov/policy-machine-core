package gov.nist.csd.pm.pip.graph.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.exceptions.PIPException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.GraphAdmin;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;

public class MySQLGraph implements Graph {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectReader reader = new ObjectMapper().readerFor(HashMap.class);
    private static final ObjectReader reader2 = new ObjectMapper().readerFor(OperationSet.class);
    private static final String NODE_NOT_FOUND_MSG = "node %s does not exist";
    private static final HashMap<Long, String> nodeType = new HashMap<>();

    public static HashMap<Long, String> getNodeType() {
        nodeType.clear();

        nodeType.put((long) 1, "OA");
        nodeType.put((long) 2, "UA");
        nodeType.put((long) 3, "U");
        nodeType.put((long) 4, "O");
        nodeType.put((long) 5, "PC");
        nodeType.put((long) 6, "OS");
        return nodeType;
    }

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

    public long getNodeIdFromName (String name) throws PIPException {

        Collection<Node> all_nodes = getNodes();
        List<Node> nodes;
        try {
            nodes = all_nodes.stream()
                    .filter(node_k -> node_k.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
            //nodes.forEach(System.out::println);
            if (nodes.size() >= 1) {
                return nodes.get(0).getId();
            }
        } catch (Exception p) {
            throw new PIPException("graph", p.getMessage());
        }
        throw new PIPException("graph", "There are no nodes with that name.");
    }

    public String getNodeNameFromId(long id) throws PIPException{
        Collection<Node> all_nodes = getNodes();
        List<Node> nodes;
        try {
            nodes = all_nodes.stream()
                    .filter(node_k -> node_k.getId() == id)
                    .collect(Collectors.toList());
            //nodes.forEach(System.out::println);
            if (nodes.size() >= 1) {
                return nodes.get(0).getName();
            }
        } catch (Exception p) {
            throw new PIPException("graph", p.getMessage());
        }
        throw new PIPException("graph","There are no nodes with that name.");
    }

    @Override
    public Node createPolicyClass(String name, Map<String, String> properties) throws PIPException {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a policy class in the mysql graph");
        }
        else if (exists(name)) {
            throw new PIPException("graph", "You cannot create the policy class. Another node with the name '" + name + "' already exists");
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
                    throw new PIPException("graph", j.getMessage());
                }
            }

            ps.executeUpdate();

            Node node = new Node(name, PC, properties);
            con.close();
            return node;
        } catch (SQLException s) {
            throw new PIPException("graph", s.getMessage());
        }
        finally {
            try {
                //We can also use DbUtils to clean up: DbUtils.closeQuietly(rs);
                if(rs_type != null) {rs_type.close();}
                if(ps != null) {ps.close();}
                if(pstmt != null) {pstmt.close();}
            } catch (SQLException e) {
                throw new PIPException("graph", e.getMessage());
            }
        }
    }

    /**
     * Create a node in the mysql graph.
     *
     * @return the ID that was passed as part of the node parameter.
     * @throws IllegalArgumentException When the provided node is null.
     * @throws IllegalArgumentException When the provided node has a null or empty name.
     * @throws IllegalArgumentException When the provided node has a null type.
     * @throws PIPException When the provided name already exists in the mysql graph
     */
    @Override
    public Node createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String ... additionalParents) throws PIPException  {
        //check for null values

        if (type == PC) {
            throw new PIPException("graph", "use createPolicyClass to create a policy class node");
        }
        else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a node in the mysql graph");
        }
        else if (type == null) {
            throw new IllegalArgumentException("a null type was provided to the mysql graph when creating a node");
        }
        else if (exists(name)) {
            throw new PIPException("graph", "You cannot create the policy class node. Another node with the name '" + name + "' already exists");
        }
        else if (initialParent.equals("0")) {
            throw new IllegalArgumentException("must specify an initial parent ID when creating a non policy class node");
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
                        throw new PIPException("graph", j.getMessage());
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
            throw new PIPException("graph", s.getMessage());
        }
        finally {
            try {
                if(rs_type != null) {rs_type.close();}
                if(ps != null) {ps.close();}
                if(pstmt != null) {pstmt.close();}
            } catch (SQLException e) {
                throw new PIPException("graph", e.getMessage());
            }
        }
    }

    /**
     * Method without initial parents
     * @param name
     * @param type
     * @param properties
     * @return
     * @throws PIPException
     */
    public void createNode(String name, NodeType type, Map<String, String> properties) throws PIPException  {
        //check for null values

        if (type == PC) {
            throw new PIPException("graph", "use createPolicyClass to create a policy class node");
        }
        else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a node in the mysql graph");
        }
        else if (type == null) {
            throw new IllegalArgumentException("a null type was provided to the mysql graph when creating a node");
        }
        else if (exists(name)) {
            throw new PIPException("graph", "You cannot create the node. Another node with the name '" + name + "' already exists");
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
                        throw new PIPException("graph", j.getMessage());
                    }
                }
            }
            ps.executeUpdate();
            con.close();
        } catch (SQLException s) {
            throw new PIPException("graph", s.getMessage());
        }
        finally {
            try {
                if(rs_type != null) {rs_type.close();}
                if(ps != null) {ps.close();}
                if(pstmt != null) {pstmt.close();}
            } catch (SQLException e) {
                throw new PIPException("graph", e.getMessage());
            }
        }
    }

    /**
     * Update a node with the given node context. Only the name and properties can be updated. If the name of the context
     * is null, then the node will not be updated.  The properties provided in the context will overwrite any existing
     * properties.  If the properties are null, they will be skipped. However, if the properties are an empty map, the
     * empty map will be set as the node's new properties.
     * @throws IllegalArgumentException When the provided node does not exist in the mysql graph
     * @throws IllegalArgumentException When the provided name is null
     * @throws PIPException if there is an error updating the node
     */

    @Override
    public void updateNode (String name, Map<String, String> properties) throws PIPException {
        //method not stable
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when updating a node in the mysql graph");
        }
        else if (!exists(name)) {
            throw new PIPException("graph", "node with the name "+ name+ "could not be found to update");
        }

        Node node = getNode(name);
    }

    public void updateNode(long id, String name, Map<String, String> properties) throws PIPException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when updating the node in the mysql graph");
        }
        else if (!exists(name)) {
            throw new PIPException("graph", "node with the name "+ name+ "could not be found to update");
        }

        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.UPDATE_NODE)
        ){
            ps.setString(1, name);
            try {
                ps.setString(2, toJSON(properties));
            } catch (JsonProcessingException j) {
                throw new PIPException("graph", j.getMessage());
            }
            ps.setLong(3, id);
            ps.executeUpdate();

        } catch (SQLException s) {
            throw new PIPException("graph", s.getMessage());
        }
    }

    /**
     * Delete the node with the given name from the graph. No error handled if nothing happens while deleting a node that does not exists.
     *
     * @param name the name of the node to delete.
     * @throws PIPException If there was an error deleting the node
     */
    @Override
    public void deleteNode(String name) throws PIPException {
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
            throw new PIPException("graph", e.getMessage());
        }
    }

    /**
     * Check that a node with the given name exists in the graph.
     *
     * @param name of the node to check for.
     * @return true or False if a node with the given name exists or not.
     * @throws PIPException if there is an error checking if the node exists in the graph.
     */
    @Override
    public boolean exists(String name) throws PIPException {
        try (            Connection con = this.conn.getConnection();
                         PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_NODE_ID_NAME_FROM_NODE)
        ){
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt("total");
            }
            return count != 0;
        } catch (SQLException ex) {
            throw new PIPException("graph", ex.getMessage());
        }
    }

    /**
     * Get the set of policy classes. The returned set is just the names of each policy class.
     *
     * @return the set of policy class names.
     * @throws PIPException if there is an error retrieving the names of the policy classes.
     */
    @Override
    public Set<String> getPolicyClasses() throws PIPException {
        Set<Node> nodes = new HashSet<>(getNodes());
        Set<String> namesPolicyClasses = new HashSet<>();
        for (Node node : nodes){
            if (node.getType().equals(NodeType.toNodeType("PC"))) {
                namesPolicyClasses.add(node.getName());
            }
        }

        if (namesPolicyClasses.isEmpty()) {
            throw new PIPException("graph", "There are no Policies in the current database");
        }
        return namesPolicyClasses;
    }

    /**
     * Retrieve the set of all nodes in the graph.
     *
     * @return a Set of all the nodes in the graph.
     * @throws PIPException if there is an error retrieving all nodes in the graph.
     */
    @Override
    public Set<Node> getNodes() throws PIPException {
        Node node = null;
        Set<Node> nodes = new HashSet<>();
        HashMap<Long, String> nodeType = getNodeType();

        try (
                Connection con = this.conn.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_ALL_FROM_NODE)

        ){
            while (rs.next()) {
                long                id = rs.getInt("node_id");
                String              name = rs.getString("name");
                long                node_type = rs.getInt("node_type_id");
                String              properties_string = rs.getString("node_property");
                Map<String, String> properties = null;

                if (properties_string != null) {
                    try {
                        properties = reader.readValue(properties_string);
                    } catch (JsonProcessingException j) {
                        throw new PIPException("graph", j.getMessage());
                    }
                }

                NodeType type = null;
                for (Map.Entry<Long, String> node_type_k : nodeType.entrySet()) {
                    if ( node_type == node_type_k.getKey()) {
                        type = NodeType.toNodeType(node_type_k.getValue());
                    }
                }
                node = new Node(id, name, type, properties);
                nodes.add(node);
            }
            con.close();
            return nodes;
        } catch (SQLException s) {
            throw new PIPException("graph", s.getMessage());
        }
    }

    @Override
    public Node getNode(NodeType type, Map<String, String> properties) throws PIPException {
        Set<Node> search = search(type, properties);
        if (search.isEmpty()) {
            throw new PIPException("graph", "a node matching the criteria (" + type +"," + properties+ ") does not exist");
        }
        return search.iterator().next();
    }

    @Override
    public Node getNode(String name) throws PIPException {
        HashMap<Long, String> nodeType = getNodeType();
        Node node = null;
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.SELECT_ALL_FROM_NAME)
        ) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long                id = rs.getInt("node_id");
                String              name_node = rs.getString("name");
                long                node_type = rs.getInt("node_type_id");
                String              properties_string = rs.getString("node_property");
                Map<String, String> properties = null;

                if (properties_string != null) {
                    try {
                        properties = reader.readValue(properties_string);
                    } catch (JsonProcessingException j) {
                        throw new PIPException("graph", j.getMessage());
                    }
                }

                NodeType type = null;
                for (Map.Entry<Long, String> node_type_k : nodeType.entrySet()) {
                    if ( node_type == node_type_k.getKey()) {
                        type = NodeType.toNodeType(node_type_k.getValue());
                    }
                }
                node = new Node(id, name, type, properties);
            }
            return node;
        } catch (SQLException s) {
            throw new PIPException("graph", s.getMessage());
        }
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
     * @throws PIPException if there is an error searching the graph.
     */
    @Override
    public Set<Node> search(NodeType type, Map<String, String> properties) throws PIPException {
        if (properties == null) {
            properties = new HashMap<>();
        }

        HashSet<Node> results = new HashSet<>();
        // iterate over the nodes to find ones that match the search parameters
        for (Node node : getNodes()) {
            // if the type parameter is not null and the current node type does not equal the type parameter, do not add
            if (type != null && !node.getType().equals(type)) {
                continue;
            }

            boolean add = true;
            for (String key : properties.keySet()) {
                String checkValue = properties.get(key);
                String foundValue = node.getProperties().get(key);
                // if the property provided in the search parameters is null or *, continue to the next property
                if (!(checkValue == null || checkValue.equals("*")) &&
                        (foundValue == null || !foundValue.equals(checkValue))) {
                    add = false;
                    break;
                }
            }

            if (add) {
                results.add(node);
            }
        }

        return results;
    }

    /**
     * Get the set of nodes that are assigned to the node with the given name.
     *
     * @param name of the node to get the children of.
     * @return the Set of NGACNodes that are assigned to the node with the given name.
     * @throws PIPException if there is an error retrieving the children of the node.
     */
    @Override
    public Set<String> getChildren(String name) throws PIPException {
        if (!exists(name)) {
            throw new PIPException("graph", "node with the name "+ name+ "could not be found to update");
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
            throw new PIPException("graph", ex.getMessage());

        }
    }

    /**
     * Get the set of nodes that the node with the given name is assigned to.
     *
     * @param name of the node to get the parents of.
     * @return the Set of NGACNodes that are assigned to the node with the given ID.
     * @throws PIPException if there is an error retrieving the parents of the node.
     */
    @Override
    public Set<String> getParents(String name) throws PIPException {
        if (!exists(name)) {
            throw new PIPException("graph", "node "+ name+ " does not exist");
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
            throw new PIPException("graph", ex.getMessage());

        }
    }

    /**
     * Assign the child node to the parent node. Both nodes must exist and both types must make a valid assignment.
     *
     * @throws IllegalArgumentException if the child node context is null or does not exist in the mysql graph.
     * @throws IllegalArgumentException if the parent node context is null or does not exist in the mysql graph.
     * @throws PIPException if the two types do not make a valid assignment.
     */
    @Override
    public void assign(String child, String parent) throws PIPException {
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
            throw new PIPException("graph", s.getMessage());
        }
    }

    /**
     * Deassign the child node from the parent node. If the 2 nodes are assigned several times, it delete all assignment.
     *
     * @throws IllegalArgumentException if the child node context is null.
     * @throws IllegalArgumentException if the parent node context is null.
     * @throws PIPException if the nodes do not exist
     */
    @Override
    public void deassign(String child, String parent) throws PIPException {

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
            throw new PIPException("graph", ex.getMessage());
        }
    }

    /**
     * Returns true if the child is assigned to the parent.
     *
     * @param child the name of the child node
     * @param parent the name of the parent node
     * @return true if the child is assigned to the parent, false otherwise
     * @throws PIPException if there is an error checking if the child is assigned to the parent
     */
    @Override
    public boolean isAssigned(String child, String parent) throws PIPException {

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
            throw new PIPException("graph", ex.getMessage());
        }
    }

    public boolean isAssociated(String child, String parent) throws PIPException {
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
            throw new PIPException("graph", ex.getMessage());
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
     * @throws PIPException if there is an error associating the two nodes.
     */
    @Override
    public void associate(String ua, String target, OperationSet operations) throws PIPException {
        //throw PIPException if nodes does not exists
        if (!exists(ua)) {
            throw new PIPException("graph", "node "+ ua+ " does not exist");
        }
        else if (!exists(target)) {
            throw new PIPException("graph", "node "+ target+ " does not exist");
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
                        throw new PIPException("graph", j.getMessage());
                    }
                }
                int i = ps.executeUpdate();
                if (i == 0) {
                    throw new IllegalArgumentException("Something went wrong associating the two nodes.");
                }

            } catch (SQLException ex) {
                throw new PIPException("graph", ex.getMessage());
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
                        throw new PIPException("graph", j.getMessage());
                    }
                }
                int i = ps.executeUpdate();
                if (i == 0) {
                    throw new IllegalArgumentException("Something went wrong associating the two nodes.");
                }

            } catch (SQLException ex) {
                throw new PIPException("graph", ex.getMessage());
            }
        }
    }

    /**
     * Delete the Association between the user attribute and Target node.
     *
     * @param ua     the name of the user attribute.
     * @param target the name of the target attribute.
     * @throws PIPException if there is an error dissociating the two nodes.
     */
    @Override
    public void dissociate(String ua, String target) throws PIPException {

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
            throw new PIPException("graph", ex.getMessage());
        }
    }

    /**
     * Retrieve the associations the given node is the source of.  The source node of an association is always a
     * user attribute and this method will throw an exception if an invalid node is provided.  The returned Map will
     * contain the target and operations of each association.
     *
     * @param source the name of the source node.
     * @return a map of the target node names and the operations for each association.
     * @throws PIPException if there is an retrieving the associations of the source node from the graph.
     */
    @Override
    public Map<String, OperationSet> getSourceAssociations(String source) throws PIPException {
        if (!exists(source)) {
            throw new PIPException("graph", "node "+ source+ " does not exist");
        }

        Node ua = getNode(source);
        if (ua.getType() != NodeType.UA) {
            throw new PIPException("graph", "The source node must be an user attribute.");
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
                        throw new PIPException("graph", j.getMessage());
                    }
                }
                sourcesAssoc.put(getNodeNameFromId(end_node_id), operations_set);

            }
            return sourcesAssoc;
        } catch (SQLException ex) {
            throw new PIPException("graph", ex.getMessage());

        }
    }

    /**
     * Retrieve the associations the given node is the target of.  The target node can be an Object Attribute or a User
     * Attribute. This method will throw an exception if a node of any other type is provided.  The returned Map will
     * contain the source node IDs and the operations of each association.
     *
     * @param target the name of the target node.
     * @return a Map of the source Ids and the operations for each association.
     * @throws PIPException if there is an retrieving the associations of the target node from the graph.
     */
    @Override
    public Map<String, OperationSet> getTargetAssociations(String target) throws PIPException {

        if (!exists(target)) {
            throw new PIPException("graph", "node "+ target+ " does not exist");
        }

        Node ua = getNode(target);

        if (ua.getType() != NodeType.UA && ua.getType() != NodeType.OA) {
            throw new PIPException("graph", "The target node must be an user attribute or an object attribute.");
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
                        throw new PIPException("graph", j.getMessage());
                    }
                }
                targetsAssoc.put(getNodeNameFromId(start_node_id), operations_set);

            }
            return targetsAssoc;
        } catch (SQLException ex) {
            throw new PIPException("graph", ex.getMessage());
        }
    }

    /**
     * Convert the graph to a json string with the format:
     * {
     * "nodes": [
     * {
     * "name": "pc1",
     * "type": "PC",
     * "properties": {}
     * },
     * ...
     * ],
     * "assignments": [
     * ["child1", "parent1"],
     * ["child1", "parent2"],
     * ...
     * ],
     * "associations": [
     * {
     * "operations": [
     * "read",
     * "write"
     * ],
     * "source": "ua",
     * "target": "oa"
     * }
     * ]
     * }
     *
     * @return the json string representation of the graph
     */
    @Override
    public String toJson() throws PMException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Collection<Node> nodes = this.getNodes().stream().filter(
                node -> !node.getName().equalsIgnoreCase("super_pc")
                        && !node.getName().equalsIgnoreCase("super_ua1")
                        && !node.getName().equalsIgnoreCase("super_ua2")
                        && !node.getName().equalsIgnoreCase("super_oa")
                        && !node.getName().equalsIgnoreCase("super")
                        && !node.getName().equalsIgnoreCase("super_pc_default_UA")
                        && !node.getName().equalsIgnoreCase("super_pc_default_OA")
                        && !node.getName().equalsIgnoreCase("super_pc_rep")
                        && !node.getName().contains("_default_UA")
                        && !node.getName().contains("_default_OA")
                        && !node.getName().contains("_rep"))
                .collect(Collectors.toList());
        HashSet<String[]> jsonAssignments = new HashSet<>();
        HashSet<JsonAssociation> jsonAssociations = new HashSet<>();

        for (Node node : nodes) {
            Set<String> parents = this.getParents(node.getName());
            for (String parent : parents) {
                jsonAssignments.add(new String[]{node.getName(), parent});
            }
            if (node.getType() == NodeType.UA) {
                Map<String, OperationSet> associations = this.getSourceAssociations(node.getName());
                for (String target : associations.keySet()) {
                    OperationSet ops = associations.get(target);
                    Node targetNode = this.getNode(target);
                    jsonAssociations.add(new JsonAssociation(node.getName(), targetNode.getName(), ops));
                }
            }
        }
        return gson.toJson(new JsonGraph(nodes, jsonAssignments, jsonAssociations));
    }

    /**
     * Load a json string representation of a graph into the current graph.
     *
     * @param json the string representation of the graph
     */
    @Override
    public void fromJson(String json) throws PMException {
       JsonGraph jsonGraph = new Gson().fromJson(json, JsonGraph.class);
        Collection<Node> nodes = jsonGraph.getNodes().stream().filter(
                node -> !node.getName().equalsIgnoreCase("super_pc")
                        && !node.getName().equalsIgnoreCase("super_ua1")
                        && !node.getName().equalsIgnoreCase("super_ua2")
                        && !node.getName().equalsIgnoreCase("super_oa")
                        && !node.getName().equalsIgnoreCase("super")
                        && !node.getName().equalsIgnoreCase("super_pc_default_UA")
                        && !node.getName().equalsIgnoreCase("super_pc_default_OA")
                        && !node.getName().equalsIgnoreCase("super_pc_rep"))
                .collect(Collectors.toList());
        for (Node node : nodes) {
            if (node.getType().equals(PC)) {
                //GraphAdmin graphAdmin = new GraphAdmin(this);
                this.createPolicyClass(node.getName(), node.getProperties());
            } else {
                this.createNode(node.getName(), node.getType(), node.getProperties());
            }
        }
        List<String[]> assignments = new ArrayList<>(jsonGraph.getAssignments());
        for (String[] assignment : assignments) {
            if (assignment.length != 2) {
                throw new PMException("invalid assignment (format=[child, parent]): " + Arrays.toString(assignment));
            }
            String source = assignment[0];
            String target = assignment[1];
            this.assign(source, target);
        }

        Set<JsonAssociation> associations = jsonGraph.getAssociations();
        for (JsonAssociation association : associations) {
            String ua = association.getSource();
            String target = association.getTarget();
            this.associate(ua, target, new OperationSet(association.getOperations()));
        }
    }

    public String toJson_with_config() throws PMException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Collection<Node> nodes = this.getNodes();
        HashSet<String[]> jsonAssignments = new HashSet<>();
        HashSet<JsonAssociation> jsonAssociations = new HashSet<>();

        for (Node node : nodes) {
            Set<String> parents = this.getParents(node.getName());
            for (String parent : parents) {
                jsonAssignments.add(new String[]{node.getName(), parent});
            }
            if (node.getType() == NodeType.UA) {
                Map<String, OperationSet> associations = this.getSourceAssociations(node.getName());
                for (String target : associations.keySet()) {
                    OperationSet ops = associations.get(target);
                    Node targetNode = this.getNode(target);
                    jsonAssociations.add(new JsonAssociation(node.getName(), targetNode.getName(), ops));
                }
            }
        }
        return gson.toJson(new JsonGraph(nodes, jsonAssignments, jsonAssociations));
    }

    public void deleteAll() throws PIPException {
        try (
                Connection con = this.conn.getConnection();
                PreparedStatement ps = con.prepareStatement(MySQLHelper.DELETE_DENYS);
                PreparedStatement ps2 = con.prepareStatement(MySQLHelper.DELETE_ASSIGNMENTS);
                PreparedStatement ps3 = con.prepareStatement(MySQLHelper.DELETE_ASSOCIATIONS);
                PreparedStatement ps4 = con.prepareStatement(MySQLHelper.DELETE_NODES)
        ){

            ps.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
            ps4.executeUpdate();
        } catch (SQLException e) {
            throw new PIPException("graph", e.getMessage());
        }
    }

    public static class JsonGraph {
        Collection<Node> nodes;
        Set<String[]>  assignments;
        Set<JsonAssociation> associations;

        JsonGraph(Collection<Node> nodes, Set<String[]> assignments, Set<JsonAssociation> associations) {
            this.nodes = nodes;
            this.assignments = assignments;
            this.associations = associations;
        }

        public Collection<Node> getNodes() {
            return nodes;
        }

        public Set<String[]> getAssignments() {
            return assignments;
        }

        public Set<JsonAssociation> getAssociations() {
            return associations;
        }
    }

    private static class JsonAssociation {
        String source;
        String target;
        Set<String> operations;

        public JsonAssociation(String source, String target, Set<String> operations) {
            this.source = source;
            this.target = target;
            this.operations = operations;
        }

        public String getSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }

        public Set<String> getOperations() {
            return operations;
        }
    }
}


