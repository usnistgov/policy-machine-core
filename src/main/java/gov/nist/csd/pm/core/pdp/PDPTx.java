package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.AdminFunction;
import gov.nist.csd.pm.core.pap.function.arg.NoArgs;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.obligation.ObligationResponse;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.core.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.core.pdp.modification.PolicyModificationAdjudicator;
import gov.nist.csd.pm.core.pdp.query.PolicyQueryAdjudicator;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.*;

public class PDPTx extends PAP {

    final PAP pap;

    private final UserContext userCtx;
    private final PDPEventPublisher eventPublisher;

    private final PolicyModificationAdjudicator pdpModifier;
    private final PolicyQueryAdjudicator pdpQuerier;

    public PDPTx(UserContext userCtx, PAP pap, List<EventSubscriber> epps) throws PMException {
        super(pap);

        this.userCtx = userCtx;
        this.pap = pap;
        this.eventPublisher = new PDPEventPublisher(epps);
        this.pdpModifier = new PolicyModificationAdjudicator(this.userCtx, this.pap, this.eventPublisher);
        this.pdpQuerier = new PolicyQueryAdjudicator(this.pap, this.userCtx);
    }

    @Override
    public PolicyModificationAdjudicator modify() {
        return pdpModifier;
    }

    @Override
    public PolicyQueryAdjudicator query() {
        return pdpQuerier;
    }

    @Override
    public ExecutionContext buildExecutionContext(UserContext userCtx) throws PMException {
        return new PDPExecutionContext(userCtx, this);
    }

    @Override
    public <R, A extends Args> R executeAdminFunction(AdminFunction<R, A> adminFunction,
                                                      Map<String, Object> argsMap) throws PMException {
        A args = adminFunction.validateAndPrepareArgs(argsMap);

        if (adminFunction instanceof Routine<R, A> routine) {
            return routine.execute(this, args);
        } else if (adminFunction instanceof Operation<R, A> operation) {
            operation.canExecute(pap, userCtx, args);
            return operation.execute(pap, args);
        }

        return adminFunction.execute(pap, args);
    }

    @Override
    public void reset() throws PMException {
        privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), RESET);

        pap.reset();
    }

    @Override
    public String serialize(PolicySerializer serializer) throws PMException {
        privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), SERIALIZE_POLICY);

        return pap.serialize(serializer);
    }

    @Override
    public void deserialize(String input, PolicyDeserializer policyDeserializer) throws PMException {
        privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), DESERIALIZE_POLICY);

        pap.deserialize(input, policyDeserializer);
    }

    public void executePML(String input) throws PMException {
        PMLCompiler pmlCompiler = new PMLCompiler();
        List<PMLStatement<?>> stmts = pmlCompiler.compilePML(pap, input);

        buildExecutionContext(userCtx)
            .executeStatements(stmts, new NoArgs());
    }

    @Override
    public void executePML(UserContext author, String input) throws PMException {
        throw new PMException("not supported by PDPTx, use executePML(String input) instead");
    }

    @Override
    public void beginTx() throws PMException {
        pap.beginTx();
    }

    @Override
    public void commit() throws PMException {
        pap.commit();
    }

    @Override
    public void rollback() throws PMException {
        pap.rollback();
    }
}
