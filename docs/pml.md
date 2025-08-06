See PML [lexer](src/main/java/gov/nist/csd/pm/core/pap/pml/antlr4/PMLLexer.g4) and [grammar](src/main/java/gov/nist/csd/pm/core/pap/pml/antlr4/PMLParser.g4) for full documentation and details. The below ANTLR4 definitions have been slightly modified for readability.  

## Execution

- Variables cannot be referenced from inside operations and routines.
- A user context is needed when executing PML. This is **only** used for obligations to define the user to execute the response on behalf of in the EPP.
	- This user must exist before any obligation is created.

```java  
String pml = "...";  
pap.executePML(new UserContext(userId), pml);
```  

- Any operations and routines will be stored in the policy store.
- All [admin policy nodes](/src/main/java/gov/nist/csd/pm/core/pap/admin/AdminPolicyNode.java) will be defined as constants during compilation and execution.  

  - PM_ADMIN_PC: The PM_ADMIN policy class node.
  - PM_ADMIN_BASE_OA: The PM_ADMIN base OA that holds all other OA nodes.
  - PM_ADMIN_POLICY_CLASSES: Used to determine user access to create and interact with PC nodes.
  - PM_ADMIN_OBLIGATIONS: Used to determine user access to create and interact with obligations with an "any user" event pattern subject.
  - PM_ADMIN_PROHIBITIONS: Used to determine user access to process prohibitions and container compelemnt conditions.
  - PM_ADMIN_OPERATIONS: Used to determine user access to operations.
  - PM_ADMIN_ROUTINES: Used to determine user access to routines.

## Basics  
  
