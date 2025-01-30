package gov.nist.csd.pm.common.event.operand;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;

import java.util.Objects;

public class StringOperandValue extends OperandValue {

	public String value;

	public StringOperandValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof StringOperandValue that)) return false;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return value;
	}

	public static StringOperandValue fromId(PAP pap, Long id) throws PMException {
		Node node = pap.query().graph().getNodeById(id);
		return new StringOperandValue(node.getName());
	}
}
