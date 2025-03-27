package gov.nist.csd.pm.pap.executable;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AdminExecutable<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final String name;
    protected final List<FormalArg<?>> formalArgs;

    public AdminExecutable(String name, List<FormalArg<?>> formalArgs) {
        this.name = name;
        this.formalArgs = formalArgs;
    }

    public abstract T execute(PAP pap, ActualArgs actualArgs) throws PMException;

    public String getName() {
        return name;
    }

    public List<FormalArg<?>> getFormalArgs() {
        return formalArgs;
    }

    public List<String> getFormalArgNames() {
        return formalArgs.stream()
                .map(FormalArg::getName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminExecutable<?> that)) return false;
	    return Objects.equals(name, that.name) && Objects.equals(formalArgs, that.formalArgs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, formalArgs);
    }
}
