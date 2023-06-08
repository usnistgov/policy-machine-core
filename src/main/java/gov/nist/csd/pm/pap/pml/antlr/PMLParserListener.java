// Generated from PMLParser.g4 by ANTLR 4.13.1
package gov.nist.csd.pm.pap.pml.antlr;
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
	 * Enter a parse tree produced by the {@code AnyUserPattern}
	 * labeled alternative in {@link PMLParser#subjectPattern}.
	 * @param ctx the parse tree
	 */
	void enterAnyUserPattern(PMLParser.AnyUserPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyUserPattern}
	 * labeled alternative in {@link PMLParser#subjectPattern}.
	 * @param ctx the parse tree
	 */
	void exitAnyUserPattern(PMLParser.AnyUserPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UserPattern}
	 * labeled alternative in {@link PMLParser#subjectPattern}.
	 * @param ctx the parse tree
	 */
	void enterUserPattern(PMLParser.UserPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UserPattern}
	 * labeled alternative in {@link PMLParser#subjectPattern}.
	 * @param ctx the parse tree
	 */
	void exitUserPattern(PMLParser.UserPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BasicSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 */
	void enterBasicSubjectPatternExpression(PMLParser.BasicSubjectPatternExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BasicSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 */
	void exitBasicSubjectPatternExpression(PMLParser.BasicSubjectPatternExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenSubjectPatternExpression(PMLParser.ParenSubjectPatternExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenSubjectPatternExpression(PMLParser.ParenSubjectPatternExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NegateSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 */
	void enterNegateSubjectPatternExpression(PMLParser.NegateSubjectPatternExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NegateSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 */
	void exitNegateSubjectPatternExpression(PMLParser.NegateSubjectPatternExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalSubjectPatternExpression(PMLParser.LogicalSubjectPatternExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalSubjectPatternExpression}
	 * labeled alternative in {@link PMLParser#subjectPatternExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalSubjectPatternExpression(PMLParser.LogicalSubjectPatternExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 */
	void enterInSubject(PMLParser.InSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 */
	void exitInSubject(PMLParser.InSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UsernameSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 */
	void enterUsernameSubject(PMLParser.UsernameSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UsernameSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 */
	void exitUsernameSubject(PMLParser.UsernameSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 */
	void enterProcessSubject(PMLParser.ProcessSubjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ProcessSubject}
	 * labeled alternative in {@link PMLParser#basicSubjectPatternExpr}.
	 * @param ctx the parse tree
	 */
	void exitProcessSubject(PMLParser.ProcessSubjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyOperation}
	 * labeled alternative in {@link PMLParser#operationPattern}.
	 * @param ctx the parse tree
	 */
	void enterAnyOperation(PMLParser.AnyOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyOperation}
	 * labeled alternative in {@link PMLParser#operationPattern}.
	 * @param ctx the parse tree
	 */
	void exitAnyOperation(PMLParser.AnyOperationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IDOperation}
	 * labeled alternative in {@link PMLParser#operationPattern}.
	 * @param ctx the parse tree
	 */
	void enterIDOperation(PMLParser.IDOperationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IDOperation}
	 * labeled alternative in {@link PMLParser#operationPattern}.
	 * @param ctx the parse tree
	 */
	void exitIDOperation(PMLParser.IDOperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#operandPattern}.
	 * @param ctx the parse tree
	 */
	void enterOperandPattern(PMLParser.OperandPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#operandPattern}.
	 * @param ctx the parse tree
	 */
	void exitOperandPattern(PMLParser.OperandPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#operandPatternElement}.
	 * @param ctx the parse tree
	 */
	void enterOperandPatternElement(PMLParser.OperandPatternElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#operandPatternElement}.
	 * @param ctx the parse tree
	 */
	void exitOperandPatternElement(PMLParser.OperandPatternElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#operandPatternExpressionArray}.
	 * @param ctx the parse tree
	 */
	void enterOperandPatternExpressionArray(PMLParser.OperandPatternExpressionArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#operandPatternExpressionArray}.
	 * @param ctx the parse tree
	 */
	void exitOperandPatternExpressionArray(PMLParser.OperandPatternExpressionArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenOperandPatternExpression(PMLParser.ParenOperandPatternExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenOperandPatternExpression(PMLParser.ParenOperandPatternExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NegateOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 */
	void enterNegateOperandPatternExpression(PMLParser.NegateOperandPatternExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NegateOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 */
	void exitNegateOperandPatternExpression(PMLParser.NegateOperandPatternExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BasicOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 */
	void enterBasicOperandPatternExpression(PMLParser.BasicOperandPatternExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BasicOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 */
	void exitBasicOperandPatternExpression(PMLParser.BasicOperandPatternExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperandPatternExpression(PMLParser.LogicalOperandPatternExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalOperandPatternExpression}
	 * labeled alternative in {@link PMLParser#operandPatternExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperandPatternExpression(PMLParser.LogicalOperandPatternExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 */
	void enterAnyPolicyElement(PMLParser.AnyPolicyElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnyPolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 */
	void exitAnyPolicyElement(PMLParser.AnyPolicyElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InPolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 */
	void enterInPolicyElement(PMLParser.InPolicyElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InPolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 */
	void exitInPolicyElement(PMLParser.InPolicyElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 */
	void enterPolicyElement(PMLParser.PolicyElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PolicyElement}
	 * labeled alternative in {@link PMLParser#basicOperandPatternExpr}.
	 * @param ctx the parse tree
	 */
	void exitPolicyElement(PMLParser.PolicyElementContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#setResourceOperationsStatement}.
	 * @param ctx the parse tree
	 */
	void enterSetResourceOperationsStatement(PMLParser.SetResourceOperationsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#setResourceOperationsStatement}.
	 * @param ctx the parse tree
	 */
	void exitSetResourceOperationsStatement(PMLParser.SetResourceOperationsStatementContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#checkStatement}.
	 * @param ctx the parse tree
	 */
	void enterCheckStatement(PMLParser.CheckStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#checkStatement}.
	 * @param ctx the parse tree
	 */
	void exitCheckStatement(PMLParser.CheckStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#checkStatementBlock}.
	 * @param ctx the parse tree
	 */
	void enterCheckStatementBlock(PMLParser.CheckStatementBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#checkStatementBlock}.
	 * @param ctx the parse tree
	 */
	void exitCheckStatementBlock(PMLParser.CheckStatementBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PMLParser#idArr}.
	 * @param ctx the parse tree
	 */
	void enterIdArr(PMLParser.IdArrContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#idArr}.
	 * @param ctx the parse tree
	 */
	void exitIdArr(PMLParser.IdArrContext ctx);
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
	 * Enter a parse tree produced by {@link PMLParser#stringArrayLit}.
	 * @param ctx the parse tree
	 */
	void enterStringArrayLit(PMLParser.StringArrayLitContext ctx);
	/**
	 * Exit a parse tree produced by {@link PMLParser#stringArrayLit}.
	 * @param ctx the parse tree
	 */
	void exitStringArrayLit(PMLParser.StringArrayLitContext ctx);
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