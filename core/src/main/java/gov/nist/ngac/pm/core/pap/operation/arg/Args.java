package gov.nist.ngac.pm.core.pap.operation.arg;

import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

/**
 * A map of an operation invocation's argument values, keyed by {@link FormalParameter} instead of name.
 */
public class Args {

	/**
	 * Builds an {@link Args} by matching the operation's formal parameters to actual values by name.
	 *
	 * @param function the operation whose formal parameters to match against
	 * @param actualArgs the actual argument values, keyed by parameter name
	 * @return the built args
	 * @throws IllegalArgumentException if the argument counts don't match, a formal parameter has no
	 * matching actual argument, or a value can't be cast to its formal parameter's type
	 */
	public static Args of(Operation<?> function, Map<String, Object> actualArgs) {
		List<FormalParameter<?>> formalParameters = function.getFormalParameters();

		Args args = new Args();

		if (formalParameters.size() != actualArgs.size()) {
			throw new IllegalArgumentException("expected the same number of formalArgs and actualArgs and got " +
				formalParameters.size() + " and " + actualArgs.size());
		}

		for (FormalParameter<?> formalParameter : formalParameters) {
			if (!actualArgs.containsKey(formalParameter.getName())) {
				throw new IllegalArgumentException("formal argument " + formalParameter.getName() + " not found in actual args");
			}

			Object actualArg = actualArgs.get(formalParameter.getName());
			put(formalParameter, actualArg, args);
		}

		return args;
	}

	private static <T> void put(FormalParameter<T> formalParameter, Object value, Args args) {
		T typedValue = formalParameter.toExpectedType(value);
		args.put(formalParameter, typedValue);
	}

	private final Map<FormalParameter<?>, Object> map;

	public Args() {
		this.map = new HashMap<>();
	}

	public Args(Map<FormalParameter<?>, Object> map) {
		this.map = map;
	}

	/**
	 * Returns the value for the given formal parameter, cast to its expected type.
	 *
	 * @param <T> the parameter's Java type
	 * @param formalParameter the parameter to look up
	 * @return the parameter's value
	 */
	public <T> T get(FormalParameter<T> formalParameter) {
		return formalParameter.toExpectedType(map.get(formalParameter));
	}

	/**
	 * Sets a value for the given formal parameter without type-checking it against the parameter's
	 * declared type.
	 *
	 * @param formalParameter the parameter to set
	 * @param value the value to set, unchecked against the parameter's declared type
	 * @return this instance, for chaining
	 */
	public Args putUnchecked(FormalParameter<?> formalParameter, Object value) {
		map.put(formalParameter, value);
		return this;
	}

	/**
	 * Sets a value for the given formal parameter.
	 *
	 * @param <T> the parameter's Java type
	 * @param formalParameter the parameter to set
	 * @param value the value to set
	 * @return this instance, for chaining
	 */
	public <T> Args put(FormalParameter<T> formalParameter, T value) {
		map.put(formalParameter, value);
		return this;
	}

	/**
	 * Invokes the given consumer with each formal parameter/value pair.
	 *
	 * @param consumer the callback to invoke per entry
	 */
	public void foreach(BiConsumer<FormalParameter<?>, Object> consumer) {
		for (Entry<FormalParameter<?>, Object> e : map.entrySet()) {
			consumer.accept(e.getKey(), e.getValue());
		}
	}

	public Map<FormalParameter<?>, Object> getMap() {
		return map;
	}

	/**
	 * Returns a copy of this args as a plain map, keyed by parameter name instead of formal parameter.
	 *
	 * @return the copied map
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<>();
		for (Entry<FormalParameter<?>, Object> e : map.entrySet()) {
			m.put(e.getKey().getName(), e.getValue());
		}

		return m;
	}
}
