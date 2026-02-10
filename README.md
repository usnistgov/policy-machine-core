# Policy Machine Core

The core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard.

## Installation

### Install to maven local
```
git clone https://github.com/usnistgov/policy-machine-core.git

cd policy-machine-core

mvn clean install
```

```
<dependency>
    <groupId>gov.nist.csd.pm</groupId>
    <artifactId>policy-machine-core</artifactId>
    <version>x.y.z</version>
</dependency>
```

### Install using Jitpack
Policy Machine Core uses [JitPack](https://jitpack.io/) to compile and build the artifact to import with maven.

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
  <groupId>com.github.usnistgov</groupId>
  <artifactId>policy-machine-core</artifactId>
  <version>x.y.z</version>
</dependency>
```
## Package Description

- `common` - Objects common to other packages.
- `pap` - Policy Administration Point. Provides the Policy Machine implementation of the NGAC PAP interfaces for modifying and querying policy.
- `pdp` - Policy Decision Point. Implementation of an administrative PDP that controls access to admin operations on the PAP.
- `epp` - Event Processing Point. The epp attaches to a PDP to listen to administrative events while exposing an interface for a PEP to send events.
- `impl` - Policy Machine supported implementations of the PAP interfaces. Included are in memory and embedded Neo4j.

The `pap` package allows you to create NAGC policies without access checks on administrative operations. The `pdp` is designed
to wrap the features of the `pap` and perform access checks on administrative operations. The `epp` package provides a means
of subscribing to PDP events and processing obligations defined in the PAP.

## Getting Started

In general, to start using this library:

```java
// Create a PAP object to modify and query policy information in the PIP
PAP pap = new MemoryPAP();

// Create a PDP object to ensure access checks on administrative operations
PDP pdp = new PDP(pap);

// Create an EPP object that uses the PAP to retrieve obligations and the PDP to execute the obligation responses
EPP epp = new EPP(pdp, pap);

// Subscribe the EPP to the events emitted by the PDP
epp.subscribeTo(pdp);
```

Below are two code snippets to show the basics of creating an NGAC policy with the Policy Machine. The first example
uses the Java API to create and test a policy. The second defines the policy in PML and tests using Java.

### Java

```java
package gov.nist.csd.pm.core.example;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNodeId;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class JavaExample {

    @Test
    void testJavaExample() throws PMException {
        PAP pap = new MemoryPAP();

        // set resource access rights
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

        // create initial graph config
        long pc1Id = pap.modify().graph().createPolicyClass("pc1");
        long usersId = pap.modify().graph().createUserAttribute("users", List.of(pc1Id));
        long adminId = pap.modify().graph().createUserAttribute("admin", List.of(pc1Id));
        pap.modify().graph().createUser("admin_user", List.of(adminId));
        pap.modify().graph().associate(adminId, usersId, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE));

        long userHomes = pap.modify().graph().createObjectAttribute("user homes", List.of(pc1Id));
        long userInboxes = pap.modify().graph().createObjectAttribute("user inboxes", List.of(pc1Id));
        pap.modify().graph().associate(adminId, userHomes, AccessRightSet.wildcard());
        pap.modify().graph().associate(adminId, userInboxes, AccessRightSet.wildcard());

        // prohibit the admin user from reading inboxes
        pap.modify().prohibitions().createNodeProhibition(
            "deny admin on user inboxes",
            adminId,
            new AccessRightSet("read"),
            Set.of(userInboxes),
            Set.of(),
            false
        );

        // create resource operation to read a file
        NodeNameFormalParameter nameFormalParameter = new NodeNameFormalParameter("name");
        ResourceOperation<Void> resourceOp = new ResourceOperation<>("read_file", VOID_TYPE, List.of(nameFormalParameter),
            List.of(new RequiredCapability(new RequiredPrivilegeOnParameter(nameFormalParameter, new AccessRightSet("read"))))) {
            @Override
            public Void execute(PolicyQuery query, Args args) throws PMException {
                return null;
            }
        };
        pap.modify().operations().createOperation(resourceOp);

        /*
        (policyQuery, userCtx, args) -> policyQuery.access()
                .computePrivileges(userCtx, new TargetContext(usersId))
                .contains(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE.toString())))
         */

        // create a custom administration operation
        FormalParameter<String> usernameParam = new FormalParameter<>("username", STRING_TYPE);
        AdminOperation<?> adminOp = new AdminOperation<>("create_new_user", VOID_TYPE, List.of(usernameParam),
            List.of(new RequiredCapability(new RequiredPrivilegeOnNodeId(usersId, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE)))) {

            @Override
            public Void execute(PAP pap, Args args) throws PMException {
                String username = args.get(usernameParam);

                pap.modify().graph().createUser(username, List.of(usersId));
                pap.modify().graph().createObjectAttribute(username + " home", List.of(userHomes));
                pap.modify().graph().createObjectAttribute(username + " inbox", List.of(userInboxes));
                return null;
            }
        };
        pap.modify().operations().createOperation(adminOp);

        // - create an obligation on the custom admin operation that when ever a user is created, add an object to their
        // inbox titled "hello " + username
        // - obligations require the use of PML to define responses, so they may be serialized
        // - obligations require an author which we will set as the admin user since they are allowed to perform the
        // operations in the response
        String pml = """
            create obligation "o1"
            when any user
            performs "create_new_user"
            do(ctx) {
                objName := "welcome " + ctx.args.username
                inboxName := ctx.args.username + " inbox"
                create o objName in [inboxName]
            }
            """;
        pap.executePML(new UserContext(adminId), pml);

        // create a PDP to run transactions
        PDP pdp = new PDP(pap);

        // create an EPP to process events in the EPP and matching obligation responses
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        // adjudicate the admin operation which will cause the EPP to execute the above obligation response
        pdp.adjudicateOperation(new UserContext(adminId), "create_new_user", Map.of("username", "testUser"));

        // check admin operation and obligation response was successful
        assertTrue(pap.query().graph().nodeExists("testUser home"));
        assertTrue(pap.query().graph().nodeExists("testUser inbox"));
        assertTrue(pap.query().graph().nodeExists("welcome testUser"));

        // try to execute the operation as the new testUser, expect unauthorized error
        long testUserId = pap.query().graph().getNodeId("testUser");
        assertThrows(
            UnauthorizedException.class,
            () -> pdp.adjudicateOperation(new UserContext(testUserId), "create_new_user", Map.of("username", "testUser2"))
        );
    }
}
```

### PML

```java
package gov.nist.csd.pm.core.example;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.pdp.bootstrap.PMLBootstrapper;
import org.junit.jupiter.api.Test;

