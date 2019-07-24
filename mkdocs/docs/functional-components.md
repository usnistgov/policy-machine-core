## PIP
The Policy Enforcement Point (PIP) package provides a set of interfaces for storing NGAC policy data and in memory implementations of each interface. The three interfaces **Graph**, **Prohibitions**, **Obligations** are explained below.

### Graph
The `Graph` interface provides the set of functions necessary for maintaining and traversing an NGAC graph.

#### MemGraph
MemGraph is an in memory implementation of the `Graph` interface called. The underlying structure is a directed acyclic graph which stores the node IDs as nodes in the graph.  Edges represent assignments and associations.

```
Graph graph = new MemGraph();
```
This will create a new instance of the in memory implementation of the Graph interface. Now it can be populated with
nodes, assignments, and associations.

```
Node node = graph.createNode(1234, "newNode", NodeType.O, null));
```
This will add a node with the ID 1234, name "newNode", type object, and no properties to the graph.

```
graph.assign(1234, NodeType.O), 4321, NodeType.OA));
```
Assuming a node with the ID 4321 and type OA have been created in the graph, this will assign the node with ID 1234 to the
node with ID 4321.

```
graph.associate(2222, NodeType.UA), 4321, NodeType.OA), new HashSet<>(Arrays.asList("read", "write")));
```
Assuming a user attribute node is created with the ID 2222, this will associate it with the object attribute that has the
ID 4321, and give the operations read and write.

### Prohibitions
The `Prohibitions` interface provides functions to maintain a list of prohibition relations.

#### MemProhibitions
`MemProhibitions` is an in memory implementation of the `Prohibitions` interface. The `Prohibition`
model object is stored in a `List`. The following is an example of creating a `Prohibition` and adding it to a `MemProhibitions` instance:
```
Prohibitions prohibitions = new MemProhibitions();

Prohibition prohibition = new Prohibition();
prohibition.setName("denyName");
prohibition.setIntersection(true);
prohibition.setOperations(new HashSet<>(Arrays.asList("read", "write")));
prohibition.setSubject(new Prohibition.Subject(1234, Prohibition.Subject.Type.USER));
prohibition.addNode(new Prohibition.Node(4321, false));

prohibitions.add(prohibition);
```

### Obligations
The `Obligations` interface provides functions to maintain a list of obligation relations. The obligation implementation is a little more complex than the prohibitions and will be explained in the [obligations](/obligations) section.

## PAP
The Policy Administration Point (PAP) provides a means for administering policies to the underlying data in the PIP.  This package is very simple as it only sets up a middle man between the PDP/EPP and the PIP.

### PAP Functional Entity
The PAP object is made up of a `Graph`, `Prohibitions`, and `Obligations`. It provides and single
administration point for an NGAC system. When a `PAP` is initialized a call to `SuperGraph.check()` is called on the `Graph` parameter. This call ensures that the super configuration is complete in the provided graph.

## PDP
The Policy Decision Point (PDP) provides three main functionalities:

1. Interfaces for querying the access state of an NGAC graph.
2. An interface for auditing an NGAC graph.
3. A PDP as a functional entity.

### Decider
The `decider` package contains two interfaces `Decider` and `ProhibitionDecider`.

- `Decider` - methods to make access decisions.
- `ProhibitionDecider` - methods to determine any prohibited permissions.

Implementations of both of these interfaces are provided as `PReviewDecider` and `MemProhibtionDecider`.

### Auditor
The `auditor` package contains an interface that can be used to audit an NGAC graph. The interface has one method called `explain` which can explain **why** a user has permissions on a target node. An implementation called `PReviewAuditor` is provided.

### PDP Functional Entity
The PDP functional entity leverages the interfaces described above to control access to an underlying PAP.
```
PDP pdp = new PDP(
  new PAP(
    new MemGraph(),
    new MemProhibitions(),
    new MemObligations(),
  )
);
```

## EPP
Coming soon...