The syntax for basic PML statements is based on [Golang]([https://github.com/antlr/grammars-v4/tree/master/golang](https://github.com/antlr/grammars-v4/tree/master/golang "https://github.com/antlr/grammars-v4/tree/master/golang"))  

### Comments  
  
```  
// this is line comment  
  
/*  
  this is a block comment  
*/  
```  
### Identifiers  
  
```  
ID: [a-zA-Z0-9_]+;  
```  
### Data Types  
  
```antlr4  
variableType:  
string 
| bool 
| []variableType  
| map[variableType]variableType 
| any ;  
```  
  
The `any` type will compile without error regardless of expected type. However, during execution if the evaluated type does not match the expected, an error will occur.   
### Expressions  
  
Expressions are the fundamental way to define values in PML statements. Each expression has a **type**. Some statements will only support expressions of a certain type.  
#### Literal  
  
```  
literal:  
stringLiteral
| boolLiteral
| mapLiteral
| arrayLiteral ;  
  
stringLiteral: '"' (~["\\\r\n] | EscapeSequence)* '"' ;  
boolLiteral: (true | false) ;  
mapLiteral: '{' expression: expression (',' expression: expression)* '}' ;  
arrayLiteral: '[' expression (',' expression)* ']' ;  
  
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
  
```  
  
- Array elements are expressions  
- Map key and value elements are expressions  
#### Logical  
  
```  
expression ('&&' | '||') expression  
```  
#### Parenthesis  
  
```  
(expression)  
```  
#### Equals  
  
```  
expression ('==' | '!=') expression  
```  
#### String Concatenation  
  
```  
expression + expression  
```  
  
Only string expressions are supported.  
  
#### Negation  
  
```  
!expression  
```  
  
The negation expression when evaluated will only affect the result if the expression is a boolean expression. For example if the expression evaluates to true, the negated expression will evaluate to false. For any other type, the negation is ignored.  
#### Variable Reference  
  
```  
variableReference:  
ID
| variableReference index;  
  
index:  
'[' expression ']' #BracketNotation  
| '.' ID;  
```  
  
`RefByIndex` requires the `variableReference` to be of type `map`. To use the `DotNotation` the variable must be map with string keys.
  
#### Operation or routine invoke  
```  
ID '(' (expression (',' expression)*)? ')'  
```  
  
### Variable Declaration  

#### Variables  
  
```  
'var' ID = expression  
ID := expression  
```  
#### Group Declaration  
  
```  
'var ('  
(ID '=' expression)*  
')'  
```  
#### Variable Reassignment  
  
```  
ID '=' expression  
```  
  
The variable to be reassigned must already exist.  
#### Variable Addition Reassignment  
  
```  
ID '+=' expression  
```  
  
The variable must already exist AND be of type **string**.  
### If  
  
```  
'if' expression '{' statement* '}'  
('else if' expression '{' statement* '}' )*  
('else' '{' statement* '}' )?  
```  

- `expression` is a **bool** expression.  
  
### Foreach  
  
Use `break` and `continue` to short circuit  
  
```  
'foreach' ID (',' ID)? 'in' expression '{' statement* '}' ;  
```  

- `expression` is a **[]any** or **map[any]any** expression.  

#### Array  
  
```  
foreach x in ["a", "b", "c"] {  
...  
}  
```  
  
`x` will hold the current element in the array during iteration.  
  
#### Map  
  
```  
foreach x, y in {"key1": "value1", "key2": "value2"} {  
...  
}  
```  
  
`x` will hold the current key in the map during iteration.  
`y` will hold the current value in the map during iteration.  
  
### Operations and Routines  
  
```  
('operation' | 'routine' | 'function') ID '(' ('@node'? variableType ID (',' '@node'? variableType ID)*)? ')' variableType? '{' checkStatement* '}' '{' statement* '}' ;    
```  

- `@node` denotes if the arg is a **node arg** (represents a node or array of nodes).

#### CheckStatement
Check if a user has an access right on a node or array of nodes. The first expression is the access right the second is the node or array of nodes. This statement will only take affect in an **Operation**, not a routine.
```
check expression on expression
```

- First `expression` is a **string** expression (access right).
- Second `expression` is a **[]string** expression (nodes).
  
#### Example  
  
Function that returns a `string`.  
  
```  
operation op1(a string, b []string, c map[string]string) string {  
	check "assign" on a
	check "assign_to" on b
}  {
	/*
		pml statements
	*/
}
```  
  
## Policy Statements  
  
### Create Policy Class  
  
Create a policy class node with the given **name**.  
  
```  
'create PC' name=expression
```  

- `name` is a **string** expression.  

### Create Non Policy Class Nodes  
  
Create a node of type object attribute, user attribute, object, or user and assign it to a set of existing nodes. Types can also be expressed using their short version: pc, ua, oa, u, o.
  
```  
'create' ('OA' | 'UA' | 'O' | 'U') name=expression   
'in' assignTo=expression   
```  

- `name` is a **string** expression.
- `assignTo` is a **[]string** expression.  
  
### Set Node Properties  
  
Set the properties for a node. This will overwrite any existing properties.  
  
```  
'set properties of' name=expression 'to' properties=expression  
```  

- `name` is a **string** expression.
- `properties` is a **map[string]string** expression.  
  
### Assign  
  
Assign a node to a set of nodes.  
  
```  
'assign' childNode=expression 'to' parentNodes=expression  
```  

- `childNode` is a **string** expression.
- `parentNodes` is a **[]string** expression.  
  
### Deassign  
  
Deassign a node from a set of nodes.  
  
```  
'deassign' childNode=expression 'from' parentNodes=expression  
```  

- `childNode` is a **string** expression.
- `parentNodes` is a **[]string** expression.  
  
### Associate  
  
Create an association between two nodes with an access right set.  
  
```  
'associate' ua=expression 'and' target=expression 'with' accessRights=expression  
```  

- `ua` is a **string** expression.
- `target` is a **string** expression.
- `accessRights` is a **[]string** expression.  
  
### Dissociate  
  
Delete an association.  
  
```  
'dissociate' ua=expression 'and' target=expression  
```  

- `ua` is a **string** expression.
- `target` is a **string** expression.  

### Set Resource Operations

Set the resource operations for the policy.

```
'set resource operations' accessRights=expression
```

- `accessRights` is a **[]string** expression.
  
### Create Prohibition  
  
Create a new prohibition.  
  
```  
'create prohibition' name=expression   
'deny' ('U' | 'UA' | 'process') subject=expression   
'access rights' accessRights=expression   
'on' ('intersection'|'union') 'of' containers=expression ;  
```  

- `name` is a **string** expression.
- `subject` is a **string** expression.
- `accessRights` is a **[]string** expression.
- `containers` is a **[]string** expression. Complement containers are specified using a [negation expression](#negation), where the negated expression is a **string**. For example, `!"oa1"`  
  
### Create Obligation
  
Create new obligation. The author of the obligation will be the user that compiles and executes the PML.  
  
```  
createObligationStatement:  
    'create obligation' name=expression '{' createRuleStatement* '}';  
createRuleStatement:  
    'create rule' ruleName=expression  
    'when' subjectPattern  
    'performs' operationPattern  
    ('on' argPattern)?  
    response ;  
  
// subject  
subjectPattern:  
    'any user' #AnyUserPattern  
    | 'user' subjectPatternExpression #UserPattern;  
  
subjectPatternExpression:  
    basicSubjectPatternExpr  
    | '!' subjectPatternExpression  
    | '(' subjectPatternExpression ')'  
    | left=subjectPatternExpression ('&&' | '||') right=subjectPatternExpression ;  
  
basicSubjectPatternExpr:  
    'in' stringLit  // contained in
    | stringLit // equals
    | 'process' stringLit ;  
  
// operation  
operationPattern:  
    'any operation'  
    | stringLit ;  
  
// args  
argPattern: '{' (argPatternElement (',' argPatternElement)*)? '}' ;  
argPatternElement: key=ID ':' (single=argPatternExpression | multiple=argPatternExpressionArray);  
  
argPatternExpressionArray: '[' argPatternExpression (',' argPatternExpression)* ']' ;  
  
argPatternExpression:  
     basicArgPatternExpr  
     | '!' argPatternExpression  
     | '(' argPatternExpression ')'  
     | left=argPatternExpression ('&&' | '||') right=argPatternExpression ;  
  
basicArgPatternExpr:  
    'any'  
    | 'in' stringLit // contained in  
    | stringLit // equals ;  
  
// response  
response:  
    'do (' ID ')' responseBlock;  
responseBlock:  
    '{' responseStatement* '}' ;  
responseStatement:  
    statement  
    | createRuleStatement  
    | deleteRuleStatement ;
```  

- `name` is a **string** expression.
- `ruleName` is a **string** expression.
- arg pattern expressions can only be defined for node args of the given operation. Any non node args (i.e. access rights) will be ignored.
- if an event context arg is an array of nodes and
	- the arg expression is a single expression, the single expression will only need to match one element in the array
	- the arg expression is multiple expressions, each expression will be compared to the array as if it was a single expression
  
### Delete Node/Prohibition/Obligation  
  
Delete a node, prohibition, or obligation.  
  
```  
'delete'  
('PC' | 'OA' | 'UA' | 'O' | 'U' | 'obligation' | 'prohibition') expression  
```  

- `expression` is a **string** expression.  
  
### Delete Obligation Rule  
  
Delete a rule from an obligation. Can only be called from an obligation response block.
  
```  
'delete rule' ruleName=expression 'from obligation' obligationName=expression ;  
```  

- `ruleName` is a **string** expression.
- `obligationName` is a **string** expression.
