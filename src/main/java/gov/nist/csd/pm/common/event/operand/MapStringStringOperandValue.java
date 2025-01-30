package gov.nist.csd.pm.common.event.operand;

import java.util.Map;

public class MapStringStringOperandValue extends OperandValue{

	private Map<String, String> map;

	public MapStringStringOperandValue(Map<String, String> map) {
		this.map = map;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public static MapStringStringOperandValue create(Map<String, String> map) {
		return new MapStringStringOperandValue(map);
	}
}
