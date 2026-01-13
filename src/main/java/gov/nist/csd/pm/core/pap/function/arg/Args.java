package gov.nist.csd.pm.core.pap.function.arg;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.Function;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeListFormalParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class Args {

	public static Args of(Function<?> function, Map<String, Object> actualArgs) {
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

	public <T> T get(FormalParameter<T> formalParameter) {
		return formalParameter.toExpectedType(map.get(formalParameter));
	}

	public List<Long> getIdList(NodeListFormalParameter formalParameter, PAP pap) throws PMException {
		List<NodeArg<?>> nodeArgList = formalParameter.toExpectedType(map.get(formalParameter));
		List<Long> ids = new ArrayList<>();
		for (NodeArg<?> nodeArg : nodeArgList) {
			ids.add(nodeArg.getId(pap));
		}

		return ids;
	}

	public List<String> getNameList(NodeListFormalParameter formalParameter, PAP pap) throws PMException {
		List<NodeArg<?>> nodeArgList = formalParameter.toExpectedType(map.get(formalParameter));
		List<String> names = new ArrayList<>();
		for (NodeArg<?> nodeArg : nodeArgList) {
			names.add(nodeArg.getName(pap));
		}

		return names;
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

	public Map<FormalParameter<?>, Object> getMap() {
		return map;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<>();
		for (Entry<FormalParameter<?>, Object> e : map.entrySet()) {
			m.put(e.getKey().getName(), e.getValue());
		}

		return m;
	}
}
