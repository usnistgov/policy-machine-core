package gov.nist.csd.pm.pap.executable.arg;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ActualArgs {

	private Map<FormalArg<?>, Object> map;

	public ActualArgs() {
		map = new HashMap<>();
	}

	public ActualArgs(Map<String, Object> map) {
		if (map == null) {
			map = new HashMap<>();
		}

		this.map = new HashMap<>();
		map.forEach((k, v) -> this.map.put(new FormalArg<>(k, v.getClass()), v));
	}

	public <T> ActualArgs put(FormalArg<T> arg, T value) {
		map.put(arg, value);
		return this;
	}

	public <T> ActualArgs put(String name, Class<T> clazz, T value) {
		map.put(new FormalArg<>(name, clazz), value);
		return this;
	}

	public <T> T get(FormalArg<T> arg) {
		Object o = map.get(arg);
		return arg.getType().cast(o);
	}

	public void foreach(Consumer<FormalArg<?>> consumer) {
		for (FormalArg<?> arg : map.keySet()) {
			consumer.accept(arg);
		}
	}
}
