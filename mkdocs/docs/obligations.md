# Obligations
Obligations are defined using a yaml syntax described below. Examples can be found [here](/examples/#obligations).

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
  anyUser:
  process:
```
The subject specification can be a user, any user, any user from a set of users and/or user attributes, or a process.  If the subject is omitted than all events will match this component of an access event.

#### user
A user is identified by it's name.

#### any_user
```yaml
anyUser:
```
The `any_user` element accepts an array of strings representing user names.  If the element is empty then any user will match.

#### process
```yaml
process:
```
The `process` element accepts a number as a process ID.

_Example:_
```yaml
anyUser: # any user
###
anyUser: # u1 or u2
  - "u1"
  - "u2"
process: 12345
```

### Policy Class
```yaml
policyClass:
  anyOf:
  ---
  eachOf:
```
The policy class specification can specify a particular policy class with a given name, any policy class, any policy class from a set, all policy classes from a set, or all policy classes. Only one of `anyOf` and `eachOf` are allowed.

_Example_
```yaml
###
policyClass: # any policy class
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
  - "op"
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

  - A rule
  - A set of nodes and assignments to containers
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
  subject: # priority goes 1. function, 2. process, 3. node
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
   complement: # true|false, default false
   intersection: # true|false, default false
   containers:
     - name:
       type:
       complement: # true|false, default false
     - function:
       complement: # true|false
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
```

## Functions
There are two main types of functions: utility and administrative.  Utility functions are functions which aid in writing 
and executing obligations.  Administrative functions provide a convenient way of bundling several administrative commands
together.

### Predefined Functions
This is a list of functions that are built into the library. They are all utility functions.

#### child_of_assign
##### Description
Return the node that is the child of the assignment that is the focus of the event.
##### Parameters
None
##### Return
`Node`
##### Event Requirements
The event must **assign**, **assign to**, **deassign** or **deassign from**.
##### Example
```yaml
function:
  name: child_of_assign  
```  
    
#### parent_of_assign
##### Description
Return the node that is the child of the assignment that is the focus of the event.
##### Parameters
None
##### Return
`Node`
##### Event Requirements
The event must **assign**, **assign to**, **deassign** or **deassign from**.
##### Example
```yaml
function:
  name: child_of_assign  
```  

#### create_node
##### Description
Create a new node and return it.
##### Parameters
1. name: string
2. type: string
3. properties: function ([to_props](#to_props))
##### Return
`Node`
##### Event Requirements
None
##### Example
```yaml
function:
  name: create_node
  args:
    - "newNode"
    - "OA"
    - function:
        name: to_props
        args:  
          - "key1=value1"
          - "key2=value2"
```  
   
#### current_process
##### Description
Return the current process ID
##### Parameters
None
##### Return
`long`
##### Event Requirements
None
##### Example
```yaml
function:
  name: current_process  
```  

#### current_target
##### Description
Return the node that is the target of the event being processed
##### Parameters
None
##### Return
`Node`
##### Event Requirements
None
##### Example
```yaml
function:
  name: current_target  
```  

#### current_user
##### Description
Return the user that triggered the event being processed
##### Parameters
None
##### Return
`Node`
##### Event Requirements
None
##### Example
```yaml
function:
  name: current_user 
```  

#### current_user_to_deny_subject
##### Description
Return a `Prohibition.Subject` with the current user.
##### Parameters
None
##### Return
`Prohibition.Subject`
##### Event Requirements
None
##### Example
```yaml
function:
  name: current_user_to_deny_subject 
```  

#### get_children
##### Description
Returns the children of a node.
##### Parameters
1. name: string
2. type: string
3. properties: function ([to_props](#to_props))
##### Return
`List<Node>`
##### Event Requirements
None
##### Example
```yaml
function:
  name: get_children 
  args:
    - "oa1"
    - "OA"
    - function:
        name: to_props
        args:
          - "key=value"
```  

#### get_node
##### Description
Returns the node that matches the given name, type, and properties.
##### Parameters
1. name: string
2. type: string
3. properties: function ([to_props](#to_props))
##### Return
`Node`
##### Event Requirements
None
##### Example
```yaml
function:
  name: get_node 
  args:
    - "oa1"
    - "OA"
    - function:
        name: to_props
        args:
          - "key=value"
```  

#### get_node_name
##### Description
Returns the name of the node that is returned by the function passed as the parameter.
##### Parameters
1. node: function
##### Return
`String`
##### Event Requirements
None
##### Example
```yaml
function:
  name: get_node_name 
  args:
    - function:
        name: current_target
```  

#### is_node_contained_in
##### Description
Returns true if the node passed as the first parameter is assigned to the node passed as the second parameter.  Both parameters
are expected to be functions.
##### Parameters
1. child: function
2. parent: funtion
##### Return
`boolean`
##### Event Requirements
None
##### Example
```yaml
function:
  name: is_node_contained_in 
  args:
    - function:
        name: get_node
        args:
          - "oa1"
          - "OA"
    - function:
        name: get_node
        args:
          - "oa1"
          - "OA"
```  

#### to_props
##### Description
Converts an array of strings with the format <key>=<value> to a Map<String, String>.
##### Parameters
1. strings: array
##### Return
`Map<String, String>`
##### Event Requirements
None
##### Example
```yaml
function:
    name: to_props
    args:  
      - "key1=value1"
      - "key2=value2"
```  

### Custom Functions
To create your own function follow the pattern used in the `gov.nist.csd.pm.epp.functions` package.

#### 1. Implement FunctionExecutor Interface
Create a class that implements the `gov.nist.csd.pm.epp.functions.FunctionExecutor` interface.

```java
/**
 * The name of the function
 * @return the name of the function.
 */
String getFunctionName();

/**
 * How many parameters are expected.
 * @return the number of parameters this function expects
 */
int numParams();

/**
 * Execute the function.
 * @param eventCtx The event that is being processed
 * @param userID The ID of the user that triggered the event
 * @param processID The ID of the process that triggered the event
 * @param pdp The PDP to access the underlying policy data
 * @param function The function information
 * @param functionEvaluator A FunctionEvaluator to evaluate a nested functions
 * @return The object that the function is expected to return
 * @throws PMException If there is any error executing the function
 */
Object exec(EventContext eventCtx, long userID, long processID, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException;
```

#### 2. Provide the EPP with the Function Executor
To make your custom function available to the EPP, use this EPP constructor:
```java
public EPP(PDP pdp, FunctionExecutor ... executors) throws PMException {
    ...
}
```

Any executors that are provided to this constructor will be available to the EPP when processing events. 

## PDP Events
The following events are triggered by the PDP:

- Assign
- Assign to
- Deassign
- Deassign From

For each call to `assign()` and `deassign()` in the PDP, there are two events.  The child is being assigned/deassigned 
and the parent is being assigned to/deassigned from.

These are only the built in events.  Also, the PDP is not the only component that can trigger an event.  The PEP is also 
capable of triggering events of any kind. This is where custom events can be triggered. 