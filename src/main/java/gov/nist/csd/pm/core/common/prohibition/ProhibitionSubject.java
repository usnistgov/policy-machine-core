package gov.nist.csd.pm.core.common.prohibition;

import java.util.Objects;

public class ProhibitionSubject {

	private long nodeId;
	private String process;

	public ProhibitionSubject(long nodeId) {
		this.nodeId = nodeId;
	}

	public ProhibitionSubject(String process) {
		this.process = process;
	}

	public long getNodeId() {
		if (nodeId == 0) {
			throw new IllegalStateException("nodeId not set");
		}

		return nodeId;
	}

	public String getProcess() {
		return process;
	}

	public boolean isNode() {
		return nodeId != 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProhibitionSubject that)) return false;
		return nodeId == that.nodeId && Objects.equals(process, that.process);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nodeId, process);
	}

	@Override
	public String toString() {
		return "ProhibitionSubject{" +
				"nodeId=" + nodeId +
				", process='" + process + '\'' +
				'}';
	}
}
