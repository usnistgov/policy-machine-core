package gov.nist.csd.pm.pap.function.arg;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ActualArgs {

	private Map<FormalArg<?>, Object> map;
	private Map<String, FormalArg<?>> argIndex;

	public ActualArgs() {
		map = new HashMap<>();
		argIndex = new HashMap<>();
	}

	public ActualArgs(Map<String, Object> map) {
		if (map == null) {
			map = new HashMap<>();
		}

		this.map = new HashMap<>();
		this.argIndex = new HashMap<>();

		map.forEach((k, v) -> {
			FormalArg<?> formalArg = new FormalArg<>(k, v.getClass());
			this.map.put(formalArg, v);
			this.argIndex.put(k, formalArg);
		});

	}

	public <T> ActualArgs put(FormalArg<T> arg, T value) {
		map.put(arg, value);
		argIndex.put(arg.getName(), arg);
		return this;
	}

	public <T> T get(FormalArg<T> arg) {
		Object o = map.get(arg);
		return arg.getType().cast(o);
	}

	public Object get(String arg) {
		FormalArg<?> formalArg = argIndex.get(arg);
		return map.get(formalArg);
	}

	public void foreach(Consumer<FormalArg<?>> consumer) {
		for (FormalArg<?> arg : map.keySet()) {
			consumer.accept(arg);
		}
	}
}
