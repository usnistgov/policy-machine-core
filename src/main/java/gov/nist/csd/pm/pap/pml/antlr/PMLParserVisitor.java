// Generated from PMLParser.g4 by ANTLR 4.13.1
package gov.nist.csd.pm.pap.pml.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PMLParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PMLParser#pml}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPml(PMLParser.PmlContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(PMLParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#statementBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementBlock(PMLParser.StatementBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createPolicyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createNonPCStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#nonPCNodeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonPCNodeType(PMLParser.NonPCNodeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createObligationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createRuleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRuleStatement(PMLParser.CreateRuleStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyUserPattern}
	 * labeled alternative in {@link PMLParser#subjectPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyUserPattern(PMLParser.AnyUserPatternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserPattern}
	 * labeled alternative in {@link PMLParser#subjectPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserPattern(PMLParser.UserPatternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BasicSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicSubjectPatternExpression(PMLParser.BasicSubjectPatternExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenSubjectPatternExpression(PMLParser.ParenSubjectPatternExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NegateSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegateSubjectPatternExpression(PMLParser.NegateSubjectPatternExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalSubjectPatternExpression(PMLParser.LogicalSubjectPatternExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInSubject(PMLParser.InSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UsernameSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsernameSubject(PMLParser.UsernameSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcessSubject(PMLParser.ProcessSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyOperation}
	 * labeled alternative in {@link PMLParser#operationPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyOperation(PMLParser.AnyOperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IDOperation}
	 * labeled alternative in {@link PMLParser#operationPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDOperation(PMLParser.IDOperationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#operandPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandPattern(PMLParser.OperandPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#operandPatternElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandPatternElement(PMLParser.OperandPatternElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#operandPatternExpressionArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperandPatternExpressionArray(PMLParser.OperandPatternExpressionArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenOperandPatternExpression(PMLParser.ParenOperandPatternExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NegateOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegateOperandPatternExpression(PMLParser.NegateOperandPatternExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BasicOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicOperandPatternExpression(PMLParser.BasicOperandPatternExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOperandPatternExpression(PMLParser.LogicalOperandPatternExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyPolicyElement(PMLParser.AnyPolicyElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InPolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInPolicyElement(PMLParser.InPolicyElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolicyElement(PMLParser.PolicyElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#response}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResponse(PMLParser.ResponseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#responseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResponseBlock(PMLParser.ResponseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#responseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResponseStatement(PMLParser.ResponseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createProhibitionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#setNodePropertiesStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#assignStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStatement(PMLParser.AssignStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#deassignStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeassignStatement(PMLParser.DeassignStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#associateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociateStatement(PMLParser.AssociateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#dissociateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDissociateStatement(PMLParser.DissociateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#setResourceOperationsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetResourceOperationsStatement(PMLParser.SetResourceOperationsStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#deleteStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStatement(PMLParser.DeleteStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteNode}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteNode(PMLParser.DeleteNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteObligation}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteObligation(PMLParser.DeleteObligationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteProhibition}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteProhibition(PMLParser.DeleteProhibitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#nodeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeType(PMLParser.NodeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#deleteRuleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(PMLParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ShortDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShortDeclaration(PMLParser.ShortDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#varSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarSpec(PMLParser.VarSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#variableAssignmentStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableAssignmentStatement(PMLParser.VariableAssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#functionDefinitionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#functionSignature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionSignature(PMLParser.FunctionSignatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#formalArgList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalArgList(PMLParser.FormalArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#formalArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalArg(PMLParser.FormalArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(PMLParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#checkStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckStatement(PMLParser.CheckStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#checkStatementBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCheckStatementBlock(PMLParser.CheckStatementBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#idArr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdArr(PMLParser.IdArrContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#functionInvokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#foreachStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachStatement(PMLParser.ForeachStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(PMLParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(PMLParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(PMLParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#elseIfStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStatement(PMLParser.ElseIfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#elseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStatement(PMLParser.ElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringType(PMLParser.StringTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanType(PMLParser.BooleanTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVarType(PMLParser.ArrayVarTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapVarType(PMLParser.MapVarTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyType(PMLParser.AnyTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#mapType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapType(PMLParser.MapTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#arrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(PMLParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NegateExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegateExpression(PMLParser.NegateExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExpression(PMLParser.LogicalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlusExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusExpression(PMLParser.PlusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionInvokeExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvokeExpression(PMLParser.FunctionInvokeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableReferenceExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableReferenceExpression(PMLParser.VariableReferenceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpression(PMLParser.LiteralExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpression(PMLParser.ParenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualsExpression(PMLParser.EqualsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(PMLParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(PMLParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(PMLParser.BoolLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiteral(PMLParser.ArrayLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(PMLParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#stringLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLit(PMLParser.StringLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#boolLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLit(PMLParser.BoolLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#arrayLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLit(PMLParser.ArrayLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#stringArrayLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringArrayLit(PMLParser.StringArrayLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#mapLit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLit(PMLParser.MapLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(PMLParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReferenceByIndex}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceByIndex(PMLParser.ReferenceByIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReferenceByID}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceByID(PMLParser.ReferenceByIDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BracketIndex}
	 * labeled alternative in {@link PMLParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketIndex(PMLParser.BracketIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotIndex}
	 * labeled alternative in {@link PMLParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotIndex(PMLParser.DotIndexContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(PMLParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#functionInvoke}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvoke(PMLParser.FunctionInvokeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#functionInvokeArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvokeArgs(PMLParser.FunctionInvokeArgsContext ctx);
}