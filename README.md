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
    <version>3.0.0-alpha.0</version>
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
    <version>v3.0.0-alpha.0</version>
</dependency>
```
## Package Description

- `pap` - Policy Administration Point. Provides the Policy Machine implementation of the NGAC PAP interfaces for modifying and querying policy.
- `pdp` - Policy Decision Point. Implementation of an administrative PDP that controls access to admin operations on the PAP.
- `epp` - Event Processing Point. The epp attaches to a PDP to listen to administrative events while exposing an interface for a PEP to send events.
- `impl` - Policy Machine supported implementations of the PAP interfaces.

## Basic Usage
The following examples use the provided in memory PAP.

```java
package gov.nist.csd.pm;

import epp.gov.nist.csd.pm.core.EPP;
import pap.memory.impl.gov.nist.csd.pm.core.MemoryPAP;
import pap.gov.nist.csd.pm.core.PAP;
import exception.common.gov.nist.csd.pm.core.PMException;
import relationship.graph.common.gov.nist.csd.pm.core.AccessRightSet;
import prohibition.common.gov.nist.csd.pm.core.ContainerCondition;
import prohibition.common.gov.nist.csd.pm.core.ProhibitionSubject;
import context.model.query.pap.gov.nist.csd.pm.core.UserContext;
import context.model.query.pap.gov.nist.csd.pm.core.TargetContext;
import pdp.gov.nist.csd.pm.core.PDP;

import java.util.List;

public class Main {

	public static void main(String[] args) throws PMException {
		// create a new memory PAP
		PAP pap = new MemoryPAP();

		// set the resource operations the policy will support
		pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));

		// create a simple configuration with one of each node type, granting u1 read access to o1.
		pap.modify().graph().createPolicyClass("pc1");
		pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
		pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
		pap.modify().graph().createUser("u1", List.of("ua1", "ua2"));
		pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
		pap.modify().graph().createObject("o1", List.of("oa1"));

		pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read", "write", "create_object_attribute", "associate", "associate_to"));
		pap.modify().graph().associate("ua2", "ua1", new AccessRightSet("associate"));

		// create a prohibition
		pap.modify().prohibitions().createProhibition(
				"deny u1 write on oa1",
				ProhibitionSubject.userAttribute("u1"),
				new AccessRightSet("write"),
				false,
				List.of(new ContainerCondition("oa1", false)));

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
		pap.executePML(new UserContext("u1"), obligationPML);


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
		deny user "u1"
		access rights ["write"]
		on union of ["oa1"]

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

		AccessRightSet privileges = pap.query().access().computePrivileges(new UserContext("u1"), new TargetContext("o1"));
		System.out.println(privileges);
		// expected output: {associate, read, create_object_attribute, associate_to}

		// create a PDP instance
		PDP pdp = new PDP(pap);

		// create a new EPP instance to listen to admin operations emitted by the PDP
		EPP epp = new EPP(pdp, pap);

		// run a tx as u1 that will trigger the above obligation
		pdp.runTx(new UserContext("u1"), policy -> {
			policy.modify().graph().createObjectAttribute("oa2", List.of("oa1"));
			return null;
		});

		System.out.println(pap.query().graph().getAssociationsWithSource("ua1"));
		// expected output: {ua1->oa1{associate, read, create_object_attribute, write, associate_to}, ua1->oa2{read, write}}
	}
}
```
