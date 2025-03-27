set resource operations ["read", "write", "delete_project", "delete_readme"]

create pc "RBAC"

    create UA "employee" in ["RBAC"]
    create UA "reader" in ["employee"]
    create UA "writer" in ["reader"]
    create UA "deleter" in ["employee"]

    create OA "project" in ["RBAC"]

    associate "reader" and "project" with ["read"]
    associate "writer" and "project" with ["write"]
    associate "deleter" and "project" with ["write"]

create pc "Location"

    create UA "US user" in ["Location"]
    create UA "EU user" in ["Location"]

    create OA "US project" in ["Location"]
    create OA "EU project" in ["Location"]

    associate "US user" and "US project" with ["*"]
    associate "EU user" and "EU project" with ["*"]

create user "us_reader1" in ["reader", "US user"]
create user "us_writer1" in ["writer", "US user"]

create user "eu_reader1" in ["reader", "EU user"]
create user "eu_writer1" in ["writer", "EU user"]

createProject("us_project1", "US project")
createProject("eu_project1", "EU project")

routine deleteAllProjects(string locProjectOA) {
    foreach project in getAdjacentAscendants(locProjectOA) {
        deleteReadme(project + " README")
        deleteProject(project)
    }
}

operation deleteReadme(@node string projectReadme) {
    check "delete_readme" on projectReadme
} {
    delete node projectReadme
}

operation deleteProject(@node string projectName) {
    check "delete_project" on projectName
} {
    delete node projectName
}

operation createProject(string projectName, @node string locProjectAttr) {
   check "assign_to" on "project"
   check "assign_to" on locProjectAttr
} {
    create oa projectName in ["project", locProjectAttr]
    create o projectName + " README" in [projectName]
}

operation createProjectAdmin(string projectName) {
    uaName := projectName + " admin"
    create UA uaName in ["writer"]
    associate uaName and projectName with ["*"]

    create prohibition "deny admin delete README"
    deny user attribute uaName
    access rights ["delete_readme"]
    on union of [projectName]
}

create obligation "create us project admin" {
    create rule "us project"
    when any user
    performs "createProject"
    on {
        locProjectAttr: "US project"
    }
    do(ctx) {
        createProjectAdmin(ctx.args.projectName)
    }

    create rule "eu project"
    when any user
    performs "createProject"
    on {
        locProjectAttr: "EU project"
    }
    do(ctx) {
        createProjectAdmin(ctx.args.projectName)
    }
}