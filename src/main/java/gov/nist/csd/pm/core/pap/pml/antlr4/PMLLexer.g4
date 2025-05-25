lexer grammar PMLLexer;

@header {
package gov.nist.csd.pm.core.pap.pml.antlr;
}

OPERATION: 'operation';
NODE_PARAM: '@node';
CHECK: 'check';
ROUTINE: 'routine';
FUNCTION: 'function';

CREATE      : 'create' ;
DELETE      : 'delete' ;

// obligation keywords
POLICY_ELEMENT: 'policy element' | 'pe' ;
CONTAINED: 'contained';
RULE: 'rule' ;
WHEN: 'when' ;
PERFORMS: 'performs' ;
AS: 'as' ;
ON: 'on' ;
IN: 'in' ;
DO: 'do' ;
ANY: 'any';
ASCENDANT_OF: 'ascendant of' ;

INTERSECTION: 'intersection' | 'inter';
UNION: 'union' ;
PROCESS: 'process';

SET_RESOURCE_OPERATIONS: 'set resource operations';
ASSIGN: 'assign' ;
DEASSIGN: 'deassign' ;
FROM: 'from' ;
SET_PROPERTIES: 'set properties' ;
OF: 'of' ;
TO: 'to' ;
ASSOCIATE: 'associate' ;
AND: 'and' ;
WITH: 'with' ;
DISSOCIATE: 'dissociate' ;
DENY: 'deny';
PROHIBITION: 'prohibition';
OBLIGATION: 'obligation';
ACCESS_RIGHTS: 'access rights' ;

NODE: 'node' ;
POLICY_CLASS: 'policy class' | 'pc' | 'PC' ;
OBJECT_ATTRIBUTE: 'object attribute' | 'oa' | 'OA' ;
USER_ATTRIBUTE: 'user attribute' | 'ua' | 'UA' ;
USER_ATTRIBUTES: 'user attributes' | 'uas' | 'UAs' ;
OBJECT_ATTRIBUTES: 'object attributes' | 'oas' | 'OAs' ;
OBJECT: 'object' | 'o' | 'O' ;
USER: 'user' | 'u' | 'U' ;
ATTRIBUTE:  'attribute';
ASSOCIATIONS: 'associations' ;

// Keywords

BREAK                  : 'break';
DEFAULT                : 'default';
MAP                    : 'map';
ELSE                   : 'else';
CONST                  : 'const';
IF                     : 'if';
RANGE                  : 'range';
CONTINUE               : 'continue';
FOREACH                : 'foreach';
RETURN                 : 'return';
VAR                    : 'var';
STRING_TYPE            : 'string' ;
BOOL_TYPE              : 'bool' ;
VOID_TYPE              : 'void' ;
ARRAY_TYPE             : 'array' ;

NIL_LIT                : 'nil';

// boolean literals
TRUE:   'true';
FALSE:  'false';


ID             : [a-zA-Z0-9_]+;

// Punctuation

OPEN_PAREN              : '(';
CLOSE_PAREN             : ')';
OPEN_CURLY              : '{';
CLOSE_CURLY             : '}';
OPEN_BRACKET            : '[';
CLOSE_BRACKET           : ']';
ASSIGN_EQUALS           : '=';
COMMA                   : ',';
SEMI                    : ';';
COLON                   : ':';
DOT                     : '.';
DECLARE_ASSIGN          : ':=';

// Logical

LOGICAL_OR              : '||';
LOGICAL_AND             : '&&';

// Relation operators

EQUALS                  : '==';
NOT_EQUALS              : '!=';

// Unary operators

EXCLAMATION             : '!';

// Mixed operators

PLUS                    : '+';

// String literals

DOUBLE_QUOTE_STRING     : '"' (~["\\\r\n] | EscapeSequence)* '"';

// Hidden tokens
// WS:                 [ \t]+ -> channel(HIDDEN);
// NL:                 [\r\n]+ -> channel(HIDDEN);
WS:                 [ \t\r\n\u000C]+ -> channel(HIDDEN);

COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);


fragment EscapeSequence
    : '\\' [btnfr"'\\]
    | '\\' ([0-3]? [0-7])? [0-7]
    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
    ;

fragment HexDigits
    : HexDigit ((HexDigit | '_')* HexDigit)?
    ;

fragment HexDigit
    : [0-9a-fA-F]
    ;

/*
 [The "BSD licence"]
 Copyright (c) 2017 Sasa Coh, Michał Błotniak
 Copyright (c) 2019 Ivan Kochurkin, kvanttt@gmail.com, Positive Technologies
 Copyright (c) 2019 Dmitry Rassadin, flipparassa@gmail.com, Positive Technologies
 Copyright (c) 2021 Martin Mirchev, mirchevmartin2203@gmail.com
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
 * A Go grammar for ANTLR 4 derived from the Go Language Specification
 * https://golang.org/ref/spec
 */