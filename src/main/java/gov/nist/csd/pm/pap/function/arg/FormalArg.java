package gov.nist.csd.pm.pap.function.arg;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import java.io.Serializable;
import java.util.Objects;

public class FormalArg<T> implements Serializable {

	private final String name;
	private final ArgType<T> type;

	public FormalArg(String name, ArgType<T> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public ArgType<T> getType() {
		return type;
	}

	/**
	 * Converts an object to the expected type with runtime type safety.
	 * @param obj The object to convert
	 * @return The converted object of type T
	 * @throws IllegalArgumentException if the conversion is not possible
	 */
	public T toExpectedType(Object obj) {
		return type.cast(obj);
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

	@Override
	public String toString() {
		return name + ": " + type;
	}
}
