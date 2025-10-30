package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AdminFunction<R> implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final String name;
    protected final List<FormalParameter<?>> parameters;

    public AdminFunction(String name, List<FormalParameter<?>> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public abstract R execute(PAP pap, Args args) throws PMException;

    public Args validateAndPrepareArgs(Map<String, Object> argsMap) {
        Set<String> expectedKeys = parameters.stream()
                                             .map(FormalParameter::getName)
                                             .collect(Collectors.toSet());
        Set<String> actualKeys = new HashSet<>(argsMap.keySet());

        if (actualKeys.size() != expectedKeys.size()) {
            throw new IllegalArgumentException(
                String.format("Argument mismatch for function '%s': Expected %d arguments (%s), but got %d (%s)",
                              name, expectedKeys.size(), expectedKeys, actualKeys.size(), actualKeys));
        }

        actualKeys.removeAll(expectedKeys);
        if (!actualKeys.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("Unexpected arguments provided for function '%s': %s", name, actualKeys));
        }

        Map<FormalParameter<?>, Object> argsWithFormalParams = new HashMap<>();
        for (var e : argsMap.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();

            FormalParameter<?> param = getFormalParamByName(key);
            if (param == null) {
                continue;
            }

            argsWithFormalParams.put(param, value);
        }

        return new Args(argsWithFormalParams);
    }

    public String getName() {
        return name;
    }

    public List<FormalParameter<?>> getFormalParameters() {
        return parameters;
    }

    public <T> T prepareArg(FormalParameter<T> formalParameter, Map<FormalParameter<?>, Object> argsMap) {
        return formalParameter.toExpectedType(argsMap.get(formalParameter));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminFunction<?> that)) return false;
	    return Objects.equals(name, that.name) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameters);
    }

    private FormalParameter<?> getFormalParamByName(String key) {
        for (FormalParameter<?> p : parameters) {
            if (p.getName().equals(key)) {
                return p;
            }
        }

        return null;
    }
}
