package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.function.basic.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLContextVisitor.toStatementBlockCtx;
import static org.junit.jupiter.api.Assertions.*;

class StatementBlockVisitorTest {

    private static Scope<Variable, PMLFunctionSignature> testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = new CompileScope();
    }

    @Test
    void testFunctionInBlockOk() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                """
                {
                    operation f1() {}
                }
                """
        );
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        assertDoesNotThrow(
                () -> new StatementBlockVisitor(visitorContext, Type.string())
                        .visitStatementBlock(ctx)
        );
    }


}