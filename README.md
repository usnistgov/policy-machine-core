# Policy Machine Core [![CircleCI](https://circleci.com/gh/PM-Master/policy-machine-core.svg?style=svg)](https://circleci.com/gh/PM-Master/policy-machine-core)

This project is comprised of the core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard. Provided are APIs to do the following:

- Manage NGAC Graphs in memory
- Query the access state of a graph
- Explain why a user has permissions on a particular resource

## Table of Contents
1. [Installation](#install-using-maven)
2. [Code Walkthrough](#packages)
3. [Implementation Caveats](#implementation-caveats)
4. [Basic Usage](#basic-usage)
5. [Functional Component Usage](#functional-component-usage)
6. [Event Response Grammar (Obligations)](https://github.com/PM-Master/policy-machine-core/tree/master/src/main/java/gov/nist/csd/pm/pip/obligations)
7. [Custom Obligations](#custom-obligations)

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
sub package also includes methods to parse [Event Response](https://github.com/PM-Master/policy-machine-core/tree/master/src/main/java/gov/nist/csd/pm/pip/obligations) grammar.
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

## Basic Usage
The following are examples of using the interfaces and implementations provided in the PIP and PDP packages. These classes
provide the basic functionality of an NAGC system.

### Graph
```java
Graph graph = new MemGraph();

Node pc1 = graph.createPolicyClass("pc1", Node.toProperties("k", "v"));
Node oa1 = graph.createNode("oa1", NodeType.OA, Node.toProperties("k1", "v1"), pc1.getName());
Node oa2 = graph.createNode("oa2", NodeType.OA, Node.toProperties("k1", "v1"), pc1.getName());
Node ua1 = graph.createNode("ua1", NodeType.UA, Node.toProperties("k1", "v1"), pc1.getName());
Node o1 = graph.createNode("o1", O, Node.toProperties("k", "v"), oa1.getName());
Node u1 = graph.createNode("u1", NodeType.U, Node.toProperties("k", "v"), ua1.getName());

graph.assign(o1.getName(), oa2.getName());

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

### Graph and Prohibitions Serialization

#### Graph
Graphs have built in functions to serialize and deserialize graphs from json.
```java
String json = graph.toJson();
graph.fromJson(json);
```
The `toJson` method returns a json string representation of the graph. The `fromJson` method loads the nodes, assignments, 
and associations into a graph.

Graphs also support a more readable format of configuration using the `GraphSerializer` interface.
```java
GraphSerializer serializer = new MemGraphSerializer(graph);
String serialized = serializer.serialize();

serializer = new MemGraphSerializer(new MemGraph());
serializer.deserialize(serialized);
```

#### Prohibitions
```java
ProhibitionsSerializer.toJson(prohibitions);
ProhibitionsSerializer.fromJson(prohibitions, json);
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
Use the static PDP.newPDP() method to create a new PDP and initialize it's EPP reference.  The static method is used because there
is a circular dependency between the PDP and EPP and a specific series of steps is required to properly initialize both.

The *EPP* will be available via `pdp.getEPP()`
```java
PDP pdp = PDP.newPDP(pap, eppOptions, resourceOps)

// access the PDP's GraphService (which sits in front of the Graph made earlier) as u1
// we'll provide an empty process identifier for this example
UserContext userCtx = new UserContext(u1.getName(), "");
Graph graphService = pdp.getGraphService(userCtx);

// create a new node through the PDP with the UserContext
Node newNode = graphService.createNode("newNode", O, null, oa1.getName());

// access the PDP as the super user and create a new prohibition to prohibit any user in ua1 the permission ASSIGN_TO on oa1
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

PDP pdp = PDP.newPDP(pap, eppOptions, resourceOps)
// add the obligation and enable it
pdp.getObligationsServiceadd(obligation, true);
```

#### Processing Event
```java
pdp.getEPP().processEvent(new AssignToEvent(oa1, o1), userID, processID);
```

### Custom Obligations

#### Custom Events
Custom events can be done in four steps:

1. Extend the [EventPattern](src/main/java/gov/nist/csd/pm/pip/obligations/model/EventPattern.java) class. 
2. Implement the [EventParser](src/main/java/gov/nist/csd/pm/pip/obligations/evr/EventParser.java) interface in order to parse the yaml of the custom event.
3. Pass the EventParser implementation to the `EVRParser` constructor.  
4. Extend the [EventContext](src/main/java/gov/nist/csd/pm/epp/events/EventContext.java) class and override the
`matchesPattern` method.
5. Call `epp.processEvent` passing the custom EventContext.

##### Example

 1. Extend `EventPattern`.

    ```java
    public class TestEventPattern extends EventPattern {
    
        private List<String> strings;
    
        public TestEventPattern() {
            strings = new ArrayList();
        }
    
        public List<String> getStrings() {
            return times;
        }
    
        public void setStrings(List<String> strings) {
            this.strings = strings;
        }
    
        public void addString(String string) {
            this.strings.add(string);
        }
    }
    ```

 2. Implement `EventParser`. For this example, we will create a custom event that accepts an array of strings.

    ```java
    public class TestEventParser implements EventParser {
    
        @Override
        public String key() {
            return "test_event";
        }
    
        @Override
        public EventPattern parse(Map map) throws EVRException {
            if (!map.containsKey(key())) {
                throw new EVRException("test event requires test_event key");
            }
    
            // expect an array of strings
            Object o = map.get(key());
            List list = EVRParser.getObject(o, List.class);
    
            TestEventPattern pattern = new TestEventPattern();
            for (Object obj : list) {
                String str = String.valueOf(EVRParser.getObject(obj, Object.class));
                pattern.addString(str);
            }
    
            return pattern;
        }
    }
    ```

    The YAML would look like:
    
    ```yaml
    label: test_event
    rules:
      - label: test_rule
        event:
          time:
            - "theString"
            - "aString1"
            - "aString2"
        response:
        ...
    ```

3. Pass the custom event parser to the `EVRParser` constructor.

    ```java
    EVRParser parser = new EVRParser(Arrays.asList(new TestEventParser()), null); // the null parameter is for custom responses
    ```

4. Extend `EventContext`. For this example, the test event pattern will match if the given string is contained in the pattern's list of strings.

    ```java
    public class TestEventContext extends EventContext {
    
        private String theString;
    
        public TimeEventContext(UserContext userCtx, String theString) {
            super(userCtx, "test_event", null);
    
            this.theString = theString;
        }
    
        @Override
        public boolean matchesPattern(EventPattern event, Graph graph) {
            if (!(event instanceof TestEventPattern)) {
                return false;
            }
    
            TestEventPattern testEventPattern = (TestEventPattern)event;
            List<String> strings = testEventPattern.getStrings();
            return strings.contains(theString);
        }
    
    }
    ```

5. Processing the custom event.

    ```java
    epp.processEvent(new TestEventContext(new UserContext("aUser"), "theString"));
    ```

#### Custom Responses
Custom responses can be done in four steps:

1. Extend the [ResponsePattern](src/main/java/gov/nist/csd/pm/pip/obligations/model/ResponsePattern.java) class and override 
the `apply` method.
2. Implement the [ResponseParser](src/main/java/gov/nist/csd/pm/pip/obligations/evr/ResponseParser.java) interface in order to parse the yaml of the custom response.
3. Pass the ResponseParser implementation to the `EVRParser` constructor.

#### Example

1. Extend `ResponsePattern`.

    ```java
    public class TestResponsePattern extends ResponsePattern {
    
       @Override
       public void apply(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator, UserContext definingUser,
                        EventContext eventCtx, Rule rule, String obligationLabel) throws PMException {
           // do something
       }
    
    }
    ```
   
2. Implement `ResponseParser`.

    ```java
   public class TestResponseParser implements ResponseParser {
       @Override
       public String key() {
           // system responses are handled in the parser and don't need a key
           return "";
       }

       public ResponsePattern parse(Map map) throws EVRException {   
           ResponsePattern responsePattern = new TestResponsePattern();
   
           // parse yaml map
       
           return responsePattern;
       }
   }
   ```
   
3. Pass the custom response parser to the `EVRParser` constructor.

    ```java
    EVRParser parser = new EVRParser(null, Arrays.asList(new TestResponseParser())); // the null parameter is for custom events
    ```
