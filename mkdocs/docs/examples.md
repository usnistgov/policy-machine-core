## Examples

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
long user1ID = graph.createNode(rand.nextLong(), "u1", U, null);
long user2ID = graph.createNode(rand.nextLong(), "u2", U, null);


// 3. Create the object, `o1` that will be the target of the access queries.
long objectID = graph.createNode(rand.nextLong(), "o1", O, null);


// 4. Create the `RBAC` policy class node.
long rbacID = graph.createNode(rand.nextLong(), "RBAC", PC, null);


// 5. Create an object attribute for the `Accounts`.
long accountsID = graph.createNode(rand.nextLong(), "Accounts", OA, null);


// 6. Create the `Teller` and `Auditor` user attributes.
long tellerID = graph.createNode(rand.nextLong(), "Teller", UA, null);
long auditorID = graph.createNode(rand.nextLong(), "Auditor", UA, null);


// 7. Assign the `Accounts` object attribute to the `RBAC` policy class node.
graph.assign(accountsID, rbacID);


// 8. Assign the object, `o1`, to the `Accounts` object attribute.
graph.assign(objectID, accountsID);


// 9. Assign `u1` to the `Teller` user attribute and `u2` to the `Auditor` user attribute.
graph.assign(user1ID, tellerID);
graph.assign(user2ID, auditorID);


// 10. Create the associations for `Teller` and `Auditor` on `Account` in RBAC. `Teller` has read and write permissions, while `Auditor` just has read permissions.
graph.associate(tellerID, accountsID, new HashSet<>(Arrays.asList("r", "w")));
graph.associate(auditorID, accountsID, new HashSet<>(Arrays.asList("r")));


// 11. Create the `Branches` policy class.
long branchesID = graph.createNode(rand.nextLong(), "branches", PC, null);


// 12. Create an object attribute for `Branch 1`.
long branch1OAID = graph.createNode(rand.nextLong(), "branch 1", OA, null);

// 13. Assign the branch 1 OA to the branches PC
graph.assign(branch1OAID, branchesID);


// 14. Create the `Branch 1` user attribute
long branches1UAID = graph.createNode(rand.nextLong(), "branch 1", UA, null);


// 15. Assign the object, `o1`, to the `Branch 1` object attribute
graph.assign(objectID, branch1OAID);


// 16. Assign the users, `u1` and `u2`, to the branch 1 user attribute
graph.assign(user1ID, branches1UAID);
graph.assign(user2ID, branches1UAID);


// 17. Create an association between the `branch 1` user attribute and the `branch 1` object attribute.
//This will give both users read and write on `o1` under the `branches` policy class.
graph.associate(branches1UAID, branch1OAID, new HashSet<>(Arrays.asList("r", "w")));


// 18. Test the configuration using the `PReviewDecider` implementation of the `Decider` interface.
//The constructor for a `PReviewDecider` receives the graph we created and a list of prohibitions.
//Since no prohibitions are used in this example, we'll pass null.
Decider decider = new PReviewDecider(graph);


// 19. Check that `u1` has read and write permissions on `o1`.
Set<String> permissions = decider.listPermissions(user1ID, objectID);
assertTrue(permissions.contains("r"));
assertTrue(permissions.contains("w"));


// 20. Check that `u1` has read permissions on `o1`.
permissions = decider.listPermissions(user2ID, objectID);
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
long salariesID = graph.createNode(rand.nextLong(), "Salaries", OA, null);
long ssnsID = graph.createNode(rand.nextLong(), "SSNs", OA, null);
long grp1SalariesID = graph.createNode(rand.nextLong(), "Grp1 Salaries", OA, null);
long grp2SalariesID = graph.createNode(rand.nextLong(), "Grp2 Salaries", OA, null);
long publicID = graph.createNode(rand.nextLong(), "Public Info", OA, null);

long bobRecID = graph.createNode(rand.nextLong(), "Bob Record", OA, null);
long bobRID = graph.createNode(rand.nextLong(), "Bob r", OA, null);
long bobRWID = graph.createNode(rand.nextLong(), "Bob r/w", OA, null);

long aliceRecID = graph.createNode(rand.nextLong(), "Alice Record", OA, null);
long aliceRID = graph.createNode(rand.nextLong(), "Alice r", OA, null);
long aliceRWID = graph.createNode(rand.nextLong(), "Alice r/w", OA, null);

// objects for bob's name, salary, and ssn
long bobNameID = graph.createNode(rand.nextLong(), "bob name", O, null);
long bobSalaryID = graph.createNode(rand.nextLong(), "bob salary", O, null);
long bobSSNID = graph.createNode(rand.nextLong(), "bob ssn", O, null);

// objects for alice's name, salary, and ssn
long aliceNameID = graph.createNode(rand.nextLong(), "alice name", O, null);
long aliceSalaryID = graph.createNode(rand.nextLong(), "alice salary", O, null);
long aliceSSNID = graph.createNode(rand.nextLong(), "alice ssn", O, null);

