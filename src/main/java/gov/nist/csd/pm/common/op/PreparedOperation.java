package gov.nist.csd.pm.common.op;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class PreparedOperation<T> implements Serializable {

    private Operation<T> op;
    private Map<String, Object> operands;

    public PreparedOperation(Operation<T> op, Map<String, Object> operands) {
        this.op = op;
        this.operands = operands;
    }

    public PreparedOperation() {
    }

    public Operation<T> getOp() {
        return op;
    }

    public void setOp(Operation<T> op) {
        this.op = op;
    }

    public Map<String, Object> getOperands() {
        return operands;
    }

    public void setOperands(Map<String, Object> operands) {
        this.operands = operands;
    }

    public T execute(PAP pap) throws PMException {
        return op.execute(pap, operands);
    }

    public EventContext execute(PAP pap, UserContext userCtx, PrivilegeChecker privilegeChecker) throws PMException {
        // check user can execute op with given operands
        op.canExecute(privilegeChecker, userCtx, operands);

        // execute the op with the given operands
        op.execute(pap, operands);

        return new EventContext(userCtx.getUser(), userCtx.getProcess(), op, operands);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PreparedOperation<?> that = (PreparedOperation<?>) o;
        return Objects.equals(op, that.op) && Objects.equals(operands, that.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, operands);
    }
}
