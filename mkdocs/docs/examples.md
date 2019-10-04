### Serialization
#### Graph
Serialize a graph into a json string.
```
Graph graph = new MemGraph();
---
String json = GraphSerializer.toJson(graph);
```
Deserialize a json string to a graph.
```
Graph graph = GraphSerializer.fromJson(new MemGraph(), json);
```
#### Prohibitions
Serialize a ProhibitionDAO into a json string.
```
ProhibitionsDAO dao = new MemProhibitionsDAO();
---
String json = ProhibitionsSerializer.toJson(dao);
```
Deserialize a json string to a ProhibitionsDAO.
```   
ProhibitionsDAO deDao = ProhibitionsSerializer.fromJson(new MemProhibitionsDAO(), json);
```

### Bank Teller

#### Graph configuration summary

- Users: u1, u2
- An object o1
- Two policy classes: RBAC and Branches
    - RBAC
        - o1 is assigned to accounts
        - u1 is a Teller that has read and write permissions on accounts
        - u2 is an Auditor that has read permissions on accounts
    - Branches
        - u1 and u2 are both assigned to the Branch 1 user attribute
        - o1 is assigned to the Branch 1 object attribute
        - the Branch 1 user attribute has read and write permissions on the Branch 1 object attribute

#### Access control state

- u1 can read and write o1
- u2 can read o1
---

#### Code Wakthrough
```
// 1. Create a new Graph instance.  For this example, we'll use the `MemGraph` which is an in memory implementation of the Graph interface.
Graph graph = new MemGraph();

// 2. Create the user nodes `u1` and `u2`.
Node user1Node = graph.createNode(rand.nextLong(), "u1", U, null);
Node user2Node = graph.createNode(rand.nextLong(), "u2", U, null);


// 3. Create the object, `o1` that will be the target of the access queries.
Node objectNode = graph.createNode(rand.nextLong(), "o1", O, null);


// 4. Create the `RBAC` policy class node.
Node rbacNode = graph.createNode(rand.nextLong(), "RBAC", PC, null);


// 5. Create an object attribute for the `Accounts`.
Node accountsNode = graph.createNode(rand.nextLong(), "Accounts", OA, null);


// 6. Create the `Teller` and `Auditor` user attributes.
Node tellerNode = graph.createNode(rand.nextLong(), "Teller", UA, null);
Node auditorNode = graph.createNode(rand.nextLong(), "Auditor", UA, null);


// 7. Assign the `Accounts` object attribute to the `RBAC` policy class node.
graph.assign(accountsNode.getID(), rbacNode.getID());


// 8. Assign the object, `o1`, to the `Accounts` object attribute.
graph.assign(objectNode.getID(), accountsNode.getID());


// 9. Assign `u1` to the `Teller` user attribute and `u2` to the `Auditor` user attribute.
graph.assign(user1Node.getID(), tellerNode.getID());
graph.assign(user2Node.getID(), auditorNode.getID());


// 10. Create the associations for `Teller` and `Auditor` on `Account` in RBAC. `Teller` has read and write permissions, while `Auditor` just has read permissions.
graph.associate(tellerNode.getID(), accountsNode.getID(), new HashSet<>(Arrays.asList("r", "w")));
graph.associate(auditorNode.getID(), accountsNode.getID(), new HashSet<>(Arrays.asList("r")));


// 11. Create the `Branches` policy class.
Node branchesNode = graph.createNode(rand.nextLong(), "branches", PC, null);


// 12. Create an object attribute for `Branch 1`.
Node branch1OANode = graph.createNode(rand.nextLong(), "branch 1", OA, null);

// 13. Assign the branch 1 OA to the branches PC
graph.assign(branch1OANode.getID(), branchesNode.getID());


// 14. Create the `Branch 1` user attribute
Node branches1UANode = graph.createNode(rand.nextLong(), "branch 1", UA, null);


// 15. Assign the object, `o1`, to the `Branch 1` object attribute
graph.assign(objectNode.getID(), branch1OANode.getID());


// 16. Assign the users, `u1` and `u2`, to the branch 1 user attribute
graph.assign(user1Node.getID(), branches1UANode.getID());
graph.assign(user2Node.getID(), branches1UANode.getID());


// 17. Create an association between the `branch 1` user attribute and the `branch 1` object attribute.
//This will give both users read and write on `o1` under the `branches` policy class.
graph.associate(branches1UANode.getID(), branch1OANode.getID(), new HashSet<>(Arrays.asList("r", "w")));
```

To make access decisions:
```
// 18. Test the configuration using the `PReviewDecider` implementation of the `Decider` interface.
//The constructor for a `PReviewDecider` receives the graph we created and a list of prohibitions.
//Since no prohibitions are used in this example, we'll pass null.
Decider decider = new PReviewDecider(graph);


// 19. Check that `u1` has read and write permissions on `o1`.
Set<String> permissions = decider.listPermissions(user1Node.getID(), objectNode.getID());
assertTrue(permissions.contains("r"));
assertTrue(permissions.contains("w"));


// 20. Check that `u1` has read permissions on `o1`.
permissions = decider.listPermissions(user2Node.getID(), objectNode.getID());
assertTrue(permissions.contains("r"));
```

