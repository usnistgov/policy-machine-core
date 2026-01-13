package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.function.Consumer;

/**
 * A custom RequiredCapabilities implementation that allows for a provided function to handle the evaluation of a users
 * privileges to check if the ReqCap is satisfied.
 */
public abstract class RequiredCapabilitiesWithFunction extends RequiredCapabilities {

    private Consumer<Parameters> consumer;

    public RequiredCapabilitiesWithFunction(Consumer<Parameters> consumer) {
        super(new AccessRightSet());

        this.consumer = consumer;
    }

    @Override
    public void check(UserContext user, TargetContext target, AccessRightSet userPrivileges) throws UnauthorizedException {
        consumer.accept(new Parameters(user, target, userPrivileges));
    }

    public static record Parameters(UserContext user, TargetContext target, AccessRightSet userPrivileges) {

    }
}
