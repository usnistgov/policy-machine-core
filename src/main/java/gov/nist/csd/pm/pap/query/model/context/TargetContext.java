package gov.nist.csd.pm.pap.query.model.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TargetContext {

	private String target;
	private List<String> attributes;

	public TargetContext(String target) {
		this.target = target;
	}

	public TargetContext(List<String> attributes) {
		this.attributes = attributes;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public boolean isNode() {
		return target != null;
	}

	public List<String> getNodes() {
		if (isNode()) {
			return new ArrayList<>(List.of(target));
		} else {
			return new ArrayList<>(attributes);
		}
	}

	@Override
	public String toString() {
		String s = "%s";
		if (isNode()) {
			return String.format(s, "target=" + target);
		} else {
			return String.format(s, "attributes=" + attributes);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TargetContext that)) return false;
		return Objects.equals(target, that.target) && Objects.equals(attributes, that.attributes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(target, attributes);
	}
}
