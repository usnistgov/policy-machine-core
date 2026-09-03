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

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation;

import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.StatementBlockVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import java.util.List;

/**
 * Compiles the body of an operation, function, or obligation response into a {@link PMLStatementBlock}.
 */
public class StatementBlockParser {

    public static PMLStatementBlock parseBasicStatementBlock(VisitorContext visitorCtx,
                                                             PMLParser.BasicStatementBlockContext ctx,
                                                             Type<?> returnType,
                                                             List<FormalParameter<?>> formalArgs,
                                                             boolean allowQueryOps) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = initLocalVisitorCtx(visitorCtx, formalArgs);

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType, allowQueryOps);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitBasicStatementBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return result.stmts();
    }

    public static PMLStatementBlock parseOnStatementBlock(VisitorContext visitorCtx,
                                                             PMLParser.OnPatternBlockContext ctx,
                                                             Type<?> returnType,
                                                             List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = initLocalVisitorCtx(visitorCtx, formalArgs);

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType, true);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitOnPatternBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return result.stmts();
    }

    public static PMLStatementBlock parseStatementBlock(VisitorContext visitorCtx,
                                                        PMLParser.StatementBlockContext statementBlockCtx,
                                                        Type<?> returnType,
                                                        List<FormalParameter<?>> formalArgs) {
        VisitorContext localVisitorCtx = initLocalVisitorCtx(visitorCtx, formalArgs);
        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType, true);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(statementBlockCtx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(statementBlockCtx, "not all conditional paths return");
        }

        return result.stmts();
    }

    private static VisitorContext initLocalVisitorCtx(VisitorContext visitorCtx, List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = visitorCtx.copy();

        // add the args to the local scope, overwriting any variables with the same ID as the formal args
        for (FormalParameter<?> formalArg : formalArgs) {
            localVisitorCtx.scope().updateVariable(
                formalArg.getName(),
                new Variable(formalArg.getName(), formalArg.getType(), false)
            );
        }

        return localVisitorCtx;
    }

}
