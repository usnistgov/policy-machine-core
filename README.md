# Policy Machine Core

The core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard.
For complete documentation and detailed examples visit the Wiki.

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

## Getting Started

```java
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pdp.PDP;

// create a PAP object to interface with the policy
PAP pap = new MemoryPAP();

// create a PDP to run transactions as users with access checks
PDP pdp = new PDP(pap);

// create an EPP and subscribe to the events published by the PDP to process obligations and respond to events
EPP epp = new EPP(pdp, pap);
epp.subscribeTo(pdp);
```




### 1. Create a PAP

```java
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;

PAP pap = new MemoryPAP();
```

### 2. Set resource access rights

**Java**
```java
pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));
```

**PML**
```pml
set resource access rights ["read", "write"]
```

### 3. Create a resource operation

**Java**
```java
ResourceOperation<?> op1 = new ResourceOperation<>("name", STRING_TYPE, List.of()) {
  @Override
  protected String execute(PolicyQuery query, Args args) throws PMException {
    return "test";
  }
};
```

**PML**
```pml

```






## Basic Usage

```java
package gov.nist.csd.pm.core.example;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import java.util.List;

public class Main {

    public static void main(String[] args) throws PMException {
        // create a new memory PAP
        PAP pap = new MemoryPAP();

        // set the resource operations the policy will support
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

        // create a simple configuration with one of each node type, granting u1 read access to o1.
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1,
            new AccessRightSet("read", "write", "create_object_attribute", "associate", "associate_to"));
        pap.modify().graph().associate(ua2, ua1, new AccessRightSet("associate"));

        // create a prohibition
        pap.modify().prohibitions().createProhibition(
            "deny u1 write on oa1",
            new ProhibitionSubject(u1),
            new AccessRightSet("write"),
            false,
            List.of(new ContainerCondition(oa1, false)));

        // create an obligation that associates ua1 with any OA
        String obligationPML = """
            create obligation "sample_obligation" {
            	create rule "rule1"
            	when any user
            	performs "create_object_attribute"
            	on {
                    descendants: "oa1"
                }
            	do(ctx) {
            		associate "ua1" and ctx.args.name with ["read", "write"]
            	}
            }""";

        // when creating an obligation a user is required
        // this is the user the obligation response will be executed on behalf of
        pap.executePML(new UserContext(u1), obligationPML);

    /*
    Alternatively, create the above policy using PML:

    String pml = """
    set resource operations ["read", "write"]

    create pc "pc1"
    create oa "oa1" in ["pc1"]
    create ua "ua1" in ["pc1"]
    create u "u1" in ["ua1"]
    create o "o1" in ["oa1"]

    associate "ua1" and "oa1" with ["read", "write", "create_object_attribute", "associate", "associate_to"]
    associate "ua2" and "ua1" with ["associate"]

    create prohibition "deny u1 write on oa1"
    deny U "u1"
    access rights ["write"]
    on union of {"oa1": false}

    create obligation "sample_obligation" {
        create rule "rule1"
        when any user
        performs "create_object_attribute"
        on {
        descendants: "oa1"
        }
        do(ctx) {
        associate "ua1" and ctx.args.name with ["read", "write"]
        }
    }
    """;

    pap.executePML(new UserContext("u1")), pml);
    */

        AccessRightSet privileges = pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1));
        System.out.println(privileges);
        // expected output: {associate, read, create_object_attribute, associate_to}

        // create a PDP instance
        PDP pdp = new PDP(pap);

        // create a new EPP instance to listen to admin operations emitted by the PDP
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        // run a tx as u1 that will trigger the above obligation
        pdp.runTx(new UserContext(u1), policy -> {
            policy.modify().graph().createObjectAttribute("oa2", List.of(oa1));
            return null;
        });

        System.out.println(pap.query().graph().getAssociationsWithSource(ua1));
        // expected output: {ua1->oa1{associate, read, create_object_attribute, write, associate_to}, ua1->oa2{read, write}}
    }
}
```
## Creating Prohibitions

Prohibitions can be created by specifying:

- **name**: or unique identifier
- **subject**: (either a user ID, user attribute ID, or process)
- **set**: of access rights to be denied
- **boolean**: flag indicating whether to take the union of the following container conditions
- **container conditions**:
  - attribute ID: the ID of an attribute node
  - complement (boolean): if true, the condition applies to everything outside the specified containers subgraph

```java
pap.modify().prohibitions().createProhibition(
    "deny u1 write on oa1",
    new ProhibitionSubject(u1),
    new AccessRightSet("write"),
    false,
    List.of(new ContainerCondition(oa1, false))
);
```

## Creating Obligations

Obligations should be defined using PML because obligation responses are defined using PML statements. See the full
[Obligation PML specification](./docs/pml.md#create-obligation) for more details

```java
String pml = """
        create obligation "sample_obligation" {
            create rule "rule1"
            when any user
            performs "create_object_attribute"
            on {
                descendants: "oa1"
            }
            do(ctx) {
                associate "ua1" and ctx.args.name with ["read", "write"]
            }
        }""";
pap.executePML(u1, pml);
```

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

## Administrative Functions

The Policy Machine supports custom, parameterized administrative functions for packaging policy modifications into single
transactions. Administrative functions can be defined in Java or PML.

### Operations

Operations are administrative functions with a single privilege check. Once the check is passed, the policy modifications
in the body of the operation are executed without privilege checks.

```java
String pml = """
        operation op1(string a, string[] b) {
            check "read" on [a]
            check "write" on b
        } {
            // PML statements
        }
        """;

pap.executePML(userContext, pml);

// Call the operation
pap.adjudicateAdminOperation(userContext, "op1", Map.of("a", "arg1", "b", List.of("arg2", "arg3")));
```

### Routines

Routines are administrative functions with privilege checks for each modification.

```java
String pml = """
        routine routine1(string a, string[] b) {
            // PML Statements
        }
        """;

pap.executePML(userContext, pml);

// Call the routine
pap.adjudicateAdminRoutine(userContext, "routine1", Map.of("a", "arg1", "b", List.of("arg2", "arg3")));
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
