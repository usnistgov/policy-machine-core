package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.common.graph.dag.Direction;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.query.ProhibitionsQuery;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProhibitionsQuerier extends Querier implements ProhibitionsQuery {

    private GraphQuerier graphQuerier;

    public ProhibitionsQuerier(PolicyStore store, GraphQuerier graphQuerier) {
        super(store);

        this.graphQuerier = graphQuerier;
    }

    @Override
    public Map<Node, Collection<Prohibition>> getProhibitions() throws PMException {
        return store.prohibitions().getNodeProhibitions();
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithSubject(ProhibitionSubject subject) throws PMException {
        return store.prohibitions().getNodeProhibitions().getOrDefault(subject, new ArrayList<>());
    }

    @Override
    public Prohibition getProhibition(String name) throws PMException {
        if (!store.prohibitions().prohibitionExists(name)) {
            throw new ProhibitionDoesNotExistException(name);
        }

        return store.prohibitions().getProhibition(name);
    }

    @Override
    public Collection<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException {
        List<Prohibition> pros = new ArrayList<>();

        new DepthFirstGraphWalker(graphQuerier)
                .withVisitor((n) -> {
                    pros.addAll(getProhibitionsWithSubject(n));
                })
                .withDirection(Direction.DESCENDANTS)
                .walk(subject);

        return pros;
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithContainer(long containerId) throws PMException {
        Collection<Prohibition> pros = new ArrayList<>();

        Map<String, Collection<Prohibition>> prohibitions = getProhibitions();
        for (String subject : prohibitions.keySet()) {
            Collection<Prohibition> subjectProhibitions = prohibitions.get(subject);
            for (Prohibition prohibition : subjectProhibitions) {
                for (ContainerCondition cc : prohibition.getContainers()) {
                    if (cc.getId().equals(containerId)) {
                        pros.add(prohibition);
                    }
                }
            }
        }

        return pros;
    }
}
