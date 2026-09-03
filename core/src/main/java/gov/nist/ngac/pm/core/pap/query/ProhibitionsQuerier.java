/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A {@link ProhibitionsQuery} implementation backed by the policy store's prohibitions store.
 */
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

        new DepthFirstGraphWalker(store.graph()::getAdjacentDescendants)
            .withVisitor((n) -> {
                pros.addAll(getNodeProhibitions(n));
            })
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
