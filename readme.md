# Policy Machine Core

This project is comprised of the core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard. This library provides APIs to manage NGAC graphs and query the access state of the graph.

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
    <version>1.0.0</version>
</dependency>
```
