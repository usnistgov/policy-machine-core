package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.dag.TargetDagResult;
import gov.nist.csd.pm.policy.model.graph.dag.UserDagResult;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.review.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public class PolicyReviewer implements PolicyReview {

    private final AccessReviewer accessReviewer;
    private final GraphReviewer graphReviewer;
    private final ProhibitionsReviewer prohibitionsReviewer;
    private final ObligationsReviewer obligationsReviewer;

    public PolicyReviewer(PAP pap) {
        this.accessReviewer = new AccessReviewer(pap);
        this.graphReviewer = new GraphReviewer(pap);
        this.prohibitionsReviewer = new ProhibitionsReviewer(pap);
        this.obligationsReviewer = new ObligationsReviewer(pap, graphReviewer);
    }

    @Override
    public AccessReviewer access() {
        return accessReviewer;
    }

    @Override
    public GraphReviewer graph() {
        return graphReviewer;
    }

    @Override
    public ProhibitionsReviewer prohibitions() {
        return prohibitionsReviewer;
    }

    @Override
    public ObligationsReviewer obligations() {
        return obligationsReviewer;
    }
}
