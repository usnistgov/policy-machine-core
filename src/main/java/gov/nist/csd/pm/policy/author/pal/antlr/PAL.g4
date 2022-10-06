grammar PAL;

pal: stmts EOF ;
stmts: (stmt)* ;
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
varStmt: (LET | CONST)? id EQUALS expression SEMI_COLON ;

// function definition
funcDefStmt:
    FUNCTION id OPEN_PAREN formalArgList CLOSE_PAREN funcReturnType? funcBody ;
formalArgList:
    (formalArg (COMMA formalArg)*)? ;
formalArg:
    formalArgType id ;
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
    FOREACH key=id (COMMA mapValue=id)? IN expression stmtBlock ;

forRangeStmt:
    FOR id IN_RANGE OPEN_BRACKET lower=NUMBER COMMA upper=NUMBER CLOSE_BRACKET stmtBlock ;

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

createPolicyStmt: CREATE POLICY_CLASS nameExpression SEMI_COLON ;
createAttrStmt: CREATE (OBJECT_ATTRIBUTE | USER_ATTRIBUTE) nameExpression IN nameExpressionArray SEMI_COLON ;
createUserOrObjectStmt: CREATE (USER | OBJECT) nameExpression IN nameExpressionArray SEMI_COLON ;

setNodePropsStmt: SET_PROPERTIES OF nameExpression TO properties=expression SEMI_COLON ;

assignStmt: ASSIGN child=nameExpression TO parent=nameExpression SEMI_COLON ;
deassignStmt: DEASSIGN child=nameExpression FROM parent=nameExpression SEMI_COLON ;

associateStmt: ASSOCIATE ua=nameExpression AND target=nameExpression WITH accessRights=accessRightArray SEMI_COLON ;
dissociateStmt: DISSOCIATE ua=nameExpression AND target=nameExpression SEMI_COLON ;

deleteStmt: DELETE deleteType nameExpression SEMI_COLON ;

createObligationStmt:
    CREATE OBLIGATION nameExpression OPEN_CURLY createRuleStmt* CLOSE_CURLY;
createRuleStmt:
    CREATE RULE ruleName=nameExpression
    WHEN subjectClause
    PERFORMS performsClause=expression
    (ON onClause)?
    response ;
subjectClause:
    ANY_USER #AnyUserSubject
    | USER user=nameExpression #UserSubject
    | USERS users=nameExpressionArray #UsersListSubject
    | ANY_USER_WITH_ATTRIBUTE attribute=nameExpression #UserAttrSubject
    | PROCESS process=nameExpression #ProcessSubject ;
onClause:
    nameExpression #PolicyElement
    | anyPe #AnyPolicyElement
    | anyPe IN nameExpression #AnyContainedIn
    | anyPe OF nameExpressionArray #AnyOfSet ;
anyPe: ANY POLICY_ELEMENT;

response:
    DO OPEN_PAREN id CLOSE_PAREN responseBlock;
responseBlock:
    OPEN_CURLY responseStmts CLOSE_CURLY ;
responseStmts:
    responseStmt* ;
responseStmt:
    stmt
    | createRuleStmt ;
deleteRuleStmt:
    DELETE RULE ruleName=nameExpression FROM OBLIGATION obligationName=nameExpression SEMI_COLON ;

createProhibitionStmt:
    CREATE PROHIBITION name=nameExpression DENY (USER | USER_ATTRIBUTE | PROCESS) subject=nameExpression
    ACCESS_RIGHTS accessRights=accessRightArray
    ON (INTERSECTION|UNION) OF containers=prohibitionContainerList
    SEMI_COLON ;
prohibitionContainerList:
    (prohibitionContainerExpression (COMMA prohibitionContainerExpression)*)?;
prohibitionContainerExpression:
    IS_COMPLEMENT? container=nameExpression ;

setResourceAccessRightsStmt:
    SET_RESOURCE_ACCESS_RIGHTS accessRightArray SEMI_COLON;

expression:
    varRef
    | funcCall
    | literal;

nameExpressionArray: nameExpression (COMMA nameExpression)* ;
nameExpression: (varRef | funcCall) ;

array:
    OPEN_BRACKET (expression (COMMA expression)*)? CLOSE_BRACKET ;
accessRightArray:
    (accessRight (COMMA accessRight)*)? ;
accessRight:
    (ALL_ACCESS_RIGHTS | ALL_RESOURCE_ACCESS_RIGHTS | ALL_ADMIN_ACCESS_RIGHTS | id) ;

map:
    OPEN_CURLY (mapEntry (COMMA mapEntry)*)? CLOSE_CURLY ;
mapEntry:
    key=expression COLON value=expression ;
mapEntryRef:
    id (OPEN_BRACKET key=expression CLOSE_BRACKET)+;

literal:
    STRING #StringLiteral
    | BOOLEAN #BooleanLiteral
    | array #ArrayLiteral
    | map #MapLiteral;
varRef:
    id #ReferenceByID
    | mapEntryRef #MapEntryReference ;

funcCall:
    id funcCallArgs ;
funcCallArgs:
    OPEN_PAREN (expression (COMMA expression)*)? CLOSE_PAREN ;

id: (IDENTIFIER | keywordAsID) ;

keywordAsID:
    ALL_ACCESS_RIGHTS
    | ALL_RESOURCE_ACCESS_RIGHTS
    | ALL_ADMIN_ACCESS_RIGHTS
    | CREATE
    | DELETE
    | ASSIGN
    | DEASSIGN
    | ASSOCIATE
    | DISSOCIATE
    | DENY
    ;

