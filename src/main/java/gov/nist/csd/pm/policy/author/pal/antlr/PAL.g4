grammar PAL;

pal: (stmt)* EOF ;
stmt: (
    varStmt
    | funcDefStmt
    | funcReturnStmt
    | foreachStmt
    | forRangeStmt
    | breakStmt
    | continueStmt
    | funcCallStmt
    | ifStmt
    | createPolicyStmt
    | createAttrStmt
    | createUserOrObjectStmt
    | createObligationStmt
    | createProhibitionStmt
    | setNodePropsStmt
    | assignStmt
    | deassignStmt
    | deleteStmt
    | associateStmt
    | dissociateStmt
    | setResourceAccessRightsStmt
    | deleteRuleStmt
);

// let
varStmt: (LET | CONST)? VARIABLE_OR_FUNCTION_NAME EQUALS expression SEMI_COLON ;

// function definition
funcDefStmt:
    FUNCTION VARIABLE_OR_FUNCTION_NAME OPEN_PAREN formalArgList CLOSE_PAREN funcReturnType? funcBody ;
formalArgList:
    (formalArg (COMMA formalArg)*)? ;
formalArg:
    formalArgType VARIABLE_OR_FUNCTION_NAME ;
formalArgType:
    varType ;
funcReturnStmt:
    (RETURN expression | RETURN) SEMI_COLON ;
funcReturnType:
    varType #VarReturnType
    | VOID_TYPE #VoidReturnType;
funcBody:
    OPEN_CURLY stmt* CLOSE_CURLY;

foreachStmt:
    FOREACH key=VARIABLE_OR_FUNCTION_NAME (COMMA mapValue=VARIABLE_OR_FUNCTION_NAME)? IN expression stmtBlock ;

forRangeStmt:
    FOR VARIABLE_OR_FUNCTION_NAME IN_RANGE OPEN_BRACKET lower=NUMBER COMMA upper=NUMBER CLOSE_BRACKET stmtBlock ;

// break
breakStmt:
    BREAK SEMI_COLON ;

// continue
continueStmt:
    CONTINUE SEMI_COLON ;

// function call
funcCallStmt:
    funcCall SEMI_COLON;

// if
ifStmt:
    IF condition=expression stmtBlock
    elseIfStmt*
    elseStmt? ;
elseIfStmt:
    ELSE IF condition=expression stmtBlock ;
elseStmt:
    ELSE stmtBlock ;

varType:
    STRING_TYPE #StringType
    | BOOLEAN_TYPE #BooleanType
    | arrayType #ArrayVarType
    | mapType #MapVarType
    | ANY #AnyType ;
mapType: MAP_TYPE OPEN_BRACKET keyType=varType CLOSE_BRACKET valueType=varType ;
arrayType: OPEN_BRACKET CLOSE_BRACKET varType ;

stmtBlock: OPEN_CURLY stmt* CLOSE_CURLY ;

// ngac commands
deleteType:
    nodeType #DeleteNode
    | OBLIGATION #DeleteObligation
    | PROHIBITION #DeleteProhibition ;
nodeType:
    (POLICY_CLASS | OBJECT_ATTRIBUTE | USER_ATTRIBUTE | OBJECT | USER) ;

createPolicyStmt: CREATE POLICY_CLASS expression SEMI_COLON ;
createAttrStmt: CREATE (OBJECT_ATTRIBUTE | USER_ATTRIBUTE) expression IN expressionArray SEMI_COLON ;
createUserOrObjectStmt: CREATE (USER | OBJECT) expression IN expressionArray SEMI_COLON ;

setNodePropsStmt: SET_PROPERTIES OF name=expression TO properties=expression SEMI_COLON ;

assignStmt: ASSIGN child=expression TO parent=expression SEMI_COLON ;
deassignStmt: DEASSIGN child=expression FROM parent=expression SEMI_COLON ;

