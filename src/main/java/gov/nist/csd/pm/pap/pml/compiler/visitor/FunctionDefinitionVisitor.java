package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperationBody;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineSignature;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateFunctionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateOperationStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRoutineStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.*;

public class FunctionDefinitionVisitor extends PMLBaseVisitor<CreateFunctionStatement> {

    public FunctionDefinitionVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateFunctionStatement visitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx) {
        PMLParser.FunctionSignatureContext functionSignatureContext = ctx.functionSignature();
        boolean isOp = functionSignatureContext.OPERATION() != null;

        PMLExecutableSignature signature = new FunctionSignatureVisitor(visitorCtx, isOp).visitFunctionSignature(functionSignatureContext);

        PMLStmtsOperationBody body = parseBody(isOp, ctx, signature.getOperands(), signature.getOperandTypes(), signature.getReturnType());

        // check if the function is an operation
        if (signature instanceof PMLOperationSignature pmlOperationSignature) {
            return new CreateOperationStatement(new PMLStmtsOperation(
                    pmlOperationSignature.getFunctionName(),
                    pmlOperationSignature.getReturnType(),
                    pmlOperationSignature.getOperands(),
                    pmlOperationSignature.getNodeOperands(),
                    pmlOperationSignature.getOperandTypes(),
                    body
            ));
        } else {
            return new CreateRoutineStatement(new PMLStmtsRoutine(
                    signature.getFunctionName(),
                    signature.getReturnType(),
                    signature.getOperands(),
                    signature.getOperandTypes(),
                    body.getStatements() // ignore body check stmts for routines
            ));
        }
    }

    private PMLStmtsOperationBody parseBody(boolean isOp, PMLParser.FunctionDefinitionStatementContext ctx,
                                            List<String> operandNames,
                                            Map<String, Type> operandTypes,
                                            Type returnType) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = visitorCtx.copy();

        // add the args to the local scope, overwriting any variables with the same ID as the formal args
        for (int i = 0; i < operandNames.size(); i++) {
            String name = operandNames.get(i);
            Type type = operandTypes.get(name);

            localVisitorCtx.scope().addOrOverwriteVariable(
                    name,
                    new Variable(name, type, false)
            );
        }

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(ctx.statementBlock());

        if (!result.allPathsReturned() && !returnType.isVoid()) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        // get checks
        PMLStatementBlock checks = new PMLStatementBlock();
        if (isOp) {
            CheckStatementBlockVisitor checkStatementBlockVisitor = new CheckStatementBlockVisitor(localVisitorCtx);
            checks = checkStatementBlockVisitor.visitCheckStatementBlock(ctx.checkStatementBlock());
        }

        return new PMLStmtsOperationBody(checks, result.stmts());
    }

    public static class FunctionSignatureVisitor extends PMLBaseVisitor<PMLStatementSerializable> {

        private boolean isOp;

        public FunctionSignatureVisitor(VisitorContext visitorCtx, boolean isOp) {
            super(visitorCtx);

            this.isOp = isOp;
        }

        @Override
        public PMLExecutableSignature visitFunctionSignature(PMLParser.FunctionSignatureContext ctx) {
            String funcName = ctx.ID().getText();
            List<FormalOperand> args = parseFormalArgs(ctx.formalArgList());
            List<String> operandNames = new ArrayList<>();
            List<String> nodeops = new ArrayList<>();
            Map<String, Type> operandTypes = new HashMap<>();

            for (FormalOperand operand : args) {
                operandNames.add(operand.name);
                operandTypes.put(operand.name, operand.type);

                if (operand.isNodeop) {
                    nodeops.add(operand.name);
                }
            }

            // write operands to scope for compiling check block
            VisitorContext copy = visitorCtx.copy();
            for (int i = 0; i < operandNames.size(); i++) {
                String name = operandNames.get(i);
                Type type = operandTypes.get(name);

                copy.scope().addOrOverwriteVariable(
                        name,
                        new Variable(name, type, false)
                );
            }

            Type returnType = parseReturnType(ctx.returnType);

            if (isOp) {
                return new PMLOperationSignature(
                        funcName,
                        returnType,
                        operandNames,
                        nodeops,
                        operandTypes
                );
            } else {
                return new PMLRoutineSignature(funcName, returnType, operandNames, operandTypes);
            }
        }

        private record FormalOperand(String name, Type type, boolean isNodeop) {}

        private List<FormalOperand> parseFormalArgs(PMLParser.FormalArgListContext formalArgListCtx) {
            List<FormalOperand> formalArgs = new ArrayList<>();
            Set<String> argNames = new HashSet<>();
            for (int i = 0; i < formalArgListCtx.formalArg().size(); i++) {
                PMLParser.FormalArgContext formalArgCtx = formalArgListCtx.formalArg().get(i);
                String name = formalArgCtx.ID().getText();
                boolean isNodeop = formalArgCtx.NODEOP() != null;

                // check that two formal args dont have the same name and that there are no constants with the same name
                if (argNames.contains(name)) {
                    throw new PMLCompilationRuntimeException(
                            formalArgCtx,
                            String.format("formal arg '%s' already defined in signature", name)
                    );
                }

                // get arg type
                PMLParser.VariableTypeContext varTypeContext = formalArgCtx.variableType();
                Type type = Type.toType(varTypeContext);

                // req cap if operation
                formalArgs.add(new FormalOperand(name, type, isNodeop));

                argNames.add(name);
            }

            return formalArgs;
        }

        private Type parseReturnType(PMLParser.VariableTypeContext variableTypeContext) {
            if (variableTypeContext == null) {
                return Type.voidType();
            }

            return Type.toType(variableTypeContext);
        }
    }
}
