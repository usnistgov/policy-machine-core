package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;

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