// LEXER RULES
ALL_ACCESS_RIGHTS : '*' ;
ALL_RESOURCE_ACCESS_RIGHTS : '*r' ;
ALL_ADMIN_ACCESS_RIGHTS : '*a' ;

CREATE: [Cc][Rr][Ee][Aa][Tt][Ee] ;
DELETE: [Dd][Ee][Ll][Ee][Tt][Ee] ;
BREAK: [Bb][Rr][Ee][Aa][Kk] ;
CONTINUE: [Cc][Oo][Nn][Tt][Ii][Nn][Uu][Ee] ;

// obligation keywords
POLICY_ELEMENT: [Pp][Oo][Ll][Ii][Cc][Yy][ ][Ee][Ll][Ee][Mm][Ee][Nn][Tt] ;
RULE: [Rr][Uu][Ll][Ee] ;
WHEN: [Ww][Hh][Ee][Nn] ;
PERFORMS: [Pp][Ee][Rr][Ff][Oo][Rr][Mm][Ss] ;
ON: [Oo][Nn] ;
DO: [Dd][Oo] ;
ANY_USER: ANY [ ] USER ;
USERS: USER [Ss] ;
ANY_USER_WITH_ATTRIBUTE: ANY_USER [ ][Ww][Ii][Tt][Hh][ ] ATTR ;
PROCESS: [Pp][Rr][Oo][Cc][Ee][Ss][Ss] ;
INTERSECTION: [Ii][Nn][Tt][Ee][Rr][Ss][Ee][Cc][Tt][Ii][Oo][Nn] ;
UNION: [Uu][Nn][Ii][Oo][Nn] ;

SET_RESOURCE_ACCESS_RIGHTS: [Ss][Ee][Tt][ ][Rr][Ee][Ss][Oo][Uu][Rr][Cc][Ee][ ] ACCESS_RIGHTS;
ASSIGN: [Aa][Ss][Ss][Ii][Gg][Nn] ;
DEASSIGN: [Dd][Ee][Aa][Ss][Ss][Ii][Gg][Nn] ;
FROM: [Ff][Rr][Oo][Mm] ;
SET_PROPERTIES: [Ss][Ee][Tt][ ][Pp][Rr][Oo][Pp][Ee][Rr][Tt][Ii][Ee][Ss] ;
OF: [Oo][Ff] ;
TO: [Tt][Oo] ;
ASSOCIATE: [Aa][Ss][Ss][Oo][Cc][Ii][Aa][Tt][Ee] ;
AND: [Aa][Nn][Dd] ;
WITH: [Ww][Ii][Tt][Hh] ;
DISSOCIATE: [Dd][Ii][Ss][Ss][Oo][Cc][Ii][Aa][Tt][Ee] ;
DENY: [Dd][Ee][Nn][Yy];
PROHIBITION: [Pp][Rr][Oo][Hh][Ii][Bb][Ii][Tt][Ii][Oo][Nn];
OBLIGATION: [Oo][Bb][Ll][Ii][Gg][Aa][Tt][Ii][Oo][Nn];
ACCESS_RIGHTS: [Aa][Cc][Cc][Ee][Ss][Ss][ ][Rr][Ii][Gg][Hh][Tt][Ss] ;

POLICY_CLASS: ([Pp][Oo][Ll][Ii][Cc][Yy][ ][Cc][Ll][Aa][Ss][Ss] | [Pp][Cc]) ;
OBJECT_ATTRIBUTE: (OBJECT [ ] ATTR | [Oo][Aa]) ;
USER_ATTRIBUTE: (USER [ ] ATTR | [Uu][Aa]);
OBJECT: ([Oo][Bb][Jj][Ee][Cc][Tt] | [Oo]);
USER: ([Uu][Ss][Ee][Rr] | [Uu]) ;
ATTR:  [Aa][Tt][Tt][Rr][Ii][Bb][Uu][Tt][Ee] ;

ANY: [Aa][Nn][Yy] ;
LET: [Ll][Ee][Tt] ;
CONST: [Cc][Oo][Nn][Ss][Tt] ;
FUNCTION: [Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn] ;
RETURN: [Rr][Ee][Tt][Uu][Rr][Nn] ;
BOOLEAN: (TRUE|FALSE) ;
TRUE: [Tt][Rr][Uu][Ee] ;
FALSE: [Ff][Aa][Ll][Ss][Ee] ;
STRING_TYPE: [Ss][Tt][Rr][Ii][Nn][Gg] ;
BOOLEAN_TYPE: [Bb][Oo][Oo][Ll][Ee][Aa][Nn] ;
VOID_TYPE: [Vv][Oo][Ii][Dd] ;
ARRAY_TYPE: [Aa][Rr][Rr][Aa][Yy] ;
MAP_TYPE: [Mm][Aa][Pp] ;
FOREACH: [Ff][Oo][Rr][Ee][Aa][Cc][Hh] ;
FOR: [Ff][Oo][Rr] ;
IN: [Ii][Nn] ;
IF: [Ii][Ff] ;
ELSE: [Ee][Ll][Ss][Ee] ;
IN_RANGE: [Ii][Nn][ ][Rr][Aa][Nn][Gg][Ee] ;

NUMBER: [0-9]+ ;
IDENTIFIER: [a-zA-Z0-9_+\-\\.@]+ ;
STRING: '\'' (~['\\])*  '\'' ;
LINE_COMMENT : '#' ~'\n'* '\n' -> channel(HIDDEN) ;
WS : [ \t\n\r]+ -> skip ;
COMMA: ',' ' '* ;
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