package gov.nist.csd.pm.pap.executable.arg;

public class FormalArg<T> {

	private final String name;
	private final Class<T> type;

	public FormalArg(String name, Class<T> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Class<T> getType() {
		return type;
	}
}
