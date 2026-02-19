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

@reqcap({
    require ["delete_readme"] on [projectReadme]
})
adminop deleteReadme(@node string projectReadme) {
    delete node projectReadme
}

@reqcap({
    require ["delete_project"] on [projectName]
})
adminop deleteProject(@node string projectName) {
    delete node projectName
}

@reqcap({
    require ["assign_to"] on [locProjectAttr]
})
adminop createProject(string projectName, @node string locProjectAttr) {
   create oa projectName in ["project", locProjectAttr]
   create o projectName + " README" in [projectName]
}

adminop createProjectAdmin(string projectName) {
    uaName := projectName + " admin"
    create UA uaName in ["writer"]
    associate uaName and projectName with ["*"]

    create conj node prohibition "deny admin delete README"
    deny uaName
    arset ["delete_readme"]
    include [projectName]
}

create obligation "create us project admin"
    when any user
    performs "createProject" on (locProjectAttr) {
      return locProjectAttr == "US project"
    }
    do(ctx) {
        createProjectAdmin(ctx.args.projectName)
    }

create obligation "create eu project admin"
    when any user
    performs "createProject" on (locProjectAttr) {
        return locProjectAttr == "EU project"
    }
    do(ctx) {
        createProjectAdmin(ctx.args.projectName)
    }