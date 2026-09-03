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

class VertexAttribute extends Vertex {

    private final LongArrayList descendants;
    private final LongArrayList ascendants;
    private final ObjectArrayList<Association> outgoingAssociations;
    private final ObjectArrayList<Association> incomingAssociations;

    public VertexAttribute(long id, String name, NodeType type) {
        super(id, name, type);
        this.descendants = new LongArrayList();
        this.ascendants = new LongArrayList();
        this.outgoingAssociations = new ObjectArrayList<>();
        this.incomingAssociations = new ObjectArrayList<>();
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public Collection<Long> getAdjacentDescendants() {
        return descendants;
    }

    @Override
    public Collection<Long> getAdjacentAscendants() {
        return ascendants;
    }

    @Override
    public Collection<Association> getOutgoingAssociations() {
        return outgoingAssociations;
    }

    @Override
    public Collection<Association> getIncomingAssociations() {
        return incomingAssociations;
    }

    @Override
    protected void addAssignment(long ascendant, long descendant) {
        if (ascendant == id) {
            descendants.add(descendant);
        } else {
            ascendants.add(ascendant);
        }
    }

    @Override
    protected void deleteAssignment(long ascendant, long descendant) {
        if (ascendant == id) {
            descendants.removeLong(descendants.indexOf(descendant));
        } else {
            ascendants.removeLong(ascendants.indexOf(ascendant));
        }
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {
        if (ua == id) {
            outgoingAssociations.add(new Association(ua, target, accessRightSet));
        } else {
            incomingAssociations.add(new Association(ua, target, accessRightSet));
        }
    }

    @Override
    public void deleteAssociation(long ua, long target) {
        if (ua == id) {
            outgoingAssociations.removeIf(a -> a.source() == ua && a.target() == target);
        } else {
            incomingAssociations.removeIf(a -> a.source() == ua && a.target() == target);
        }
    }
}

