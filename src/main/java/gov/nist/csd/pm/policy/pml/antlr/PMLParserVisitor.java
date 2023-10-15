// Generated from PMLParser.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.pml.antlr;
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
	 * Visit a parse tree produced by {@link PMLParser#hierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchy(PMLParser.HierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#userAttrsHierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserAttrsHierarchy(PMLParser.UserAttrsHierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#objectAttrsHierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectAttrsHierarchy(PMLParser.ObjectAttrsHierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#associationsHierarchy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociationsHierarchy(PMLParser.AssociationsHierarchyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#hierarchyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchyBlock(PMLParser.HierarchyBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#associationsHierarchyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociationsHierarchyBlock(PMLParser.AssociationsHierarchyBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#hierarchyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHierarchyStatement(PMLParser.HierarchyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#associationsHierarchyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociationsHierarchyStatement(PMLParser.AssociationsHierarchyStatementContext ctx);
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
	 * Visit a parse tree produced by the {@code AnyUserSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyUserSubject(PMLParser.AnyUserSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UsersSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsersSubject(PMLParser.UsersSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UsersInUnionSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsersInUnionSubject(PMLParser.UsersInUnionSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UsersInIntersectionSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsersInIntersectionSubject(PMLParser.UsersInIntersectionSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProcessesSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcessesSubject(PMLParser.ProcessesSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyTarget(PMLParser.AnyTargetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyInUnionTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyInUnionTarget(PMLParser.AnyInUnionTargetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyInIntersectionTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyInIntersectionTarget(PMLParser.AnyInIntersectionTargetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OnTargets}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnTargets(PMLParser.OnTargetsContext ctx);
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
	 * Visit a parse tree produced by the {@code ConstDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDeclaration(PMLParser.ConstDeclarationContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#constSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstSpec(PMLParser.ConstSpecContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(PMLParser.IdentifierListContext ctx);
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