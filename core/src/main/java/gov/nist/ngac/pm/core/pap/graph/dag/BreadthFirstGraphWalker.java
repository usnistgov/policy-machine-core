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

package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link GraphWalker} that visits each level of adjacent nodes before recursing into the next, evaluating
 * short circuits and propagating a level at a time rather than per node.
 */
public class BreadthFirstGraphWalker extends GraphWalker {

    public BreadthFirstGraphWalker(AdjacencyRetriever adjacencyRetriever) {
        super(adjacencyRetriever);
    }

    @Override
    public void walk(long start) throws PMException {
        visitor.visit(start);
        if (allPathsShortCircuit.evaluate(start) || singlePathShortCircuit.evaluate(start)) {
            return;
        }

        walkInternal(start);
    }

    private boolean walkInternal(long start) throws PMException {
        Collection<Long> nextLevel = adjacencyRetriever.getAdjacent(start);
        Set<Long> skip = new HashSet<>();
        for (long n : nextLevel) {
            visitor.visit(n);
            if (allPathsShortCircuit.evaluate(n)) {
                return true;
            } else if (singlePathShortCircuit.evaluate(n)) {
                skip.add(n);
                continue;
            }

            propagator.propagate(n, start);
        }

        for (long n : nextLevel) {
            if (skip.contains(n)) {
                continue;
            }

            if (walkInternal(n)) {
                return true;
            }
        }

        return false;
    }
}
