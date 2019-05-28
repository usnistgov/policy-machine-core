## Usage

### Creating a Graph
```
Graph graph = new Graph();
```
This will create a new instance of the in memory implementation of the Graph interface. Now it can be populated with
nodes, assignments, and associations.

```
graph.createNode(1234, "newNode", NodeType.O, null));
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

**Examples of creating graphs are provided [here](/examples/#examples)**

### Access Decisions
To make an access decision, instantiate a `PReviewDecider` which implements the `Decider` interface. The interface provides
several methods to query the current access state of the graph.
```
Decider decider = new PReviewDecider(graph);
decider.listPermissions(userID, NO_PROCESS, targetID);
```
The `listPermissions` method returns the permissions a user has on a target node.

### Audit
#### Explain
Explain answers the question why does a user have access to a given target node? To perform this, instantiate a new
`PReviewAuditor` and call the `explain` method.
```
Auditor auditor = new PReviewAuditor(graph);
Explain explain = auditor.explain(1234, 4321);
```
The result of the explain method is as follows.
```
public class Explain {
    private Set<String>              permissions;
    private Map<String, PolicyClass> policyClasses;
    ...
}
```
Provided in the result are the permissions the user has on the target node and a map containing the paths from the user to the target under each policy class.
A PolicyClass object looks like:
```
public class PolicyClass {
    private Set<String> operations;
    private List<Path> paths;
    ...
}
```
The PolicyClass object stores the operations granted to the user under a policy class and the paths that provide those operations.
A Path is just a sequence of nodes, starting at the user and ending at the target node:
```
public class Path {
    private Set<String> operations;
    private List<Node> nodes;
    ...
}
```
Also provided is the set of operations that the specific path contributes to the overall set for the policy class.

Here is the output of an `explain` example.
```
operations: [read]
policyClasses:
  - pc2:
      operations: [read]
      paths:
        - u1-ua2-oa2-o1 ops=[read]
  - pc1:
      operations: [read, write]
      paths:
        - u1-ua1-oa1-o1 ops=[read, write]
```


**See an example [here](/examples/#explain)**
