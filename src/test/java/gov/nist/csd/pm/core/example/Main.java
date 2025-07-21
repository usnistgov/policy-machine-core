package gov.nist.csd.pm.core.example;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import java.util.List;

public class Main {

    public static void main(String[] args) throws PMException {
        // create a new memory PAP
        PAP pap = new MemoryPAP();

        // set the resource operations the policy will support
        pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));

        // create a simple configuration with one of each node type, granting u1 read access to o1.
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1, ua2));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long o1 = pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().graph().associate(ua1, oa1, new AccessRightSet("read", "write", "create_object_attribute", "associate", "associate_to"));
        pap.modify().graph().associate(ua2, ua1, new AccessRightSet("associate"));

        // create a prohibition
        pap.modify().prohibitions().createProhibition(
            "deny u1 write on oa1",
            new ProhibitionSubject(u1),
            new AccessRightSet("write"),
            false,
            List.of(new ContainerCondition(oa1, false)));

        // create an obligation that associates ua1 with any OA
        String obligationPML = """
				create obligation "sample_obligation" {
					create rule "rule1"
					when any user
					performs "create_object_attribute"
					on {
				        descendants: "oa1"
				    }
					do(ctx) {
						associate "ua1" and ctx.args.name with ["read", "write"]
					}
				}""";

        // when creating an obligation a user is required
        // this is the user the obligation response will be executed on behalf of
        pap.executePML(new UserContext(u1), obligationPML);

		/*
		Alternatively, create the above policy using PML:

		String pml = """
		set resource operations ["read", "write"]

		create pc "pc1"
		create oa "oa1" in ["pc1"]
		create ua "ua1" in ["pc1"]
		create u "u1" in ["ua1"]
		create o "o1" in ["oa1"]

		associate "ua1" and "oa1" with ["read", "write", "create_object_attribute", "associate", "associate_to"]
		associate "ua2" and "ua1" with ["associate"]

		create prohibition "deny u1 write on oa1"
		deny U "u1"
		access rights ["write"]
		on union of ["oa1"]

		create obligation "sample_obligation" {
		    create rule "rule1"
		    when any user
		    performs "create_object_attribute"
		    on {
			descendants: "oa1"
		    }
		    do(ctx) {
			associate "ua1" and ctx.args.name with ["read", "write"]
		    }
		}
		""";

		pap.executePML(new UserContext("u1")), pml);
		*/

        AccessRightSet privileges = pap.query().access().computePrivileges(new UserContext(u1), new TargetContext(o1));
        System.out.println(privileges);
        // expected output: {associate, read, create_object_attribute, associate_to}

        // create a PDP instance
        PDP pdp = new PDP(pap);

        // create a new EPP instance to listen to admin operations emitted by the PDP
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        // run a tx as u1 that will trigger the above obligation
        pdp.runTx(new UserContext(u1), policy -> {
            policy.modify().graph().createObjectAttribute("oa2", List.of(oa1));
            return null;
        });

        System.out.println(pap.query().graph().getAssociationsWithSource(ua1));
        // expected output: {ua1->oa1{associate, read, create_object_attribute, write, associate_to}, ua1->oa2{read, write}}
    }
}
