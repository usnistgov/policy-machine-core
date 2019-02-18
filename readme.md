# Policy Machine Core

This project is comprised of the core components of the NIST Policy Machine, a reference implementation of the Next Generation Access Control (NGAC) standard. This library provides APIs to manage NGAC graphs and query the access state of the graph.

## Graph
The `Graph` interface contains the methods necessary to build and traverse an NGAC graph.  An implementation is provided that uses an in-memory data structure to store the graph.  The `GraphSerializer` provides methods to convert a graph to JSON and vice versa.

## Decider
The `Decider` interface provides methods to query the access state of the graph. An implementation of the `Decider` interface called, `PReviewDecider`, is included.  The methods available in `PReviewDecider` allow you to analye a `Graph` and make access decisions according to the NGAC standard. 

## ProhibitionDecider
The `ProhibitionDecider` provides a method of determining the prohibited (denied) permissions a user or process has on a target node.

## PMException
A `PMException` is a generic exception that all methods of the interfaces throw.  This makes it possible to implement the interfaces using any data source that may throw an exception, such as a database.
