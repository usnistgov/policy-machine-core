package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.TestPMLParser.toStatementBlockCtx;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StatementBlockVisitorTest {

    private static CompileScope testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = new CompileScope(new MemoryPAP());
    }

    @Test
    void testMultipleStatementErrors() throws PMException {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
            """
            {
                badOp1()
                badOp2()
            }
            """
        );
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        PMLCompilationRuntimeException e = assertThrows(
            PMLCompilationRuntimeException.class,
            () -> new StatementBlockVisitor(visitorContext, STRING_TYPE, true).visitStatementBlock(ctx)
        );
        assertEquals(2, e.getErrors().size());
    }

    @Test
    void testOperationInBlockOk() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                """
                {
                    adminop f1() {}
                }
                """
        );
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        assertDoesNotThrow(
                () -> new StatementBlockVisitor(visitorContext, STRING_TYPE, true)
                        .visitStatementBlock(ctx)
        );
    }


}