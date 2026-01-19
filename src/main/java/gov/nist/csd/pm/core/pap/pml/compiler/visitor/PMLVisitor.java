package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.function.FunctionDefinitionVisitor;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.function.FunctionSignatureVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLRoutineSignature;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.BasicFunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.antlr.v4.runtime.ParserRuleContext;

public class PMLVisitor extends PMLBaseVisitor<List<PMLStatement<?>>> {

    public PMLVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<PMLStatement<?>> visitPml(PMLParser.PmlContext ctx) {
        SortedStatements sortedStatements = sortStatements(ctx);

        CompiledFunctions functions = compileFunctions(
                sortedStatements.operationCtxs,
                sortedStatements.routineCtxs,
                sortedStatements.functionCtxs
        );

        List<PMLStatement<?>> stmts = new ArrayList<>();
        stmts.addAll(functions.operations);
        stmts.addAll(functions.routines);
        stmts.addAll(functions.functions);
        stmts.addAll(compileStatements(sortedStatements.statementCtxs));

        return stmts;
    }

    private SortedStatements sortStatements(PMLParser.PmlContext ctx) {
        List<PMLParser.AdminOpDefinitionStatementContext> adminOpCtxs = new ArrayList<>();
        List<PMLParser.RoutineDefinitionStatementContext> routineCtxs = new ArrayList<>();
        List<PMLParser.BasicFunctionDefinitionStatementContext> functionCtxs = new ArrayList<>();
        List<PMLParser.StatementContext> statementCtxs = new ArrayList<>();

        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
            PMLParser.BasicStatementContext basicStatementContext = stmtCtx.basicStatement();
            PMLParser.OperationStatementContext operationStatementContext = stmtCtx.operationStatement();

            if (basicStatementContext != null) {
                if (basicStatementContext.basicFunctionDefinitionStatement() != null) {
                    functionCtxs.add(basicStatementContext.basicFunctionDefinitionStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            } else if (operationStatementContext != null) {
                if (operationStatementContext.adminOpDefinitionStatement() != null) {
                    adminOpCtxs.add(operationStatementContext.adminOpDefinitionStatement());
                } else if (operationStatementContext.routineDefinitionStatement() != null) {
                    routineCtxs.add(operationStatementContext.routineDefinitionStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            }
        }

        return new SortedStatements(adminOpCtxs, routineCtxs, functionCtxs, statementCtxs);
    }

    private record SortedStatements(List<PMLParser.AdminOpDefinitionStatementContext> operationCtxs,
                                    List<PMLParser.RoutineDefinitionStatementContext> routineCtxs,
                                    List<PMLParser.BasicFunctionDefinitionStatementContext> functionCtxs,
                                    List<PMLParser.StatementContext> statementCtxs) {}

    private CompiledFunctions compileFunctions(List<PMLParser.AdminOpDefinitionStatementContext> operationCtxs,
                                               List<PMLParser.RoutineDefinitionStatementContext> routineCtxs,
                                               List<PMLParser.BasicFunctionDefinitionStatementContext> functionCtxs) {
        Map<String, PMLFunctionSignature> functionSignatures = new HashMap<>(visitorCtx.scope().getFunctions());

        // track the function definitions statements to be processed,
        // function signatures are compiled first in the event that one function calls another
        // any function with an error won't be processed but execution will continue inorder to find anymore errors
        Map<String, PMLParser.AdminOpDefinitionStatementContext> validOperationDefs = new HashMap<>();
        Map<String, PMLParser.RoutineDefinitionStatementContext> validRoutineDefs = new HashMap<>();
        Map<String, PMLParser.BasicFunctionDefinitionStatementContext> validFunctionDefs = new HashMap<>();

        FunctionSignatureVisitor signatureVisitor = new FunctionSignatureVisitor(visitorCtx, true);

        // operations
        for (PMLParser.AdminOpDefinitionStatementContext operationCtx : operationCtxs) {
            PMLOperationSignature signature = signatureVisitor.visitAdminOpSignature(operationCtx.adminOpSignature());
            processSignature(operationCtx, signature, functionSignatures, (ctx) -> validOperationDefs.put(signature.getName(), operationCtx));
        }

        // routines
        for (PMLParser.RoutineDefinitionStatementContext routineCtx : routineCtxs) {
            PMLRoutineSignature signature = signatureVisitor.visitRoutineSignature(routineCtx.routineSignature());
            processSignature(routineCtx, signature, functionSignatures, (ctx) -> validRoutineDefs.put(signature.getName(), routineCtx));
        }

        // functions
        for (PMLParser.BasicFunctionDefinitionStatementContext functionCtx : functionCtxs) {
            PMLBasicFunctionSignature signature = signatureVisitor.visitBasicFunctionSignature(functionCtx.basicFunctionSignature());
            processSignature(functionCtx, signature, functionSignatures, (ctx) -> validFunctionDefs.put(signature.getName(), functionCtx));
        }

        // compile all function bodies now that all signatures are compiled -- do not add signatures to ctx again
        signatureVisitor.setAddToCtx(false);
        FunctionDefinitionVisitor functionDefinitionVisitor = new FunctionDefinitionVisitor(visitorCtx, signatureVisitor);
        List<AdminOpDefinitionStatement> operations = compileFunctions(operationCtxs,
            functionDefinitionVisitor::visitAdminOpDefinitionStatement);
        List<RoutineDefinitionStatement> routines = compileFunctions(routineCtxs,
            functionDefinitionVisitor::visitRoutineDefinitionStatement);
        List<BasicFunctionDefinitionStatement> basicFunctions = compileFunctions(functionCtxs,
            functionDefinitionVisitor::visitBasicFunctionDefinitionStatement);

        return new CompiledFunctions(operations, routines, basicFunctions);
    }

    private <T, R> List<R> compileFunctions(List<T> contexts, java.util.function.Function<T, R> visitor) {
        List<R> results = new ArrayList<>();
        for (T context : contexts) {
            try {
                results.add(visitor.apply(context));
            } catch (PMLCompilationRuntimeException e) {
                visitorCtx.errorLog().addErrors(e.getErrors());
            }
        }

        return results;
    }

    private void processSignature(ParserRuleContext statementCtx,
                                  PMLFunctionSignature signature,
                                  Map<String, PMLFunctionSignature> functionSignatures,
                                  Consumer<ParserRuleContext> consumer) {
        // visit the signature which will add to the scope, if an error occurs, log it and continue
        try {
            // check that the function isn't already defined in the pml or global scope
            if (functionSignatures.containsKey(signature.getName())) {
                visitorCtx.errorLog().addError(
                        statementCtx,
                        "function '" + signature.getName() + "' already defined in scope"
                );

                return;
            }


            functionSignatures.put(signature.getName(), signature);
            consumer.accept(statementCtx);
        } catch (PMLCompilationRuntimeException e) {
            visitorCtx.errorLog().addErrors(e.getErrors());
        }
    }

    private record CompiledFunctions(List<AdminOpDefinitionStatement> operations,
                                     List<RoutineDefinitionStatement> routines,
                                     List<BasicFunctionDefinitionStatement> functions) {}

    private List<PMLStatement<?>> compileStatements(List<PMLParser.StatementContext> statementCtxs) {
        List<PMLStatement<?>> statements = new ArrayList<>();
        for (PMLParser.StatementContext stmtCtx : statementCtxs) {
            StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);

            try {
                PMLStatement<?> statement = statementVisitor.visitStatement(stmtCtx);
                statements.add(statement);
            } catch (PMLCompilationRuntimeException e) {
                visitorCtx.errorLog().addErrors(e.getErrors());
            }
        }

        return statements;
    }

}
