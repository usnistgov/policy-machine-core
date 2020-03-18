package gov.nist.csd.pm.pip.graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;

import java.sql.*;
import java.util.*;

public class MySQLGraph implements Graph {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectReader reader = new ObjectMapper().readerFor(HashMap.class);
    private static final String NODE_NOT_FOUND_MSG = "node %s does not exist";



    public static String toJSON(Map<String, String> map) throws JsonProcessingException {
        return objectMapper.writeValueAsString(map);
    }

    public static String hashSetToJSON(Set<String> set) throws JsonProcessingException {
        return objectMapper.writeValueAsString(set);
    }

    @Override
    public Node createPolicyClass(long id, String name, Map<String, String> properties) throws PMException {
        return null;
    }

    /**
     * Create a node in the mysql graph.  The ID field of the passed Node must not be 0.
     *
     * @return the ID that was passed as part of the node parameter.
     * @throws IllegalArgumentException When the provided node is null.
     * @throws IllegalArgumentException When the provided node has an ID of 0.
     * @throws IllegalArgumentException When the provided node has a null or empty name.
     * @throws IllegalArgumentException When the provided node has a null type.
     * @throws PMException When the provided ID already exists in the mysql graph
     */
    @Override
    public Node createNode(long id, String name, NodeType type, Map<String, String> properties, long initialParent, long ... additionalParents) throws PMException  {
        //check for null values
        if (id == 0) {
            throw new IllegalArgumentException("no ID was provided when creating a node in the mysql graph");
        }
        else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a node in the mysql graph");
        }
        else if (type == null) {
            throw new IllegalArgumentException("a null type was provided to the mysql graph when creating a node");
        }

        else if (exists(id)) {
            throw new PMException("You cannot create a node with that ID, another node with that ID already exists.");
        }

