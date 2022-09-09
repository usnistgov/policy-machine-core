grammar PAL;

pal: stmts EOF ;
stmts: (stmt)* ;
stmt: (
    varStmt
    | funcDefStmt
    | funcReturnStmt
    | foreachStmt
    | breakStmt
    | continueStmt
    | funcCallStmt
    | ifStmt
    | createStmt
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
varStmt: (LET | CONST)? IDENTIFIER EQUALS expression SEMI_COLON ;

// function definition
funcDefStmt:
    FUNCTION IDENTIFIER OPEN_PAREN formalArgList CLOSE_PAREN funcReturnType? funcBody ;
formalArgList:
    (formalArg (COMMA formalArg)*)? ;
formalArg:
    formalArgType IDENTIFIER ;
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
    FOREACH key=IDENTIFIER (COMMA mapValue=IDENTIFIER)? IN expression stmtBlock ;

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

createStmt:
    CREATE (createPolicyStmt
            | createAttrStmt
            | createUserOrObjectStmt
            | createObligationStmt
            | createProhibitionStmt) ;

createPolicyStmt: POLICY_CLASS name=expression SEMI_COLON ;
createAttrStmt: (OBJECT_ATTRIBUTE | USER_ATTRIBUTE) name=expression ASSIGN_TO assignTo=expression SEMI_COLON ;
createUserOrObjectStmt: (USER | OBJECT) name=expression ASSIGN_TO assignTo=expression SEMI_COLON ;

setNodePropsStmt: SET_PROPERTIES OF name=expression TO properties=expression SEMI_COLON ;

assignStmt: ASSIGN child=expression TO assignTo=expression SEMI_COLON ;
deassignStmt: DEASSIGN child=expression FROM deassignFrom=expression SEMI_COLON ;

associateStmt: ASSOCIATE ua=expression AND target=expression WITH_ACCESS_RIGHTS accessRights=accessRightArray SEMI_COLON ;
dissociateStmt: DISSOCIATE ua=expression AND target=expression SEMI_COLON ;

deleteStmt: DELETE deleteType name=expression SEMI_COLON ;

createObligationStmt:
    OBLIGATION label=expression OPEN_CURLY createRuleStmt* CLOSE_CURLY;
createRuleStmt:
    CREATE RULE label=expression
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
    DO OPEN_PAREN IDENTIFIER CLOSE_PAREN responseBlock;
responseBlock:
    OPEN_CURLY responseStmts CLOSE_CURLY ;
responseStmts:
    responseStmt* ;
responseStmt:
    stmt
    | createRuleStmt ;
deleteRuleStmt:
    DELETE RULE ruleName=expression FROM OBLIGATION obligationName=expression SEMI_COLON ;

createProhibitionStmt:
    PROHIBITION label=expression DENY (USER | USER_ATTRIBUTE | PROCESS) subject=expression
    ACCESS_RIGHTS accessRights=accessRightArray
    ON (INTERSECTION|UNION) OF containers=prohibitionContainerList
    SEMI_COLON ;
prohibitionContainerList:
    (prohibitionContainerExpression (COMMA prohibitionContainerExpression)*)?;
prohibitionContainerExpression:
    IS_COMPLEMENT? container=expression ;

setResourceAccessRightsStmt:
    SET_RESOURCE_ACCESS_RIGHTS accessRightArray SEMI_COLON;

expression:
    varRef #VariableReference
    | funcCall #FunctionCall
    | literal #LiteralExpr;

array:
    OPEN_BRACKET (expression (COMMA expression)*)? CLOSE_BRACKET ;
accessRightArray:
    OPEN_BRACKET (accessRight (COMMA accessRight)*)? CLOSE_BRACKET ;
accessRight:
    ('*' | '*r' | '*a' | IDENTIFIER) ;
map:
    OPEN_CURLY (mapEntry (COMMA mapEntry)*)? CLOSE_CURLY ;
mapEntry:
    key=expression COLON value=expression ;
mapEntryRef:
    IDENTIFIER (OPEN_BRACKET key=expression CLOSE_BRACKET)+;

literal:
    STRING #StringLiteral
    | BOOLEAN #BooleanLiteral
    | array #ArrayLiteral
    | map #MapLiteral;
varRef:
    IDENTIFIER #ReferenceByID
    | mapEntryRef #MapEntryReference ;

funcCall:
    IDENTIFIER funcCallArgs ;
funcCallArgs:
    OPEN_PAREN (expression (COMMA expression)*)? CLOSE_PAREN ;

// LEXER RULES
CREATE: [Cc][Rr][Ee][Aa][Tt][Ee] ;
UPDATE: [Uu][Pp][Dd][Aa][Tt][Ee] ;
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
WITH_ACCESS_RIGHTS: [Ww][Ii][Tt][Hh][ ] ACCESS_RIGHTS ;
DISSOCIATE: [Dd][Ii][Ss][Ss][Oo][Cc][Ii][Aa][Tt][Ee] ;
ASSIGN_TO: [Aa][Ss][Ss][Ii][Gg][Nn][ ][Tt][Oo]  ;
DEASSIGN_FROM: [Dd][Ee][Aa][Ss][Ss][Ii][Gg][Nn][ ][Ff][Rr][Oo][Mm] ;
DENY: [Dd][Ee][Nn][Yy];
PROHIBITION: [Pp][Rr][Oo][Hh][Ii][Bb][Ii][Tt][Ii][Oo][Nn];
OBLIGATION: [Oo][Bb][Ll][Ii][Gg][Aa][Tt][Ii][Oo][Nn];
ACCESS_RIGHTS: [Aa][Cc][Cc][Ee][Ss][Ss][ ][Rr][Ii][Gg][Hh][Tt][Ss] ;

POLICY_CLASS: [Pp][Oo][Ll][Ii][Cc][Yy][ ][Cc][Ll][Aa][Ss][Ss] ;
OBJECT_ATTRIBUTE: OBJECT [ ] ATTR ;
USER_ATTRIBUTE: USER [ ] ATTR ;
OBJECT: [Oo][Bb][Jj][Ee][Cc][Tt] ;
USER: [Uu][Ss][Ee][Rr] ;
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
IN: [Ii][Nn] ;
IF: [Ii][Ff] ;
ELSE: [Ee][Ll][Ss][Ee];

IDENTIFIER: [a-zA-Z_]+ [a-zA-Z0-9_]* ;
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
DOT: '.' ;