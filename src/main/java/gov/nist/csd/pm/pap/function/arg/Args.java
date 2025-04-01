package gov.nist.csd.pm.pap.function.arg;

import gov.nist.csd.pm.pap.function.AdminFunction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class Args {

	public static Args of(AdminFunction<?> adminFunction, Map<String, Object> actualArgs) {
		List<FormalArg<?>> formalArgs = adminFunction.getFormalArgs();

		Args args = new Args();

		if (formalArgs.size() != actualArgs.size()) {
			throw new IllegalArgumentException("expected the same number of formalArgs and actualArgs and got " +
				formalArgs.size() + " and " + actualArgs.size());
		}

		for (FormalArg<?> formalArg : formalArgs) {
			if (!actualArgs.containsKey(formalArg.getName())) {
				throw new IllegalArgumentException("formal argument " + formalArg.getName() + " not found in actual args");
			}

			Object actualArg = actualArgs.get(formalArg.getName());
			put(formalArg, actualArg, args);
		}

		return args;
	}

	private static <T> void put(FormalArg<T> formalArg, Object value, Args args) {
		T typedValue = formalArg.toExpectedType(value);
		args.put(formalArg, typedValue);
	}

	private final Map<FormalArg<?>, Object> map;

	public Args() {
		this.map = new HashMap<>();
	}

	public Args(Map<FormalArg<?>, Object> map) {
		this.map = map;
	}

	public <T> T get(FormalArg<T> formalArg) {
		return formalArg.getType().cast(map.get(formalArg));
	}

	public <T> Args put(FormalArg<T> formalArg, T value) {
		map.put(formalArg, value);
		return this;
	}

	public void foreach(BiConsumer<FormalArg<?>, Object> consumer) {
		for (Entry<FormalArg<?>, Object> e : map.entrySet()) {
			consumer.accept(e.getKey(), e.getValue());
		}
	}
}
