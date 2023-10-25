/*
 block comment
*/
// constants
const C1 = "constant1"
const C2 = "constant1"
const C3 = "constant1"

// functions
function f1() string {
    return "hello world"
}

function f2(string s) string {
    return s
}

// graph
set resource access rights ["read", "write"]

// policy class: pm_admin:policy
create object attribute "pc1:target" assign to [POLICY_CLASS_TARGETS]
create object attribute "pc2:target" assign to [POLICY_CLASS_TARGETS]

// policy class: pc1
create policy class "pc1"
create user attribute "ua1" assign to ["pc1"]
set properties of "ua1" to {"k1": "v1", "k": "v"}
create object attribute "oa1" assign to ["pc1"]
associate "ua1" and "oa1" with ["read", "create_policy_class", "write"]
associate "ua1" and PML_FUNCTIONS_TARGET with [create_function]
associate "ua1" and "pc2:target" with [create_user_attribute]

// policy class: pc2
create policy class "pc2"
create user attribute "ua2" assign to ["pc2"]
create object attribute "oa2" assign to ["pc2"]
associate "ua2" and "oa2" with ["read", "write"]

// users
create user "u1" assign to ["ua1", "ua2"]
create user "u2" assign to ["ua1", "ua2"]

// objects
create object "o1" assign to ["oa1", "oa2"]

// prohibitions
create prohibition "u2-prohibition" deny user "u2" access rights ["write"] on intersection of ["oa1", "oa2"]

// obligations
create obligation "o1-obligation" {create rule "o1-assignment-rule" when any user performs ["assign"] on ["o1"] do (evtCtx) {parent := evtCtx["parent"]associate "ua1" and parent with ["read", "write"]associate "ua2" and parent with ["read", "write"] create prohibition "u2-prohibition" deny user "u2" access rights ["write"] on intersection of ["oa1", "oa2"]}}