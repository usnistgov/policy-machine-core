package gov.nist.csd.pm.pip.obligations.model.functions;

import java.util.List;

public class Function {
    private String    name;
    private List<Arg> args;

    public Function(String name, List<Arg> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Arg> getArgs() {
        return args;
    }

    public void setArgs(List<Arg> args) {
        this.args = args;
    }
}
