set resource access rights ["read", "write"]

create policy class "pc1"
create user attribute "ua1" in ["pc1"]
create user attribute "oa1" in ["pc1"]
associate "ua1" and "oa1" with ["read", "write"]

create policy class "pc2"
create user attribute "ua2" in ["pc2"]
create object attribute "oa2" in ["pc2"]
associate "ua2" and "oa2" with ["read", "write"]

create user "u1" in ["ua1", "ua2"]
create user "u2" in ["ua1", "ua2"]

create object "o1" in ["oa1", "oa2"]

create prohibition "u2-prohibition"
deny user "u2"
access rights ["write"]
on intersection of ["oa1", "oa2"]

create obligation "o1-obligation" {
    create rule "o1-assignment-rule"
    when any user
    performs ["assign"]
    on "o1"
    do(evtCtx) {
        let parent = evtCtx["parent"]
        associate "ua1" and parent with ["read", "write"]
        associate "ua2" and parent with ["read", "write"]
    }
}
