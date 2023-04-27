// Generated from PML.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.pml.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PMLVisitor<T> extends ParseTreeVisitor<T> {
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
	 * Visit a parse tree produced by {@link PMLParser#createPolicyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createAttributeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateAttributeStatement(PMLParser.CreateAttributeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createUserOrObjectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUserOrObjectStatement(PMLParser.CreateUserOrObjectStatementContext ctx);
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
	 * Visit a parse tree produced by the {@code AnyUserSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyUserSubject(PMLParser.AnyUserSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserSubject(PMLParser.UserSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UsersListSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsersListSubject(PMLParser.UsersListSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserAttrSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserAttrSubject(PMLParser.UserAttrSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcessSubject(PMLParser.ProcessSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolicyElement(PMLParser.PolicyElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyPolicyElement(PMLParser.AnyPolicyElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyContainedIn}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyContainedIn(PMLParser.AnyContainedInContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyOfSet}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyOfSet(PMLParser.AnyOfSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#anyPe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyPe(PMLParser.AnyPeContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#prohibitionContainerList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProhibitionContainerList(PMLParser.ProhibitionContainerListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#prohibitionContainerExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProhibitionContainerExpression(PMLParser.ProhibitionContainerExpressionContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#setResourceAccessRightsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetResourceAccessRightsStatement(PMLParser.SetResourceAccessRightsStatementContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarationStatement(PMLParser.VariableDeclarationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#functionDefinitionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#formalArgType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalArgType(PMLParser.FormalArgTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#functionReturnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReturnStatement(PMLParser.FunctionReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableReturnType(PMLParser.VariableReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VoidReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidReturnType(PMLParser.VoidReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#funcBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncBody(PMLParser.FuncBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#foreachStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachStatement(PMLParser.ForeachStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#forRangeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForRangeStatement(PMLParser.ForRangeStatementContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#functionInvokeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#statementBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementBlock(PMLParser.StatementBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(PMLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(PMLParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap(PMLParser.MapContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#mapEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapEntry(PMLParser.MapEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#entryReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryReference(PMLParser.EntryReferenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(PMLParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(PMLParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NumberLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberLiteral(PMLParser.NumberLiteralContext ctx);
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
	 * Visit a parse tree produced by the {@code ReferenceByID}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceByID(PMLParser.ReferenceByIDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReferenceByEntry}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceByEntry(PMLParser.ReferenceByEntryContext ctx);
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