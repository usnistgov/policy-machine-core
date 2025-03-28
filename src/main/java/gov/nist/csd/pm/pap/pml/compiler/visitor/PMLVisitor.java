package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.basic.BasicFunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.OperationDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.RoutineDefinitionStatement;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PMLVisitor extends PMLBaseVisitor<List<PMLStatement>> {

    public PMLVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<PMLStatement> visitPml(PMLParser.PmlContext ctx) {
        SortedStatements sortedStatements = sortStatements(ctx);

        CompiledFunctions functions = compileFunctions(
                sortedStatements.operationCtxs,
                sortedStatements.routineCtxs,
                sortedStatements.functionCtxs
        );

        List<PMLStatement> stmts = new ArrayList<>();
        stmts.addAll(functions.operations);
        stmts.addAll(functions.routines);
        stmts.addAll(functions.functions);
        stmts.addAll(compileStatements(sortedStatements.statementCtxs));

        return stmts;
    }

    private SortedStatements sortStatements(PMLParser.PmlContext ctx) {
        List<PMLParser.OperationDefinitionStatementContext> operationCtxs = new ArrayList<>();
        List<PMLParser.RoutineDefinitionStatementContext> routineCtxs = new ArrayList<>();
        List<PMLParser.FunctionDefinitionStatementContext> functionCtxs = new ArrayList<>();
        List<PMLParser.StatementContext> statementCtxs = new ArrayList<>();

        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
            PMLParser.BasicStatementContext programmingStatementContext = stmtCtx.basicStatement();
            PMLParser.OperationStatementContext policyStatementContext = stmtCtx.operationStatement();

            if (programmingStatementContext != null) {
                if (programmingStatementContext.functionDefinitionStatement() != null) {
                    functionCtxs.add(programmingStatementContext.functionDefinitionStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            } else if (policyStatementContext != null) {
                if (policyStatementContext.operationDefinitionStatement() != null) {
                    operationCtxs.add(policyStatementContext.operationDefinitionStatement());
                } else if (policyStatementContext.routineDefinitionStatement() != null) {
                    routineCtxs.add(policyStatementContext.routineDefinitionStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            }
        }

        return new SortedStatements(operationCtxs, routineCtxs, functionCtxs, statementCtxs);
    }

    private record SortedStatements(List<PMLParser.OperationDefinitionStatementContext> operationCtxs,
                                    List<PMLParser.RoutineDefinitionStatementContext> routineCtxs,
                                    List<PMLParser.FunctionDefinitionStatementContext> functionCtxs,
                                    List<PMLParser.StatementContext> statementCtxs) {}

    private CompiledFunctions compileFunctions(List<PMLParser.OperationDefinitionStatementContext> operationCtxs,
                                               List<PMLParser.RoutineDefinitionStatementContext> routineCtxs,
                                               List<PMLParser.FunctionDefinitionStatementContext> functionCtxs) {
        Map<String, PMLFunctionSignature> functions = new HashMap<>(visitorCtx.scope().getFunctions());

        // track the function definitions statements to be processed,
        // function signatures are compiled first in the event that one function calls another
        // any function with an error won't be processed but execution will continue inorder to find anymore errors
        Map<String, PMLParser.OperationDefinitionStatementContext> validOperationDefs = new HashMap<>();
        Map<String, PMLParser.RoutineDefinitionStatementContext> validRoutineDefs = new HashMap<>();
        Map<String, PMLParser.FunctionDefinitionStatementContext> validFunctionDefs = new HashMap<>();

        FunctionDefinitionVisitor.SignatureVisitor signatureVisitor =
                new FunctionDefinitionVisitor.SignatureVisitor(visitorCtx, true);

        // operations
        for (PMLParser.OperationDefinitionStatementContext operationCtx : operationCtxs) {
            PMLFunctionSignature signature = signatureVisitor.visitOperationSignature(operationCtx.operationSignature());
            processSignature(visitorCtx, operationCtx, signature, functions, (ctx) -> validOperationDefs.put(signature.getName(), operationCtx));
        }

        // routines
        for (PMLParser.RoutineDefinitionStatementContext routineCtx : routineCtxs) {
            PMLFunctionSignature signature = signatureVisitor.visitRoutineSignature(routineCtx.routineSignature());
            processSignature(visitorCtx, routineCtx, signature, functions, (ctx) -> validRoutineDefs.put(signature.getName(), routineCtx));
        }

        // functions
        for (PMLParser.FunctionDefinitionStatementContext functionCtx : functionCtxs) {
            PMLFunctionSignature signature = signatureVisitor.visitFunctionSignature(functionCtx.functionSignature());
            processSignature(visitorCtx, functionCtx, signature, functions, (ctx) -> validFunctionDefs.put(signature.getName(), functionCtx));
        }

        // compile all function bodies now that all signatures are compiled
        FunctionDefinitionVisitor functionDefinitionVisitor = new FunctionDefinitionVisitor(visitorCtx, false);
        List<OperationDefinitionStatement> operations = compileFunctions(operationCtxs, functionDefinitionVisitor::visitOperationDefinitionStatement);
        List<RoutineDefinitionStatement> routines = compileFunctions(routineCtxs, functionDefinitionVisitor::visitRoutineDefinitionStatement);
        List<BasicFunctionDefinitionStatement> basicFunctions = compileFunctions(functionCtxs, functionDefinitionVisitor::visitFunctionDefinitionStatement);

        return new CompiledFunctions(operations, routines, basicFunctions);
    }

    private <T, R> List<R> compileFunctions(List<T> contexts, Function<T, R> visitor) {
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

    private void processSignature(VisitorContext vistorCtx,
                                  ParserRuleContext statementCtx,
                                  PMLFunctionSignature signature,
                                  Map<String, PMLFunctionSignature> functions,
                                  Consumer<ParserRuleContext> consumer) {
        // visit the signature which will add to the scope, if an error occurs, log it and continue
        try {
            // check that the function isn't already defined in the pml or global scope
            if (functions.containsKey(signature.getName())) {
                visitorCtx.errorLog().addError(
                        statementCtx,
                        "function '" + signature.getName() + "' already defined in scope"
                );

                return;
            }


            functions.put(signature.getName(), signature);
            consumer.accept(statementCtx);
        } catch (PMLCompilationRuntimeException e) {
            visitorCtx.errorLog().addErrors(e.getErrors());
        }
    }

    private record CompiledFunctions(List<OperationDefinitionStatement> operations,
                                     List<RoutineDefinitionStatement> routines,
                                     List<BasicFunctionDefinitionStatement> functions) {}

    private List<PMLStatement> compileStatements(List<PMLParser.StatementContext> statementCtxs) {
        List<PMLStatement> statements = new ArrayList<>();
        for (PMLParser.StatementContext stmtCtx : statementCtxs) {
            StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);

            try {
                PMLStatement statement = statementVisitor.visitStatement(stmtCtx);
                statements.add(statement);
            } catch (PMLCompilationRuntimeException e) {
                visitorCtx.errorLog().addErrors(e.getErrors());
            }
        }

        return statements;
    }

}
