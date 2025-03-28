package gov.nist.csd.pm.pap.function.arg;

import java.io.Serializable;
import java.util.Objects;

public class FormalArg<T> implements Serializable {

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FormalArg<?> formalArg))
			return false;
        return Objects.equals(name, formalArg.name) && Objects.equals(type, formalArg.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type);
	}
}
