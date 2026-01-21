package gov.nist.csd.pm.core.pap.query.model.explain;

import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public record ExplainAssociation(Node ua, AccessRightSet arset, Collection<Path> userPaths) {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ExplainAssociation that)) return false;
		return Objects.equals(ua, that.ua) && Objects.equals(arset, that.arset) &&
				Objects.equals(new HashSet<>(userPaths), new HashSet<>(that.userPaths));
	}

	@Override
	public int hashCode() {
		return Objects.hash(ua, arset, new HashSet<>(userPaths));
	}

	@Override
	public String toString() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}
