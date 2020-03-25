# Policy Machine Core [![CircleCI](https://circleci.com/gh/PM-Master/policy-machine-core.svg?style=svg)](https://circleci.com/gh/PM-Master/policy-machine-core)

This project is comprised of the core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard. Provided are APIs to do the following:

- Manage NGAC Graphs in memory
- Query the access state of a graph
- Explain why a user has permissions on a particular resource

## Table of Contents
1. [Installation](#install-using-maven)
2. [Code Walkthrough](#code-walkthrough)
3. [Implementation Caveats](#imp)
3. [Interface Usage](#interface-usage)
4. [Functional Component Usage](#functional-component-usage)
5. [Event Response Grammar (Obligations)](https://github.com/PM-Master/policy-machine-core/tree/master/src/main/java/gov/nist/csd/pm/pip/obligations)

## Install using Maven
Policy Machine Core uses [JitPack](https://jitpack.io/) to compile and build the artifact to import into projects.
First, add jitpack as a repository
```xml
<project>
  --
  <repositories>
      <repository>
          <id>jitpack.io</id>
          <url>https://jitpack.io</url>
      </repository>
  </repositories>
  --
</project>
```
Then, add the maven dependency
```xml
<dependency>
    <groupId>com.github.PM-Master</groupId>
    <artifactId>policy-machine-core</artifactId>
    <version>LATEST</version>
</dependency>
```

## Packages
There are 4 main packages in the core library, each representing a functional component in the NGAC architecture. There is 
the Policy Information Point (PIP), the Policy Administration Point(PAP), the Event Processing Point (EPP), and the Policy
Decision Point (PDP). The Policy Enforcement Point is not represented in this library because a PEP is anything that calls
a PDP.

### PIP
The PIP package provides 3 interfaces: `Graph`, `Prohibitions`, and `Obligations` and implementations of each. The obligations 
sub package also includes methods to parse [Event Response](https://github.com/PM-Master/policy-machine-core/tree/master/src/main/java/gov/nist/csd/pm/pip/obligationsd) grammar.
### PAP
The PAP provides a single class that aggregates the elements in the PIP.
### EPP
The EPP processes events in the PDP and exposes functions for processing events in PEPs.
### PDP
The PDP package provides two things:

1. The functional component class `PDP` which is an administrative decision point for an underlying `PAP`.  The goal of this 
class is to provide access control to the Graph, Prohibitions, and Obligations of the PAP.
2. The `Decider` and `Auditor` interfaces and implementations for query the access state of a graph and explaining *why* a user has
access to a node, respectively.

## Implementation Caveats
The `PIP` package provides a bare bones implementation of an NGAC system, without any extra "fluff".  The `PDP` package however, 
has a little more "fluff" that we decided were important to our reference implementation but are not required.

### No Authentication
The PDP does not have an authentication layer on top of it, users are not forced to use a specific authentication scheme. 
Instead the goal of this library is to provide all the functional pieces of an NGAC system, which should be put together in a secure manner based on individual use cases and needs.

### Super Policy
What came first the graph or the access control?

In other words who has permission to create the first policy class or the first user? We ran into this question during development of the PDP.  If we put a layer of access control above the PAP,
assuming every method is protected, how do we allow a user to make administrative changes to the graph that doesn't exist yet?

Our solution was to incorporate a "super" user, and a policy surrounding this user. The super policy is created in the PAP that is supplied to the PDP during PDP creation.
It contains a single policy class called "super_pc", with attributes "super_ua1", "super_ua2", and "super_oa1".  The reason for two "super" user attributes is to provide the super user
permissions on itself, so it can assign itself to other attributes.  The super user is given "*" or All Ops on everything in the graph.
There is also a super policy class rep that is assigned to "super_oa1" that will be explained in the following sections.

**Note: the super user has a hard coded name "super" an the PDP does not have authentication as explained above.**

### Policy Class Creation
While using the PDP, in order to create a policy class, the requesting user must have the CREATE_POLICY_CLASS permission on the 
representative of the super policy class.  This is an object attribute created with the super policy explained above. This allows
the super user to create policy classes as well as delegate policy class creation given the nature of the super policy. 

### Policy Class Representatives
Policy class representative attributes provide a means of controlling access to policy class nodes, since policy classes 
themselves cannot be the target of an access decision, these rep attributes provide a means to do so. 
When a policy class is created a representative attribute is also created and assigned to super_oa1. Any time a policy 
class is the target of an access decision, this rep attribute will be used instead.

## Interface Usage
The following are examples of using the interfaces and implementations provided in the PIP and PDP packages. These classes
provide the basic functionality of an NAGC system.

### Graph
```java
Graph graph = new MemGraph();

Node pc1 = graph.createPolicyClass("pc1", Node.toProperties("k", "v"));
Node oa1 = graph.createNode("oa1", NodeType.OA, Node.toProperties("k1", "v1"), pc1.getName());
Node oa2 = graph.createNode("oa2", NodeType.OA, Node.toProperties("k1", "v1"), pc1.getName());
Node ua1 = graph.createNode("ua1", NodeType.UA, Node.toProperties("k1", "v1"), pc1.getName());
Node o1 = graph.createNode("o1", O, Node.toProperties("k", "v"), oa1.getName(), oa2.getName());
Node u1 = graph.createNode("u1", NodeType.U, Node.toProperties("k", "v"), ua1.getName());

graph.associate(ua1.getName(), oa1.getName(), new OperationSet(READ, WRITE));
```

### Prohibitions
The `Prohibitions` interface provides methods for storing prohibitions.
```java
Prohibitions prohibitions = new MemProhibitions();
Prohibition prohibition = new Prohibition.Builder("test-prohibition", ua1.getName(), new OperationSet(WRITE))
        .addContainer(oa1.getName(), false)
        .addContainer(oa2.getName(), true)
        .build();
prohibitions.add(prohibition);
```
    
### Decider and Auditor
The `Decider` interface provides functions for making access decisions on a graph.    
```java 
Decider decider = new PReviewDecider(graph, prohibitions);
Set<String> permissions = decider.list(u1.getName(), "", o1.getName());
System.out.println(permissions);
```

The `Auditor` interface provides a function to explain why a user has access to a node. Currently, prohibitions are not
taken into account in the explanation.
```java
Auditor auditor = new PReviewAuditor(graph);
Explain explain = auditor.explain(u1.getName(), o1.getName());
System.out.println(explain);
```

## Functional Component Usage
### Policy Information Point (PIP)
The PIP package provides the necessary interfaces (and in memory implementations) for managing an NGAC graph,
prohibitions, and obligations.

```java
Graph graph = new MemGraph();
Prohibitions prohibitions = new MemProhibitions();
Obligations obligations = new MemObligations();

// add some nodes, assignments, and associations to the graph
// create a policy class
Node pc1 = graph.createPolicyClass("pc1", Node.toProperties("k", "v"));
// create an object and user attribute and assign to pc1
Node oa1 = graph.createNode("oa1", NodeType.OA, Node.toProperties("k1", "v1"), pc1.getName());
Node ua1 = graph.createNode("ua1", NodeType.UA, Node.toProperties("k1", "v1"), pc1.getName());
// create and object and user
Node o1 = graph.createNode("o1", O, Node.toProperties("k", "v"), oa1.getName());
Node u1 = graph.createNode("u1", NodeType.U, Node.toProperties("k", "v"), ua1.getName());
// associate ua1 and oa1
graph.associate(ua1.getName(), oa1.getName(), new OperationSet(READ, WRITE, ASSIGN, ASSIGN_TO));

// add a prohibition
Prohibition prohibition = new Prohibition.Builder("test-prohibition", "ua1", new OperationSet(WRITE))
        .addContainer("oa1", false)
        .build();
prohibitions.add(prohibition);

// *note: obligations will be demonstrated in another tutorial
```

### Policy Administration Point (PAP)
The PAP provides a single class that holds a graph, prohibitions, and obligations, to be used by the PDP

```java
PAP pap = new PAP(graph, prohibitions, obligations);
```

### Policy Decision Point (PDP)
The PDP implements the same interfaces in the PIP but provides a layer of access control to restrict access to the administrative commands of the PAP.

```java
PDP pdp = new PDP(pap, new EPPOptions());

// access the PDP's GraphService (which sits in front of the Graph made earlier) as u1
// we'll provide an empty process identifier for this example
UserContext userCtx = new UserContext(u1.getName(), "");
Graph graphService = pdp.getGraphService(userCtx);

// create a new node through the PDP with the UserContext
Node newNode = graphService.createNode("newNode", O, null, oa1.getName());

// access the PDP as the super user and create a new prohibition
userCtx = new UserContext("super", "");
Prohibitions prohibitionsService = pdp.getProhibitionsService(userCtx);
prohibition = new Prohibition.Builder("new-prohibition", "ua1", new OperationSet(ASSIGN_TO))
        .addContainer("oa1", false)
        .build();
prohibitionsService.add(prohibition);

// get the permissions for u1 on newNode
userCtx = new UserContext(u1.getName(), "");
AnalyticsService analyticsService = pdp.getAnalyticsService(userCtx);

// permissions should be [read, assign]
Set<String> permissions = analyticsService.getPermissions(newNode.getName());
```

### Event Processing Point (EPP)
The below obligation yaml creates a rule that when any user assigns anything to oa1, create a new node called "new OA"
and assign it to oa1 **if** the node o1 is assigned to oa1.

#### YAML
```yaml
label: test
rules:
  - label: rule1
    event:
      subject:
      operations:
        - assign to
      target:
        policyElements:
          - name: oa1
            type: OA
    response:
      condition:
        - function:
            name: is_node_contained_in
            args:
              - function:
                  name: get_node
                  args:
                    - o1
                    - O
              - function:
                  name: get_node
                  args:
                    - oa1
                    - OA
      actions:
        - create:
            what:
              - name: new OA
                type: OA
                properties:
                  k: v
            where:
              - name: oa1
                type: OA
```

#### Loading Obligation
```java
InputStream is = getClass().getClassLoader().getResourceAsStream("obligation.yml");
Obligation obligation = EVRParser.parse(is);

Obligations obligations = new MemObligations();

PDP pdp = new PDP(graph, new MemProhibitions(), obligations);
// add the obligation and enable it
pdp.getPAP().getObligationsPAP().add(obligation, true);
```

#### Processing Event
```java
pdp.getEPP().processEvent(new AssignToEvent(oa1, o1), userID, processID);
```
