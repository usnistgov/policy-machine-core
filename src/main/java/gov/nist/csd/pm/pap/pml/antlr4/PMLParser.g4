parser grammar PMLParser;

@header {
package gov.nist.csd.pm.pap.pml.antlr;
}

options {
	tokenVocab = PMLLexer;
}

pml: (statement)* EOF ;

statement:
    basicStatement
    | operationStatement ;

basicStatement: (
    variableAssignmentStatement
    | variableDeclarationStatement
    | foreachStatement
    | returnStatement
    | breakStatement
    | continueStatement
    | functionInvokeStatement
    | ifStatement
    | basicFunctionDefinitionStatement
) ;

operationStatement: (
    createPolicyStatement
    | createNonPCStatement
    | createObligationStatement
    | createProhibitionStatement
    | setNodePropertiesStatement
    | assignStatement
    | deassignStatement
    | associateStatement
    | dissociateStatement
    | setResourceOperationsStatement
    | deleteStatement
    | deleteRuleStatement
    | operationDefinitionStatement
    | routineDefinitionStatement
) ;

statementBlock: OPEN_CURLY statement* CLOSE_CURLY ;

createPolicyStatement: CREATE POLICY_CLASS name=expression;

createNonPCStatement:
    CREATE nonPCNodeType name=expression
    IN in=expression ;
nonPCNodeType:
    (OBJECT_ATTRIBUTE | USER_ATTRIBUTE | OBJECT | USER) ;

createObligationStatement:
    CREATE OBLIGATION expression OPEN_CURLY createRuleStatement* CLOSE_CURLY;
createRuleStatement:
    CREATE RULE ruleName=expression
    WHEN subjectPattern
    PERFORMS operationPattern
    (ON argPattern)?
    response ;

// subject
subjectPattern:
    ANY USER #AnyUserPattern
    | USER subjectPatternExpression #UserPattern;

subjectPatternExpression:
    basicSubjectPatternExpr #BasicSubjectPatternExpression
    | EXCLAMATION subjectPatternExpression #NegateSubjectPatternExpression
    | OPEN_PAREN subjectPatternExpression CLOSE_PAREN #ParenSubjectPatternExpression
    | left=subjectPatternExpression (LOGICAL_AND | LOGICAL_OR) right=subjectPatternExpression #LogicalSubjectPatternExpression ;

basicSubjectPatternExpr:
    IN stringLit #InSubject
    | stringLit #UsernameSubject
    | PROCESS stringLit #ProcessSubject ;

// operation
operationPattern:
    ANY OPERATION #AnyOperation
    | stringLit #IDOperation ;

// args
argPattern: OPEN_CURLY (argPatternElement (COMMA argPatternElement)*)? CLOSE_CURLY ;
argPatternElement: key=ID COLON (single=argPatternExpression | multiple=argPatternExpressionArray);

argPatternExpressionArray: OPEN_BRACKET argPatternExpression (COMMA argPatternExpression)* CLOSE_BRACKET ;

argPatternExpression:
     basicArgPatternExpr #BasicArgPatternExpression
     | EXCLAMATION argPatternExpression #NegateArgPatternExpression
     | OPEN_PAREN argPatternExpression CLOSE_PAREN #ParenArgPatternExpression
     | left=argPatternExpression (LOGICAL_AND | LOGICAL_OR) right=argPatternExpression #LogicalArgPatternExpression ;

basicArgPatternExpr:
    ANY #AnyPolicyElement
    | IN stringLit #InPolicyElement
    | stringLit #PolicyElement ;

// response
response:
    DO OPEN_PAREN ID CLOSE_PAREN responseBlock;
responseBlock:
    OPEN_CURLY responseStatement* CLOSE_CURLY ;
responseStatement:
    statement
    | createRuleStatement
    | deleteRuleStatement ;

createProhibitionStatement:
    CREATE PROHIBITION name=expression
    DENY (USER | USER_ATTRIBUTE | PROCESS) subject=expression
    ACCESS_RIGHTS accessRights=expression
    ON (INTERSECTION|UNION) OF containers=expression ;

/*
uncomment for operaiton prohibitions
createProhibitionStatement:
    CREATE PROHIBITION name=expression
    DENY subject=expression
    (ACCESS_RIGHTS accessRights=expression ON containers=expression) #Arset
    | (OPERATION op=expression ON argPatterns=patternMap) #Operation ;*/

setNodePropertiesStatement:
    SET_PROPERTIES OF name=expression TO properties=expression ;

assignStatement:
    ASSIGN ascendantNode=expression TO descendantNodes=expression ;