        MySQLConnection conn = new MySQLConnection();
        ResultSet rs = null;
        ResultSet rs_type = null;
        PreparedStatement pstmt = null;
        PreparedStatement ps = null;
        int nodeId = 0;
        try {
            Connection con = conn.getConnection();
            //====================  NodeType parser : Retrieve node_type_id ====================
            String query = "SELECT * from node_type where name =?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, type.toString());
            rs_type = pstmt.executeQuery();
            int node_type_id = 0;
            while (rs_type.next()){
                node_type_id = rs_type.getInt("node_type_id");
            }
            //==================== create the node from (id, node_type_id, name, properties) ====================
            ps = con.prepareStatement("INSERT INTO node(node_id, node_type_id, name, node_property)" +
                    " VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, id);
            ps.setInt(2, node_type_id);
            ps.setString(3,name);
            //Json serialization using Jackson
            if ( properties == null) {
                ps.setString(4, null);
            } else {
                try {
                    ps.setString(4, toJSON(properties));
                } catch (JsonProcessingException j) {
                    j.printStackTrace();
                }
            }

            int rowAffected = ps.executeUpdate();

            Node node = new Node(nodeId, name, type, properties);
            con.close();
            return node;
        } catch (SQLException s) {
            s.printStackTrace();
        }
        finally {
            try {
                //We can also use DbUtils to clean up: DbUtils.closeQuietly(rs);
                if(rs != null) {rs.close();}
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
    public void updateNode(long id, String name, Map<String, String> properties) throws PMException {
        if (id == 0) {
            throw new IllegalArgumentException("no ID was provided when updating the node in the mysql graph");
        }
        else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when updating the node in the mysql graph");
        }

        MySQLConnection connection = new MySQLConnection();
        PreparedStatement ps = null;

        try {
            Connection con = connection.getConnection();
            String query = "UPDATE node SET name = ?, node_property = ? WHERE node_id = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, name);
            try {
                ps.setString(2, toJSON(properties));
            } catch (JsonProcessingException j) {
                j.printStackTrace();
            }
            ps.setLong(3, id);
            ps.executeUpdate();
            if (ps.executeUpdate() == 0) {
                con.close();
                throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, id));
            }
            System.out.println("The following node has been updated : " + id);
            con.close();
        } catch (SQLException s) {
            s.printStackTrace();
            throw new PMException("Cannot connect to the database");
        }
        finally {
            try {
                if(ps != null) {ps.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Delete the node with the given ID from the graph.
     *
     * @param nodeID the ID of the node to delete.
     * @throws IllegalArgumentException When the provided node does not exist in the mysql graph
     * @throws PMException If there was an error deleting the node
     */
    @Override
    public void deleteNode(long nodeID) throws PMException {
        MySQLConnection mySQLConnection = new MySQLConnection();
        PreparedStatement ps = null;

        try {
            Connection con = mySQLConnection.getConnection();
            ps = con.prepareStatement("DELETE from node where node_id=?");
            ps.setLong(1, nodeID);
            if (ps.executeUpdate() == 0) {
                con.close();
                throw new IllegalArgumentException("The node you want to delete does not exist");
            }
            ps.executeUpdate();
            con.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (ps != null) {ps.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Check that a node with the given ID exists in the graph.
     *
     * @param nodeID the ID of the node to check for.
     * @return true or False if a node with the given ID exists or not.
     * @throws PMException if there is an error checking if the node exists in the graph.
     */
    @Override
    public boolean exists(long nodeID) throws PMException {
        MySQLConnection mySQLConnection = new MySQLConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection con = mySQLConnection.getConnection();
            stmt = con.createStatement();
            String query = "SELECT * from node where node_id=" + nodeID;
            rs = stmt.executeQuery(query);
            List<Node> node = new ArrayList<>();
            while (rs.next()) {
                long                id = rs.getInt("node_id");
                String              name = rs.getString("name");
                Node cur_node = new Node(id, name, null, null);
                node.add(cur_node);

            }
            if (!node.isEmpty()) {
                con.close();
                return true;
            }
            con.close();
            return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (rs != null) {rs.close();}
                if (stmt != null) {stmt.close();}

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Get the set of policy classes. The returned set is just the IDs of each policy class.
     *
     * @return the set of policy class IDs.
     * @throws PMException if there is an error retrieving the IDs of the policy classes.
     */
    @Override
    public Set<Long> getPolicyClasses() throws PMException {
        Set<Node> nodes = new HashSet<>(getNodes());
        Set<Long> Ids = new HashSet<>();
        for (Node node : nodes){
            if (node.getType().equals(NodeType.toNodeType("PC"))) {
                Ids.add(node.getID());
            }
        }

        if (Ids.isEmpty()) {
            throw new PMException("There are no Policies in the current database");
        }
        return Ids;
    }

    /**
     * Retrieve the set of all nodes in the graph.
     *
     * @return a Set of all the nodes in the graph.
     * @throws PMException if there is an error retrieving all nodes in the graph.
     */
    @Override
    public Set<Node> getNodes() throws PMException {
        MySQLConnection mySQLConnection = new MySQLConnection();
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs_type = null;
        PreparedStatement pstmt = null;
        Node node = null;
        Set<Node> nodes = new HashSet<>();
        List<Long> node_types = new ArrayList<>();

        try {
            Connection con = mySQLConnection.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * from node");

            // return node_type_id instead of numeric value of the node
            long node_type =  0;
            while (rs.next()) {
                long                node_id = rs.getInt("node_id");
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
                node = new Node(node_id, name, type, properties);
                node_types.add(node_type);
                nodes.add(node);
            }
            //retrieve all nodes
            int index = 0;
            for (Node node_k: nodes) {
                pstmt = con.prepareStatement("SELECT * from node_type where node_type_id =?");
                pstmt.setLong(1, node_types.get(index));
                rs_type = pstmt.executeQuery();
                String name_type = "";
                while (rs_type.next()){
                    name_type = rs_type.getString("name");
                }
                node_k.setType(NodeType.toNodeType(name_type));
                index ++;
            }
            con.close();
            if (nodes.size() == 0 ) {
                throw new PMException("There are no nodes.");
            }else {
                return nodes;
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        finally {
            try {
                if(rs != null) {rs.close();}
                if (stmt != null) { stmt.close();}
                if(pstmt != null) {pstmt.close();}
                if(rs_type != null) {rs_type.close();}

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return nodes;
    }

    @Override
    public Node getNode(String name, NodeType type, Map<String, String> properties) throws PMException {
        return null;
    }

    @Override
    public Node getNode(long id) throws PMException {

        Collection<Node> nodes = getNodes();
        Node node;
        try {
            node = nodes.stream()
                    .filter(node_k -> node_k.getID() == id)
                    .iterator().next();
        } catch (Exception p) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, id));
        }
        return node;
    }

    /**
     * Search the graph for nodes matching the given parameters. A node must
     * contain all properties provided to be returned.
     * To get all the nodes that have a specific property key with any value use "*" as the value in the parameter.
     * (i.e. {key=*})
     *
     * @param name       the name of the nodes to search for.
     * @param type       the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return a set of nodes that match the given search criteria.
     * @throws PMException if there is an error searching the graph.
     */
    @Override
    public Set<Node> search(String name, NodeType type, Map<String, String> properties) throws PMException {

        if (properties == null) {
            properties = new HashMap<>();
        }

        HashSet<Node> results = new HashSet<>();
        // iterate over the nodes to find ones that match the search parameters
        for (Node node : getNodes()) {
            // if the name parameter is not null and the current node name does not equal the name parameter, do not add
            // if the type parameter is not null and the current node type does not equal the type parameter, do not add
            if (name != null && !node.getName().equals(name) ||
                    type != null && !node.getType().equals(type)) {
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
     * Get the set of nodes that are assigned to the node with the given ID.
     *
     * @param nodeID the ID of the node to get the children of.
     * @return the Set of NGACNodes that are assigned to the node with the given ID.
     * @throws PMException if there is an error retrieving the children of the node.
     */
    @Override
    public Set<Long> getChildren(long nodeID) throws PMException {
        if (!exists(nodeID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, nodeID));
        }

        MySQLConnection mySQLConnection = new MySQLConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Set<Long> sources = new HashSet<>();

        try {
            Connection con = mySQLConnection.getConnection();
            stmt = con.createStatement();
            String query = "SELECT * from assignment where start_node_id=" + nodeID;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                long              end_node_id = rs.getInt("end_node_id");
                sources.add(end_node_id);

            }
            con.close();
            return sources;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (rs != null) {rs.close();}
                if (stmt != null) {stmt.close();}

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Get the set of nodes that the node with the given ID is assigned to.
     *
     * @param nodeID the ID of the node to get the parents of.
     * @return the Set of NGACNodes that are assigned to the node with the given ID.
     * @throws PMException if there is an error retrieving the parents of the node.
     */
    @Override
    public Set<Long> getParents(long nodeID) throws PMException {
        if (!exists(nodeID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, nodeID));
        }

        MySQLConnection mySQLConnection = new MySQLConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Set<Long> targets = new HashSet<>();

        try {
            Connection con = mySQLConnection.getConnection();
            stmt = con.createStatement();
            String query = "SELECT * from assignment where end_node_id=" + nodeID;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                long              start_node_id = rs.getInt("start_node_id");
                targets.add(start_node_id);

            }
            con.close();
            return targets;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (rs != null) {rs.close();}
                if (stmt != null) {stmt.close();}

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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
    public void assign(long childID, long parentID) throws PMException {
        Node childNode = getNode(childID);
        Node parentNode = getNode(parentID);
        if (!exists(childID)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, childID));
        }
        else if (!exists(parentID)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, parentID));
        }
        Assignment.checkAssignment(childNode.getType(), parentNode.getType());

        MySQLConnection mySQLConnection = new MySQLConnection();
        PreparedStatement ps = null;

        try {
            Connection con = mySQLConnection.getConnection();
            ps = con.prepareStatement("INSERT into assignment( start_node_id, end_node_id)" +
                    "VALUES (?, ?)");
            ps.setLong(1, childID);
            ps.setLong(2, parentID);
            ps.executeUpdate();

        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            try {
                if (ps != null) { ps.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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
    public void deassign(long childID, long parentID) throws PMException {

        if (!exists(childID)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, childID));
        }
        else if (!exists(parentID)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, parentID));
        }

        MySQLConnection mySQLConnection = new MySQLConnection();
        PreparedStatement ps = null;

        try {
            Connection con = mySQLConnection.getConnection();
            ps = con.prepareStatement("DELETE from assignment where start_node_id=? AND end_node_id = ?");
            ps.setLong(1, childID);
            ps.setLong(2, parentID);
            if (ps.executeUpdate() == 0) {
                con.close();
                throw new IllegalArgumentException("The assignment you want to delete does not exist");
            }
            ps.executeUpdate();
            con.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (ps != null) {ps.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Returns true if the child is assigned to the parent.
     *
     * @param childID the ID of the child node
     * @param parentID the ID of the parent node
     * @return true if the child is assigned to the parent, false otherwise
     * @throws PMException if there is an error checking if the child is assigned to the parent
     */
    @Override
    public boolean isAssigned(long childID, long parentID) throws PMException {
        return true;
    }

    /**
     * Create an Association between the user attribute and the Target node with the provided operations. If an association
     * already exists between these two nodes, overwrite the existing operations with the ones provided.  Associations
     * can only begin at a user attribute but can point to either an Object or user attribute
     *
     * @param uaID The ID of the user attribute.
     * @param targetID The ID of the target attribute.
     * @param operations A Set of operations to add to the association.
     * @throws PMException if there is an error associating the two nodes.
     */
    @Override
    public void associate(long uaID, long targetID, OperationSet operations) throws PMException {
        //throw PMException if nodes does not exists
        if (!exists(uaID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, uaID));
        }
        else if (!exists(targetID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, targetID));
        }

        Node ua = getNode(uaID);
        Node target = getNode(targetID);

        // check that the association is valid
        Association.checkAssociation(ua.getType(), target.getType());

        MySQLConnection mySQLConnection = new MySQLConnection();
        PreparedStatement ps = null;
        try {
            Connection con = mySQLConnection.getConnection();
            ps = con.prepareStatement("INSERT into association( start_node_id, end_node_id, operation_set)" +
                    "VALUES (?, ?, ?)");
            ps.setLong(1, uaID);
            ps.setLong(2, targetID);

            //Json serialization using Jackson
            if ( operations == null) {
                ps.setString(3, null);
            } else {
                try {
                    ps.setString(3, hashSetToJSON(operations));
                } catch (JsonProcessingException j) {
                    j.printStackTrace();
                }
            }
            int i = ps.executeUpdate();
            if (i > 0) {
                con.close();
            } else {
                con.close();
                throw new IllegalArgumentException("Something went wrong associating the two nodes.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (ps != null) {ps.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Delete the Association between the user attribute and Target node.
     *
     * @param uaID     the ID of the user attribute.
     * @param targetID the ID of the target attribute.
     * @throws PMException if there is an error dissociating the two nodes.
     */
    @Override
    public void dissociate(long uaID, long targetID) throws PMException {
        MySQLConnection mySQLConnection = new MySQLConnection();
        PreparedStatement ps = null;

        try {
            Connection con = mySQLConnection.getConnection();
            ps = con.prepareStatement("DELETE from association where start_node_id=? AND end_node_id = ?");
            ps.setLong(1, uaID);
            ps.setLong(2, targetID);
            int i = ps.executeUpdate();
            if (i > 0) {
                con.close();
            } else {
                con.close();
                throw new IllegalArgumentException("The association you want to delete does not exist");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (ps != null) {ps.close();}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Retrieve the associations the given node is the source of.  The source node of an association is always a
     * user attribute and this method will throw an exception if an invalid node is provided.  The returned Map will
     * contain the target and operations of each association.
     *
     * @param sourceID the ID of the source node.
     * @return a Map of the target node IDs and the operations for each association.
     * @throws PMException if there is an retrieving the associations of the source node from the graph.
     */
    @Override
    public Map<Long, OperationSet> getSourceAssociations(long sourceID) throws PMException {
        if (!exists(sourceID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, sourceID));
        }

        Node ua = getNode(sourceID);
        if (ua.getType() != NodeType.UA) {
            throw new PMException("The source node must be an user attribute.");
        }

        MySQLConnection mySQLConnection = new MySQLConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Map<Long, OperationSet> sourcesAssoc = new HashMap<>();

        try {
            Connection con = mySQLConnection.getConnection();
            stmt = con.createStatement();
            String query = "SELECT * from association where start_node_id=" + sourceID;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                long              end_node_id = rs.getInt("end_node_id");
                String            operations = rs.getString("operation_set");
                //remove excessive '[', ']'
                operations= operations.replace("[","");
                operations = operations.replace("]", "");

                OperationSet operations_set = new OperationSet();
                operations_set.add(operations);
                sourcesAssoc.put(end_node_id, operations_set);

            }
            con.close();
            return sourcesAssoc;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (stmt != null) {stmt.close();}
                if (rs != null) {rs.close();}

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Retrieve the associations the given node is the target of.  The target node can be an Object Attribute or a User
     * Attribute. This method will throw an exception if a node of any other type is provided.  The returned Map will
     * contain the source node IDs and the operations of each association.
     *
     * @param targetID the ID of the target node.
     * @return a Map of the source Ids and the operations for each association.
     * @throws PMException if there is an retrieving the associations of the target node from the graph.
     */
    @Override
    public Map<Long, OperationSet> getTargetAssociations(long targetID) throws PMException {

        if (!exists(targetID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, targetID));
        }

        Node ua = getNode(targetID);
        if (ua.getType() != NodeType.UA && ua.getType() != NodeType.OA) {
            throw new PMException("The source node must be an user attribute or an object attribute.");
        }

        MySQLConnection mySQLConnection = new MySQLConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Map<Long, OperationSet> targetsAssoc = new HashMap<>();

        try {
            Connection con = mySQLConnection.getConnection();
            stmt = con.createStatement();
            String query = "SELECT * from association where end_node_id=" + targetID;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                long              start_node_id = rs.getInt("start_node_id");
                String            operations = rs.getString("operation_set");
                //remove excessive '[', ']'
                operations= operations.replace("[","");
                operations = operations.replace("]", "");

                OperationSet operations_set = new OperationSet();
                operations_set.add(operations);
                targetsAssoc.put(start_node_id, operations_set);
            }
            con.close();
            return targetsAssoc;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new PMException("Cannot connect to the Database" + ex);

        } finally {
            try {
                if (stmt != null) {stmt.close();}
                if (rs != null) {rs.close();}

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
