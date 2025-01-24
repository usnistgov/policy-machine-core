package gov.nist.csd.pm.pap.query.model.context;

import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.store.GraphStore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TargetContext {

	private long targetId;
	private long[] attributeIds;

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

	public TargetContext(long[] attributeIds) {
		this.attributeIds = attributeIds;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public long[] getAttributeIds() {
		return attributeIds;
	}

	public void setAttributeIds(long[] attributeIds) {
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
		String s = "%s";
		if (isNode()) {
			return String.format(s, "target=" + targetId);
		} else {
			return String.format(s, "attributes=" + Arrays.toString(attributeIds));
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
		return Objects.hash(targetId, Arrays.hashCode(attributeIds));
	}
}
