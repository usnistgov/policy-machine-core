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
	 * Visit a parse tree produced by {@link PMLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(PMLParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#varStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarStmt(PMLParser.VarStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#funcDefStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDefStmt(PMLParser.FuncDefStmtContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#funcReturnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncReturnStmt(PMLParser.FuncReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarReturnType(PMLParser.VarReturnTypeContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#foreachStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachStmt(PMLParser.ForeachStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#forRangeStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForRangeStmt(PMLParser.ForRangeStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#breakStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(PMLParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#continueStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(PMLParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#funcCallStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallStmt(PMLParser.FuncCallStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(PMLParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#elseIfStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStmt(PMLParser.ElseIfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#elseStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStmt(PMLParser.ElseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringType(PMLParser.StringTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanType(PMLParser.BooleanTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayVarType(PMLParser.ArrayVarTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapVarType(PMLParser.MapVarTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PMLParser#varType}.
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
	 * Visit a parse tree produced by {@link PMLParser#stmtBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmtBlock(PMLParser.StmtBlockContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#createPolicyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatePolicyStmt(PMLParser.CreatePolicyStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createAttrStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateAttrStmt(PMLParser.CreateAttrStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createUserOrObjectStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateUserOrObjectStmt(PMLParser.CreateUserOrObjectStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#setNodePropsStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetNodePropsStmt(PMLParser.SetNodePropsStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#assignStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStmt(PMLParser.AssignStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#deassignStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeassignStmt(PMLParser.DeassignStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#associateStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssociateStmt(PMLParser.AssociateStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#dissociateStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDissociateStmt(PMLParser.DissociateStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#deleteStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStmt(PMLParser.DeleteStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createObligationStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateObligationStmt(PMLParser.CreateObligationStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createRuleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateRuleStmt(PMLParser.CreateRuleStmtContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#responseStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResponseStmt(PMLParser.ResponseStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#deleteRuleStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteRuleStmt(PMLParser.DeleteRuleStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#createProhibitionStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProhibitionStmt(PMLParser.CreateProhibitionStmtContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#setResourceAccessRightsStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetResourceAccessRightsStmt(PMLParser.SetResourceAccessRightsStmtContext ctx);
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
	 * Visit a parse tree produced by {@link PMLParser#entryRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryRef(PMLParser.EntryRefContext ctx);
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
	 * labeled alternative in {@link PMLParser#varRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceByID(PMLParser.ReferenceByIDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EntryReference}
	 * labeled alternative in {@link PMLParser#varRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntryReference(PMLParser.EntryReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#funcCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(PMLParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link PMLParser#funcCallArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCallArgs(PMLParser.FuncCallArgsContext ctx);
}