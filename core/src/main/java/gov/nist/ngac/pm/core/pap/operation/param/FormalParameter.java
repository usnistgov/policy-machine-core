package gov.nist.ngac.pm.core.pap.operation.param;

import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import java.io.Serializable;
import java.util.Objects;

/**
 * A declared parameter of an {@link gov.nist.ngac.pm.core.pap.operation.Operation}.
 *
 * @param <T> the parameter's Java type
 */
public class FormalParameter<T> implements Serializable {

	private final String name;
	private final Type<T> type;
	private final boolean required;

	public FormalParameter(String name, Type<T> type) {
		this.name = name;
		this.type = type;
		this.required = true;
	}

	public FormalParameter(String name, Type<T> type, boolean required) {
		this.name = name;
		this.type = type;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public Type<T> getType() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}

	/**
	 * Casts an object to this parameter's type.
	 * @param obj the object to convert
	 * @return the converted object
	 * @throws IllegalArgumentException if obj is not of the expected type
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