public class PMLExample {
    String pml = """
        // set resource access rights
        set resource access rights ["read", "write"]
        
        // create initial graph config
        create pc "pc1"
        create ua "users" in ["pc1"]
        create ua "admin" in ["pc1"]
        // the admin_user will be created automatically during bootstrapping 
        assign "admin_user" to ["admin"]
        associate "admin" and "users" with ["admin:graph:assignment:descendant:create"]
        
        create oa "user homes" in ["pc1"]
        create oa "user inboxes" in ["pc1"]
        associate "admin" and "user homes" with ["*"]
        associate "admin" and "user inboxes" with ["*"]
        
        // prohibit the admin user from reading inboxes
        create conj node prohibition "deny admin on user inboxes"
        deny "admin"
        arset ["read"]
        include ["user inboxes"]
        
        // create resource operation to read a file
        @reqcap({name: ["read"]})
        resourceop read_file(@node string name) { }
        
        // create a custom administration operation
        adminop create_new_user(string username) {
            check ["admin:graph:assignment:descendant:create"] on ["users"]
        
            create u username in ["users"]
            create oa username + " home" in ["user homes"]
            create oa username + " inbox" in ["user inboxes"]
        }
        
        // - create an obligation on the custom admin operation that when ever a user is created, add an object to their
        // inbox titled "hello " + username
        // - obligations require the use of PML to define responses, so they may be serialized
        // - obligations require an author which we will set as the admin user since they are allowed to perform the
        // operations in the response
        create obligation "o1"
        when any user
        performs "create_new_user"
        do(ctx) {
            objName := "welcome " + ctx.args.username
            inboxName := ctx.args.username + " inbox"
            create o objName in [inboxName]
        }
        """;
    @Test
    void testPMLExample() throws PMException {
        PAP pap = new MemoryPAP();
        // we bootstrap instead of calling pap.executePML because the admin_user needs to exist before executing the PML
        // the call to executePML requires a UserContext with the node id which wouldn't exist yet if the admin_user was created in the PML.
        // Notice the admin_user is only assigned to the "admin" UA in the PML instead of being created.
        // The PMLBootstrapped handles creating the user then executes the PML as the admin_user.
        pap.bootstrap(new PMLBootstrapper("admin_user", pml));

        // create a PDP to run transactions
        PDP pdp = new PDP(pap);

        // create an EPP to process events in the EPP and matching obligation responses
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        // adjudicate the admin operation which will cause the EPP to execute the above obligation response
        long adminId = pap.query().graph().getNodeId("admin");
        pdp.executePML(new UserContext(adminId), """
            create_new_user("testUser")
            """);

        // check admin operation and obligation response was successful
        assertTrue(pap.query().graph().nodeExists("testUser home"));
        assertTrue(pap.query().graph().nodeExists("testUser inbox"));
        assertTrue(pap.query().graph().nodeExists("welcome testUser"));

        // try to execute the operation as the new testUser, expect unauthorized error
        long testUserId = pap.query().graph().getNodeId("testUser");
        assertThrows(
            UnauthorizedException.class,
            () -> pdp.executePML(new UserContext(testUserId), """
                create_new_user("testUser2")
                """)
        );
    }
}
```

## Operations

Operations are a fundamental part of NGAC and the policy-machine-core library. There are 5 types of supported operations:

- Admin Operations: Modify the policy.
- Resource Operations: Represents access on a resource (object).
- Query Operations: Query the policy information.
- Routines: A set of operations, with access checks on each statement rather than the routine itself or its args.
- Functions: Reusable utility operations that do not access the policy.

Only `Admin` and `Resource` operations emit events to the EPP. All operations define a set of [FormalParameters](./src/main/java/gov/nist/csd/pm/core/pap/operation/param/FormalParameter.java).
`Admin`, `Resource`, and `Query` operations can define FormalParameters with RequiredCapabilities which are a set of access rights 
a user needs on the actual argument in the call to the operation in order to successfully execute the operation. The
access rights defined in the RequiredCapabilities must be 
[resource access rights](./src/main/java/gov/nist/csd/pm/core/pap/modification/OperationsModification.java) 
or 
[admin access rights](./src/main/java/gov/nist/csd/pm/core/pap/admin/AdminAccessRights.java).

### Define an operation
Define an admin and resource operation using PML.

```pml
set resource access rights ["read"]

