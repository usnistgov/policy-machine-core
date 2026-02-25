package gov.nist.csd.pm.core.pap.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OperationArgValidationTest {

    private static final FormalParameter<String>     PARAM_A    = new FormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<String>     PARAM_B    = new FormalParameter<>("b", STRING_TYPE);
    private static final FormalParameter<Long>       PARAM_ID   = new FormalParameter<>("id", LONG_TYPE);
    private static final NodeNameFormalParameter     NODE_NAME  = new NodeNameFormalParameter("nodeName");
    private static final NodeIdFormalParameter       NODE_ID    = new NodeIdFormalParameter("nodeId");

    private static Operation<Void> opNoEventParams(List<FormalParameter<?>> params) {
        return new AdminOperation<>(null, null, params, List.of()) {
            @Override
            public Void execute(PAP pap, Args args) throws PMException { return null; }
        };
    }

    private static Operation<Void> opWithEventParams(List<FormalParameter<?>> params,
                                                     List<FormalParameter<?>> eventParams) {
        return new AdminOperation<>(null, null, params, eventParams, List.of()) {
            @Override
            public Void execute(PAP pap, Args args) throws PMException { return null; }
        };
    }

    @Nested
    class ValidateArgs {

        @Test
        void exactMatchReturnsTypedArgs() {
            Operation<Void> op = opNoEventParams(List.of(PARAM_A, PARAM_B));

            Args args = op.validateArgs(Map.of("a", "foo", "b", "bar"));

            assertEquals("foo", args.get(PARAM_A));
            assertEquals("bar", args.get(PARAM_B));
        }

        @Test
        void missingNodeFormalParameterThrows() {
            Operation<Void> op = opNoEventParams(List.of(NODE_NAME, PARAM_A));

            assertThrows(IllegalArgumentException.class,
                () -> op.validateArgs(Map.of("a", "foo")));
        }

        @Test
        void missingNonNodeParamSucceeds() {
            Operation<Void> op = opNoEventParams(List.of(NODE_NAME, PARAM_A));

            Args args = op.validateArgs(Map.of("nodeName", "n1"));

            assertEquals("n1", args.get(NODE_NAME));
        }

        @Test
        void mixedParamsOnlyNodeRequired() {
            Operation<Void> op = opNoEventParams(List.of(NODE_NAME, NODE_ID, PARAM_A, PARAM_B));

            Args args = op.validateArgs(Map.of("nodeName", "n1", "nodeId", 42L));

            assertEquals("n1", args.get(NODE_NAME));
            assertEquals(42L, args.get(NODE_ID));
        }

        @Test
        void unexpectedArgThrows() {
            Operation<Void> op = opNoEventParams(List.of(PARAM_A));

            assertThrows(IllegalArgumentException.class,
                () -> op.validateArgs(Map.of("a", "foo", "extra", "val")));
        }

        @Test
        void wrongTypeThrows() {
            Operation<Void> op = opNoEventParams(List.of(PARAM_ID));

            assertThrows(IllegalArgumentException.class,
                () -> op.validateArgs(Map.of("id", "not-a-long")));
        }

        @Test
        void emptyArgsOkForNoParamOperation() {
            Operation<Void> op = opNoEventParams(List.of());

            Args args = op.validateArgs(Map.of());

            assertEquals(Map.of(), args.toMap());
        }
    }

    @Nested
    class ValidateEventContextArgs {

        @Test
        void fullEventParamSetReturnsTypedArgs() {
            Operation<Void> op = opWithEventParams(
                List.of(PARAM_A, PARAM_B),
                List.of(PARAM_ID, PARAM_A, PARAM_B)
            );

            Args args = op.validateEventContextArgs(Map.of("id", 42L, "a", "foo", "b", "bar"));

            assertEquals(42L, args.get(PARAM_ID));
            assertEquals("foo", args.get(PARAM_A));
            assertEquals("bar", args.get(PARAM_B));
        }

        @Test
        void subsetOfEventParamsIsAccepted() {
            Operation<Void> op = opWithEventParams(
                List.of(PARAM_A, PARAM_B),
                List.of(PARAM_ID, PARAM_A, PARAM_B)
            );

            Args args = op.validateEventContextArgs(Map.of("a", "foo", "b", "bar"));

            assertEquals("foo", args.get(PARAM_A));
            assertEquals("bar", args.get(PARAM_B));
        }

        @Test
        void emptyArgsIsAcceptedAsSubset() {
            Operation<Void> op = opWithEventParams(
                List.of(PARAM_A),
                List.of(PARAM_ID, PARAM_A)
            );

            Args args = op.validateEventContextArgs(Map.of());

            assertEquals(Map.of(), args.toMap());
        }

        @Test
        void argNotInEventParamsThrows() {
            Operation<Void> op = opWithEventParams(
                List.of(PARAM_A),
                List.of(PARAM_A, PARAM_B)
            );

            assertThrows(IllegalArgumentException.class,
                () -> op.validateEventContextArgs(Map.of("a", "foo", "extra", "val")));
        }

        @Test
        void wrongTypeForEventOnlyParamThrows() {
            Operation<Void> op = opWithEventParams(
                List.of(PARAM_A),
                List.of(PARAM_ID, PARAM_A)
            );

            assertThrows(IllegalArgumentException.class,
                () -> op.validateEventContextArgs(Map.of("id", "not-a-long", "a", "foo")));
        }

        @Test
        void whenNoEventParamsSetDefaultsToParameters() {
            Operation<Void> op = opNoEventParams(List.of(PARAM_A, PARAM_B));

            Args args = op.validateEventContextArgs(Map.of("a", "foo"));

            assertEquals("foo", args.get(PARAM_A));
        }
    }
}
