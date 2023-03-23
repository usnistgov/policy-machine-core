package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;
import java.util.Map;

public interface PolicyReader {

    AccessRightSet getResourceAccessRights() throws PMException;
    boolean nodeExists(String name) throws PMException;
    Node getNode(String name) throws PMException;
    List<String> search(NodeType type, Map<String, String> properties) throws PMException;
    List<String> getPolicyClasses() throws PMException;
    List<String> getChildren(String node) throws PMException;
    List<String> getParents(String node) throws PMException;
    List<Association> getAssociationsWithSource(String ua) throws PMException;
    List<Association> getAssociationsWithTarget(String target) throws PMException;

    Map<String, List<Prohibition>> getProhibitions() throws PMException;
    boolean prohibitionExists(String label) throws PMException;
    List<Prohibition> getProhibitionsWithSubject(String subject) throws PMException;
    Prohibition getProhibition(String label) throws PMException;

    List<Obligation> getObligations() throws PMException;
    boolean obligationExists(String label) throws PMException;
    Obligation getObligation(String label) throws PMException;

    Map<String, FunctionDefinitionStatement> getPALFunctions() throws PMException;
    Map<String, Value> getPALConstants() throws PMException;
    PALContext getPALContext() throws PMException;

}