// user attributes
long hrID = graph.createNode(rand.nextLong(), "HR", UA, null);
long grp1MgrID = graph.createNode(rand.nextLong(), "Grp1Mgr", UA, null);
long grp2MgrID = graph.createNode(rand.nextLong(), "Grp2Mgr", UA, null);
long staffID = graph.createNode(rand.nextLong(), "Staff", UA, null);
long bobUAID = graph.createNode(rand.nextLong(), "Bob", UA, null);
long aliceUAID = graph.createNode(rand.nextLong(), "Alice", UA, null);

// users
long bobID = graph.createNode(rand.nextLong(), "bob", U, null);
long aliceID = graph.createNode(rand.nextLong(), "alice", U, null);
long charlieID = graph.createNode(rand.nextLong(), "charlie", U, null);

// policy class
long pcID = graph.createNode(rand.nextLong(), "Employee Records", PC, null);


// assignments
// assign users to user attributes
graph.assign(charlieID, hrID);
graph.assign(bobID, grp1MgrID);
graph.assign(aliceID, grp2MgrID);
graph.assign(charlieID, staffID);
graph.assign(bobID, staffID);
graph.assign(aliceID, staffID);
graph.assign(bobID, bobUAID);
graph.assign(aliceID, aliceUAID);

// assign objects to object attributes
// salary objects
graph.assign(bobSalaryID, salariesID);
graph.assign(bobSalaryID, grp1SalariesID);
graph.assign(bobSalaryID, bobRID);

graph.assign(aliceSalaryID, salariesID);
graph.assign(aliceSalaryID, grp2SalariesID);
graph.assign(aliceSalaryID, aliceRID);

// ssn objects
graph.assign(bobSSNID, ssnsID);
graph.assign(bobSSNID, bobRWID);

graph.assign(aliceSSNID, aliceID);
graph.assign(aliceSSNID, aliceRWID);

// name objects
graph.assign(bobNameID, publicID);
graph.assign(bobNameID, bobRWID);

graph.assign(aliceNameID, publicID);
graph.assign(aliceNameID, aliceRWID);

// bob and alice r/w containers to their records
graph.assign(bobRID, bobRecID);
graph.assign(bobRWID, bobRecID);

graph.assign(aliceRID, aliceRecID);
graph.assign(aliceRWID, aliceRecID);


// assign object attributes to policy classes
graph.assign(salariesID, pcID);
graph.assign(ssnsID, pcID);
graph.assign(grp1SalariesID, pcID);
graph.assign(grp2SalariesID, pcID);
graph.assign(publicID, pcID);
graph.assign(bobRecID, pcID);
graph.assign(aliceRecID, pcID);

// associations
Set<String> rw = new HashSet<>(Arrays.asList("r", "w"));
Set<String> r = new HashSet<>(Arrays.asList("r"));

graph.associate(hrID, salariesID, rw);
graph.associate(hrID, ssnsID, rw);
graph.associate(grp1MgrID, grp1SalariesID, r);
graph.associate(grp2MgrID, grp2SalariesID, r);
graph.associate(staffID, publicID, r);
graph.associate(bobUAID, bobRWID, rw);
graph.associate(bobUAID, bobRID, r);
graph.associate(aliceUAID, aliceRWID, rw);
graph.associate(aliceUAID, aliceRID, r);

// test configuration
// create a decider
// not using prohibitions in this example, so null is passed
Decider decider = new PReviewDecider(graph);

// user: bob
// target: 'bob ssn'
// expected: [r, w]
// actual: [r, w]
Set<String> permissions = decider.listPermissions(bobID, bobSSNID);
assertTrue(permissions.contains("r"));
assertTrue(permissions.contains("w"));

// user: bob
// target: 'bob ssn'
// expected: [r]
// actual: [r]
permissions = decider.listPermissions(bobID, bobSalaryID);
assertTrue(permissions.contains("r"));

// user: bob
// target: 'alice ssn'
// expected: []
// actual: []
permissions = decider.listPermissions(bobID, aliceSSNID);
assertTrue(permissions.isEmpty());

// user: bob
// target: 'alice salary'
// expected: []
// actual: []
permissions = decider.listPermissions(bobID, aliceSalaryID);
assertTrue(permissions.isEmpty());

// user: bob
// target: 'bob ssn'
// expected: [r, w]
// actual: [r, w]
permissions = decider.listPermissions(aliceID, aliceSSNID);
assertTrue(permissions.contains("r"));
assertTrue(permissions.contains("w"));

// user: charlie
// target: 'alice salary'
// expected: [r, w]
// actual: [r, w]
permissions = decider.listPermissions(charlieID, aliceSalaryID);
assertTrue(permissions.contains("r"));
assertTrue(permissions.contains("w"));
```

#### Visualization
Below is a visual representation of the graph created in the employee record example.
[![alt text](images/emprec.png "employee record example")](images/emprec.png)

### Audit
#### Explain
Using the bank teller example described [above](#bank-teller), `auditor.explain(user1ID, objectID)` will result in:
```
RBAC
	u1-Teller-[r,w]-Accounts-o1
branches
	u1-branch 1-[r,w]-branch 1-o1
```
1. `u1` to `o1` via an association `Teller --[r, w]--> Accounts` under `RBAC`
2. `u1` to `o1` via an association `branch 1 --[r, w]--> branch 1` under `branches`

From the returned paths we can deduce that `u1` has `r, w` on `o1`.