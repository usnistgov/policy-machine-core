/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.example;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.epp.EPP;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pdp.PDP;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.pdp.bootstrap.PMLBootstrapper;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

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
        associate "admin" to "users" with ["admin:graph:assignment:descendant:create"]
        
        create oa "user homes" in ["pc1"]
        create oa "user inboxes" in ["pc1"]
        associate "admin" to "user homes" with ["*"]
        associate "admin" to "user inboxes" with ["*"]
        
        // prohibit the admin user from reading inboxes
        create conj node prohibition "deny admin on user inboxes"
        deny "admin"
        arset ["read"]
        include ["user inboxes"]
        
        // create resource operation to read a file
        @ReqCap({
            require ["read"] on [name]
        })
        resourceop read_file(@Node string name) { }
        
        // create a custom administration operation
        @ReqCap({
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
        pdp.executePML(NodeUserContext.of(adminUserId), """
            create_new_user(username="testUser")
            """);

        // check admin operation and obligation response was successful
        assertTrue(pap.query().graph().nodeExists("testUser home"));
        assertTrue(pap.query().graph().nodeExists("testUser inbox"));
        assertTrue(pap.query().graph().nodeExists("welcome testUser"));

        // try to execute the operation as the new testUser, expect unauthorized error
        long testUserId = pap.query().graph().getNodeId("testUser");
        assertThrows(
            UnauthorizedException.class,
            () -> pdp.executePML(NodeUserContext.of(testUserId), """
            create_new_user(username="testUser2")
            """)
        );
    }
}
