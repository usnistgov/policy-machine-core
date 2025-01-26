package gov.nist.csd.pm.common.prohibition;

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
		if (process == null) {
			throw new IllegalStateException("process not set");
		}

		return process;
	}

	public boolean isNode() {
		return nodeId != 0;
	}
}
