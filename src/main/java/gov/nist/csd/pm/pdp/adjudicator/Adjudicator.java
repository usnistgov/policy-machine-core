package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.SuperPolicy;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.pml.PMLContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssociationException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC_REP;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.GET_ASSOCIATIONS;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.PC;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class Adjudicator implements PolicySerializable, Policy {

    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    private final GraphAdjudicator graphAdjudicator;
    private final ProhibitionsAdjudicator prohibitionsAdjudicator;
    private final ObligationsAdjudicator obligationsAdjudicator;
    private final UserDefinedPMLAdjudicator userDefinedPMLAdjudicator;

    public Adjudicator(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = new AccessRightChecker(pap, policyReviewer);

        graphAdjudicator = new GraphAdjudicator(userCtx, pap, accessRightChecker);
        prohibitionsAdjudicator = new ProhibitionsAdjudicator(userCtx, pap, accessRightChecker);
        obligationsAdjudicator = new ObligationsAdjudicator(userCtx, pap, accessRightChecker);
        userDefinedPMLAdjudicator = new UserDefinedPMLAdjudicator(userCtx, pap, accessRightChecker);
    }

    @Override
    public String toString(PolicySerializer policySerializer) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, TO_STRING);

        return null;
    }

    @Override
    public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, FROM_STRING);
    }

    @Override
    public GraphAdjudicator graph() {
        return graphAdjudicator;
    }

    @Override
    public ProhibitionsAdjudicator prohibitions() {
        return prohibitionsAdjudicator;
    }

    @Override
    public ObligationsAdjudicator obligations() {
        return obligationsAdjudicator;
    }

    @Override
    public UserDefinedPMLAdjudicator userDefinedPML() {
        return userDefinedPMLAdjudicator;
    }
}
