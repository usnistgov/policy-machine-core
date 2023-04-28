grammar PML;

pml: (statement)* EOF ;
statement: (
    createPolicyStatement
    | createAttributeStatement
    | createUserOrObjectStatement
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
    | variableDeclarationStatement
    | functionDefinitionStatement
    | functionReturnStatement
    | foreachStatement
    | forRangeStatement
    | breakStatement
    | continueStatement
    | functionInvokeStatement
    | ifStatement
) ;

createPolicyStatement:
    CREATE POLICY_CLASS expression ;

createAttributeStatement:
    CREATE (OBJECT_ATTRIBUTE | USER_ATTRIBUTE) name=expression IN parents=expression ;

createUserOrObjectStatement:
    CREATE (USER | OBJECT) name=expression IN parents=expression ;

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
    | USER user=expression #UserSubject
    | USERS users=expression #UsersListSubject
    | ANY_USER_WITH_ATTRIBUTE attribute=expression #UserAttrSubject
    | PROCESS process=expression #ProcessSubject ;
onClause:
    expression #PolicyElement
    | anyPe #AnyPolicyElement
    | anyPe IN expression #AnyContainedIn
    | anyPe OF expression #AnyOfSet ;
anyPe: ANY POLICY_ELEMENT;
response:
    DO OPEN_PAREN ID CLOSE_PAREN responseBlock;
responseBlock:
    OPEN_CURLY responseStatement* CLOSE_CURLY ;
responseStatement:
    statement
    | createRuleStatement
    | deleteRuleStatement ;

createProhibitionStatement:
    CREATE PROHIBITION name=expression DENY (USER | USER_ATTRIBUTE | PROCESS) subject=expression
    ACCESS_RIGHTS accessRights=expression
    ON (INTERSECTION|UNION) OF containers=prohibitionContainerList ;
prohibitionContainerList:
    OPEN_BRACKET (prohibitionContainerExpression (COMMA prohibitionContainerExpression)*)? CLOSE_BRACKET ;
prohibitionContainerExpression:
    IS_COMPLEMENT? container=expression ;

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
    (LET | CONST)? ID EQUALS expression ;

functionDefinitionStatement:
    FUNCTION ID OPEN_PAREN formalArgList CLOSE_PAREN funcReturnType? funcBody ;
formalArgList:
    (formalArg (COMMA formalArg)*)? ;
formalArg:
    formalArgType ID ;
formalArgType:
    variableType ;
functionReturnStatement:
    (RETURN expression | RETURN) ;
funcReturnType:
    variableType #VariableReturnType
    | VOID_TYPE #VoidReturnType;
funcBody:
    OPEN_CURLY statement* CLOSE_CURLY;

foreachStatement:
    FOREACH key=ID (COMMA mapValue=ID)? IN expression statementBlock ;

forRangeStatement:
    FOR ID IN_RANGE
    lowerBound=(OPEN_BRACKET|OPEN_PAREN) lower=expression COMMA upper=expression upperBound=(CLOSE_BRACKET|CLOSE_PAREN)
    statementBlock ;

breakStatement:
    BREAK ;

continueStatement:
    CONTINUE ;

functionInvokeStatement:
    functionInvoke;

ifStatement:
    IF (IS_COMPLEMENT)? condition=expression statementBlock
    elseIfStatement*
    elseStatement? ;
elseIfStatement:
    ELSE IF (IS_COMPLEMENT)? condition=expression statementBlock ;
elseStatement:
    ELSE statementBlock ;

variableType:
    STRING_TYPE #StringType
    | BOOLEAN_TYPE #BooleanType
    | arrayType #ArrayVarType
    | mapType #MapVarType
    | ANY #AnyType ;
mapType: MAP_TYPE OPEN_BRACKET keyType=variableType CLOSE_BRACKET valueType=variableType ;
arrayType: OPEN_BRACKET CLOSE_BRACKET variableType ;

statementBlock: OPEN_CURLY statement* CLOSE_CURLY ;

expression:
    variableReference
    | functionInvoke
    | literal
    | left=expression (EQUALS_OP | NOT_EQUALS_OP) right=expression
    | left=expression (AND_OP | OR_OP) right=expression;

array:
    OPEN_BRACKET (expression (COMMA expression)*)? CLOSE_BRACKET ;

map:
    OPEN_CURLY (mapEntry (COMMA mapEntry)*)? CLOSE_CURLY ;
mapEntry:
    key=expression COLON value=expression ;
entryReference:
    ID (OPEN_BRACKET key=expression CLOSE_BRACKET)+ ;

literal:
    STRING #StringLiteral
    | BOOLEAN #BooleanLiteral
    | NUMBER #NumberLiteral
    | array #ArrayLiteral
    | map #MapLiteral ;

variableReference:
    ID #ReferenceByID
    | entryReference #ReferenceByEntry ;

