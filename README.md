# Policy Machine Core [![CircleCI](https://circleci.com/gh/PM-Master/policy-machine-core.svg?style=svg)](https://circleci.com/gh/PM-Master/policy-machine-core)

The core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard.

## Table of Contents
1. [Installation](#install-using-maven)
2. [Basic Usage](#basic-usage)
3. [Policy Author Language (PAL)](/pal/README.md)

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

## Basic Usage

### 1. Policy Author Language (PAL) definition
```java
String pal = """
set resource access rights "read", "write";

create policy class "pc1";
create user attribute "ua1" in "pc1";
create user attribute "oa1" in "pc1";
associate "ua1" and "oa1" with "read", "write";

create policy class "pc2";
assign "ua2" to "pc2";
create user attribute "oa2" in "pc2";
associate "ua2" and "oa2" with "read", "write";

create user "u1" in "ua1", "ua2";
create user "u2" in "ua1", "ua2";

create object "o1" in "oa1", "oa2";

create prohibition "u2-prohibition"
deny user "u2"
access rights "write"
on intersection of "oa1", "oa2";

create obligation "o1-obligation" {
    create rule "o1-assignment-rule"
    when any user
    performs "assign"
    on "o1"
    do(evtCtx) {
        let parent = evtCtx["parent"];
        associate "ua1" and parent with "read", "write";
        associate "ua2" and parent with "read", "write";
    }
}
"""
```

#### 2. Load PAL into a Memory Policy Administration Point (PAP) as the super user
No access checks are done yet, the user is needed to know who the author of any obligations are.
```java
UserContext superUser = new UserContext(SUPER_USER);
PAP pap = new MemoryPAP();
pap.fromString(input, new PALDeserializer(superUser);
```

#### 3. Wrap in a PDP object to add permission checks
```java
PDP pdp = new MemoryPDP(pap);
```

#### 4. Run a PDP Transaction as the super user
This transaction will create 'pc3' and 'oa3', then assign 'o1' to 'oa3'. This will trigger the obligation to associate
'ua1' and 'ua2' with 'oa3'.
```java
pdp.runTx(superUser, (policy) -> {
    policy.graph().createPolicyClass("pc3")
    policy.graph().createObjectAttribute("oa3", "pc2");
    policy.graph().assign("o1", "oa3");
});
```

#### 5. Run a PDP transaction as u1 that will fail
u1 does not have permission to create an object attribute in 'oa1'. This transaction will fail and 'newOA' will not be created.
```java
pdp.runTx(superUser, (policy) -> {
    policy.graph().createObjectAttribute("newOA", "oa1");
});
```
