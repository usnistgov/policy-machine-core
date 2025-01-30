package gov.nist.csd.pm.pap.query.model.explain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.common.graph.node.Node;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
