package gov.nist.csd.pm.pip.graph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

/**
 * GraphSerializer is a supplemental way of serializing a Graph object. The format is as follows:
 * # this is a comment
 * node PC pc1
 * node OA oa1
 * node UA ua1
 *
 * assign ua1 pc1
 * assign oa1 pc1
 *
 * # operations are space separated
 * assoc ua1 oa1 read write
 */
public interface GraphSerializer {

    String serialize() throws PMException;

    Graph deserialize(String s) throws PMException;

}
