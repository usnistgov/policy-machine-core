// Generated from PML.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.pml.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PMLParser}.
 */
public interface PMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PMLParser#pml}.
	 * @param ctx the parse tree
	 */
	void enterPml(PMLParser.PmlContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#pml}.
	 * @param ctx the parse tree
	 */
	void exitPml(PMLParser.PmlContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(PMLParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(PMLParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#varStmt}.
	 * @param ctx the parse tree
	 */
	void enterVarStmt(PMLParser.VarStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#varStmt}.
	 * @param ctx the parse tree
	 */
	void exitVarStmt(PMLParser.VarStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#funcDefStmt}.
	 * @param ctx the parse tree
	 */
	void enterFuncDefStmt(PMLParser.FuncDefStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#funcDefStmt}.
	 * @param ctx the parse tree
	 */
	void exitFuncDefStmt(PMLParser.FuncDefStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#formalArgList}.
	 * @param ctx the parse tree
	 */
	void enterFormalArgList(PMLParser.FormalArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#formalArgList}.
	 * @param ctx the parse tree
	 */
	void exitFormalArgList(PMLParser.FormalArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#formalArg}.
	 * @param ctx the parse tree
	 */
	void enterFormalArg(PMLParser.FormalArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#formalArg}.
	 * @param ctx the parse tree
	 */
	void exitFormalArg(PMLParser.FormalArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#formalArgType}.
	 * @param ctx the parse tree
	 */
	void enterFormalArgType(PMLParser.FormalArgTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#formalArgType}.
	 * @param ctx the parse tree
	 */
	void exitFormalArgType(PMLParser.FormalArgTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#funcReturnStmt}.
	 * @param ctx the parse tree
	 */
	void enterFuncReturnStmt(PMLParser.FuncReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#funcReturnStmt}.
	 * @param ctx the parse tree
	 */
	void exitFuncReturnStmt(PMLParser.FuncReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void enterVarReturnType(PMLParser.VarReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void exitVarReturnType(PMLParser.VarReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VoidReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void enterVoidReturnType(PMLParser.VoidReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VoidReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void exitVoidReturnType(PMLParser.VoidReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#funcBody}.
	 * @param ctx the parse tree
	 */
	void enterFuncBody(PMLParser.FuncBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#funcBody}.
	 * @param ctx the parse tree
	 */
	void exitFuncBody(PMLParser.FuncBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#foreachStmt}.
	 * @param ctx the parse tree
	 */
	void enterForeachStmt(PMLParser.ForeachStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#foreachStmt}.
	 * @param ctx the parse tree
	 */
	void exitForeachStmt(PMLParser.ForeachStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#forRangeStmt}.
	 * @param ctx the parse tree
	 */
	void enterForRangeStmt(PMLParser.ForRangeStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#forRangeStmt}.
	 * @param ctx the parse tree
	 */
	void exitForRangeStmt(PMLParser.ForRangeStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(PMLParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#breakStmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(PMLParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#continueStmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(PMLParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#continueStmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(PMLParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#funcCallStmt}.
	 * @param ctx the parse tree
	 */
	void enterFuncCallStmt(PMLParser.FuncCallStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#funcCallStmt}.
	 * @param ctx the parse tree
	 */
	void exitFuncCallStmt(PMLParser.FuncCallStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(PMLParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(PMLParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#elseIfStmt}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStmt(PMLParser.ElseIfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#elseIfStmt}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStmt(PMLParser.ElseIfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#elseStmt}.
	 * @param ctx the parse tree
	 */
	void enterElseStmt(PMLParser.ElseStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#elseStmt}.
	 * @param ctx the parse tree
	 */
	void exitElseStmt(PMLParser.ElseStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterStringType(PMLParser.StringTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitStringType(PMLParser.StringTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterBooleanType(PMLParser.BooleanTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitBooleanType(PMLParser.BooleanTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterArrayVarType(PMLParser.ArrayVarTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitArrayVarType(PMLParser.ArrayVarTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterMapVarType(PMLParser.MapVarTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitMapVarType(PMLParser.MapVarTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void enterAnyType(PMLParser.AnyTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PMLParser#varType}.
	 * @param ctx the parse tree
	 */
	void exitAnyType(PMLParser.AnyTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#mapType}.
	 * @param ctx the parse tree
	 */
	void enterMapType(PMLParser.MapTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#mapType}.
	 * @param ctx the parse tree
	 */
	void exitMapType(PMLParser.MapTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(PMLParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(PMLParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#stmtBlock}.
	 * @param ctx the parse tree
	 */
	void enterStmtBlock(PMLParser.StmtBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#stmtBlock}.
	 * @param ctx the parse tree
	 */
	void exitStmtBlock(PMLParser.StmtBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteNode}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void enterDeleteNode(PMLParser.DeleteNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteNode}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void exitDeleteNode(PMLParser.DeleteNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteObligation}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void enterDeleteObligation(PMLParser.DeleteObligationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteObligation}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void exitDeleteObligation(PMLParser.DeleteObligationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteProhibition}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void enterDeleteProhibition(PMLParser.DeleteProhibitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteProhibition}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void exitDeleteProhibition(PMLParser.DeleteProhibitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#nodeType}.
	 * @param ctx the parse tree
	 */
	void enterNodeType(PMLParser.NodeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#nodeType}.
	 * @param ctx the parse tree
	 */
	void exitNodeType(PMLParser.NodeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createPolicyStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreatePolicyStmt(PMLParser.CreatePolicyStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createPolicyStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreatePolicyStmt(PMLParser.CreatePolicyStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createAttrStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateAttrStmt(PMLParser.CreateAttrStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createAttrStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateAttrStmt(PMLParser.CreateAttrStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createUserOrObjectStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserOrObjectStmt(PMLParser.CreateUserOrObjectStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createUserOrObjectStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserOrObjectStmt(PMLParser.CreateUserOrObjectStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#setNodePropsStmt}.
	 * @param ctx the parse tree
	 */
	void enterSetNodePropsStmt(PMLParser.SetNodePropsStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#setNodePropsStmt}.
	 * @param ctx the parse tree
	 */
	void exitSetNodePropsStmt(PMLParser.SetNodePropsStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignStmt(PMLParser.AssignStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignStmt(PMLParser.AssignStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#deassignStmt}.
	 * @param ctx the parse tree
	 */
	void enterDeassignStmt(PMLParser.DeassignStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#deassignStmt}.
	 * @param ctx the parse tree
	 */
	void exitDeassignStmt(PMLParser.DeassignStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#associateStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssociateStmt(PMLParser.AssociateStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#associateStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssociateStmt(PMLParser.AssociateStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#dissociateStmt}.
	 * @param ctx the parse tree
	 */
	void enterDissociateStmt(PMLParser.DissociateStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#dissociateStmt}.
	 * @param ctx the parse tree
	 */
	void exitDissociateStmt(PMLParser.DissociateStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#deleteStmt}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStmt(PMLParser.DeleteStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#deleteStmt}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStmt(PMLParser.DeleteStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createObligationStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateObligationStmt(PMLParser.CreateObligationStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createObligationStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateObligationStmt(PMLParser.CreateObligationStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createRuleStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateRuleStmt(PMLParser.CreateRuleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createRuleStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateRuleStmt(PMLParser.CreateRuleStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyUserSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyUserSubject(PMLParser.AnyUserSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyUserSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyUserSubject(PMLParser.AnyUserSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUserSubject(PMLParser.UserSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUserSubject(PMLParser.UserSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UsersListSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUsersListSubject(PMLParser.UsersListSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UsersListSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUsersListSubject(PMLParser.UsersListSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserAttrSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUserAttrSubject(PMLParser.UserAttrSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserAttrSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUserAttrSubject(PMLParser.UserAttrSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterProcessSubject(PMLParser.ProcessSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitProcessSubject(PMLParser.ProcessSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterPolicyElement(PMLParser.PolicyElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitPolicyElement(PMLParser.PolicyElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyPolicyElement(PMLParser.AnyPolicyElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyPolicyElement(PMLParser.AnyPolicyElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyContainedIn}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyContainedIn(PMLParser.AnyContainedInContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyContainedIn}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyContainedIn(PMLParser.AnyContainedInContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyOfSet}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyOfSet(PMLParser.AnyOfSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyOfSet}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyOfSet(PMLParser.AnyOfSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#anyPe}.
	 * @param ctx the parse tree
	 */
	void enterAnyPe(PMLParser.AnyPeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#anyPe}.
	 * @param ctx the parse tree
	 */
	void exitAnyPe(PMLParser.AnyPeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#response}.
	 * @param ctx the parse tree
	 */
	void enterResponse(PMLParser.ResponseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#response}.
	 * @param ctx the parse tree
	 */
	void exitResponse(PMLParser.ResponseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#responseBlock}.
	 * @param ctx the parse tree
	 */
	void enterResponseBlock(PMLParser.ResponseBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#responseBlock}.
	 * @param ctx the parse tree
	 */
	void exitResponseBlock(PMLParser.ResponseBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#responseStmt}.
	 * @param ctx the parse tree
	 */
	void enterResponseStmt(PMLParser.ResponseStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#responseStmt}.
	 * @param ctx the parse tree
	 */
	void exitResponseStmt(PMLParser.ResponseStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#deleteRuleStmt}.
	 * @param ctx the parse tree
	 */
	void enterDeleteRuleStmt(PMLParser.DeleteRuleStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#deleteRuleStmt}.
	 * @param ctx the parse tree
	 */
	void exitDeleteRuleStmt(PMLParser.DeleteRuleStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createProhibitionStmt}.
	 * @param ctx the parse tree
	 */
	void enterCreateProhibitionStmt(PMLParser.CreateProhibitionStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createProhibitionStmt}.
	 * @param ctx the parse tree
	 */
	void exitCreateProhibitionStmt(PMLParser.CreateProhibitionStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#prohibitionContainerList}.
	 * @param ctx the parse tree
	 */
	void enterProhibitionContainerList(PMLParser.ProhibitionContainerListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#prohibitionContainerList}.
	 * @param ctx the parse tree
	 */
	void exitProhibitionContainerList(PMLParser.ProhibitionContainerListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#prohibitionContainerExpression}.
	 * @param ctx the parse tree
	 */
	void enterProhibitionContainerExpression(PMLParser.ProhibitionContainerExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#prohibitionContainerExpression}.
	 * @param ctx the parse tree
	 */
	void exitProhibitionContainerExpression(PMLParser.ProhibitionContainerExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#setResourceAccessRightsStmt}.
	 * @param ctx the parse tree
	 */
	void enterSetResourceAccessRightsStmt(PMLParser.SetResourceAccessRightsStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#setResourceAccessRightsStmt}.
	 * @param ctx the parse tree
	 */
	void exitSetResourceAccessRightsStmt(PMLParser.SetResourceAccessRightsStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PMLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PMLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(PMLParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(PMLParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#map}.
	 * @param ctx the parse tree
	 */
	void enterMap(PMLParser.MapContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#map}.
	 * @param ctx the parse tree
	 */
	void exitMap(PMLParser.MapContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#mapEntry}.
	 * @param ctx the parse tree
	 */
	void enterMapEntry(PMLParser.MapEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#mapEntry}.
	 * @param ctx the parse tree
	 */
	void exitMapEntry(PMLParser.MapEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#entryRef}.
	 * @param ctx the parse tree
	 */
	void enterEntryRef(PMLParser.EntryRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#entryRef}.
	 * @param ctx the parse tree
	 */
	void exitEntryRef(PMLParser.EntryRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(PMLParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(PMLParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(PMLParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(PMLParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NumberLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNumberLiteral(PMLParser.NumberLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumberLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNumberLiteral(PMLParser.NumberLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterArrayLiteral(PMLParser.ArrayLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitArrayLiteral(PMLParser.ArrayLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MapLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterMapLiteral(PMLParser.MapLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MapLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitMapLiteral(PMLParser.MapLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReferenceByID}
	 * labeled alternative in {@link PMLParser#varRef}.
	 * @param ctx the parse tree
	 */
	void enterReferenceByID(PMLParser.ReferenceByIDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReferenceByID}
	 * labeled alternative in {@link PMLParser#varRef}.
	 * @param ctx the parse tree
	 */
	void exitReferenceByID(PMLParser.ReferenceByIDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EntryReference}
	 * labeled alternative in {@link PMLParser#varRef}.
	 * @param ctx the parse tree
	 */
	void enterEntryReference(PMLParser.EntryReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EntryReference}
	 * labeled alternative in {@link PMLParser#varRef}.
	 * @param ctx the parse tree
	 */
	void exitEntryReference(PMLParser.EntryReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void enterFuncCall(PMLParser.FuncCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#funcCall}.
	 * @param ctx the parse tree
	 */
	void exitFuncCall(PMLParser.FuncCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#funcCallArgs}.
	 * @param ctx the parse tree
	 */
	void enterFuncCallArgs(PMLParser.FuncCallArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#funcCallArgs}.
	 * @param ctx the parse tree
	 */
	void exitFuncCallArgs(PMLParser.FuncCallArgsContext ctx);
}