parser grammar PMLParser;

@header {
package gov.nist.csd.pm.core.pap.pml.antlr;
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
    | setResourceAccessRightsStatement
    | deleteStatement
    | deleteRuleStatement
    | adminOpDefinitionStatement
    | resourceOpDefinitionStatement
    | routineDefinitionStatement
) ;

statementBlock: OPEN_CURLY statement* CLOSE_CURLY ;

createPolicyStatement: CREATE PC name=expression;

createNonPCStatement:
    CREATE nonPCNodeType name=expression
    IN in=expression ;
nonPCNodeType:
    (OA | UA | O | U) ;

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
    DENY (U | UA | PROCESS) subject=expression
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

setResourceAccessRightsStatement:
    SET_RESOURCE_ACCESS_RIGHTS accessRightsArr=expression;

deleteStatement:
    DELETE (IF_EXISTS)? deleteType expression ;
deleteType:
    NODE #DeleteNode
    | ADMIN_OP #DeleteAdminOp
    | RESOURCE_OP #DeleteResourceOp
    | OBLIGATION #DeleteObligation
    | PROHIBITION #DeleteProhibition ;

deleteRuleStatement:
    DELETE RULE ruleName=expression FROM OBLIGATION obligationName=expression ;

variableDeclarationStatement:
    VAR (varSpec | OPEN_PAREN (varSpec)* CLOSE_PAREN) #VarDeclaration
    | ID DECLARE_ASSIGN expression #ShortDeclaration;
varSpec: ID ASSIGN_EQUALS expression;

variableAssignmentStatement: ID PLUS? ASSIGN_EQUALS expression;

adminOpDefinitionStatement: adminOpSignature adminOpStatementBlock ;
resourceOpDefinitionStatement: resourceOpSignature resourceOpStatementBlock? ;
routineDefinitionStatement: routineSignature statementBlock ;
basicFunctionDefinitionStatement: basicFunctionSignature basicStatementBlock ;

adminOpSignature: ADMIN_OP ID OPEN_PAREN operationFormalParamList CLOSE_PAREN returnType=variableType? ;
resourceOpSignature: RESOURCE_OP ID OPEN_PAREN operationFormalParamList CLOSE_PAREN;
routineSignature: ROUTINE ID OPEN_PAREN formalParamList CLOSE_PAREN returnType=variableType? ;
basicFunctionSignature: FUNCTION ID OPEN_PAREN formalParamList CLOSE_PAREN returnType=variableType? ;

operationFormalParamList: (operationFormalParam (COMMA operationFormalParam)*)? ;
operationFormalParam: nodeArgAnnotation? variableType ID reqCap=stringArrayLit?;

nodeArgAnnotation: NODE_ARG (OPEN_PAREN (stringLit (COMMA stringLit)*)? CLOSE_PAREN)? ;

adminOpStatementBlock: OPEN_CURLY adminOpStatement* CLOSE_CURLY ;
adminOpStatement:
  statement #BasicOrOperationAdminOpStatement
  | checkStatement #CheckAdminOpStatement
  ;
resourceOpStatementBlock: OPEN_CURLY checkStatement* CLOSE_CURLY ;

formalParamList: (formalParam (COMMA formalParam)*)? ;
formalParam: variableType ID;

returnStatement: RETURN expression?;

checkStatement: CHECK ar=expression ON target=expression ;
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
    | INT64_TYPE #Int64Type
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
    | DOT key=idIndex #DotIndex;

functionInvoke: ID functionInvokeArgs ;
functionInvokeArgs: OPEN_PAREN expressionList? CLOSE_PAREN ;

idIndex:
    ID
    | OPERATION
    | CHECK
    | ROUTINE
    | FUNCTION
    | CREATE
    | DELETE
    | RULE
    | WHEN
    | PERFORMS
    | ON
    | IN
    | DO
    | ANY
    | INTERSECTION
    | UNION
    | PROCESS
    | ASSIGN
    | DEASSIGN
    | FROM
    | OF
    | TO
    | ASSOCIATE
    | AND
    | WITH
    | DISSOCIATE
    | DENY
    | PROHIBITION
    | OBLIGATION
    | USER
    | NODE
    | PC
    | OA
    | UA
    | O
    | U
    | BREAK
    | DEFAULT
    | MAP
    | ELSE
    | CONST
    | IF
    | RANGE
    | CONTINUE
    | FOREACH
    | RETURN
    | VAR
    | STRING_TYPE
    | BOOL_TYPE
    | VOID_TYPE
    | ARRAY_TYPE
    | NIL_LIT
    | TRUE
    | FALSE
    ;
