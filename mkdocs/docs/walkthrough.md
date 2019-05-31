## PIP
The **Policy Enforcement Point** provides a set of interfaces to persist NGAC policy data.  Also provided, are in memory
implementations of these interfaces.

### Graph
The Graph interface provides methods to manage and traverse an NGAC graph.
```
Graph graph = new MemGraph();
```
This will create a new instance of the in memory implementation of the Graph interface. Now it can be populated with nodes, assignments, and associations.

```
graph.createNode(1234, "newNode", NodeType.O, null));
```
This will add a node with the ID 1234, name "newNode", type object, and no properties to the graph.

```
graph.assign(1234, NodeType.O), 4321, NodeType.OA));
```
Assuming a node with the ID 4321 and type OA have been created in the graph, this will assign the node with ID 1234 to the node with ID 4321.

```
graph.associate(2222, NodeType.UA), 4321, NodeType.OA), new HashSet<>(Arrays.asList("read", "write")));
```
Assuming a user attribute node is created with the ID 2222, this will associate it with the object attribute that has the ID 4321, and give the operations read and write.

### Prohibitions
The Prohibitions interface provides a set of methods to maintain Prohibition relations.

### Obligations
The Obligations interface provides a set of methods to maintain Obligation relations.

## PDP
The **Policy Decision Point** provides the access control logic to requests on the data stored in the [PAP](#pap).

### Decision Making
#### Access Decisions
#### Prohibition Decisions

### Auditing
#### Explain

#### Creating a Policy Class
#### Creating a Non Policy Class
#### Permissions required for each method
#### Events
##### Assign, deassign, assign to deassign from

## EPP
The **Event Processing Point** is responsible for reacting to events that occur in the NGAC system. The events can be
triggered in the PDP or the PEP (Policy Enforcement Point, not included in this library).

### Events
#### EventContext
#### Functions

## PAP
The **Policy Administration Point** is responsible for administering the access control policies.  This includes persisting the
data in the PIP and providing the underlying policy data to the PDP and EPP.
