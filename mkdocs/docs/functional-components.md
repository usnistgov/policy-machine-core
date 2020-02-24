## PIP
The Policy Enforcement Point (PIP) package provides a set of interfaces for storing NGAC policy data and in memory implementations of each interface. The three interfaces **Graph**, **Prohibitions**, **Obligations** are explained below.

### Graph
The `Graph` interface provides the set of functions necessary for maintaining and traversing an NGAC graph.

#### MemGraph
MemGraph is an in memory implementation of the `Graph` interface. The underlying structure is a directed acyclic graph which stores the node IDs as nodes in the graph.  Edges represent assignments and associations.

```
Graph graph = new MemGraph();
```
This will create a new instance of the in memory implementation of the Graph interface. Now it can be populated with
nodes, assignments, and associations.

A characteristic of NGAC graphs is that all nodes must be connected.  You can start building a graph with Policy Classes by calling
```
Node pc1 = graph.createPolicyClass(id, "PC1", null)
```

Once a policy class is created, Object and User Attributes can then be created and assigned to the policy class
```
Node ua = graph.createNode(id, "UA1", NodeType.UA, null, pc1.getID());
Node oa = graph.createNode(id, "OA1", NodeType.OA, null, pc1.getID());
```

It is possible to create a node and assign it to more than one parent
```
Node oa1 = graph.createNode(id, "OA1", NodeType.OA, null, pc1.getID());
Node oa2 = graph.createNode(id, "OA2", NodeType.OA, null, pc1.getID());

Node o = graph.createNode(id, "O1", NodeType.O, null, oa1.getID(), oa2.getID());
```

The `createNode` method only requires one initial parent node, but assignments can still be made later on
```
graph.assign(o1.getID(), oa3.getID());
```

To associate two nodes
```
graph.associate(ua1.getID(), oa1.getID(), new OperationSet("read", "write")));
```
This will associate the ua1 and oa1 nodes with read and write operations.

### Prohibitions
The `Prohibitions` interface provides functions to maintain a list of prohibition relations.

#### MemProhibitions
`MemProhibitions` is an in memory implementation of the `Prohibitions` interface. Prohibitions are stored with respect to the subject of the prohibition.
 This allows for efficient look up later on. The following is an example of creating a `Prohibition` and adding it to a `MemProhibitions` instance:
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
The PAP object is made up of a `Graph`, `Prohibitions`, and `Obligations`. It provides and single administration point for an NGAC system.

## PDP
The Policy Decision Point (PDP) provides three main functionalities:

1. Interfaces for querying the access state of an NGAC graph.
2. An interface for auditing an NGAC graph.
3. A PDP as a functional entity.

### Decider
The `decider` package contains the `Decider` interface which provides methods to query the access state of an NGAC graph, and an implementation of this interface called `PReviewDecider` (short for Policy Review Decider).

#### Usage
The `PReviewDecider` receives a graph and prohibitions
```java
Decider decider = new PReviewDecider(graph, prohibitions);
```

Access decisions can then be made using one of the several available methods
```java
Set<String> permissions = decder.list(userID, processID, targetID)
```


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
