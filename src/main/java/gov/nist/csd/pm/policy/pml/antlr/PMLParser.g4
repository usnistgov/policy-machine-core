parser grammar PMLParser;

options {
	tokenVocab = PMLLexer;
}

pml: (statement)* EOF ;
statement: (
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
    | variableAssignmentStatement
    | variableDeclarationStatement
    | functionDefinitionStatement
    | returnStatement
    | foreachStatement
    | breakStatement
    | continueStatement
    | functionInvokeStatement
    | ifStatement
) ;

statementBlock: OPEN_CURLY statement* CLOSE_CURLY ;

createPolicyStatement: CREATE POLICY_CLASS name=expression (WITH_PROPERTIES properties=expression)? hierarchy?;
hierarchy: OPEN_CURLY userAttrsHierarchy? objectAttrsHierarchy? associationsHierarchy? CLOSE_CURLY ;
userAttrsHierarchy: USER_ATTRIBUTES hierarchyBlock ;
objectAttrsHierarchy: OBJECT_ATTRIBUTES hierarchyBlock;
associationsHierarchy : ASSOCIATIONS associationsHierarchyBlock;
hierarchyBlock: OPEN_CURLY hierarchyStatement* CLOSE_CURLY ;
associationsHierarchyBlock: OPEN_CURLY associationsHierarchyStatement* CLOSE_CURLY ;
hierarchyStatement: name=expression properties=expression?;
associationsHierarchyStatement: ua=expression AND target=expression WITH arset=expression;

createNonPCStatement:
    CREATE nonPCNodeType name=expression
    (WITH_PROPERTIES properties=expression)?
    ASSIGN_TO assignTo=expression;
nonPCNodeType:
    (OBJECT_ATTRIBUTE | USER_ATTRIBUTE | OBJECT | USER) ;


createObligationStatement:
    CREATE OBLIGATION expression OPEN_CURLY createRuleStatement* CLOSE_CURLY;
createRuleStatement:
    CREATE RULE ruleName=expression
    WHEN subjectClause
    PERFORMS performsClause=expression
    (ON onClause)?
    response ;
subjectClause:
    ANY_USER #AnyUserSubject
    | USERS expression #UsersSubject
    | USERS IN UNION OF expression #UsersInUnionSubject
    | USERS IN INTERSECTION OF expression #UsersInIntersectionSubject
    | PROCESSES expression #ProcessesSubject;
onClause:
    ANY #AnyTarget
    | UNION OF expression #AnyInUnionTarget
    | INTERSECTION OF expression #AnyInIntersectionTarget
    | expression #OnTargets;
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
    ACCESS_RIGHTS? accessRights=expression
    ON (INTERSECTION|UNION) OF containers=expression ;

setNodePropertiesStatement:
    SET_PROPERTIES OF name=expression TO properties=expression ;

assignStatement:
    ASSIGN childNode=expression TO parentNodes=expression ;

deassignStatement:
    DEASSIGN childNode=expression FROM parentNodes=expression ;

associateStatement:
    ASSOCIATE ua=expression AND target=expression WITH accessRights=expression ;

dissociateStatement:
    DISSOCIATE ua=expression AND target=expression ;

setResourceAccessRightsStatement:
    SET_RESOURCE_ACCESS_RIGHTS accessRights=expression;

deleteStatement:
    DELETE deleteType expression ;
deleteType:
    nodeType #DeleteNode
    | OBLIGATION #DeleteObligation
    | PROHIBITION #DeleteProhibition ;
nodeType:
    (POLICY_CLASS | OBJECT_ATTRIBUTE | USER_ATTRIBUTE | OBJECT | USER) ;

deleteRuleStatement:
    DELETE RULE ruleName=expression FROM OBLIGATION obligationName=expression ;

variableDeclarationStatement:
    CONST (constSpec | OPEN_PAREN (constSpec)* CLOSE_PAREN) #ConstDeclaration
    | VAR (varSpec | OPEN_PAREN (varSpec)* CLOSE_PAREN) #VarDeclaration
    | ID DECLARE_ASSIGN expression #ShortDeclaration;
constSpec: ID ASSIGN_EQUALS expression;
varSpec: ID ASSIGN_EQUALS expression;

variableAssignmentStatement: ID PLUS? ASSIGN_EQUALS expression;

functionDefinitionStatement: FUNCTION ID OPEN_PAREN formalArgList CLOSE_PAREN returnType=variableType? statementBlock ;
formalArgList: (formalArg (COMMA formalArg)*)? ;
formalArg: variableType ID ;
returnStatement: RETURN expression?;

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

// basic elements
variableType:
    STRING_TYPE #StringType
    | BOOL_TYPE #BooleanType
    | arrayType #ArrayVarType
    | mapType #MapVarType
    | ANY #AnyType ;
mapType: MAP OPEN_BRACKET keyType=variableType CLOSE_BRACKET valueType=variableType ;
arrayType: OPEN_BRACKET CLOSE_BRACKET variableType ;

expression:
    variableReference #VariableReferenceExpression
    | functionInvoke #FunctionInvokeExpression
    | literal #LiteralExpression
    | EXCLAMATION expression #NegateExpression
    | OPEN_PAREN expression CLOSE_PAREN #ParenExpression
	| left=expression PLUS right=expression #PlusExpression
    | left=expression (EQUALS | NOT_EQUALS) right=expression #EqualsExpression
    | left=expression (LOGICAL_AND | LOGICAL_OR) right=expression #LogicalExpression;
expressionList: expression (COMMA expression)*;
identifierList: ID (COMMA ID)*;

literal:
    stringLit #StringLiteral
    | boolLit #BoolLiteral
    | arrayLit #ArrayLiteral
    | mapLit #MapLiteral;
stringLit: DOUBLE_QUOTE_STRING;
boolLit: TRUE | FALSE;
arrayLit: OPEN_BRACKET expressionList? CLOSE_BRACKET ;
mapLit: OPEN_CURLY (element (COMMA element)*)? CLOSE_CURLY ;
element: key=expression COLON value=expression ;

variableReference:
    ID #ReferenceByID
    | variableReference index #ReferenceByIndex ;
index:
    OPEN_BRACKET key=expression CLOSE_BRACKET #BracketIndex
    | DOT key=id #DotIndex;
id: ID;

functionInvoke: ID functionInvokeArgs ;
functionInvokeArgs: OPEN_PAREN expressionList? CLOSE_PAREN ;