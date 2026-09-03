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
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * A single node on an explain path, paired with the associations reached at that node.
 *
 * @param node the node on the path
 * @param associations the associations reached at that node
 */
public record ExplainNode(Node node, Collection<ExplainAssociation> associations) {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ExplainNode that)) return false;
		return Objects.equals(node, that.node) && Objects.equals(new HashSet<>(associations), new HashSet<>(that.associations));
	}

	@Override
	public int hashCode() {
		return Objects.hash(node, new HashSet<>(associations));
	}

	@Override
	public String toString() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}
