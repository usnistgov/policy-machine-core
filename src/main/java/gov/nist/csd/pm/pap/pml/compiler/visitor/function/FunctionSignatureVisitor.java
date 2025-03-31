package gov.nist.csd.pm.pap.pml.compiler.visitor.function;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.BasicFunctionSignatureContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.OperationSignatureContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.RoutineSignatureContext;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutineSignature;
import gov.nist.csd.pm.pap.pml.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.pap.pml.type.Type;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

public class FunctionSignatureVisitor extends PMLBaseVisitor<PMLFunctionSignature> {

    private boolean addToCtx;

    public FunctionSignatureVisitor(VisitorContext visitorCtx, boolean addToCtx) {
        super(visitorCtx);
        this.addToCtx = addToCtx;
    }

    public boolean isAddToCtx() {
        return addToCtx;
    }

    public void setAddToCtx(boolean addToCtx) {
        this.addToCtx = addToCtx;
    }

    @Override
    public PMLOperationSignature visitOperationSignature(OperationSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type returnType = parseReturnType(ctx.returnType);
        List<PMLFormalArg> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());

        writeArgsToScope(visitorCtx, args);

        PMLOperationSignature pmlOperationSignature = new PMLOperationSignature(
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlOperationSignature, addToCtx);

        return pmlOperationSignature;
    }

    @Override
    public PMLRoutineSignature visitRoutineSignature(RoutineSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type returnType = parseReturnType(ctx.returnType);
        List<PMLFormalArg> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());

        writeArgsToScope(visitorCtx, args);

        PMLRoutineSignature pmlRoutineSignature = new PMLRoutineSignature(
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlRoutineSignature, addToCtx);

        return pmlRoutineSignature;
    }

    @Override
    public PMLBasicFunctionSignature visitBasicFunctionSignature(BasicFunctionSignatureContext ctx) {
        String funcName = ctx.ID().getText();
        Type returnType = parseReturnType(ctx.returnType);
        List<PMLFormalArg> args = new FormalArgListVisitor(visitorCtx).visitFormalArgList(ctx.formalArgList());

        writeArgsToScope(visitorCtx, args);

        PMLBasicFunctionSignature pmlFunctionSignature = new PMLBasicFunctionSignature(
            funcName,
            returnType,
            args
        );

        addSignatureToCtx(visitorCtx, ctx, funcName, pmlFunctionSignature, addToCtx);

        return pmlFunctionSignature;
    }

    private <U extends ParserRuleContext> void addSignatureToCtx(VisitorContext visitorCtx,
                                                                       U ctx,
                                                                       String funcName,
                                                                       PMLFunctionSignature signature, boolean addToCtx) {
        if (!addToCtx) {
            return;
        }

        try {
            visitorCtx.scope().addFunction(funcName, signature);
        } catch (FunctionAlreadyDefinedInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }
    }

    private void writeArgsToScope(VisitorContext visitorCtx, List<PMLFormalArg> args) {
        // write args to scope for compiling check block
        VisitorContext copy = visitorCtx.copy();
        for (PMLFormalArg formalArg : args) {
            copy.scope().updateVariable(
                formalArg.getName(),
                new Variable(formalArg.getName(), formalArg.getPmlType(), false)
            );
        }
    }

    private Type parseReturnType(PMLParser.VariableTypeContext variableTypeContext) {
        if (variableTypeContext == null) {
            return Type.voidType();
        }

        return Type.toType(variableTypeContext);
    }
}
