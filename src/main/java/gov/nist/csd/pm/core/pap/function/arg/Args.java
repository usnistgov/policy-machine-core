package gov.nist.csd.pm.core.pap.function.arg;

import gov.nist.csd.pm.core.pap.function.AdminFunction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class Args {

	public static Args of(AdminFunction<?> adminFunction, Map<String, Object> actualArgs) {
		List<FormalParameter<?>> formalParameters = adminFunction.getFormalParameters();

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

	public <T> T get(FormalParameter<T> formalParameter) {
		return formalParameter.toExpectedType(map.get(formalParameter));
	}

	public Args putUnchecked(FormalParameter<?> formalParameter, Object value) {
		map.put(formalParameter, value);
		return this;
	}

	public <T> Args put(FormalParameter<T> formalParameter, T value) {
		map.put(formalParameter, value);
		return this;
	}

	public void foreach(BiConsumer<FormalParameter<?>, Object> consumer) {
		for (Entry<FormalParameter<?>, Object> e : map.entrySet()) {
			consumer.accept(e.getKey(), e.getValue());
		}
	}

	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<>();
		for (Entry<FormalParameter<?>, Object> e : map.entrySet()) {
			m.put(e.getKey().getName(), e.getValue());
		}

		return m;
	}
}
