/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.expression.reference;


import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class ReferenceByBracketIndexTest {

    @Test
    void testGetType() throws PMException {
        BracketIndexExpression<?> a = new BracketIndexExpression<>(
            new VariableReferenceExpression<>("a", MapType.of(STRING_TYPE, ListType.of(STRING_TYPE))),
            new StringLiteralExpression("b"),
            ListType.of(STRING_TYPE)
        );
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorContext.scope().addVariable("a", new Variable("a", MapType.of(STRING_TYPE, ListType.of(STRING_TYPE)), false));

        assertEquals(
                ListType.of(STRING_TYPE),
                a.getType()
        );
    }

    @Test
    void testExecute() throws PMException {
        BracketIndexExpression<?> a = new BracketIndexExpression<>(
            new VariableReferenceExpression<>("a", MapType.of(STRING_TYPE, ListType.of(STRING_TYPE))),
            new StringLiteralExpression("b"),
            ListType.of(STRING_TYPE)
        );
        ExecutionContext executionContext = new ExecutionContext(NodeUserContext.of(0), new MemoryPAP());
        List<String> expected = List.of("1","2");
        Map<String, List<String>> mapValue = Map.of("b", expected);
        executionContext.scope().addVariable("a", mapValue);

        PAP pap = new TestPAP();
        Object actual = a.execute(executionContext, pap);
        assertEquals(expected, actual);
    }

    @Test
    void testIndexChain() throws PMException {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }
                    }
                }
                
                create PC a["b"]["c"]["d"]
                """;
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(0), pml);

        assertTrue(pap.query().graph().nodeExists("e"));
    }

    @Test
    void testWrongKeyType() throws PMException {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }
                    }
                }
                
                create PC a[true]["c"]["d"]
                """;
        PAP pap = new TestPAP();
        PMLCompilationException e = assertThrows(PMLCompilationException.class,
                                                 () -> pap.executePML(NodeUserContext.of("u1"), pml));
        assertEquals("expected expression type bool, got string", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testKeyDoesNotExist() throws PMException {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }  
                    }
                }
                
                create PC a["z"]["c"]["d"]
                """;
        PAP pap = new TestPAP();
        assertThrows(IllegalArgumentException.class,
                     () -> pap.executePML(NodeUserContext.of(0), pml));
    }

    @Test
    void testArrayKey() throws PMException {
        String pml = """
                a := {
                    ["a"]: "test"
                }
                
                create PC a[["a"]]
                """;
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(0), pml);

        assertTrue(pap.query().graph().nodeExists("test"));
    }

}