#### Visualization
Below is a visual representation of the graph created in the bank teller example.
[![alt text](images/bankteller.png "bank teller example")](images/bankteller.png)


### Employee Record

#### Example configuration summary

- One policy class
- Users: bob, alice, charlie
- The objects are bob's and alice's name, salary, and ssn.
- All users are assigned to the Staff user attribute
- The Staff user attribute has read permissions on Public Info, which in this case is names.
- Charlie has the HR attribute
- HR has read and write permissions on Salaries and SSNs
- Bob and Alice have the Grp1Mgr and Grp2Mgr attributes, respectively
- Grp1Mgr and Grp2Mgr have read permissions on Grp1Salaries and Grp2Salaries, respectively
- Bob and Alice have read and write permissions on their name and ssn, and read permissions on their salaries.

#### Access control state

- Alice can read and write her name and SSN, and read her salary, and the salaries of those in Group 2.
- Bob can read and write his name and SSN, and read his salary, and salaries of those in Group 1.
- Charlie can read and write all salaries and SSNs, and read all names.

```
Graph graph = new MemGraph();

// create nodes
// object attributes
Node salariesNode = graph.createNode(rand.nextLong(), "Salaries", OA, null);
Node ssnsNode = graph.createNode(rand.nextLong(), "SSNs", OA, null);
Node grp1SalariesNode = graph.createNode(rand.nextLong(), "Grp1 Salaries", OA, null);
Node grp2SalariesNode = graph.createNode(rand.nextLong(), "Grp2 Salaries", OA, null);
Node publicNode = graph.createNode(rand.nextLong(), "Public Info", OA, null);

Node bobRecNode = graph.createNode(rand.nextLong(), "Bob Record", OA, null);
Node bobRNode = graph.createNode(rand.nextLong(), "Bob r", OA, null);
Node bobRWNode = graph.createNode(rand.nextLong(), "Bob r/w", OA, null);

Node aliceRecNode = graph.createNode(rand.nextLong(), "Alice Record", OA, null);
Node aliceRNode = graph.createNode(rand.nextLong(), "Alice r", OA, null);
Node aliceRWNode = graph.createNode(rand.nextLong(), "Alice r/w", OA, null);

// objects for bob's name, salary, and ssn
Node bobNameNode = graph.createNode(rand.nextLong(), "bob name", O, null);
Node bobSalaryNode = graph.createNode(rand.nextLong(), "bob salary", O, null);
Node bobSSNNode = graph.createNode(rand.nextLong(), "bob ssn", O, null);

// objects for alice's name, salary, and ssn
Node aliceNameNode = graph.createNode(rand.nextLong(), "alice name", O, null);
Node aliceSalaryNode = graph.createNode(rand.nextLong(), "alice salary", O, null);
Node aliceSSNNode = graph.createNode(rand.nextLong(), "alice ssn", O, null);

// user attributes
Node hrNode = graph.createNode(rand.nextLong(), "HR", UA, null);
Node grp1MgrNode = graph.createNode(rand.nextLong(), "Grp1Mgr", UA, null);
Node grp2MgrNode = graph.createNode(rand.nextLong(), "Grp2Mgr", UA, null);
Node staffNode = graph.createNode(rand.nextLong(), "Staff", UA, null);
Node bobUANode = graph.createNode(rand.nextLong(), "Bob", UA, null);
Node aliceUANode = graph.createNode(rand.nextLong(), "Alice", UA, null);

// users
Node bobNode = graph.createNode(rand.nextLong(), "bob", U, null);
Node aliceNode = graph.createNode(rand.nextLong(), "alice", U, null);
Node charlieNode = graph.createNode(rand.nextLong(), "charlie", U, null);

// policy class
Node pcNode = graph.createNode(rand.nextLong(), "Employee Records", PC, null);


// assignments
// assign users to user attributes
graph.assign(charlieNode.getID(), hrNode.getID());
graph.assign(bobNode.getID(), grp1MgrNode.getID());
graph.assign(aliceNode.getID(), grp2MgrNode.getID());
graph.assign(charlieNode.getID(), staffNode.getID());
graph.assign(bobNode.getID(), staffNode.getID());
graph.assign(aliceNode.getID(), staffNode.getID());
graph.assign(bobNode.getID(), bobUANode.getID());
graph.assign(aliceNode.getID(), aliceUANode.getID());

// assign objects to object attributes
// salary objects
graph.assign(bobSalaryNode.getID(), salariesNode.getID());
graph.assign(bobSalaryNode.getID(), grp1SalariesNode.getID());
graph.assign(bobSalaryNode.getID(), bobRNode.getID());

graph.assign(aliceSalaryNode.getID(), salariesNode.getID());
graph.assign(aliceSalaryNode.getID(), grp2SalariesNode.getID());
graph.assign(aliceSalaryNode.getID(), aliceRNode.getID());

// ssn objects
graph.assign(bobSSNNode.getID(), ssnsNode.getID());
graph.assign(bobSSNNode.getID(), bobRWNode.getID());

graph.assign(aliceSSNNode.getID(), aliceNode.getID());
graph.assign(aliceSSNNode.getID(), aliceRWNode.getID());

// name objects
graph.assign(bobNameNode.getID(), publicNode.getID());
graph.assign(bobNameNode.getID(), bobRWNode.getID());

graph.assign(aliceNameNode.getID(), publicNode.getID());
graph.assign(aliceNameNode.getID(), aliceRWNode.getID());

// bob and alice r/w containers to their records
graph.assign(bobRNode.getID(), bobRecNode.getID());
graph.assign(bobRWNode.getID(), bobRecNode.getID());

graph.assign(aliceRNode.getID(), aliceRecNode.getID());
graph.assign(aliceRWNode.getID(), aliceRecNode.getID());


// assign object attributes to policy classes
graph.assign(salariesNode.getID(), pcNode.getID());
graph.assign(ssnsNode.getID(), pcNode.getID());
graph.assign(grp1SalariesNode.getID(), pcNode.getID());
graph.assign(grp2SalariesNode.getID(), pcNode.getID());
graph.assign(publicNode.getID(), pcNode.getID());
graph.assign(bobRecNode.getID(), pcNode.getID());
graph.assign(aliceRecNode.getID(), pcNode.getID());

// associations
Set<String> rw = new HashSet<>(Arrays.asList("r", "w"));
Set<String> r = new HashSet<>(Arrays.asList("r"));

graph.associate(hrNode.getID(), salariesNode.getID(), rw);
graph.associate(hrNode.getID(), ssnsNode.getID(), rw);
graph.associate(grp1MgrNode.getID(), grp1SalariesNode.getID(), r);
graph.associate(grp2MgrNode.getID(), grp2SalariesNode.getID(), r);
graph.associate(staffNode.getID(), publicNode.getID(), r);
graph.associate(bobUANode.getID(), bobRWNode.getID(), rw);
graph.associate(bobUANode.getID(), bobRNode.getID(), r);
graph.associate(aliceUANode.getID(), aliceRWNode.getID(), rw);
graph.associate(aliceUANode.getID(), aliceRNode.getID(), r);

// test configuration
// create a decider
// not using prohibitions in this example, so null is passed
Decider decider = new PReviewDecider(graph);

// user: bob
// target: 'bob ssn'
// expected: [r, w]
// actual: [r, w]
Set<String> permissions = decider.listPermissions(bobNode.getID(), bobSSNNode.getID());
assertTrue(permissions.contains("r"));
assertTrue(permissions.contains("w"));

// user: bob
// target: 'bob ssn'
// expected: [r]
// actual: [r]
permissions = decider.listPermissions(bobNode.getID(), bobSalaryNode.getID());
assertTrue(permissions.contains("r"));

// user: bob
// target: 'alice ssn'
// expected: []
// actual: []
permissions = decider.listPermissions(bobNode.getID(), aliceSSNNode.getID());
assertTrue(permissions.isEmpty());

// user: bob
// target: 'alice salary'
// expected: []
// actual: []
permissions = decider.listPermissions(bobNode.getID(), aliceSalaryNode.getID());
assertTrue(permissions.isEmpty());

// user: bob
// target: 'bob ssn'
// expected: [r, w]
// actual: [r, w]
permissions = decider.listPermissions(aliceNode.getID(), aliceSSNNode.getID());
assertTrue(permissions.contains("r"));
assertTrue(permissions.contains("w"));

// user: charlie
// target: 'alice salary'
// expected: [r, w]
// actual: [r, w]
permissions = decider.listPermissions(charlieNode.getID(), aliceSalaryNode.getID());
assertTrue(permissions.contains("r"));
assertTrue(permissions.contains("w"));
```

#### Visualization
Below is a visual representation of the graph created in the employee record example.
[![alt text](images/emprec.png "employee record example")](images/emprec.png)

### Audit
#### Explain
Using the bank teller example described [above](#bank-teller), `auditor.explain(user1Node.getID(), objectNode.getID())` will result in:
```
operations: [r, w]
policyClasses:
  RBAC
    operations: [r, w]
    paths:
      - u1-Teller-Accounts-o1 ops=[r, w]
  branches
    operations: [r, w]
    paths:
      - u1-branch 1-branch 1-o1 ops=[r, w]

```

### Obligations

#### Obligation YAML
The below obligation yaml creates a rule that when any user assigns anything to oa1, create a new node called "new OA"
and assign it to oa1 **if** the node o1 is assigned to oa1.

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
// add the obligation and enable it
pdp.getPAP().getObligationsPAP().add(obligation, true);
```

#### Processing Event
```java
pdp.getEPP().processEvent(new AssignToEvent(oa1, o1), userID, processID);
```
