package gov.nist.csd.pm.pap.pml.compiler.visitor;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.UnknownFieldSet;
import gov.nist.csd.pm.pap.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.op.routine.proto.CreateAdminRoutineOpOrBuilder;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.AssignStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.List;
import java.util.Map;

public class AssignStmtVisitor extends PMLBaseVisitor<AssignStatement> {

    public static void main(String[] args) {
        new CreateAdminRoutineOpOrBuilder() {
            @Override
            public Message getDefaultInstanceForType() {
                return null;
            }

            @Override
            public boolean isInitialized() {
                return false;
            }

            @Override
            public List<String> findInitializationErrors() {
                return List.of();
            }

            @Override
            public String getInitializationErrorString() {
                return "";
            }

            @Override
            public Descriptors.Descriptor getDescriptorForType() {
                return null;
            }

            @Override
            public Map<Descriptors.FieldDescriptor, Object> getAllFields() {
                return Map.of();
            }

            @Override
            public boolean hasOneof(Descriptors.OneofDescriptor oneof) {
                return false;
            }

            @Override
            public Descriptors.FieldDescriptor getOneofFieldDescriptor(Descriptors.OneofDescriptor oneof) {
                return null;
            }

            @Override
            public boolean hasField(Descriptors.FieldDescriptor field) {
                return false;
            }

            @Override
            public Object getField(Descriptors.FieldDescriptor field) {
                return null;
            }

            @Override
            public int getRepeatedFieldCount(Descriptors.FieldDescriptor field) {
                return 0;
            }

            @Override
            public Object getRepeatedField(Descriptors.FieldDescriptor field, int index) {
                return null;
            }

            @Override
            public UnknownFieldSet getUnknownFields() {
                return null;
            }

            @Override
            public String getPml() {
                return "";
            }

            @Override
            public ByteString getPmlBytes() {
                return null;
            }
        };
    }

    public AssignStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public AssignStatement visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        Expression ascendant = Expression.compile(visitorCtx, ctx.ascendantNode, Type.string());
        Expression descendants = Expression.compile(visitorCtx, ctx.descendantNodes, Type.array(Type.string()));

        return new AssignStatement(ascendant, descendants);
    }
}
