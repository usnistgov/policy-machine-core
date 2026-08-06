package gov.nist.ngac.pm.core.pap.query.model.explain;

import com.google.gson.GsonBuilder;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * A single association reached on an explain path: the user attribute source, the access rights it
 * grants, and the paths from the user to that user attribute.
 *
 * @param ua the association's user attribute source
 * @param arset the access rights the association grants
 * @param userPaths the paths from the user to the user attribute
 */
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
