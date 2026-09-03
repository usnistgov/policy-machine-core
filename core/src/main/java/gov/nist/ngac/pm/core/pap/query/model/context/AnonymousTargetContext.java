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

package gov.nist.ngac.pm.core.pap.query.model.context;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.query.NodeLookup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Represents an anonymous target context identified by a set of object attribute IDs or names.
 */
public class AnonymousTargetContext extends TargetContext {

    /**
     * Builds an anonymous target context identified by object attribute ids.
     *
     * @param attributeIds the object attribute ids identifying the context
     * @return the built context
     */
    public static AnonymousTargetContext ofIds(Set<Long> attributeIds) {
        return new AnonymousTargetContext(attributeIds, null);
    }

    /**
     * Builds an anonymous target context identified by object attribute names.
     *
     * @param attributeNames the object attribute names identifying the context
     * @return the built context
     */
    public static AnonymousTargetContext ofNames(Set<String> attributeNames) {
        return new AnonymousTargetContext(null, attributeNames);
    }

    private final Set<Long> attributeIds;
    private final Set<String> attributeNames;

    private AnonymousTargetContext(Set<Long> attributeIds, Set<String> attributeNames) {
        this.attributeIds = attributeIds;
        this.attributeNames = attributeNames;
    }

    public Set<Long> getAttributeIds() {
        return attributeIds;
    }

    public Set<String> getAttributeNames() {
        return attributeNames;
    }

    @Override
    public Collection<Long> resolveNodeIds(NodeLookup nodeLookup) throws PMException {
        if (attributeIds != null) {
            return attributeIds;
        }

        return namesToIds(nodeLookup);
    }

    private Collection<Long> namesToIds(NodeLookup nodeLookup) throws PMException {
        List<Long> ids = new ArrayList<>();

        for (String attributeName : attributeNames) {
            Node node = nodeLookup.getNodeByName(attributeName);
            ids.add(node.getId());
        }

        return ids;
    }
}
