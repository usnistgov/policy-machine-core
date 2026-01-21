package gov.nist.csd.pm.core.example;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
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
        assign "admin_user" to ["admin"]
        associate "admin" and "users" with ["assign_to"]
        
        create oa "user homes" in ["pc1"]
        create oa "user inboxes" in ["pc1"]
        associate "admin" and "user homes" with ["*"]
        associate "admin" and "user inboxes" with ["*"]
        
        // prohibit the admin user from reading inboxes
        create prohibition "deny admin on user inboxes"
        deny u "admin"
        access rights ["read"]
        on union of {"user inboxes": false}
        
        // create resource operation to read a file
        resourceop read_file(@node("read") string name) { }
        
        // create a custom administration operation
        adminop create_new_user(string username) {
            check ["assign_to"] on ["users"]
            
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
        performs create_new_user
        do(ctx) {
            objName := "welcome " + ctx.args.username
            inboxName := ctx.args.username + " inbox"
            create o objName in [inboxName]
        }
        """;
    @Test
    void testPMLExample() throws PMException {
        PAP pap = new MemoryPAP();
        pap.bootstrap(new PMLBootstrapper("admin_user", pml));

        // create a PDP to run transactions
        PDP pdp = new PDP(pap);

        // create an EPP to process events in the EPP and matching obligation responses
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        // adjudicate the admin operation which will cause the EPP to execute the above obligation response
        long adminId = pap.query().graph().getNodeId("admin");
        pdp.executePML(new UserContext(adminId), """
            create_new_user("testUser")
            """);

        // check admin operation and obligation response was successful
        assertTrue(pap.query().graph().nodeExists("testUser home"));
        assertTrue(pap.query().graph().nodeExists("testUser inbox"));
        assertTrue(pap.query().graph().nodeExists("welcome testUser"));

        long testUserId = pap.query().graph().getNodeId("testUser");
        assertThrows(
            UnauthorizedException.class,
            () -> pdp.executePML(new UserContext(testUserId), """
            create_new_user("testUser2")
            """)
        );
    }
}
