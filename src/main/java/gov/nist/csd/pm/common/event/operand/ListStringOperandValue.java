package gov.nist.csd.pm.common.event.operand;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;

import java.util.*;

public class ListStringOperandValue extends OperandValue {

	private List<StringOperandValue> values;

	private ListStringOperandValue() {
		this.values = new ArrayList<>();
	}

	public ListStringOperandValue(StringOperandValue ... values) {
		this.values = new ArrayList<>(Arrays.asList(values));
	}

	public ListStringOperandValue(Collection<StringOperandValue> values) {
		this.values = new ArrayList<>(values);
	}

	public ListStringOperandValue(AccessRightSet accessRights) {
		this.values = new ArrayList<>();
		accessRights.forEach(r -> this.values.add(new StringOperandValue(r)));
	}

	public List<StringOperandValue> getValues() {
		return values;
	}

	public void setValues(List<StringOperandValue> values) {
		this.values = values;
	}

	public static ListStringOperandValue fromIds(PAP pap, Collection<Long> ids) throws PMException {
		ListStringOperandValue val = new ListStringOperandValue();
		for (Long id : ids) {
			Node node = pap.query().graph().getNodeById(id);
			val.values.add(new StringOperandValue(node.getName()));
		}

		return val;
	}
}
