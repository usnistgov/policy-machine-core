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

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.AdminOperationStatementContext;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation.OperationDefinitionVisitor;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation.OperationSignatureVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.FunctionDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.QueryOperationDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.ResourceOpDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Compiles a full PML source file into its list of top-level statements.
 */
public class PMLVisitor extends PMLBaseVisitor<List<PMLStatement<?>>> {

    public PMLVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<PMLStatement<?>> visitPml(PMLParser.PmlContext ctx) {
        SortedStatements sortedStatements = sortStatements(ctx);

        CompiledOperations operations = compileOperations(
            sortedStatements.operationCtxs,
            sortedStatements.resourceCtxs,
            sortedStatements.queryCtxs,
            sortedStatements.routineCtxs,
            sortedStatements.functionCtxs
        );

        List<PMLStatement<?>> stmts = new ArrayList<>();
        stmts.addAll(operations.adminOps);
        stmts.addAll(operations.resourceOps);
        stmts.addAll(operations.queries);
        stmts.addAll(operations.routines);
        stmts.addAll(operations.functions);
        stmts.addAll(compileStatements(sortedStatements.statementCtxs));

        return stmts;
    }

    private SortedStatements sortStatements(PMLParser.PmlContext ctx) {
        List<PMLParser.AdminOpDefinitionStatementContext> adminOpCtxs = new ArrayList<>();
        List<PMLParser.ResourceOpDefinitionStatementContext> resourceOpCtxs = new ArrayList<>();
        List<PMLParser.QueryOpDefinitionStatementContext> queryCtxs = new ArrayList<>();
        List<PMLParser.RoutineDefinitionStatementContext> routineCtxs = new ArrayList<>();
        List<PMLParser.FunctionDefinitionStatementContext> functionCtxs = new ArrayList<>();
        List<PMLParser.StatementContext> statementCtxs = new ArrayList<>();

        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
            PMLParser.BasicStatementContext basicStatementContext = stmtCtx.basicStatement();
            AdminOperationStatementContext operationStatementContext = stmtCtx.adminOperationStatement();

            if (basicStatementContext != null) {
                if (basicStatementContext.functionDefinitionStatement() != null) {
                    functionCtxs.add(basicStatementContext.functionDefinitionStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            } else if (operationStatementContext != null) {
                if (operationStatementContext.adminOpDefinitionStatement() != null) {
                    adminOpCtxs.add(operationStatementContext.adminOpDefinitionStatement());
                } else if (operationStatementContext.resourceOpDefinitionStatement() != null) {
                    resourceOpCtxs.add(operationStatementContext.resourceOpDefinitionStatement());
                } else if (operationStatementContext.queryOpDefinitionStatement() != null) {
                    queryCtxs.add(operationStatementContext.queryOpDefinitionStatement());
                } else if (operationStatementContext.routineDefinitionStatement() != null) {
                    routineCtxs.add(operationStatementContext.routineDefinitionStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            }
        }

        return new SortedStatements(adminOpCtxs, resourceOpCtxs, queryCtxs, routineCtxs, functionCtxs, statementCtxs);
    }

    private record SortedStatements(List<PMLParser.AdminOpDefinitionStatementContext> operationCtxs,
                                    List<PMLParser.ResourceOpDefinitionStatementContext> resourceCtxs,
                                    List<PMLParser.QueryOpDefinitionStatementContext> queryCtxs,
                                    List<PMLParser.RoutineDefinitionStatementContext> routineCtxs,
                                    List<PMLParser.FunctionDefinitionStatementContext> functionCtxs,
                                    List<PMLParser.StatementContext> statementCtxs) {}

    private CompiledOperations compileOperations(List<PMLParser.AdminOpDefinitionStatementContext> operationCtxs,
                                                                 List<PMLParser.ResourceOpDefinitionStatementContext> resourceCtxs,
                                                                 List<PMLParser.QueryOpDefinitionStatementContext> queryCtxs,
                                                                 List<PMLParser.RoutineDefinitionStatementContext> routineCtxs,
                                                                 List<PMLParser.FunctionDefinitionStatementContext> functionCtxs) {
        Map<String, PMLOperationSignature> functionSignatures = new HashMap<>(visitorCtx.scope().getOperations());

        // track the function definitions statements to be processed,
        // function signatures are compiled first in the event that one function calls another
        // any function with an error won't be processed but execution will continue inorder to find anymore errors
        Map<String, PMLParser.AdminOpDefinitionStatementContext> validAdminDefs = new HashMap<>();
        Map<String, PMLParser.ResourceOpDefinitionStatementContext> validResourceDefs = new HashMap<>();
        Map<String, PMLParser.QueryOpDefinitionStatementContext> validQueryDefs = new HashMap<>();
        Map<String, PMLParser.RoutineDefinitionStatementContext> validRoutineDefs = new HashMap<>();
        Map<String, PMLParser.FunctionDefinitionStatementContext> validFunctionDefs = new HashMap<>();

        OperationSignatureVisitor signatureVisitor = new OperationSignatureVisitor(visitorCtx, true);

        // admin
        for (PMLParser.AdminOpDefinitionStatementContext operationCtx : operationCtxs) {
            PMLOperationSignature signature = signatureVisitor.visitAdminOpSignature(operationCtx.adminOpSignature());
            processSignature(operationCtx, signature, functionSignatures, (ctx) -> validAdminDefs.put(signature.getName(), operationCtx));
        }

        // resource
        for (PMLParser.ResourceOpDefinitionStatementContext resourceCtx : resourceCtxs) {
            PMLOperationSignature signature = signatureVisitor.visitResourceOpSignature(resourceCtx.resourceOpSignature());
            processSignature(resourceCtx, signature, functionSignatures, (ctx) -> validResourceDefs.put(signature.getName(), resourceCtx));
        }

        // query
        for (PMLParser.QueryOpDefinitionStatementContext queryCtx : queryCtxs) {
            PMLOperationSignature signature = signatureVisitor.visitQueryOpSignature(queryCtx.queryOpSignature());
            processSignature(queryCtx, signature, functionSignatures, (ctx) -> validQueryDefs.put(signature.getName(), queryCtx));
        }

        // routines
        for (PMLParser.RoutineDefinitionStatementContext routineCtx : routineCtxs) {
            PMLOperationSignature signature = signatureVisitor.visitRoutineSignature(routineCtx.routineSignature());
            processSignature(routineCtx, signature, functionSignatures, (ctx) -> validRoutineDefs.put(signature.getName(), routineCtx));
        }

        // functions
        for (PMLParser.FunctionDefinitionStatementContext functionCtx : functionCtxs) {
            PMLOperationSignature signature = signatureVisitor.visitFunctionSignature(functionCtx.functionSignature());
            processSignature(functionCtx, signature, functionSignatures, (ctx) -> validFunctionDefs.put(signature.getName(), functionCtx));
        }

        // compile all function bodies now that all signatures are compiled -- do not add signatures to ctx again
        signatureVisitor.setAddToCtx(false);
        OperationDefinitionVisitor operationDefinitionVisitor = new OperationDefinitionVisitor(visitorCtx, signatureVisitor);

        return new CompiledOperations(
            compileOperations(operationCtxs, operationDefinitionVisitor::visitAdminOpDefinitionStatement),
            compileOperations(resourceCtxs, operationDefinitionVisitor::visitResourceOpDefinitionStatement),
            compileOperations(queryCtxs, operationDefinitionVisitor::visitQueryOpDefinitionStatement),
            compileOperations(routineCtxs, operationDefinitionVisitor::visitRoutineDefinitionStatement),
            compileOperations(functionCtxs, operationDefinitionVisitor::visitFunctionDefinitionStatement)
        );
    }

    private <T, R> List<R> compileOperations(List<T> contexts, java.util.function.Function<T, R> visitor) {
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

    private record CompiledOperations(List<AdminOpDefinitionStatement> adminOps,
                                      List<ResourceOpDefinitionStatement> resourceOps,
                                      List<QueryOperationDefinitionStatement> queries,
                                      List<RoutineDefinitionStatement> routines,
                                      List<FunctionDefinitionStatement> functions) {}

    private void processSignature(ParserRuleContext statementCtx,
                                  PMLOperationSignature signature,
                                  Map<String, PMLOperationSignature> functionSignatures,
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
