package gov.nist.csd.pm.core.common.event;

import java.util.Map;
import java.util.Objects;

public record EventContext(EventContextUser user, String opName, Map<String, Object> args) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventContext that)) {
            return false;
        }
        return Objects.equals(opName, that.opName) && Objects.equals(user, that.user)
            && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, opName, args);
    }
}
