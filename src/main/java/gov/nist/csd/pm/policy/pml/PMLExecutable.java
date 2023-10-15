package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

public interface PMLExecutable {

    void executePML(UserContext userContext, String input, FunctionDefinitionStatement... functionDefinitionStatements)
    throws PMException;

    void executePMLFunction(UserContext userContext, String functionName, Value ... args) throws PMException;

    static String valuesToArgs(Value[] values) {
        StringBuilder args = new StringBuilder();

        for (Value value : values) {
            if (args.length() > 0) {
                args.append(", ");
            }

            args.append(value.toString());
        }

        return args.toString();
    }
}
