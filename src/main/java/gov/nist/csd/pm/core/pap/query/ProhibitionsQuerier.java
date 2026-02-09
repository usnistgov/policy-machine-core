package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.core.common.graph.dag.Direction;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.store.GraphStoreDFS;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProhibitionsQuerier extends Querier implements ProhibitionsQuery {

    public ProhibitionsQuerier(PolicyStore store) {
        super(store);
    }

    @Override
    public Collection<Prohibition> getProhibitions() throws PMException {
        return store.prohibitions().getAllProhibitions();
    }

    @Override
    public Collection<Prohibition> getNodeProhibitions(long nodeId) throws PMException {
        return store.prohibitions().getNodeProhibitions(nodeId);
    }

    @Override
    public Collection<Prohibition> getProcessProhibitions(String process) throws PMException {
        return store.prohibitions().getProcessProhibitions(process);
    }

    @Override
    public Prohibition getProhibition(String name) throws PMException {
        if (!store.prohibitions().prohibitionExists(name)) {
            throw new ProhibitionDoesNotExistException(name);
        }

        return store.prohibitions().getProhibition(name);
    }

    @Override
    public boolean prohibitionExists(String name) throws PMException {
        return store.prohibitions().prohibitionExists(name);
    }

    @Override
    public Collection<Prohibition> getInheritedProhibitionsFor(long subjectId) throws PMException {
        List<Prohibition> pros = new ArrayList<>();

        new GraphStoreDFS(store.graph())
            .withVisitor((n) -> {
                pros.addAll(getNodeProhibitions(n));
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
            if (prohibition.getInclusionSet().contains(containerId)
                || prohibition.getExclusionSet().contains(containerId)) {
                pros.add(prohibition);
            }
        }

        return pros;
    }
}
