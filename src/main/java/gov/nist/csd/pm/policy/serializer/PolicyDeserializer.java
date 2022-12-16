package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.policy.author.GraphAuthor;
import gov.nist.csd.pm.policy.author.ObligationsAuthor;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.ProhibitionsAuthor;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

@FunctionalInterface
public interface PolicyDeserializer {

    void deserialize(PolicyAuthor policyAuthor, String s) throws PMException;


}
