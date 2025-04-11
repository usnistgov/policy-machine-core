package gov.nist.csd.pm.pap.pml.compiler.visitor.function;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.function.arg.ArgTypeResolver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormalParameterListVisitor extends PMLBaseVisitor<List<FormalParameter<?>>> {

    public FormalParameterListVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<FormalParameter<?>> visitFormalParamList(PMLParser.FormalParamListContext ctx) {
        List<FormalParameter<?>> formalArgs = new ArrayList<>();
        Set<String> argNames = new HashSet<>();
        for (int i = 0; i < ctx.formalParam().size(); i++) {
            PMLParser.FormalParamContext formalArgCtx = ctx.formalParam().get(i);
            String name = formalArgCtx.ID().getText();
            boolean isNodeop = formalArgCtx.NODE_PARAM() != null;

            // check that two formal args dont have the same name and that there are no constants with the same name
            if (argNames.contains(name)) {
                throw new PMLCompilationRuntimeException(
                    formalArgCtx,
                    String.format("formal arg '%s' already defined in signature", name)
                );
            }

            // get arg type
            PMLParser.VariableTypeContext varTypeContext = formalArgCtx.variableType();
            ArgType<?> type = ArgTypeResolver.resolveFromParserCtx(varTypeContext);

            if (isNodeop) {
                formalArgs.add(new NodeFormalParameter<>(name, type));
            } else {
                formalArgs.add(new FormalParameter<>(name, type));
            }

            argNames.add(name);
        }

        return formalArgs;
    }
}
