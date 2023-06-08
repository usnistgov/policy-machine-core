package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.builtin.Equals;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.scope.GlobalScope;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLContextVisitor.toStatementBlockCtx;
import static org.junit.jupiter.api.Assertions.*;

class StatementBlockVisitorTest {

    private static GlobalScope<Variable, PMLExecutableSignature> testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = new CompileGlobalScope();
        testGlobalScope.addExecutable("equals", new Equals().getSignature());
    }

    @Test
    void testFunctionInBlock() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                """
                {
                    operation f1() {}
                }
                """
        );
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> new StatementBlockVisitor(visitorContext, Type.string())
                        .visitStatementBlock(ctx)
        );
        assertEquals(1, e.getErrors().size(), visitorContext.errorLog().toString());
        assertEquals("operations are not allowed inside statement blocks",
                     e.getErrors().get(0).errorMessage());
    }


}