# Obligations

## Common Elements
### Nodes
A node represents a node in an NGAC graph. A node has a name, type, and properties. A node can also be derived from a function.
```yaml
name:
type:
properties:
  - key: value
```

### function
A function refers to a previously defined function that is supported by the Policy Machine Event Processing Point (EPP).  A list of valid functions, as well as tutorial on how to add functions can be found [here](#functions).

_Example_
```yaml
function:
  name:
  args:
    - ""
    - function:
```
A function has a name and a list of arguments. The arguments are a list of string values or other functions.


##  Obligation
There is one obligation per yaml file. An obligation can have zero or more rules.
```yaml
label:
rules:
```
- **_label_** - A label to give the obligation.  If one is not specified, then a random value will be used.
- **_rules_** - Contains a set of zero or more rules.

##  Rule
```yaml
label:
event:
response:
```
- **_label_** - A label to give the rule.  If one is not specified a random value will be used.
- **_event_** - The event pattern for this rule.
- **_response_** - The response to the event.

## Event Pattern
```yaml
event:
  subject:
  policyClass:
  operations:
  target:
```
The Event Pattern specifies an event involving the policy elements of the Policy Machine.  An example is a user performing a read operation on an object.  This is called an access event, which is the primary focus of obligations as described in the NGAC standard. An access event has four components: The subject, policy class, operations, and target.  All of these are optional, but omitting them will have different consequences, which will be described in the sections below.

While the Policy Machine focuses on access events, it is possible to extend the functionality of the Event Pattern to other events such as time.  The section [How to Extend the Event Pattern](#how-to-extend-the-event-pattern) section provides a tutorial on how this is possible with the Policy Machine.

### Subject
```yaml
subject:
  user:
  any_user:
  process:
```
The subject specification can be a user, any user, any user from a set of users and/or user attributes, or a process.  If the subject is omitted than all events will match this component of an access event.

#### user
A user is identified by it's name.

#### any_user
```yaml
any_user:
```
The `any_user` element accepts an array of strings representing user names.  If the element is empty then any user will match.

#### process
```yaml
process:
```
The `process` element accepts a number as a process ID.

_Example:_
```yaml
any_user: # any user
###
any_user: # u1 or u2
  - "u1"
  - "u2"
process: 12345
```

### Policy Class
```yaml
policy_class:
  anyOf:
  ---
  eachOf:
```
The policy class specification can specify a particular policy class with a given name, any policy class, any policy class from a set, all policy classes from a set, or all policy classes. Only one of `anyOf` and `eachOf` are allowed.

_Example_
```yaml
###
policyClass: # a;; policy class
###
policyClass: # PC1 or PC2
  anyOf:
    - "PC1"
    - "PC2"
###
policyClass: # PC1 and PC2
  eachOf:
    - "PC1"
    - "PC2"
```

### Operations
```yaml
operations:
  - "operation_name"
```
The operations specification is a string array of operation names. Any event that matches an element of the array will match the operations event pattern.

_Example:_
```yaml
operations:
  - "read"
  - "write"
```

### Target
The target of an event can be

- A specific policy element

```yaml
policyElements:
  - name: name
    type: type
```

- Any policy element
```yaml
policyElements:
```    
  \* Omitting `policyElements` will have the same effect

- Any policy element that is contained in other policy elements
```yaml
containers:
  - name:
    type:
  - name:
    type:
```

- Any policy element from a set of policy elements
```yaml
policyElements:
  - name: name
    type: type
  - name: name
    type: type
```

- If both `policyElements` and `containers` are omitted it will be "any policyElement in any container"
- If `containers` is present then it will be "any policyElement in the containers", regardless of if policyElements is present
- If `policyElements` is present its "any policyElement from the list provided"

## Response
A response is a series of conditional actions. A condition can also be applied to the response itself.

### Condition
A condition is a set of boolean expressions that if all evaluate to true, allow for a response or specific action to be executed.

```yaml
condition:
  - function:
  - function:
```

### Create Action
Create
1. A rule
2. A set of nodes and assign them to a set of containers
#### rule
```yaml
create:
  rule:
    label:
    event:
    response:
```

#### nodes
Creating nodes requires the name and type of the nodes to create and the containers in which to assign them. A node can be specified with a set of properties if applicable.
```yaml
create:
  what:
    - name: node1
      type: UA
  where:
    - name: container1
      type: UA
```

### Assign Action
```yaml
assign:
  what:
  where:
```
The `what` and `where` elements are arrays of nodes.  The nodes in `what` will be assigned to the nodes in `where`.

### Grant Action
Associate each node in `subjects` with each node in `targets`.

```yaml
grant:
  subjects:
  operations:
  targets:
```

- `subjects` is an array of nodes that will be the subject of the associations created.
- `operations` is an array of operations to add to the associations.
- `targets` is an array of nodes that will be the targets of the associations.

### Deny Action
Deny a subject a set of operations on a set of target attributes. The subject can be a function, a process, or a node.
The operations are an array of string.  The target of the deny can be the intersection of a set of containers. It can also be the complement of the logical evaluation of the containers. Each container is identified by a name and type (properties are optional).  If more than one node matches the provided name and type all will be taken into account. It is possible to take the complement of an individual container using the `complement` element.

```yaml
deny:
  subject: priority goes 1. function, 2. process, 3. node
   function:
   ---
   process:
   ---
   name:
   type:
   properties:
 operations:
   - ""
   - ""
 target:
   complement: true|false, default false
   intersection: true|false, default false
   containers:
     - name:
       type:
       complement: true|false, default false
     - function:
       complement: true|false
```

### Delete Action
Delete

- assignment relations
- deny relations
- grant relations
- created policy elements
- created rules

```yaml
delete:
  create:
  assign:
  grant:
  deny:
``` s



## Functions
### Predefined Functions
1. current_user
2. current_process
### How to add a function
## How to Extend the Event Pattern
