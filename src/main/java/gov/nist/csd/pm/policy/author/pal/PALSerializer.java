package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.Map;

import static gov.nist.csd.pm.pap.policies.SuperPolicy.SUPER_USER;

class PALSerializer {

    public static void main(String[] args) throws PMException {
        MemoryPAP pap = new MemoryPAP();

        String pal = """
                const test = 'test';
                
                function testFunc() void {
                    let x = 'hello world';
                    create policy class x;
                    
                    foreach x in ['a', 'b'] {
                        create policy class x;
                    }
                    
                    create obligation 'o1' {
                        create rule 'rule1'
                        when any user
                        performs 'event'
                        on any policy element
                        do(evtCtx) {
                            foreach x in ['c', 'd'] {
                                create policy class x;
                            }

                            create policy class test;
                        }
                    }
                }
                """;

        pap.compileAndExecutePAL(new UserContext(SUPER_USER), pal);

        pal = pap.toPAL();
        System.out.println(pal);
    }

    private static final String TAB_SPACES = "    ";
    private static final String SEMI_COLON = ";";

    private final PolicyAuthor policy;

    PALSerializer(PolicyAuthor policy) {
        this.policy = policy;
    }

    String toPAL() throws PMException {
        String pal = "%s\n%s\n%s\n%s\n%s";

        String constants = serializeConstants();
        String functions = serializeFunctions();

        // functions and constants
        // graph
        //   resource access rights
        //   bfs from each policy class node
        //    do uas first then oas
        //   for each ua do assocaitions after
        //   try grouping associations by policy class
        // prohibitions
        // obligations


        return String.format(pal, constants, functions, "", "", "");
    }

    private String serializeFunctions() throws PMException {
        StringBuilder pal = new StringBuilder();
        Map<String, FunctionDefinitionStatement> functions = policy.pal().getFunctions();
        for (String funcName : functions.keySet()) {
            pal.append("\n").append(functions.get(funcName).toString()).append("\n");
        }

        return pal.toString();
    }

    private String serializeConstants() throws PMException {
        String pal = "";
        Map<String, Value> constants = policy.pal().getConstants();
        for (String c : constants.keySet()) {
            Value v = constants.get(c);
            pal += serializeConstant(c, v) + SEMI_COLON;
        }
        return pal;
    }

    private String serializeConstant(String name, Value value) {
        return String.format("const %s = %s", name, value.toString());
    }

    private String indentLine(String line, int indentNum) {
        return TAB_SPACES.repeat(Math.max(0, indentNum)) + line;
    }

}
