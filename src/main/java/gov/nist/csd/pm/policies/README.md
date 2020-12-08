# Policies

This package will contain all of the higher level functions for any custom NGAC workflows.

---

## RBAC - Role Based Access Control

This is the workflow in which each user gets a role and can get access to protected resources based on that role. More information can be found here. 

### Configuration

- rbac_default_ua
- rbac_default_oa

### Functions

- Create User - Create a new user and assign it to a role
- Create Role - Create a new role
- Assign Role - Assign user to corresponding role node
- Remove Role - Deassign user from corresponding role node
- Get User Roles - Get parents of user in the RBAC PC
- Set User Roles - Set parents of user in the RBAC PC
- Give Role Permissions - Create association from role to give objects with given permissions

---

## DAC - Discretionary Access Control

This is the workflow is where all protected resources must be specifically given to each user. Each user will get its own "home" folder, in which objects will originally reside. Then a user can share a document with another through delegations. More information can be found here.

### Configuration

- dac_users - all users must get assigned here
- dac_objects
- Obligation to create the "home folder" nodes for any user assigned to the DAC PC.

### Functions

- Delegation (delegator, delegatee, ops, targets) - Delegates the designated object to another user. This is all done from the context of the suer, not the administrator.
    - Prequesites:
        1. All of the target types must be consistent (ether all OA/O or all UA/A)
        2. Delegator must be a U
        3. Delegatee must either be a UA or U
        4. Delegator must have "create association" and "assign" access rights on delegatee
        5. Delegator must have "create association", "assign" and all given ops on all targets
- Assign owner - adds the given object or user to the owner's "home folder".
