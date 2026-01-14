package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.util.Collection;
import java.util.Objects;

public class TargetContext {

	private long targetId;
	private Collection<Long> attributeIds;

	public TargetContext(long targetId) {
		this.targetId = targetId;
	}

	public TargetContext(UserContext targetId) {
		if (targetId.isUserDefined()) {
			this.targetId = targetId.getUser();
		} else {
			this.attributeIds = targetId.getAttributeIds();
		}
	}

	public TargetContext(Collection<Long> attributeIds) {
		this.attributeIds = attributeIds;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public Collection<Long> getAttributeIds() {
		return attributeIds;
	}

	public void setAttributeIds(Collection<Long> attributeIds) {
		this.attributeIds = attributeIds;
	}

	public boolean isNode() {
		return targetId != 0;
	}

	public void checkExists(GraphStore graphStore) throws PMException {
		if (isNode()) {
			if (!graphStore.nodeExists(targetId)) {
				throw new NodeDoesNotExistException(targetId);
			}
		} else {
			for (long attribute : attributeIds) {
				if (!graphStore.nodeExists(attribute)) {
					throw new NodeDoesNotExistException(attribute);
				}
			}
		}
	}

	@Override
	public String toString() {
		String s = "{%s}";
		if (isNode()) {
			return String.format(s, "target: " + targetId);
		} else {
			return String.format(s, "attributes: " + attributeIds);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TargetContext that)) return false;
		return targetId == that.targetId && Objects.deepEquals(attributeIds, that.attributeIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetId, attributeIds);
	}
}