functionInvoke:
    ID functionInvokeArgs ;
functionInvokeArgs:
    OPEN_PAREN (expression (COMMA expression)*)? CLOSE_PAREN ;

// LEXER RULEs
CREATE: [c][r][e][a][t][e] ;
DELETE: [d][e][l][e][t][e] ;
BREAK: [b][r][e][a][k] ;
CONTINUE: [c][o][n][t][i][n][u][e] ;

// obligation keywords
POLICY_ELEMENT: [p][o][l][i][c][y][ ][e][l][e][m][e][n][t] ;
RULE: [r][u][l][e] ;
WHEN: [w][h][e][n] ;
PERFORMS: [p][e][r][f][o][r][m][s] ;
AS: [a][s] ;
ON: [o][n] ;
DO: [d][o] ;
ANY_USER: ANY [ ] USER ;
USERS: USER [s] ;
ANY_USER_WITH_ATTRIBUTE: ANY_USER [ ][w][i][t][h][ ] ATTR ;
PROCESS: [p][r][o][c][e][s][s] ;
INTERSECTION: [i][n][t][e][r][s][e][c][t][i][o][n] ;
UNION: [u][n][i][o][n] ;

SET_RESOURCE_ACCESS_RIGHTS: [s][e][t][ ][r][e][s][o][u][r][c][e][ ] ACCESS_RIGHTS;
ASSIGN: [a][s][s][i][g][n] ;
DEASSIGN: [d][e][a][s][s][i][g][n] ;
FROM: [f][r][o][m] ;
SET_PROPERTIES: [s][e][t][ ][p][r][o][p][e][r][t][i][e][s] ;
OF: [o][f] ;
TO: [t][o] ;
ASSOCIATE: [a][s][s][o][c][i][a][t][e] ;
AND: [a][n][d] ;
WITH: [w][i][t][h] ;
DISSOCIATE: [d][i][s][s][o][c][i][a][t][e] ;
DENY: [d][e][n][y];
PROHIBITION: [p][r][o][h][i][b][i][t][i][o][n];
OBLIGATION: [o][b][l][i][g][a][t][i][o][n];
ACCESS_RIGHTS: [a][c][c][e][s][s][ ][r][i][g][h][t][s] ;

POLICY_CLASS: ([p][o][l][i][c][y][ ][c][l][a][s][s] | [p][c]) ;
OBJECT_ATTRIBUTE: (OBJECT [ ] ATTR | [o][a]) ;
USER_ATTRIBUTE: (USER [ ] ATTR | [u][a]);
OBJECT: ([o][b][j][e][c][t] | [o]);
USER: ([u][s][e][r] | [u]) ;
ATTR:  [a][t][t][r][i][b][u][t][e] ;

ANY: [a][n][y] ;
LET: [l][e][t] ;
CONST: [c][o][n][s][t] ;
FUNCTION: [f][u][n][c][t][i][o][n] ;
RETURN: [r][e][t][u][r][n] ;
BOOLEAN: (TRUE|FALSE) ;
TRUE: [t][r][u][e] ;
FALSE: [f][a][l][s][e] ;
STRING_TYPE: [s][t][r][i][n][g] ;
BOOLEAN_TYPE: [b][o][o][l][e][a][n] ;
VOID_TYPE: [v][o][i][d] ;
ARRAY_TYPE: [a][r][r][a][y] ;
MAP_TYPE: [m][a][p] ;
FOREACH: [f][o][r][e][a][c][h] ;
FOR: [f][o][r] ;
IN: [i][n] ;
IF: [i][f] ;
ELSE: [e][l][s][e] ;
IN_RANGE: [i][n][ ][r][a][n][g][e] ;

NUMBER: [0-9]+ ;
ID: [a-zA-Z0-9_]+ ;
STRING: DOUBLE_QUOTE_STRING | SINGLE_QUOTE_STRING ;
DOUBLE_QUOTE_STRING : '"' ( '\\"' | ~('\n'|'\r') )*? '"' ;
SINGLE_QUOTE_STRING : '\'' ( '\\\'' | ~('\n'|'\r') )*? '\'' ;
LINE_COMMENT : '#' ~'\n'* '\n' -> channel(HIDDEN) ;
WS : [ \t\n\r]+ -> skip ;
COMMA: ',' ;
COLON: ':' ;
OPEN_CURLY: '{' ;
CLOSE_CURLY: '}' ;
OPEN_BRACKET: '[' ;
CLOSE_BRACKET: ']' ;
OPEN_ANGLE_BRACKET: '<' ;
CLOSE_ANGLE_BRACKET: '>' ;
OPEN_PAREN: '(' ;
CLOSE_PAREN: ')' ;
IS_COMPLEMENT: '!' ;
EQUALS: '=' ;
AND_OP: '&&' ;
OR_OP: '||' ;
EQUALS_OP: '==' ;
NOT_EQUALS_OP: '!=' ;