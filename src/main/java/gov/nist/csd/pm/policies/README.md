# Policies

This package will contain all of the higher level functions for any custom NGAC workflows.

---

## RBAC - Role Based Access Control

This is the workflow in which each user gets a role and can get access to protected resources based on that role. More information can be found here. 

### Configuration

- None

### Functions

- Create user
- Create role
- Create object
- Get user roles

---

## DAC - Discretionary Access Control

This is the workflow is where all protected resources must be specifically given to each user. Each user will get its own "home" folder, in which objects will originally reside. Then a user can share a document with another through delegations. More information can be found here.

### Configuration

- Obligation to create the "home folder" nodes for any user assigned to the DAC PC.

### Functions

- Delegation
- Assign owner
