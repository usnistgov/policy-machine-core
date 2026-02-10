parser grammar PMLParser;

options {
	tokenVocab = PMLLexer;
}

pml: (statement)* EOF ;

statement:
    basicStatement
    | adminOperationStatement ;

basicStatement: (
    variableAssignmentStatement
    | variableDeclarationStatement
    | foreachStatement
    | returnStatement
    | breakStatement
    | continueStatement
    | operationInvokeStatement
    | ifStatement
    | functionDefinitionStatement
) ;

adminOperationStatement: (
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
    | adminOpDefinitionStatement
    | resourceOpDefinitionStatement
    | routineDefinitionStatement
    | queryOpDefinitionStatement
) ;

statementBlock: OPEN_CURLY statement* CLOSE_CURLY ;

createPolicyStatement: CREATE PC name=expression;

createNonPCStatement:
    CREATE nonPCNodeType name=expression
    IN in=expression ;
nonPCNodeType:
    (OA | UA | O | U) ;

createObligationStatement:
    CREATE OBLIGATION name=expression
    eventPattern
    response ;

eventPattern:
  WHEN subjectPattern
  PERFORMS operationPattern
  ;

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
    | (opName=stringLit onPattern?) #OperationPatternFunc ;
onPattern: ON OPEN_PAREN argNames? CLOSE_PAREN onPatternBlock? ;
onPatternBlock: OPEN_CURLY basicStatement* CLOSE_CURLY ;
argNames: ID (COMMA ID)*;

// response
response:
    DO OPEN_PAREN ID CLOSE_PAREN responseBlock;
responseBlock:
    OPEN_CURLY statement* CLOSE_CURLY ;

createProhibitionStatement:
    CREATE type=(CONJ | DISJ)
    entity=(NODE | PROCESS) PROHIBITION name=expression
    DENY node=expression (PROCESS process=expression)?
    ARSET arset=expression
    (INCLUDE inclusionSet=expression)?
    (EXCLUDE exclusionSet=expression)?
  ;

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
    | OBLIGATION #DeleteObligation
    | PROHIBITION #DeleteProhibition
    | OPERATION #DeleteOperation
    ;

variableDeclarationStatement:
    VAR (varSpec | OPEN_PAREN (varSpec)* CLOSE_PAREN) #VarDeclaration
    | ID DECLARE_ASSIGN expression #ShortDeclaration;
varSpec: ID ASSIGN_EQUALS expression;

variableAssignmentStatement: ID PLUS? ASSIGN_EQUALS expression;

adminOpDefinitionStatement: adminOpSignature adminOpStatementBlock ;
queryOpDefinitionStatement: queryOpSignature basicAndCheckStatementBlock ;
resourceOpDefinitionStatement: resourceOpSignature basicAndCheckStatementBlock? ;
routineDefinitionStatement: routineSignature statementBlock ;
functionDefinitionStatement: functionSignature basicStatementBlock ;

adminOpSignature: reqCapList? ADMIN_OP ID OPEN_PAREN operationFormalParamList CLOSE_PAREN returnType=variableType? ;
queryOpSignature: reqCapList? QUERY ID OPEN_PAREN operationFormalParamList CLOSE_PAREN returnType=variableType? ;
resourceOpSignature: reqCapList? RESOURCE_OP ID OPEN_PAREN operationFormalParamList CLOSE_PAREN returnType=variableType?;
routineSignature: ROUTINE ID OPEN_PAREN formalParamList CLOSE_PAREN returnType=variableType? ;
functionSignature: FUNCTION ID OPEN_PAREN formalParamList CLOSE_PAREN returnType=variableType? ;

reqCapList: reqCap+ ;
reqCap: REQ_CAP OPEN_PAREN (OPEN_CURLY reqCapEntry (COMMA reqCapEntry)*? CLOSE_CURLY) CLOSE_PAREN ;
reqCapEntry:(param=ID | node=stringLit) COLON arset=stringArrayLit ;

operationFormalParamList: (operationFormalParam (COMMA operationFormalParam)*)? ;
operationFormalParam: NODE_ARG? variableType ID paramReqCap=stringArrayLit?;

adminOpStatementBlock: OPEN_CURLY adminOpStatement* CLOSE_CURLY ;
adminOpStatement:
  statement #BasicOrAdminOpStatement
  | checkStatement #CheckAdminOpStatement
  ;

basicAndCheckStatementBlock: OPEN_CURLY basicAndCheckStatement* CLOSE_CURLY ;
basicAndCheckStatement:
  basicStatement #BasicResourceOpStatement
  | checkStatement #CheckResourceOpStatement
  ;

formalParamList: (formalParam (COMMA formalParam)*)? ;
formalParam: variableType ID;

returnStatement: RETURN expression?;

checkStatement: CHECK ar=expression ON target=expression ;
basicStatementBlock: OPEN_CURLY basicStatement* CLOSE_CURLY ;

idArr: OPEN_BRACKET (ID (COMMA ID)*)? CLOSE_BRACKET ;
operationInvokeStatement: operationInvoke;

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
    operationInvoke #OperationInvokeExpression
    | variableReference #VariableReferenceExpression
    | literal #LiteralExpression
    | EXCLAMATION expression #NegateExpression
    | OPEN_PAREN expression CLOSE_PAREN #ParenExpression
    | expression index #IndexExpression
    | left=expression PLUS right=expression #PlusExpression
    | left=expression (EQUALS | NOT_EQUALS) right=expression #EqualsExpression
    | left=expression (LOGICAL_AND | LOGICAL_OR) right=expression #LogicalExpression ;
expressionList: expression (COMMA expression)* ;

literal:
    stringLit #StringLiteral
    | int64Lit #Int64Literal
    | boolLit #BoolLiteral
    | arrayLit #ArrayLiteral
    | mapLit #MapLiteral;
int64Lit: INT64_DECIMAL ;
stringLit: DOUBLE_QUOTE_STRING;
boolLit: TRUE | FALSE;
arrayLit: OPEN_BRACKET expressionList? CLOSE_BRACKET ;
stringArrayLit: OPEN_BRACKET (stringLit (COMMA stringLit)*)? CLOSE_BRACKET ;
mapLit: OPEN_CURLY (element (COMMA element)*)? CLOSE_CURLY ;
element: key=expression COLON value=expression ;

variableReference: ID ;

index:
    OPEN_BRACKET key=expression CLOSE_BRACKET #BracketIndex
    | DOT key=idIndex #DotIndex;

operationInvoke: ID operationInvokeArgs ;
operationInvokeArgs: OPEN_PAREN expressionList? CLOSE_PAREN ;

idIndex:
    ID
    | OPERATION
    | ADMIN_OP
    | RESOURCE_OP
    | QUERY
    | FUNCTION
    | CHECK
    | ROUTINE
    | CREATE
    | DELETE
    | RULE
    | WHEN
    | PERFORMS
    | ON
    | IN
    | DO
    | ANY
    | DISJ
    | CONJ
    | INCLUDE
    | EXCLUDE
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
    | ARSET
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
    | INT64_TYPE
    | NIL_LIT
    | TRUE
    | FALSE
    ;
