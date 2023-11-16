// Generated from PMLParser.g4 by ANTLR 4.8
package gov.nist.csd.pm.policy.pml.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PMLParser}.
 */
public interface PMLParserListener extends ParseTreeListener {
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
	 * Enter a parse tree produced by {@link PMLParser#hierarchy}.
	 * @param ctx the parse tree
	 */
	void enterHierarchy(PMLParser.HierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#hierarchy}.
	 * @param ctx the parse tree
	 */
	void exitHierarchy(PMLParser.HierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#userAttrsHierarchy}.
	 * @param ctx the parse tree
	 */
	void enterUserAttrsHierarchy(PMLParser.UserAttrsHierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#userAttrsHierarchy}.
	 * @param ctx the parse tree
	 */
	void exitUserAttrsHierarchy(PMLParser.UserAttrsHierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#objectAttrsHierarchy}.
	 * @param ctx the parse tree
	 */
	void enterObjectAttrsHierarchy(PMLParser.ObjectAttrsHierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#objectAttrsHierarchy}.
	 * @param ctx the parse tree
	 */
	void exitObjectAttrsHierarchy(PMLParser.ObjectAttrsHierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#associationsHierarchy}.
	 * @param ctx the parse tree
	 */
	void enterAssociationsHierarchy(PMLParser.AssociationsHierarchyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#associationsHierarchy}.
	 * @param ctx the parse tree
	 */
	void exitAssociationsHierarchy(PMLParser.AssociationsHierarchyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#hierarchyBlock}.
	 * @param ctx the parse tree
	 */
	void enterHierarchyBlock(PMLParser.HierarchyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#hierarchyBlock}.
	 * @param ctx the parse tree
	 */
	void exitHierarchyBlock(PMLParser.HierarchyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#associationsHierarchyBlock}.
	 * @param ctx the parse tree
	 */
	void enterAssociationsHierarchyBlock(PMLParser.AssociationsHierarchyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#associationsHierarchyBlock}.
	 * @param ctx the parse tree
	 */
	void exitAssociationsHierarchyBlock(PMLParser.AssociationsHierarchyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#hierarchyStatement}.
	 * @param ctx the parse tree
	 */
	void enterHierarchyStatement(PMLParser.HierarchyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#hierarchyStatement}.
	 * @param ctx the parse tree
	 */
	void exitHierarchyStatement(PMLParser.HierarchyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#associationsHierarchyStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssociationsHierarchyStatement(PMLParser.AssociationsHierarchyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#associationsHierarchyStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssociationsHierarchyStatement(PMLParser.AssociationsHierarchyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#createNonPCStatement}.
	 * @param ctx the parse tree
	 */
	void enterCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#createNonPCStatement}.
	 * @param ctx the parse tree
	 */
	void exitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#nonPCNodeType}.
	 * @param ctx the parse tree
	 */
	void enterNonPCNodeType(PMLParser.NonPCNodeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#nonPCNodeType}.
	 * @param ctx the parse tree
	 */
	void exitNonPCNodeType(PMLParser.NonPCNodeTypeContext ctx);
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
	 * Enter a parse tree produced by the {@code UsersSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUsersSubject(PMLParser.UsersSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UsersSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUsersSubject(PMLParser.UsersSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UsersInUnionSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUsersInUnionSubject(PMLParser.UsersInUnionSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UsersInUnionSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUsersInUnionSubject(PMLParser.UsersInUnionSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UsersInIntersectionSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterUsersInIntersectionSubject(PMLParser.UsersInIntersectionSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UsersInIntersectionSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitUsersInIntersectionSubject(PMLParser.UsersInIntersectionSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ProcessesSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void enterProcessesSubject(PMLParser.ProcessesSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ProcessesSubject}
	 * labeled alternative in {@link PMLParser#subjectClause}.
	 * @param ctx the parse tree
	 */
	void exitProcessesSubject(PMLParser.ProcessesSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyTarget(PMLParser.AnyTargetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyTarget(PMLParser.AnyTargetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyInUnionTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyInUnionTarget(PMLParser.AnyInUnionTargetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyInUnionTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyInUnionTarget(PMLParser.AnyInUnionTargetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyInIntersectionTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterAnyInIntersectionTarget(PMLParser.AnyInIntersectionTargetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyInIntersectionTarget}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitAnyInIntersectionTarget(PMLParser.AnyInIntersectionTargetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OnTargets}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void enterOnTargets(PMLParser.OnTargetsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OnTargets}
	 * labeled alternative in {@link PMLParser#onClause}.
	 * @param ctx the parse tree
	 */
	void exitOnTargets(PMLParser.OnTargetsContext ctx);
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
	 * Enter a parse tree produced by the {@code DeleteFunction}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void enterDeleteFunction(PMLParser.DeleteFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteFunction}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void exitDeleteFunction(PMLParser.DeleteFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteConst}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void enterDeleteConst(PMLParser.DeleteConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteConst}
	 * labeled alternative in {@link PMLParser#deleteType}.
	 * @param ctx the parse tree
	 */
	void exitDeleteConst(PMLParser.DeleteConstContext ctx);
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
	 * Enter a parse tree produced by the {@code ConstDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterConstDeclaration(PMLParser.ConstDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitConstDeclaration(PMLParser.ConstDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(PMLParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(PMLParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ShortDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterShortDeclaration(PMLParser.ShortDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ShortDeclaration}
	 * labeled alternative in {@link PMLParser#variableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitShortDeclaration(PMLParser.ShortDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#constSpec}.
	 * @param ctx the parse tree
	 */
	void enterConstSpec(PMLParser.ConstSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#constSpec}.
	 * @param ctx the parse tree
	 */
	void exitConstSpec(PMLParser.ConstSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#varSpec}.
	 * @param ctx the parse tree
	 */
	void enterVarSpec(PMLParser.VarSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#varSpec}.
	 * @param ctx the parse tree
	 */
	void exitVarSpec(PMLParser.VarSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#variableAssignmentStatement}.
	 * @param ctx the parse tree
	 */
	void enterVariableAssignmentStatement(PMLParser.VariableAssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#variableAssignmentStatement}.
	 * @param ctx the parse tree
	 */
	void exitVariableAssignmentStatement(PMLParser.VariableAssignmentStatementContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#functionSignature}.
	 * @param ctx the parse tree
	 */
	void enterFunctionSignature(PMLParser.FunctionSignatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#functionSignature}.
	 * @param ctx the parse tree
	 */
	void exitFunctionSignature(PMLParser.FunctionSignatureContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(PMLParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(PMLParser.ReturnStatementContext ctx);
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
	 * Enter a parse tree produced by the {@code NegateExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNegateExpression(PMLParser.NegateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NegateExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNegateExpression(PMLParser.NegateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(PMLParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(PMLParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PlusExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPlusExpression(PMLParser.PlusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PlusExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPlusExpression(PMLParser.PlusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionInvokeExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionInvokeExpression(PMLParser.FunctionInvokeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionInvokeExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionInvokeExpression(PMLParser.FunctionInvokeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VariableReferenceExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVariableReferenceExpression(PMLParser.VariableReferenceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VariableReferenceExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVariableReferenceExpression(PMLParser.VariableReferenceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpression(PMLParser.LiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpression(PMLParser.LiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(PMLParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(PMLParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqualsExpression(PMLParser.EqualsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualsExpression}
	 * labeled alternative in {@link PMLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqualsExpression(PMLParser.EqualsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(PMLParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(PMLParser.ExpressionListContext ctx);
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
	 * Enter a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(PMLParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link PMLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(PMLParser.BoolLiteralContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#stringLit}.
	 * @param ctx the parse tree
	 */
	void enterStringLit(PMLParser.StringLitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#stringLit}.
	 * @param ctx the parse tree
	 */
	void exitStringLit(PMLParser.StringLitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#boolLit}.
	 * @param ctx the parse tree
	 */
	void enterBoolLit(PMLParser.BoolLitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#boolLit}.
	 * @param ctx the parse tree
	 */
	void exitBoolLit(PMLParser.BoolLitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#arrayLit}.
	 * @param ctx the parse tree
	 */
	void enterArrayLit(PMLParser.ArrayLitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#arrayLit}.
	 * @param ctx the parse tree
	 */
	void exitArrayLit(PMLParser.ArrayLitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#mapLit}.
	 * @param ctx the parse tree
	 */
	void enterMapLit(PMLParser.MapLitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#mapLit}.
	 * @param ctx the parse tree
	 */
	void exitMapLit(PMLParser.MapLitContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(PMLParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(PMLParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReferenceByIndex}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void enterReferenceByIndex(PMLParser.ReferenceByIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReferenceByIndex}
	 * labeled alternative in {@link PMLParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void exitReferenceByIndex(PMLParser.ReferenceByIndexContext ctx);
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
	 * Enter a parse tree produced by the {@code BracketIndex}
	 * labeled alternative in {@link PMLParser#index}.
	 * @param ctx the parse tree
	 */
	void enterBracketIndex(PMLParser.BracketIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BracketIndex}
	 * labeled alternative in {@link PMLParser#index}.
	 * @param ctx the parse tree
	 */
	void exitBracketIndex(PMLParser.BracketIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DotIndex}
	 * labeled alternative in {@link PMLParser#index}.
	 * @param ctx the parse tree
	 */
	void enterDotIndex(PMLParser.DotIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DotIndex}
	 * labeled alternative in {@link PMLParser#index}.
	 * @param ctx the parse tree
	 */
	void exitDotIndex(PMLParser.DotIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(PMLParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(PMLParser.IdContext ctx);
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