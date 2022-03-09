// Generated from PAL.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.author.pal.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PALParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PALVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PALParser#pal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPal(PALParser.PalContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#stmts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmts(PALParser.StmtsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(PALParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#varStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarStmt(PALParser.VarStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#funcDefStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDefStmt(PALParser.FuncDefStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#formalArgList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalArgList(PALParser.FormalArgListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#formalArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalArg(PALParser.FormalArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#formalArgType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalArgType(PALParser.FormalArgTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#funcReturnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncReturnStmt(PALParser.FuncReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarReturnType}
	 * labeled alternative in {@link PALParser#funcReturnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarReturnType(PALParser.VarReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VoidReturnType}
	 * labeled alternative in {@link PALParser#funcReturnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidReturnType(PALParser.VoidReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#funcBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncBody(PALParser.FuncBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#foreachStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachStmt(PALParser.ForeachStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#breakStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(PALParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#continueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(PALParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#funcCallStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallStmt(PALParser.FuncCallStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(PALParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#elseIfStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStmt(PALParser.ElseIfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#elseStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStmt(PALParser.ElseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringType(PALParser.StringTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanType(PALParser.BooleanTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVarType(PALParser.ArrayVarTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapVarType(PALParser.MapVarTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyType(PALParser.AnyTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#mapType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapType(PALParser.MapTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#arrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(PALParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#stmtBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtBlock(PALParser.StmtBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteNode}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteNode(PALParser.DeleteNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteObligation}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteObligation(PALParser.DeleteObligationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteProhibition}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteProhibition(PALParser.DeleteProhibitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#nodeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeType(PALParser.NodeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#createStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateStmt(PALParser.CreateStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#createPolicyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatePolicyStmt(PALParser.CreatePolicyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#createAttrStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateAttrStmt(PALParser.CreateAttrStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#createUserOrObjectStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUserOrObjectStmt(PALParser.CreateUserOrObjectStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#setNodePropsStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetNodePropsStmt(PALParser.SetNodePropsStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#assignStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStmt(PALParser.AssignStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#deassignStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeassignStmt(PALParser.DeassignStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#associateStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociateStmt(PALParser.AssociateStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#dissociateStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDissociateStmt(PALParser.DissociateStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#deleteStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStmt(PALParser.DeleteStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#createObligationStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateObligationStmt(PALParser.CreateObligationStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#createRuleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRuleStmt(PALParser.CreateRuleStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyUserSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyUserSubject(PALParser.AnyUserSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserSubject(PALParser.UserSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UsersListSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsersListSubject(PALParser.UsersListSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UserAttrSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserAttrSubject(PALParser.UserAttrSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcessSubject(PALParser.ProcessSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolicyElement(PALParser.PolicyElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyPolicyElement(PALParser.AnyPolicyElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyContainedIn}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyContainedIn(PALParser.AnyContainedInContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyOfSet}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyOfSet(PALParser.AnyOfSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#anyPe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyPe(PALParser.AnyPeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#response}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResponse(PALParser.ResponseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#responseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResponseBlock(PALParser.ResponseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#responseStmts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResponseStmts(PALParser.ResponseStmtsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#responseStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResponseStmt(PALParser.ResponseStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#deleteRuleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteRuleStmt(PALParser.DeleteRuleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#createProhibitionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProhibitionStmt(PALParser.CreateProhibitionStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#prohibitionContainerList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProhibitionContainerList(PALParser.ProhibitionContainerListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#prohibitionContainerExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProhibitionContainerExpression(PALParser.ProhibitionContainerExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#setResourceAccessRightsStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetResourceAccessRightsStmt(PALParser.SetResourceAccessRightsStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VariableReference}
	 * labeled alternative in {@link PALParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableReference(PALParser.VariableReferenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionCall}
	 * labeled alternative in {@link PALParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(PALParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link PALParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpr(PALParser.LiteralExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(PALParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#accessRightArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessRightArray(PALParser.AccessRightArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#accessRight}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessRight(PALParser.AccessRightContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap(PALParser.MapContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#mapEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapEntry(PALParser.MapEntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#mapEntryRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapEntryRef(PALParser.MapEntryRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(PALParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(PALParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiteral(PALParser.ArrayLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapLiteral(PALParser.MapLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReferenceByID}
	 * labeled alternative in {@link PALParser#varRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceByID(PALParser.ReferenceByIDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapEntryReference}
	 * labeled alternative in {@link PALParser#varRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapEntryReference(PALParser.MapEntryReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(PALParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link PALParser#funcCallArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallArgs(PALParser.FuncCallArgsContext ctx);
}