deassignStatement:
    DEASSIGN ascendantNode=expression FROM descendantNodes=expression ;

associateStatement:
    ASSOCIATE ua=expression AND target=expression WITH accessRights=expression ;

dissociateStatement:
    DISSOCIATE ua=expression AND target=expression ;

setResourceOperationsStatement:
    SET_RESOURCE_OPERATIONS accessRightsArr=expression;

deleteStatement:
    DELETE deleteType expression ;
deleteType:
    NODE #DeleteNode
    | OBLIGATION #DeleteObligation
    | PROHIBITION #DeleteProhibition ;

deleteRuleStatement:
    DELETE RULE ruleName=expression FROM OBLIGATION obligationName=expression ;

variableDeclarationStatement:
    VAR (varSpec | OPEN_PAREN (varSpec)* CLOSE_PAREN) #VarDeclaration
    | ID DECLARE_ASSIGN expression #ShortDeclaration;
varSpec: ID ASSIGN_EQUALS expression;

variableAssignmentStatement: ID PLUS? ASSIGN_EQUALS expression;

operationDefinitionStatement: operationSignature checkStatementBlock? statementBlock ;
routineDefinitionStatement: routineSignature checkStatementBlock? statementBlock ;
basicFunctionDefinitionStatement: basicFunctionSignature basicStatementBlock ;

operationSignature: OPERATION ID OPEN_PAREN formalParamList CLOSE_PAREN returnType=variableType? ;
routineSignature: ROUTINE ID OPEN_PAREN formalParamList CLOSE_PAREN returnType=variableType? ;
basicFunctionSignature: FUNCTION ID OPEN_PAREN formalParamList CLOSE_PAREN returnType=variableType? ;

formalParamList: (formalParam (COMMA formalParam)*)? ;
formalParam: NODE_PARAM? variableType ID;

returnStatement: RETURN expression?;

checkStatement: CHECK ar=expression ON target=expression ;
checkStatementBlock: OPEN_CURLY checkStatement* CLOSE_CURLY ;

basicStatementBlock: OPEN_CURLY basicStatement* CLOSE_CURLY ;

idArr: OPEN_BRACKET (ID (COMMA ID)*)? CLOSE_BRACKET ;
functionInvokeStatement: functionInvoke;

foreachStatement: FOREACH key=ID (COMMA value=ID)? IN expression statementBlock ;
breakStatement: BREAK ;
continueStatement: CONTINUE ;

ifStatement:
    IF condition=expression statementBlock
    elseIfStatement*
    elseStatement? ;
elseIfStatement:
    ELSE IF condition=expression statementBlock ;
elseStatement:
    ELSE statementBlock ;

variableType:
    STRING_TYPE #StringType
    | BOOL_TYPE #BooleanType
    | arrayType #ArrayVarType
    | mapType #MapVarType
    | ANY #AnyType ;
mapType: MAP OPEN_BRACKET keyType=variableType CLOSE_BRACKET valueType=variableType ;
arrayType: OPEN_BRACKET CLOSE_BRACKET variableType ;

expression:
    functionInvoke #FunctionInvokeExpression
    | variableReference #VariableReferenceExpression
    | literal #LiteralExpression
    | EXCLAMATION expression #NegateExpression
    | OPEN_PAREN expression CLOSE_PAREN #ParenExpression
	| left=expression PLUS right=expression #PlusExpression
    | left=expression (EQUALS | NOT_EQUALS) right=expression #EqualsExpression
    | left=expression (LOGICAL_AND | LOGICAL_OR) right=expression #LogicalExpression ;
expressionList: expression (COMMA expression)* ;

literal:
    stringLit #StringLiteral
    | boolLit #BoolLiteral
    | arrayLit #ArrayLiteral
    | mapLit #MapLiteral;
stringLit: DOUBLE_QUOTE_STRING;
boolLit: TRUE | FALSE;
arrayLit: OPEN_BRACKET expressionList? CLOSE_BRACKET ;
stringArrayLit: OPEN_BRACKET (stringLit (COMMA stringLit)*)? CLOSE_BRACKET ;
mapLit: OPEN_CURLY (element (COMMA element)*)? CLOSE_CURLY ;
element: key=expression COLON value=expression ;

variableReference: ID (index)* ;

index:
    OPEN_BRACKET key=expression CLOSE_BRACKET #BracketIndex
    | DOT key=id #DotIndex;
id: ID;

functionInvoke: ID functionInvokeArgs ;
functionInvokeArgs: OPEN_PAREN expressionList? CLOSE_PAREN ;
