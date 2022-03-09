package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.function.*;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

import java.util.Arrays;
import java.util.List;

public class PALBuiltinFunctions {

    public static List<FunctionDefinitionStatement> getBuiltinFunctions() {
        return Arrays.asList(
                // util functions
                new Concat(),
                new Equals(),
                new Contains(),
                new ContainsKey(),

                // policy functions
                new GetAssociationsWithSource(),
                new GetAssociationsWithTarget(),
                new GetChildren(),
                new GetParents(),
                new GetNodeProperties(),
                new GetNodeType(),
                new GetProhibitionsFor(),
                new HasPropertyKey(),
                new HasPropertyValue(),
                new NodeExists(),
                new GetNode(),
                new Search()
        );
    }

}