associateStmt: ASSOCIATE ua=expression AND target=expression WITH accessRights=expressionArray SEMI_COLON ;
dissociateStmt: DISSOCIATE ua=expression AND target=expression SEMI_COLON ;

deleteStmt: DELETE deleteType expression SEMI_COLON ;

createObligationStmt:
    CREATE OBLIGATION expression OPEN_CURLY createRuleStmt* CLOSE_CURLY;
createRuleStmt:
    CREATE RULE ruleName=expression
    WHEN subjectClause
    PERFORMS performsClause=expressionArray
    (ON onClause)?
    response ;
subjectClause:
    ANY_USER #AnyUserSubject
    | USER user=expression #UserSubject
    | USERS users=expressionArray #UsersListSubject
    | ANY_USER_WITH_ATTRIBUTE attribute=expression #UserAttrSubject
    | PROCESS process=expression #ProcessSubject ;
onClause:
    expression #PolicyElement
    | anyPe #AnyPolicyElement
    | anyPe IN expression #AnyContainedIn
    | anyPe OF expressionArray #AnyOfSet ;
anyPe: ANY POLICY_ELEMENT;

response:
    DO OPEN_PAREN VARIABLE_OR_FUNCTION_NAME CLOSE_PAREN responseBlock;
responseBlock:
    OPEN_CURLY responseStmt* CLOSE_CURLY ;
responseStmt:
    stmt
    | createRuleStmt
    | deleteRuleStmt
    ;
deleteRuleStmt:
    DELETE RULE ruleName=expression FROM OBLIGATION obligationName=expression SEMI_COLON ;

createProhibitionStmt:
    CREATE PROHIBITION name=expression DENY (USER | USER_ATTRIBUTE | PROCESS) subject=expression
    ACCESS_RIGHTS accessRights=expressionArray
    ON (INTERSECTION|UNION) OF containers=prohibitionContainerList
    SEMI_COLON ;
prohibitionContainerList:
    (prohibitionContainerExpression (COMMA prohibitionContainerExpression)*)?;
prohibitionContainerExpression:
    IS_COMPLEMENT? container=expression ;

setResourceAccessRightsStmt:
    SET_RESOURCE_ACCESS_RIGHTS expressionArray SEMI_COLON;

expressionArray:
    expression (COMMA expression)? ;

expression:
    varRef
    | funcCall
    | literal;

array:
    OPEN_BRACKET (expression (COMMA expression)*)? CLOSE_BRACKET ;

map:
    OPEN_CURLY (mapEntry (COMMA mapEntry)*)? CLOSE_CURLY ;
mapEntry:
    key=expression COLON value=expression ;
mapEntryRef:
    VARIABLE_OR_FUNCTION_NAME (OPEN_BRACKET key=expression CLOSE_BRACKET)+;

literal:
    STRING #StringLiteral
    | BOOLEAN #BooleanLiteral
    | array #ArrayLiteral
    | map #MapLiteral;
varRef:
    VARIABLE_OR_FUNCTION_NAME #ReferenceByID
    | mapEntryRef #MapEntryReference ;

funcCall:
    VARIABLE_OR_FUNCTION_NAME funcCallArgs ;
funcCallArgs:
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
VARIABLE_OR_FUNCTION_NAME: [a-zA-Z0-9_]+ ;
STRING: DOUBLE_QUOTE_STRING | SINGLE_QUOTE_STRING ;
DOUBLE_QUOTE_STRING : '"' ( '\\"' | ~('\n'|'\r') )*? '"' ;
SINGLE_QUOTE_STRING : '\'' ( '\\\'' | ~('\n'|'\r') )*? '\'' ;
LINE_COMMENT : '#' ~'\n'* '\n' -> channel(HIDDEN) ;
WS : [ \t\n\r]+ -> skip ;
COMMA: ',' ;
COLON: ':' ;
SEMI_COLON: ';' ;
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