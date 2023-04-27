# Policy Machine Language

## Build Antlr4 source files
`mvn clean generate-sources`

## Language Specification

### Types

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

### Variables

```pml

```

### Functions

#### Builtin Functions

#### Java Functions

### For loops

### If statements

### Policy Statements

#### Set Resource Access Rights
#### Create Policy Class
#### Create User|Object Attribute
#### Create User|Object
#### Set Node Properties
#### Assign
#### Deassign
#### Associate
#### Dissociate
#### Create Prohibition
#### Create Obligation
#### Delete Node|Prohibition|Obligation|Rule
