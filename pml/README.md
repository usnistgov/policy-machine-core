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
PML does not currently support boolean operations such as 

```pml

```

### For

### Functions

#### Definitions

#### Invocation

### Set Resource Access Rights
### Create Policy Class
### Create User|Object Attribute
### Create User|Object
### Set Node Properties
### Assign
### Deassign
### Associate
### Dissociate
### Create Prohibition
### Create Obligation
### Delete Node|Prohibition|Obligation|Rule
