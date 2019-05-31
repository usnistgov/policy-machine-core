## PIP
The **Policy Enforcement Point** provides a set of interfaces to persist NGAC policy data.  Also provided, are in memory 
implementations of these interfaces.

### Interfaces

1. Graph - Manage and traverse an NGAC graph.
2. Prohibitions - Manage prohibition relations.
3. Obligations - Manage obligation relations.

## PDP
The **Policy Decision Point** provides the access control logic to requests on the data stored in the [PAP](#pap). 

### Decision Making
#### Access Decisions
#### Prohibition Decisions

### Auditing
#### Explain

#### Creating a Policy Class
#### Creating a Non Policy Class
#### Permissions required for each method
#### Events
##### Assign, deassign, assign to deassign from

## EPP
The **Event Processing Point** is responsible for reacting to events that occur in the NGAC system. The events can be 
triggered in the PDP or the PEP (Policy Enforcement Point, not included in this library).

### Events
#### EventContext

## PAP
The **Policy Administration Point** is responsible for administering the access control policies.  This includes persisting the
data in the PIP and providing the underlying policy data to the PDP and EPP.