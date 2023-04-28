# Policy Machine Language (PML)

The Policy Machine Language (PML) defines a set of statements that can be used to create NGAC graphs, prohibitions, and obligations. 
It also supports logic and control statements such as [if](#if) statements and [for](#for) loops.

## ANTLR4
PML is defined using [ANTLR4](https://www.antlr.org/). The PML grammar is defined [here](/src/main/java/gov/nist/csd/pm/policy/pml/antlr/PML.g4).

To build the ANTLR4 Java files from PML.g4, run:
`mvn clean generate-sources`


## Examples

### Variable declaration
Variable types can be one of:

- string

    ```pml
    "hello world"
    'hello world'
    ```

- boolean

    ```pml
    true
    false
    ```

- number (int)

    ```pml
    123
    ```

- array - element type can be any supported type

    ```pml
    ["1", "2", "3"]
    ```

- map - **key** type must be string, **value** type can be any supported type

    ```
    {
        "key1": "hello",
        "key2": "world"
    }
    
    {
        "key1": ["1", "2", "3"],
        "key2": ["3", "4", "5"]
    }
    ```

- any - type will be determined at runtime, an error may occur if the value is used improperly for it's assigned type
  (i.e. accessing a map key on a string)

#### Examples

```pml
# variable declaration
let a = "hello world"
let b = 123
let c = true
let d = {
    "key1": "hello",
    "key2": "world"
}

# constant declaration
const e = "hello world"
```

### If
PML supports logical operators `&&` and `||` as well as comparison operators `==` and `!=`.

```pml
let x = "a";
let y = "b";
let z = "a";

if x == y {
  # do something
} else if x == z || y == z {
  # do something
} else {
  # do something
}
```

### For

#### Foreach

```pml
let arr = ["1", "2", "3"];
foreach x in arr {
  # do something with x
} 

let m = {"k1": "v1", "k2": "v2"}
foreach key, value in m {
    # do something
    # key = map key
    # value = map value
}
```

#### For range
Iterate from one bound to another. Use `(` or `)` to indicate an exclusive bound and `[` or `]` for an inclusive bound.

```pml
for i in range [0, 100) {
  # do something with i
}
```

### Functions

#### Definition
```pml
function testFunc(string a, map[string]string b, []string c) string {

}
```

#### Invocation
```pml
testFunc("...", {...}, [...])
```

### Set Resource Access Rights
```pml
set resource access rights ["read", "write"]
```

### Create Policy Class
```pml
create policy class 'pc1'
```

### Create User|Object Attribute
```pml
create user attribute 'ua1' in ['pc1', 'pc2']
create object attribute 'oa1' in ['pc1']
```

### Create User|Object
```pml
create user 'u1' in ['ua1', 'ua2']
create object 'o1' in ['oa1']
```

### Set Node Properties
```pml
set properties of 'oa1' to {"key": "value", "key2": "value2"}
```

### Assign
```pml
assign 'u1' to ['ua1', 'ua2']
# or
assign 'u1' to 'ua1'
assign 'u1' to 'ua2'
```

### Deassign
```pml
deassign "u1" from ["ua1", "ua2"];
# or
deassign "u1" from "ua1";
deassign "u1" from "ua2";
```

### Associate
```pml
associate "ua1" and "oa1" with ["read", "write"]
```

### Dissociate
```pml
dissociate "ua1" and "oa1"
```

### Create Prohibition

```pml
# user prohibition
create prohibition 'prohibition1'
deny user 'u1'
access rights ["read"]
on intersection of ["oa1", !"oa2"] # ! denotes complement of attribute

# user attribute prohibition
create prohibition 'prohibition1'
deny user attribute 'ua1'
access rights ["read"]
on union of ["oa1", "oa2"]

# process prohibition
create prohibition 'prohibition1'
deny process '123'
access rights ["read"]
on union of ["oa1", "oa2"]
```

### Create Obligation
An obligation response context `ctx` holds information pertaining to the event that triggered the response. The ctx variable
is of type `map[string]any`.

```pml
create obligation 'obl1' {
  create rule 'rule1'
  when any user
  performs ["assign", assign_to"]
  on "oa1"
  do(ctx) {
    # response defined in PML
    # ctx will have event information depending on the event that triggers this response
    # ctx could be an "assign" event or "assign_to" event
  }
}
```

### Delete Node|Prohibition|Obligation|Rule
```pml
delete node "oa1"

delete prohibition "prohibition1"

delete obligation "obligation1"

delete rule "rule1" from obligation "obligation1"
```

### Example
```pml
set resource access rights ["read", "write"]

create policy class "pc1"
create user attribute "ua1" in ["pc1"]
create user attribute "oa1" in ["pc1"]
associate "ua1" and "oa1" with ["read", "write"]

create policy class "pc2"
create user attribute "ua2" in ["pc2"]
create object attribute "oa2" in ["pc2"]
associate "ua2" and "oa2" with ["read", "write"]

create user "u1" in ["ua1", "ua2"]
create user "u2" in ["ua1", "ua2"]

create object "o1" in ["oa1", "oa2"]

create prohibition "u2-prohibition"
deny user "u2"
access rights ["write"]
on intersection of ["oa1", "oa2"]

create obligation "o1-obligation" {
    create rule "o1-assignment-rule"
    when any user
    performs ["assign"]
    on "o1"
    do(evtCtx) {
        let parent = evtCtx["parent"]
        associate "ua1" and parent with ["read", "write"]
        associate "ua2" and parent with ["read", "write"]
    }
}
```