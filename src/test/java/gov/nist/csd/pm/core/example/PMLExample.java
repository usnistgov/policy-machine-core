package gov.nist.csd.pm.core.example;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.pdp.bootstrap.PMLBootstrapper;
import org.junit.jupiter.api.Test;

public class PMLExample {
    String pml = """
        // set resource access rights
        set resource access rights ["read", "write"]
        
        // create initial graph config
        create pc "pc1"
        create ua "users" in ["pc1"]
        create ua "admin" in ["pc1"]
        // the admin_user will be created automatically during bootstrapping 
        assign "admin_user" to ["admin", "users"]
        associate "admin" and "users" with ["admin:graph:assignment:descendant:create"]
        
        create oa "user homes" in ["pc1"]
        create oa "user inboxes" in ["pc1"]
        associate "admin" and "user homes" with ["*"]
        associate "admin" and "user inboxes" with ["*"]
        
        // prohibit the admin user from reading inboxes
        create conj node prohibition "deny admin on user inboxes"
        deny "admin"
        arset ["read"]
        include ["user inboxes"]
        
        // create resource operation to read a file
        @reqcap({
            require ["read"] on [name]
        })
        resourceop read_file(@node string name) { }
        
        // create a custom administration operation
        @reqcap({
            require ["admin:graph:assignment:descendant:create"] on ["users"]
        })
        adminop create_new_user(string username) {
            create u username in ["users"]
            create oa username + " home" in ["user homes"]
            create oa username + " inbox" in ["user inboxes"]
        }
        
        // - create an obligation on the custom admin operation that when ever a user is created, add an object to their
        // inbox titled "hello " + username
        // - obligations require the use of PML to define responses, so they may be serialized
        // - obligations require an author which we will set as the admin user since they are allowed to perform the
        // operations in the response
        create obligation "o1"
        when any user
        performs "create_new_user"
        do(ctx) {
            objName := "welcome " + ctx.args.username
            inboxName := ctx.args.username + " inbox"
            create o objName in [inboxName]
        }
        """;
    @Test
    void testPMLExample() throws PMException {
        PAP pap = new MemoryPAP();
        // we bootstrap instead of calling pap.executePML because the admin_user needs to exist before executing the PML
        // the call to executePML requires a UserContext with the node id which wouldn't exist yet if the admin_user was created in the PML.
        // Notice the admin_user is only assigned to the "admin" UA in the PML instead of being created.
        // The PMLBootstrapped handles creating the user then executes the PML as the admin_user.
        pap.bootstrap(new PMLBootstrapper("admin_user", pml));

        // create a PDP to run transactions
        PDP pdp = new PDP(pap);

        // create an EPP to process events in the EPP and matching obligation responses
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);


        // adjudicate the admin operation which will cause the EPP to execute the above obligation response
        long adminUserId = pap.query().graph().getNodeId("admin_user");
        pdp.executePML(new UserContext(adminUserId), """
            create_new_user("testUser")
            """);

        // check admin operation and obligation response was successful
        assertTrue(pap.query().graph().nodeExists("testUser home"));
        assertTrue(pap.query().graph().nodeExists("testUser inbox"));
        assertTrue(pap.query().graph().nodeExists("welcome testUser"));

        // try to execute the operation as the new testUser, expect unauthorized error
        long testUserId = pap.query().graph().getNodeId("testUser");
        assertThrows(
            UnauthorizedException.class,
            () -> pdp.executePML(new UserContext(testUserId), """
            create_new_user("testUser2")
            """)
        );
    }
}
