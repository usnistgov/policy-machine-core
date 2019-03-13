# Policy Machine Core

This project is comprised of the core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard. Provided are APIs to do the following:

- Manage NGAC Graphs in memory
- Query the access state of a graph
- Explain why a user has permissions on a particular resource

## Full Documentation
Full documentation can be found [here](https://pm-master.github.io/pm-master/policy-machine-core/)

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
    <version>1.3.0</version>
</dependency>
```
