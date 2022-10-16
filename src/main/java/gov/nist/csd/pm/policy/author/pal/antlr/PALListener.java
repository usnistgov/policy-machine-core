// Generated from PAL.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.author.pal.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PALParser}.
 */
public interface PALListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PALParser#pal}.
	 * @param ctx the parse tree
	 */
	void enterPal(PALParser.PalContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#pal}.
	 * @param ctx the parse tree
	 */
	void exitPal(PALParser.PalContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(PALParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(PALParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#varStmt}.
	 * @param ctx the parse tree
	 */
	void enterVarStmt(PALParser.VarStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#varStmt}.
	 * @param ctx the parse tree
	 */
	void exitVarStmt(PALParser.VarStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#funcDefStmt}.
	 * @param ctx the parse tree
	 */
	void enterFuncDefStmt(PALParser.FuncDefStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#funcDefStmt}.
	 * @param ctx the parse tree
	 */
	void exitFuncDefStmt(PALParser.FuncDefStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#formalArgList}.
	 * @param ctx the parse tree
	 */
	void enterFormalArgList(PALParser.FormalArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#formalArgList}.
	 * @param ctx the parse tree
	 */
	void exitFormalArgList(PALParser.FormalArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#formalArg}.
	 * @param ctx the parse tree
	 */
	void enterFormalArg(PALParser.FormalArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#formalArg}.
	 * @param ctx the parse tree
	 */
	void exitFormalArg(PALParser.FormalArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#formalArgType}.
	 * @param ctx the parse tree
	 */
	void enterFormalArgType(PALParser.FormalArgTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#formalArgType}.
	 * @param ctx the parse tree
	 */
	void exitFormalArgType(PALParser.FormalArgTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#funcReturnStmt}.
	 * @param ctx the parse tree
	 */
	void enterFuncReturnStmt(PALParser.FuncReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#funcReturnStmt}.
	 * @param ctx the parse tree
	 */
	void exitFuncReturnStmt(PALParser.FuncReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarReturnType}
	 * labeled alternative in {@link PALParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void enterVarReturnType(PALParser.VarReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarReturnType}
	 * labeled alternative in {@link PALParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void exitVarReturnType(PALParser.VarReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VoidReturnType}
	 * labeled alternative in {@link PALParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void enterVoidReturnType(PALParser.VoidReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VoidReturnType}
	 * labeled alternative in {@link PALParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void exitVoidReturnType(PALParser.VoidReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#funcBody}.
	 * @param ctx the parse tree
	 */
	void enterFuncBody(PALParser.FuncBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#funcBody}.
	 * @param ctx the parse tree
	 */
	void exitFuncBody(PALParser.FuncBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#foreachStmt}.
	 * @param ctx the parse tree
	 */
	void enterForeachStmt(PALParser.ForeachStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#foreachStmt}.
	 * @param ctx the parse tree
	 */
	void exitForeachStmt(PALParser.ForeachStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#forRangeStmt}.
	 * @param ctx the parse tree
	 */
	void enterForRangeStmt(PALParser.ForRangeStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#forRangeStmt}.
	 * @param ctx the parse tree
	 */
	void exitForRangeStmt(PALParser.ForRangeStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(PALParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(PALParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#continueStmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(PALParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#continueStmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(PALParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#funcCallStmt}.
	 * @param ctx the parse tree
	 */
	void enterFuncCallStmt(PALParser.FuncCallStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#funcCallStmt}.
	 * @param ctx the parse tree
	 */
	void exitFuncCallStmt(PALParser.FuncCallStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(PALParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(PALParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#elseIfStmt}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStmt(PALParser.ElseIfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#elseIfStmt}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStmt(PALParser.ElseIfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#elseStmt}.
	 * @param ctx the parse tree
	 */
	void enterElseStmt(PALParser.ElseStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#elseStmt}.
	 * @param ctx the parse tree
	 */
	void exitElseStmt(PALParser.ElseStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterStringType(PALParser.StringTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitStringType(PALParser.StringTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterBooleanType(PALParser.BooleanTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitBooleanType(PALParser.BooleanTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterArrayVarType(PALParser.ArrayVarTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitArrayVarType(PALParser.ArrayVarTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterMapVarType(PALParser.MapVarTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitMapVarType(PALParser.MapVarTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterAnyType(PALParser.AnyTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PALParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitAnyType(PALParser.AnyTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#mapType}.
	 * @param ctx the parse tree
	 */
	void enterMapType(PALParser.MapTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#mapType}.
	 * @param ctx the parse tree
	 */
	void exitMapType(PALParser.MapTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(PALParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(PALParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#stmtBlock}.
	 * @param ctx the parse tree
	 */
	void enterStmtBlock(PALParser.StmtBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#stmtBlock}.
	 * @param ctx the parse tree
	 */
	void exitStmtBlock(PALParser.StmtBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteNode}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void enterDeleteNode(PALParser.DeleteNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteNode}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void exitDeleteNode(PALParser.DeleteNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteObligation}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void enterDeleteObligation(PALParser.DeleteObligationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteObligation}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void exitDeleteObligation(PALParser.DeleteObligationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteProhibition}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void enterDeleteProhibition(PALParser.DeleteProhibitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteProhibition}
	 * labeled alternative in {@link PALParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void exitDeleteProhibition(PALParser.DeleteProhibitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#nodeType}.
	 * @param ctx the parse tree
	 */
	void enterNodeType(PALParser.NodeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#nodeType}.
	 * @param ctx the parse tree
	 */
	void exitNodeType(PALParser.NodeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#createPolicyStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatePolicyStmt(PALParser.CreatePolicyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#createPolicyStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatePolicyStmt(PALParser.CreatePolicyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#createAttrStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateAttrStmt(PALParser.CreateAttrStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#createAttrStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateAttrStmt(PALParser.CreateAttrStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#createUserOrObjectStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserOrObjectStmt(PALParser.CreateUserOrObjectStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#createUserOrObjectStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserOrObjectStmt(PALParser.CreateUserOrObjectStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#setNodePropsStmt}.
	 * @param ctx the parse tree
	 */
	void enterSetNodePropsStmt(PALParser.SetNodePropsStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#setNodePropsStmt}.
	 * @param ctx the parse tree
	 */
	void exitSetNodePropsStmt(PALParser.SetNodePropsStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignStmt(PALParser.AssignStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignStmt(PALParser.AssignStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#deassignStmt}.
	 * @param ctx the parse tree
	 */
	void enterDeassignStmt(PALParser.DeassignStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#deassignStmt}.
	 * @param ctx the parse tree
	 */
	void exitDeassignStmt(PALParser.DeassignStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#associateStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssociateStmt(PALParser.AssociateStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#associateStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssociateStmt(PALParser.AssociateStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#dissociateStmt}.
	 * @param ctx the parse tree
	 */
	void enterDissociateStmt(PALParser.DissociateStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#dissociateStmt}.
	 * @param ctx the parse tree
	 */
	void exitDissociateStmt(PALParser.DissociateStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#deleteStmt}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStmt(PALParser.DeleteStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#deleteStmt}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStmt(PALParser.DeleteStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#createObligationStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateObligationStmt(PALParser.CreateObligationStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#createObligationStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateObligationStmt(PALParser.CreateObligationStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#createRuleStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateRuleStmt(PALParser.CreateRuleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#createRuleStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateRuleStmt(PALParser.CreateRuleStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyUserSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyUserSubject(PALParser.AnyUserSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyUserSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyUserSubject(PALParser.AnyUserSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUserSubject(PALParser.UserSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUserSubject(PALParser.UserSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UsersListSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUsersListSubject(PALParser.UsersListSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UsersListSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUsersListSubject(PALParser.UsersListSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserAttrSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUserAttrSubject(PALParser.UserAttrSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserAttrSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUserAttrSubject(PALParser.UserAttrSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterProcessSubject(PALParser.ProcessSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PALParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitProcessSubject(PALParser.ProcessSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterPolicyElement(PALParser.PolicyElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitPolicyElement(PALParser.PolicyElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyPolicyElement(PALParser.AnyPolicyElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyPolicyElement(PALParser.AnyPolicyElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyContainedIn}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyContainedIn(PALParser.AnyContainedInContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyContainedIn}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyContainedIn(PALParser.AnyContainedInContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyOfSet}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyOfSet(PALParser.AnyOfSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyOfSet}
	 * labeled alternative in {@link PALParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyOfSet(PALParser.AnyOfSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#anyPe}.
	 * @param ctx the parse tree
	 */
	void enterAnyPe(PALParser.AnyPeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#anyPe}.
	 * @param ctx the parse tree
	 */
	void exitAnyPe(PALParser.AnyPeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#response}.
	 * @param ctx the parse tree
	 */
	void enterResponse(PALParser.ResponseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#response}.
	 * @param ctx the parse tree
	 */
	void exitResponse(PALParser.ResponseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#responseBlock}.
	 * @param ctx the parse tree
	 */
	void enterResponseBlock(PALParser.ResponseBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#responseBlock}.
	 * @param ctx the parse tree
	 */
	void exitResponseBlock(PALParser.ResponseBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#responseStmt}.
	 * @param ctx the parse tree
	 */
	void enterResponseStmt(PALParser.ResponseStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#responseStmt}.
	 * @param ctx the parse tree
	 */
	void exitResponseStmt(PALParser.ResponseStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#deleteRuleStmt}.
	 * @param ctx the parse tree
	 */
	void enterDeleteRuleStmt(PALParser.DeleteRuleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#deleteRuleStmt}.
	 * @param ctx the parse tree
	 */
	void exitDeleteRuleStmt(PALParser.DeleteRuleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#createProhibitionStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateProhibitionStmt(PALParser.CreateProhibitionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#createProhibitionStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateProhibitionStmt(PALParser.CreateProhibitionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#prohibitionContainerList}.
	 * @param ctx the parse tree
	 */
	void enterProhibitionContainerList(PALParser.ProhibitionContainerListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#prohibitionContainerList}.
	 * @param ctx the parse tree
	 */
	void exitProhibitionContainerList(PALParser.ProhibitionContainerListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#prohibitionContainerExpression}.
	 * @param ctx the parse tree
	 */
	void enterProhibitionContainerExpression(PALParser.ProhibitionContainerExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#prohibitionContainerExpression}.
	 * @param ctx the parse tree
	 */
	void exitProhibitionContainerExpression(PALParser.ProhibitionContainerExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#setResourceAccessRightsStmt}.
	 * @param ctx the parse tree
	 */
	void enterSetResourceAccessRightsStmt(PALParser.SetResourceAccessRightsStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#setResourceAccessRightsStmt}.
	 * @param ctx the parse tree
	 */
	void exitSetResourceAccessRightsStmt(PALParser.SetResourceAccessRightsStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#expressionArray}.
	 * @param ctx the parse tree
	 */
	void enterExpressionArray(PALParser.ExpressionArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#expressionArray}.
	 * @param ctx the parse tree
	 */
	void exitExpressionArray(PALParser.ExpressionArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PALParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PALParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(PALParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(PALParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#map}.
	 * @param ctx the parse tree
	 */
	void enterMap(PALParser.MapContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#map}.
	 * @param ctx the parse tree
	 */
	void exitMap(PALParser.MapContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#mapEntry}.
	 * @param ctx the parse tree
	 */
	void enterMapEntry(PALParser.MapEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#mapEntry}.
	 * @param ctx the parse tree
	 */
	void exitMapEntry(PALParser.MapEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#mapEntryRef}.
	 * @param ctx the parse tree
	 */
	void enterMapEntryRef(PALParser.MapEntryRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#mapEntryRef}.
	 * @param ctx the parse tree
	 */
	void exitMapEntryRef(PALParser.MapEntryRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(PALParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(PALParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(PALParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(PALParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterArrayLiteral(PALParser.ArrayLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitArrayLiteral(PALParser.ArrayLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MapLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterMapLiteral(PALParser.MapLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MapLiteral}
	 * labeled alternative in {@link PALParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitMapLiteral(PALParser.MapLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReferenceByID}
	 * labeled alternative in {@link PALParser#varRef}.
	 * @param ctx the parse tree
	 */
	void enterReferenceByID(PALParser.ReferenceByIDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReferenceByID}
	 * labeled alternative in {@link PALParser#varRef}.
	 * @param ctx the parse tree
	 */
	void exitReferenceByID(PALParser.ReferenceByIDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MapEntryReference}
	 * labeled alternative in {@link PALParser#varRef}.
	 * @param ctx the parse tree
	 */
	void enterMapEntryReference(PALParser.MapEntryReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MapEntryReference}
	 * labeled alternative in {@link PALParser#varRef}.
	 * @param ctx the parse tree
	 */
	void exitMapEntryReference(PALParser.MapEntryReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(PALParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(PALParser.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link PALParser#funcCallArgs}.
	 * @param ctx the parse tree
	 */
	void enterFuncCallArgs(PALParser.FuncCallArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PALParser#funcCallArgs}.
	 * @param ctx the parse tree
	 */
	void exitFuncCallArgs(PALParser.FuncCallArgsContext ctx);
}