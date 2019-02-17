package gov.nist.csd.pm.db.neo4j;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Neo4j helper methods
 */
public class Neo4jHelper {

    public static final String COMMA_DELIMETER     = ",\\s*";
    public static final Label  NODE_LABEL          = Label.label("NODE");
    public static final Label  PC_LABEL            = Label.label("PC");
    public static final String ID_PROPERTY         = "id";
    public static final String NAME_PROPERTY       = "name";
    public static final String TYPE_PROPERTY       = "type";
    public static final String OPERATIONS_PROPERTY = "operations";

    public enum RelTypes implements RelationshipType
    {
        ASSIGNED_TO,
        ASSOCIATED_WITH
    }

    /**
     * Given a ResultSet, extract a list of nodes. Each element in the ResultSet is a json representation of a Node
     *
     * @param rs the ResultSet containing the nodes from the database.
     * @return the set of nodes from the ResultSet.
     * @throws PMDBException if there is an error iterating through the ResultSet.
     * @throws PMGraphException if there is an error converting a map to a Node.
     */
    public static HashSet<NodeContext> getNodesFromResultSet(ResultSet rs) throws PMDBException, PMGraphException {
        try {
            HashSet<NodeContext> nodes = new HashSet<>();
            while (rs.next()) {
                HashMap map = (HashMap) rs.getObject(1);
                NodeContext node = mapToNode(map);
                nodes.add(node);
            }
            return nodes;
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Given a map of properties representing a Node, return a Node object.  If the given map is null, then return null.
     * @param map the map to convert into a NodeContext
     * @return a NodeContext representation of the provided map, or null if the map provided was null.
     * @throws PMGraphException if there is an error converting the map to a Node.
     */
    public static NodeContext mapToNode(Map map) throws PMGraphException {
        if(map == null) {
            return null;
        }

        // first, convert the json to a map
        long id = (long) map.get("id");
        if(id == 0) {
            throw new PMGraphException("encountered an ID of 0 when converting a map to a node");
        }

        String name = (String)map.get("name");
        if(name == null || name.isEmpty()) {
            throw new PMGraphException(String.format("the node with the ID %d has a null or empty name", id));
        }

        NodeType type = NodeType.toNodeType((String) map.get("type"));
        if(type == null) {
            throw new PMGraphException(String.format("the node with the ID %d has a null type", id));
        }

        HashMap<String, String> properties = new HashMap<>();
        for (Object o : map.keySet()) {
            String key = (String)o;
            if(!(key.equals("id") || key.equals("name") || key.equals("type") || key.startsWith("_"))) {
                properties.put(key, (String) map.get(o));
            }
        }

        return new NodeContext(id, name, type, properties);
    }

    /**
     * Convert a Collection of Strings to a cypher array string.
     * (i.e. ["read", "write"] to "['read', 'write']"
     * @param c the HashSet to convert to a string
     * @return a string representation of the given HashSet
     */
    public static String setToCypherArray(HashSet<String> c) {
        String str = "[";
        for (String op : c) {
            op = "'" + op + "'";
            if (str.length()==1) {
                str += op;
            }
            else {
                str += "," + op;
            }
        }
        str += "]";
        return str;
    }

    /**
     * Convert a json string representing a set of strings to an actual set of Strings.
     * @param json the json string to convert into a set of strings
     * @return the set of string converted from the given json string.
     */
    public static HashSet<String> getStringSetFromJson(String json) {
        HashSet<String> set = new HashSet<>();
        if(json != null) {
            String[] opsArr = json.replaceAll("[\\[\\]\"]", "").split(COMMA_DELIMETER);
            if (!opsArr[0].isEmpty()) {
                set.addAll(Arrays.asList(opsArr));
            }
        }
        return set;
    }
}