// check for the read access right on the node passed to the filename param
// return a map representing the node name, type, and properties
resourceop read_file(@node("read") string filename) map[string]any {
    return getNode(filename)
}

adminop create_new_user(string username) {  
    check ["assign_to"] on ["users"]        
    
    create u username in ["users"]  
    create oa username + " home" in ["user homes"]    
    create oa username + " inbox" in ["user inboxes"]
}  
```

### Execute operation
```java
PAP pap = new MemoryPAP();
pap.executePML(userCtx, pml);

// or

PDP pdp = new PDP(pap);
pdp.executePML(useCtx, pml);
```

### Obligation example
Now that the operations are persisted in the PIP, they can be executed by the PDP and the resulting events can be processed
by the EPP against any obligations.

```pml
create obligation "o1"  
when any user  
performs read_file on (filename) {
    return filename == "file1.txt"
} 
do(ctx) {  
    // do something
}
```

Now when the PDP executes an operation:

```java
UserContext userCtx = new UserContext(adminUserId);

PDP pdp = new PDP(pap);
pdp.adjudicateResourceOperation(userCtx, "read_file", Map.of("filename", "file1.txt"))
```

It will emit an event:

```json
{
  "user": "<username>",
  "process": "<process>",
  "opName": "read_file",
  "args": {
    "filename": "file1.txt"
  }
}
```

Which matches the obligation's operation pattern: `filename == "file1.txt"` and the EPP will execute the obligation
response.

## Policy Machine Language (PML)

PML is a domain-specific language for defining NGAC access control policies. It provides a declarative syntax that is
easier to use and maintainable than using the Java API. See the full [PML specification](./docs/pml.md).

## JSON Serialization

Policies can be exported and imported using the Policy Machine [JSON schema](./src/main/resources/json/pm.schema.json).

```java
String json = pap.serialize(new JSONSerializer());

// Create new PAP and import policy
PAP newPap = new MemoryPAP();
newPap.deserialize(json, new JSONDeserializer());
```

## Custom Policy Store Implementations

[In memory and Neo4j embedded implementations](/src/main/java/gov/nist/csd/pm/core/impl/) of the 
[PAP](/src/main/java/gov/nist/csd/pm/core/pap/PAP.java)  and [PolicyStore](/src/main/java/gov/nist/csd/pm/core/pap/store/PolicyStore.java) are provided.

To implement a custom policy store:

1. Implement the PolicyStore Interface
```java
public class CustomPolicyStore implements PolicyStore {
    // Implementation details...
}
```

2. Implement Required Store Interfaces
- `GraphStore`
- `ProhibitionsStore`
- `ObligationsStore`
- `OperationsStore`
- `RoutinesStore`

3. Create a Custom PAP that extends [PAP](/src/main/java/gov/nist/csd/pm/core/pap/PAP.java).

See the [MemoryPAP implementation](src/main/java/gov/nist/csd/pm/core/impl/memory/pap/MemoryPAP.java) for a complete example.

## Update Protocol Buffers
```
git submodule update --remote --merge protos
git add protos
git commit -m "Update protos submodule"
git push
```