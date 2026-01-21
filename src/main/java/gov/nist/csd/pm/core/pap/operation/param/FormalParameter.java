package gov.nist.csd.pm.core.pap.operation.param;

import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import java.io.Serializable;
import java.util.Objects;

public class FormalParameter<T> implements Serializable {

	private final String name;
	private final Type<T> type;

	public FormalParameter(String name, Type<T> type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Type<T> getType() {
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
		if (!(o instanceof FormalParameter<?> formalParameter))
			return false;
		return Objects.equals(name, formalParameter.name) && Objects.equals(type, formalParameter.type);
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
