# Policy Machine Core

The core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard. For complete documentation and detailed examples visit the [Wiki](https://github.com/PM-Master/policy-machine-core/wiki).
## Importing

### Install using Maven
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
    <groupId>com.github.PM-Master</groupId>
    <artifactId>policy-machine-core</artifactId>
    <version>3.0.0</version>
</dependency>
```
## Package Description

- `policy` - Basic policy interfaces and model.
- `pap` - In memory and MySQL policy stores, as well as a PAP object wrapper to provide transaction support to policy stores.
- `pdp` - Implementation of an administrative Policy Decision Point (PDP). Functionality to bootstrap a policy when creating a new PDP instance.
- `epp` - Implementation of the Event Processing Point (EPP). The epp attaches to a PDP to listen to administrative events while exposing an interface for a PEP to send events.
## PAP Usage
### Create a policy with `Policy` interface
```java
PolicyStore policyStore = new MemoryPolicyStore();
PAP pap = new PAP(policyStore);

pap.setResourceAccessRights(new AccessRightSet("read", "write"))

pap.graph().createPolicyClass("pc1");  
pap.graph().createUserAttribute("ua1", "pc1");  
pap.graph().createObjectAttribute("oa1", "pc1");  
pap.graph().associate("ua1", "oa1", new AccessRightSet("read", "write"));

pap.prohibitions().create(
	"sample_prohibition", 
	new ProhibitionSubject("ua2", USER_ATTRIBUTE), 
	new AccessRightSet("write"), 
	false, 
	new ContainerCondition("oa2", false)
);

// The best way to create obligations is with PML because the responses 
// must be serialized and stored for later execution.
String obligationPML = """
create obligation "sample_obligation" {
	create rule "rule1"
	when any user
	perfroms ["assign_to"]
	on ["oa1"]
	do(ctx) {
		assign ctx.event.child to ["oa2"]
	}
}
""";
pap.executePML(new UserContext("u1")), obligationPML);
```

### Create a policy with `PML`
```java
String pml = """
set resource access rights ["read", "write"]

create policy class "pc1" {
	user attributes {
		"ua1"
	}
	object attributes {
		"oa1"
	}
	associations {
		"ua1" and "oa1" with ["read", "write"]
	}
}

create policy class "pc2" {
	user attributes {
		"ua2"
	}
	object attributes {
		"oa2"
	}
	associations {
		"ua2" and "oa2" with ["read", "write"]
	}
}

create user "u2" in ["ua1", "ua2"]
create object "o1" in ["oa1", "oa2"]

create prohibition "sample_prohibition" 
deny user attribute "ua2" 
access rights ["write"] 
on union of ["oa2"]

create obligation "sample_obligation" {
	create rule "rule1"
	when any user
	perfroms ["assign_to"]
	on ["oa1"]
	do(ctx) {
		assign ctx.event.child to ["oa2"]
	}
}
""";
```

A user is required to execute PML. This user will be the defined author of any obligations created.
```java
// execute the pml and apply to existing policy
pap.executePML(new UserContext("u1")), pml);

// or

// reset the current policy befire applying the PML
pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer())
```

## PDP Usage
### Initialization
```java
PDP pdp = new PDP(pap);
```
### Run a transaction as a user
```java
pdp.runTx(new UserContext("u1"), (policy) -> {
    policy.graph().createPolicyClass("pc3")
    policy.graph().createObjectAttribute("oa3", "pc2");
    policy.graph().assign("o1", "oa3");
});
```

## EPP Usage
An EPP will listen to policy events from the provided PDP and process obligations in the PAP accordingly. The EPP and PDP uses an event listener pattern. The EPP listens to events from the PDP, attaching itself within the EPP constructor.
```java
EPP epp = new EPP(pdp, pap);
```
