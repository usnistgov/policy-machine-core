package gov.nist.csd.pm.policy.review;

import gov.nist.csd.pm.policy.model.graph.dag.TargetDagResult;
import gov.nist.csd.pm.policy.model.graph.dag.UserDagResult;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_RESOURCE_ACCESS_RIGHTS;

public abstract class PolicyReview implements AccessReview, GraphReview, ProhibitionsReview, ObligationsReview {

}
