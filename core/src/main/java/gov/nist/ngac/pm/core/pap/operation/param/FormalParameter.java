/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
