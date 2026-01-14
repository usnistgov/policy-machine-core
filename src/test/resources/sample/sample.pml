set resource access rights ["read", "write", "delete_project", "delete_readme"]

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

create U "us_reader1" in ["reader", "US user"]
create U "us_writer1" in ["writer", "US user"]

create U "eu_reader1" in ["reader", "EU user"]
create U "eu_writer1" in ["writer", "EU user"]

createProject("us_project1", "US project")
createProject("eu_project1", "EU project")

routine deleteAllProjects(string locProjectOA) {
    foreach project in getAdjacentAscendants(locProjectOA) {
        deleteReadme(project + " README")
        deleteProject(project)
    }
}

adminop deleteReadme(node projectReadme) {
    check "delete_readme" on [projectReadme]

    delete node projectReadme
}

adminop deleteProject(node projectName) {
    check "delete_project" on [projectName]

    delete node projectName
}

adminop createProject(string projectName, node locProjectAttr) {
   check "assign_to" on ["project"]
   check "assign_to" on [locProjectAttr]

   create oa projectName in ["project", locProjectAttr]
   create o projectName + " README" in [projectName]
}

adminop createProjectAdmin(string projectName) {
    uaName := projectName + " admin"
    create UA uaName in ["writer"]
    associate uaName and projectName with ["*"]

    create prohibition "deny admin delete README"
    deny UA uaName
    access rights ["delete_readme"]
    on union of {projectName: false}
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