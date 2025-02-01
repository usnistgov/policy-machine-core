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
    public Collection<Prohibition> getProhibitions() throws PMException {
        List<Prohibition> prohibitions = new ArrayList<>();

        Map<Long, Collection<Prohibition>> nodeProhibitions = store.prohibitions().getNodeProhibitions();
        nodeProhibitions.values().forEach(prohibitions::addAll);

        Map<String, Collection<Prohibition>> processProhibitions = store.prohibitions().getProcessProhibitions();
        processProhibitions.values().forEach(prohibitions::addAll);

        return prohibitions;
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithSubject(ProhibitionSubject subject) throws PMException {
        if (subject.isNode()) {
            Node node = store.graph().getNodeById(subject.getNodeId());
            return store.prohibitions().getNodeProhibitions().getOrDefault(node.getId(), new ArrayList<>());
        } else {
            return store.prohibitions().getProcessProhibitions().getOrDefault(subject.getProcess(), new ArrayList<>());
        }

    }

    @Override
    public Prohibition getProhibition(String name) throws PMException {
        if (!store.prohibitions().prohibitionExists(name)) {
            throw new ProhibitionDoesNotExistException(name);
        }

        return store.prohibitions().getProhibition(name);
    }

    @Override
    public Collection<Prohibition> getInheritedProhibitionsFor(long subjectId) throws PMException {
        List<Prohibition> pros = new ArrayList<>();

        new DepthFirstGraphWalker(graphQuerier)
                .withVisitor((n) -> {
                    pros.addAll(getProhibitionsWithSubject(new ProhibitionSubject(n)));
                })
                .withDirection(Direction.DESCENDANTS)
                .walk(subjectId);

        return pros;
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithContainer(long containerId) throws PMException {
        Collection<Prohibition> pros = new ArrayList<>();

        Collection<Prohibition> prohibitions = getProhibitions();
        for (Prohibition prohibition : prohibitions) {
            for (ContainerCondition cc : prohibition.getContainers()) {
                if (cc.getId() == (containerId)) {
                    pros.add(prohibition);
                }
            }
        }

        return pros;
    }
}
