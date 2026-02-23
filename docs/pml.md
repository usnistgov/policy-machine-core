# Policy Machine Language (PML) Reference

PML (Policy Machine Language) is a domain-specific language for defining and managing NGAC access control policies in 
the Policy Machine. This document provides a complete reference for PML syntax and semantics. For an example, see [Example](#example)

---
## Table of Contents

1. [Basic Elements](#basic-elements)
2. [Types](#types)
3. [Literals](#literals)
4. [Expressions](#expressions)
5. [Variables](#variables)
6. [Control Flow Statements](#control-flow-statements)
7. [Policy Modification Statements](#policy-modification-statements)
8. [Operation Definitions](#operation-definitions)
9. [Builtin Operations](#builtin-operations)
10. [Example](#example)

---

## Basic Elements

### Comments

```pml
// Single-line comment

/*
 * Multi-line
 * comment
 */
```

### Identifiers

Identifiers start with a letter or underscore, followed by any combination of letters, digits, or underscores:

```
ID := [a-zA-Z_][a-zA-Z0-9_]*
```

### Keywords

Reserved keywords cannot be used as identifiers.

```
adminop, resourceop, query, routine, function
create, delete, if exists
rule, when, performs, on, in, do, any, ascendant of
intersection, inter, union, process
set resource access rights, assign, deassign, from, set properties, of, to
associate, to, with, dissociate
deny, prohibition, obligation, access rights
PC, OA, UA, U, O, user, node
break, default, map, else, const, if, range, continue, foreach, return, var
string, bool, void, array, int64
true, false
check, @node
```

### Whitespace

Whitespace (spaces, tabs, newlines) is ignored except as a token separator.

---

## Types

PML is a dynamically-typed language with the following supported types:

| Type | Description | Example |
|------|-------------|---------|
| `string` | UTF-8 text string | `"hello"` |
| `bool` | Boolean value | `true`, `false` |
| `int64` | 64-bit signed integer | `42`, `-10` |
| `[]T` | Array of type T | `["a", "b"]` |
| `map[K]V` | Map from key type K to value type V | `{"key": "value"}` |
| `any` | Dynamic type (matches any type) | - |

### Type Inference

- **Array type inference**: When an array literal contains elements of mixed types, the element type becomes `any`.
- **Map type inference**: When a map literal contains keys or values of mixed types, the respective type becomes `any`.
- **Type casting**: Types can be implicitly cast to `any`, and `any` can be cast to specific types at runtime.

---

## Literals

### String Literals

Strings are enclosed in double quotes with escape sequence support:

```pml
"hello world"
```

### Integer Literals

64-bit signed integers in decimal notation.

```pml
0
42
-100
9223372036854775807     // max int64
```

### Boolean Literals

```pml
true
false
```

### Array Literals

Arrays are ordered collections of elements:

```pml
[]                      // empty array
["a", "b", "c"]         // []string
[1, 2, 3]               // []int64
["mixed", 42, true]     // []any
```

### Map Literals

Maps are key-value collections:

```pml
{}                              // empty map
{"name": "Alice", "age": "30"}  // map[string]string
{1: "one", 2: "two"}            // map[int64]string
```

---

## Expressions

Expressions produce values and can be composed using operators.

### Variable References

Access a variable's value by its name:

```pml
myVar
```

### Index Expressions

Access elements in maps.

```pml
myMap["key"]            // bracket index
myMap.key               // dot index (string keys only)
```

### Operation Invocation

Call operations with arguments. The argument types must match those of the operation's defined formal parameters.

```pml
operationName(arg1, arg2, arg3)
```

### Arithmetic Expressions

String concatenation using the `+` operator.

```pml
"Hello, " + name + "!"
```

### Comparison Expressions

```pml
a == b          // equality
a != b          // inequality
```

### Logical Expressions

```pml
a && b          // logical AND
a || b          // logical OR
!a              // logical NOT
```

### Parenthesized Expressions

Group expressions to control evaluation order:

```pml
(a + b) == c
!(x && y)
```

---

## Variables

### Variable Declaration with `var`

Declare one or more variables with initial values.

```pml
// Single declaration
var x = "hello"

// Multiple declarations
var (
    x = "hello"
    y = 42
    z = ["a", "b"]
)
```

### Short Declaration

Shorthand for declaring and initializing a variable. Type is inferred from the value.

```pml
x := "hello"
```

### Variable Assignment

Assign a new value to an existing variable. PML is dynamically typed.

```pml
x = "new value"
```

### Compound Assignment

Append to a string variable.

```pml
x += " appended"
```

---

## Control Flow Statements

### If Statement

Conditional execution with optional else-if and else branches.

```pml
if condition {
    // statements
}

if condition {
    // statements
} else {
    // statements
}

if condition1 {
    // statements
} else if condition2 {
    // statements
} else {
    // statements
}
```

### Foreach Statement

Iterate over arrays or maps.

```pml
// Iterate over array elements
foreach element in myArray {
    // element contains each array item
}

// Iterate over map keys
foreach key in myMap {
    // key contains each map key
}

// Iterate over map key-value pairs
foreach key, value in myMap {
    // key contains the map key
    // value contains the map value
}
```

### Break Statement

Exit the innermost loop.

```pml
foreach item in items {
    if item == "stop" {
        break
    }
}
```

### Continue Statement

Skip to the next iteration.

```pml
foreach item in items {
    if item == "skip" {
        continue
    }
	
	// process item
}
```

### Return Statement

Return a value from an operation or a PML block.

```pml
return value
return              // void return
```

---

## Admin Operation Statements

### Set Resource Access Rights

Define the set of valid resource access rights:

```pml
set resource access rights ["read", "write", "execute"]
```

### Create Policy Class

Create a new policy class.

```pml
create PC "pc1"
```

### Create Nodes

Create user attributes (UA), object attributes (OA), users (U), or objects (O). These nodes must have at least one descendant provided in the `in` array.

```pml
create UA "ua1" in ["pc1"]
create OA "oa1" in ["pc1"]
create U "u1" in ["ua1", "ua2"]
create O "o1" in ["oa1"]
```

### Delete Nodes

Delete a node.

```pml
delete node "nodeName"
delete if exists node "nodeName"    // no error if node doesn't exist
```

### Set Node Properties

Set properties on a node.

```pml
set properties of "nodeName" to {"key1": "value1", "key2": "value2"}
```

### Assign

Create assignment.

```pml
assign "childNode" to ["parentNode1", "parentNode2"]
```

### Deassign

Delete assignment:

```pml
deassign "childNode" from ["parentNode1", "parentNode2"]
```
### Associate

Create an association.

```pml
associate "ua1" to "oa1" with ["read", "write"]
```

### Dissociate

```pml
dissociate "ua1" from "oa1"
```

### Create Prohibition

Create a node or process prohibition. Prohibitions can also be conjunctive or disjunctive.

```pml
create conjunctive node prohibition "UserProhibition"
deny "u1" 
arset ["write", "delete"]
include ["oa1"]
exclude ["oa2"]

create disjunctive process prohibition "UAProhibition"
deny "ua1"
arset ["write"]
include ["oa1"]

create conjunctive prohibition "ProcessProhibition"
deny "u1" process "123"
arest ["execute"]
include ["oa1"]
```

### Delete Prohibition

Delete a prohibition by name.

```pml
delete prohibition "ProhibitionName"
delete if exists prohibition "ProhibitionName"
```
### Create Obligation

Create a new obligation.

```pml
create obligation "ObligationName"
when <subject_pattern>
performs <operation_pattern>
do (eventCtx) {
    // response statements
}
```

#### Subject Patterns

Define the obligation subject pattern using 4 available patterns: `any user`, `<username>`, `in <user attribute name>`, `process <process>`.

```pml
when any user           // any user
when user "u1"          // a user
when user in "ua1"      // any user ascendant of a user attribute
when user process "123" // a process
```

**Logical operators** can be used to define the subject pattern.

```pml
when user "u1" || "u2"           // u1 or u2   
when user in "ua1" && !"u2"      // user ascendant of ua1 AND not u2
when user (in "ua1" || in "ua2") // user ascendant of ua1 OR ua2
```

#### Operation Patterns

Define the operation(s) that trigger the obligation. See [Query Operation](#query-operation).

```pml
performs any operation                       // any operation

performs assign                              // specific operation

performs assign on (ascendant, descendants) { // with argument matching
	// this is an anonymous query operation that uses the provided args from the matched operation defined above
	// this query op should return a bool value to indicate if the evnt matches or not.
	// Example:
	
	ascName := name(ascendant)
	descs := getAdjacentDescendants(ascname)
	return contains(descs, "oa1")
}
```

When specifying `on (args)`, the argument names must match formal parameters of the operation. The block must return a `bool` value.

#### Response Block

The obligation response is defined in the `do` block. The event context variable is a `map[string]any` with the following fields:

| Field | Type | Description |
|-------|------|-------------|
| `user` | `string` | The name of the user that triggered the event |
| `process` | `string` | The process identifier (if present, else "") |
| `opName` | `string` | The name of the operation that was performed |
| `args` | `map[string]any` | The arguments passed to the operation |

```pml
do (evt) {
    userName := evt.user
    operationName := evt.opName
    operationArgs := evt.args
    
	// any PML statement    
}
```

### Delete Obligation

```pml
delete obligation "ObligationName"
delete if exists obligation "ObligationName"
```

---

## Operation Definitions

### Formal Parameters

```pml
(string a, int64 b, []string c, map[string]bool d)
```

### @node, @reqcap, and @eventctx Annotations

The `@node` annotation indicates a parameter represents a node or list of nodes. You can specify the required capabilities on the node by passing a comma separated list of known access rights as an argument to the annotation. An annotation with no args indicates that no capabilities are required. 

**Note:** 
- The `@node` annotation can only be used in `adminop`, `resourceop`, and `query` operations.
- Only formal parameters of type `int64`, `int64[]`, `string`, `string[]` can use the `@node` annotation.

```pml
resourceop delete_file(@node string filename) {
    delete node filename
}
```

The `@reqcap` annotation precedes an operation definition, and defines a set of capabilities required to execute the operation.
Multiple `@reqcap` annotations are supported with only one needing to be satisfied to execute the operation.

```pml
@reqcap({
    require ["delete"] on [filename]
})
resourceop delete_file(string filename) { 
    delete node filename
}
```

The `@eventctx` annotation also precedes the operation definition and defines the parameters that will be available in an 
event context as a result of the operation being sent to the EPP. If this annotation is omitted, then the parameters in 
the operation signature are used. You can add additional params as well as omit certain params found in the signature.


```pml
@reqcap({
    require ["create"] on ["files"]
})
@eventctx(string id, filename)
resourceop create_file(string filename) { 
    delete node filename
}
```

In the above example, the id param will not be evaluated during adjudication but will bve available in the response
of an obligation. the filename param will be available in both scenarios.

### Require Statement

To customize the authorization check for an operation, use the `require` statement.
**Note:** The `require` statement can only be used in `adminop`, `resourceop`, and `query` operations.

```pml
require ["read", "write"] on ["targetNode1", "targetNode2"]
```

### Return Types

Operations can declare a return type. If the return type is omitted the return type will be `void`.

```pml
query test() int64 {
    return 42
}

adminop test(string name) string {
    create OA name in ["Objects"]
    return name
}

adminop test(string name) {
    return
}
```

### Operation Types

| Type              | Keyword      | Allowed Statements                                    | Check Statement |
| ----------------- | ------------ |-------------------------------------------------------| --------------- |
| Admin Operation   | `adminop`    | All statements                                        | Yes             |
| Resource Operation | `resourceop` | Basic statements; functions and query operations only | Yes             |
| Query Operation   | `query`      | Basic statements; functions and query operations only | Yes             |
| Routine           | `routine`    | All statements                                        | No              |
| Function     | `function`   | Basic statements; functions only                      | No              |

### Admin Operation

Admin operations modify the policy.

```pml
adminop create_new_user(string username) {  
    check ["assign_to"] on ["users"]        
    
    create u username in ["users"]  
    create oa username + " home" in ["user homes"]    
    create oa username + " inbox" in ["user inboxes"]
}
```

### Resource Operation

Resource operations denote an operation on a resource (object). Optionally, return data to the caller.

```pml
@reqcap({
    require ["read"] on [filename]
})
resourceop read_file(@node string filename) { }

@reqcap({
    require ["read"] on [filename]
})
resourceop read_file(@node string filename) { 
	return getNode(filename)
}
```

### Query Operation

Query operations are read-only operations using the PolicyQuery interface.

```pml
query query1(string a) []string {
    return getAdjacentAscendants(a)
}
```

### Routine

Routines are a series of operations, with access checks on each statement rather than the routine itself or its args.

```pml
routine routine1(string a) {
    create oa "oa1" in [a]
    create oa "oa2" in [a]
    create o "o1" in ["oa1", "oa2"]
}
```

### Function

Functions use only basic statements and can only call other functions. These are utility operations to persist often used processes
that do not access policy information.

```pml
function formatName(string first, string last) string {
    return first + " " + last
}
```

---

## Builtin Operations

PML provides built-in functions and query operations.

### Functions

| Operation | Signature | Description |
|-----------|-----------|-------------|
| `contains` | `contains([]any arr, any element) bool` | Check if array contains element |
| `containsKey` | `containsKey(map[any]any m, any key) bool` | Check if map contains key |
| `append` | `append([]any arr, any element) []any` | Append element to array |
| `appendAll` | `appendAll([]any arr, []any elements) []any` | Append all elements to array |
| `env` | `env(string varName) string` | Get environment variable value |

### Query Operations

| Operation                   | Signature                                                          | Description                            |
| --------------------------- | ------------------------------------------------------------------ | -------------------------------------- |
| `nodeExists`                | `nodeExists(string name) bool`                                     | Check if node exists                   |
| `getNode`                   | `getNode(string name) map[string]any`                              | Get node name, type and properties     |
| `getNodeType`               | `getNodeType(string name) string`                                  | Get node type (PC, UA, OA, U, O)       |
| `getNodeProperties`         | `getNodeProperties(string name) map[string]string`                 | Get node properties                    |
| `hasPropertyKey`            | `hasPropertyKey(string nodeName, string key) bool`                 | Check if node has property key         |
| `hasPropertyValue`          | `hasPropertyValue(string nodeName, string key, string value) bool` | Check if node has property with value  |
| `search`                    | `search(string type, map[string]string props) []map[string]any`    | Search nodes by type and properties    |
| `getAdjacentAscendants`     | `getAdjacentAscendants(string nodeName) []string`                  | Get adjacent ascendant nodes           |
| `getAdjacentDescendants`    | `getAdjacentDescendants(string nodeName) []string`                 | Get adjecent descendant nodes          |
| `getAssociationsWithSource` | `getAssociationsWithSource(string ua) []map[string]any`            | Get associations with ua as the source |
| `getAssociationsWithTarget` | `getAssociationsWithTarget(string target) []map[string]any`        | Get associations target as the target  |
| `name`                      | `name(long id) string`                                             | Get the name of the node with the ID   |
| `id`                        | `id(string name) int64`                                            | Get the ID of the node with the name   |

---

## Example

```pml
// set resource access rights  
set resource access rights ["read", "write"]  
  
// create initial graph config  
create pc "pc1"  
create ua "users" in ["pc1"]  
create ua "admin" in ["pc1"]  
// the admin_user will be created automatically during bootstrapping 
assign "admin_user" to ["admin"]  
associate "admin" to "users" with ["assign_to"]  
  
create oa "user homes" in ["pc1"]  
create oa "user inboxes" in ["pc1"]  
associate "admin" to "user homes" with ["*"]  
associate "admin" to "user inboxes" with ["*"]  
  
// prohibit the admin user from reading inboxes  
create conjunctive node prohibition "deny admin on user inboxes"  
deny "admin"  
arset ["read"]  
include ["user inboxes"]
  
// create resource operation to read a file  
@reqcap({
    require ["read"] on [filename]
})
resourceop read_file(@node string name) { }  
  
// create a custom administration operation  
adminop create_new_user(string username) {  
    check ["assign_to"] on ["users"]   
         
    create u username in ["users"]  
    create oa username + " home" in ["user homes"]    
    create oa username + " inbox" in ["user inboxes"]
}  
  
// - create an obligation on the custom admin operation that when ever a user is created, add an object to their  
// inbox titled "hello " + username  
// - obligations require the use of PML to define responses, so they may be serialized  
// - obligations require an author which we will set as the admin user since they are allowed to perform the  
// operations in the response  
create obligation "o1"  
when any user  
performs create_new_user  
do(ctx) {  
    objName := "welcome " + ctx.args.username    
    inboxName := ctx.args.username + " inbox"    
    create o objName in [inboxName]
}
```