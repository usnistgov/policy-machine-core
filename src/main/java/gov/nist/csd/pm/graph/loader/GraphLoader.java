package gov.nist.csd.pm.graph.loader;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.relationships.Assignment;
import gov.nist.csd.pm.graph.model.relationships.Association;

import java.util.HashSet;

/**
 * This interface provides methods needed to load a graph into memory from a database.
 */
public interface GraphLoader {

    /**
     * Get all of the nodes in the graph.
     * @return the set of all nodes in the graph.
     * @throws PMDBException if there is an error getting the nodes from the database.
     * @throws PMGraphException if there is an exception converting the data in the database to nodes.
     */
    HashSet<NodeContext> getNodes() throws PMDBException, PMGraphException;

    /**
     * Get all of the assignments in the graph.
     * @return a set of all the assignments in the graph.
     * @throws PMDBException if there is an error loading the assignments.
     */
    HashSet<Assignment> getAssignments() throws PMDBException;

    /**
     * Get all of the associations in the graph.
     * @return a set of all the associations in the graph.
     * @throws PMDBException if there is an error loading the associations.
     */
    HashSet<Association> getAssociations() throws PMDBException;
}
