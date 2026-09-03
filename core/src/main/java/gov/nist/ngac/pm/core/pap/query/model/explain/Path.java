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

package gov.nist.ngac.pm.core.pap.query.model.explain;

import com.google.gson.GsonBuilder;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An ordered path of nodes from a target (or user) up through the graph, used to explain an access
 * decision.
 */
public class Path extends ArrayList<Node> {

    public Path(int initialCapacity) {
        super(initialCapacity);
    }

    public Path() {
    }

    public Path(Node... p) {
        this(new ArrayList<>(List.of(p)));
    }

    public Path(Collection<Node> c) {
        super(c);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
