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

package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class VertexPolicyClass extends Vertex {
    private final LongArrayList ascendants;

    public VertexPolicyClass(long id, String name) {
        super(id, name, NodeType.PC);
        this.ascendants = new LongArrayList();
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    protected Collection<Long> getAdjacentDescendants() {
        return new LongArrayList();
    }

    @Override
    protected Collection<Long> getAdjacentAscendants() {
        return ascendants;
    }

    @Override
    protected Collection<Association> getOutgoingAssociations() {
        return new ObjectArrayList<>();
    }

    @Override
    protected Collection<Association> getIncomingAssociations() {
        return new ObjectArrayList<>();
    }

    @Override
    public void addAssignment(long ascendant, long descendant) {
        ascendants.add(ascendant);
    }

    @Override
    public void deleteAssignment(long ascendant, long descendant) {
        ascendants.removeLong(ascendants.indexOf(ascendant));
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(long ua, long target) {

    }
}
