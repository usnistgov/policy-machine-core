package gov.nist.csd.pm.pap.function.arg;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class Args {

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
