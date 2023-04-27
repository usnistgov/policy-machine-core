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
	 * Enter a parse tree produced by {@link PMLParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(PMLParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(PMLParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createPolicyStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createPolicyStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createAttributeStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateAttributeStatement(PMLParser.CreateAttributeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createAttributeStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateAttributeStatement(PMLParser.CreateAttributeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createUserOrObjectStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateUserOrObjectStatement(PMLParser.CreateUserOrObjectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createUserOrObjectStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateUserOrObjectStatement(PMLParser.CreateUserOrObjectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createObligationStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createObligationStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createRuleStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateRuleStatement(PMLParser.CreateRuleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createRuleStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateRuleStatement(PMLParser.CreateRuleStatementContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#responseStatement}.
	 * @param ctx the parse tree
	 */
	void enterResponseStatement(PMLParser.ResponseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#responseStatement}.
	 * @param ctx the parse tree
	 */
	void exitResponseStatement(PMLParser.ResponseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createProhibitionStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createProhibitionStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#setNodePropertiesStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#setNodePropertiesStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#assignStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignStatement(PMLParser.AssignStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#assignStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignStatement(PMLParser.AssignStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#deassignStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeassignStatement(PMLParser.DeassignStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#deassignStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeassignStatement(PMLParser.DeassignStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#associateStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssociateStatement(PMLParser.AssociateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#associateStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssociateStatement(PMLParser.AssociateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#dissociateStatement}.
	 * @param ctx the parse tree
	 */
	void enterDissociateStatement(PMLParser.DissociateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#dissociateStatement}.
	 * @param ctx the parse tree
	 */
	void exitDissociateStatement(PMLParser.DissociateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#setResourceAccessRightsStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetResourceAccessRightsStatement(PMLParser.SetResourceAccessRightsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#setResourceAccessRightsStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetResourceAccessRightsStatement(PMLParser.SetResourceAccessRightsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement(PMLParser.DeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement(PMLParser.DeleteStatementContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#deleteRuleStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#deleteRuleStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteRuleStatement(PMLParser.DeleteRuleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarationStatement(PMLParser.VariableDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarationStatement(PMLParser.VariableDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#functionDefinitionStatement}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#functionDefinitionStatement}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#functionReturnStatement}.
	 * @param ctx the parse tree
	 */
	void enterFunctionReturnStatement(PMLParser.FunctionReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#functionReturnStatement}.
	 * @param ctx the parse tree
	 */
	void exitFunctionReturnStatement(PMLParser.FunctionReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void enterVariableReturnType(PMLParser.VariableReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableReturnType}
	 * labeled alternative in {@link PMLParser#funcReturnType}.
	 * @param ctx the parse tree
	 */
	void exitVariableReturnType(PMLParser.VariableReturnTypeContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#foreachStatement}.
	 * @param ctx the parse tree
	 */
	void enterForeachStatement(PMLParser.ForeachStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#foreachStatement}.
	 * @param ctx the parse tree
	 */
	void exitForeachStatement(PMLParser.ForeachStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#forRangeStatement}.
	 * @param ctx the parse tree
	 */
	void enterForRangeStatement(PMLParser.ForRangeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#forRangeStatement}.
	 * @param ctx the parse tree
	 */
	void exitForRangeStatement(PMLParser.ForRangeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(PMLParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(PMLParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(PMLParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(PMLParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#functionInvokeStatement}.
	 * @param ctx the parse tree
	 */
	void enterFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#functionInvokeStatement}.
	 * @param ctx the parse tree
	 */
	void exitFunctionInvokeStatement(PMLParser.FunctionInvokeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(PMLParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(PMLParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#elseIfStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatement(PMLParser.ElseIfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#elseIfStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatement(PMLParser.ElseIfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseStatement(PMLParser.ElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseStatement(PMLParser.ElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void enterStringType(PMLParser.StringTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void exitStringType(PMLParser.StringTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void enterBooleanType(PMLParser.BooleanTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void exitBooleanType(PMLParser.BooleanTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void enterArrayVarType(PMLParser.ArrayVarTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayVarType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void exitArrayVarType(PMLParser.ArrayVarTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void enterMapVarType(PMLParser.MapVarTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MapVarType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void exitMapVarType(PMLParser.MapVarTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PMLParser#variableType}.
	 * @param ctx the parse tree
	 */
	void enterAnyType(PMLParser.AnyTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyType}
	 * labeled alternative in {@link PMLParser#variableType}.
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
	 * Enter a parse tree produced by {@link PMLParser#statementBlock}.
	 * @param ctx the parse tree
	 */
	void enterStatementBlock(PMLParser.StatementBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#statementBlock}.
	 * @param ctx the parse tree
	 */
	void exitStatementBlock(PMLParser.StatementBlockContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#entryReference}.
	 * @param ctx the parse tree
	 */
	void enterEntryReference(PMLParser.EntryReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#entryReference}.
	 * @param ctx the parse tree
	 */
	void exitEntryReference(PMLParser.EntryReferenceContext ctx);
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
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void enterReferenceByID(PMLParser.ReferenceByIDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReferenceByID}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void exitReferenceByID(PMLParser.ReferenceByIDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReferenceByEntry}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void enterReferenceByEntry(PMLParser.ReferenceByEntryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReferenceByEntry}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void exitReferenceByEntry(PMLParser.ReferenceByEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#functionInvoke}.
	 * @param ctx the parse tree
	 */
	void enterFunctionInvoke(PMLParser.FunctionInvokeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#functionInvoke}.
	 * @param ctx the parse tree
	 */
	void exitFunctionInvoke(PMLParser.FunctionInvokeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#functionInvokeArgs}.
	 * @param ctx the parse tree
	 */
	void enterFunctionInvokeArgs(PMLParser.FunctionInvokeArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#functionInvokeArgs}.
	 * @param ctx the parse tree
	 */
	void exitFunctionInvokeArgs(PMLParser.FunctionInvokeArgsContext ctx);
}