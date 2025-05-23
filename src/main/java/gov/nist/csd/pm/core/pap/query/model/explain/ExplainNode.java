package gov.nist.csd.pm.core.pap.query.model.explain;

import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.core.common.graph.node.Node;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
