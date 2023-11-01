package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.policy.model.graph.dag.walker.dfs.DepthFirstGraphWalker;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.review.ProhibitionsReview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProhibitionsReviewer implements ProhibitionsReview {

    private final Policy policy;

    public ProhibitionsReviewer(Policy policy) {
        this.policy = policy;
    }

    @Override
    public List<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException {
        List<Prohibition> pros = new ArrayList<>();

        new DepthFirstGraphWalker(policy.graph())
                .withVisitor((n) -> {
                    pros.addAll(policy.prohibitions().getWithSubject(n));
                })
                .withDirection(Direction.PARENTS)
                .walk(subject);

        return pros;
    }

    @Override
    public List<Prohibition> getProhibitionsWithContainer(String container) throws PMException {
        List<Prohibition> pros = new ArrayList<>();

        Map<String, List<Prohibition>> prohibitions = policy.prohibitions().getAll();
        for (String subject : prohibitions.keySet()) {
            List<Prohibition> subjectProhibitions = prohibitions.get(subject);
            for (Prohibition prohibition : subjectProhibitions) {
                for (ContainerCondition cc : prohibition.getContainers()) {
                    if (cc.getName().equals(container)) {
                        pros.add(prohibition);
                    }
                }
            }
        }

        return pros;
    }

}
