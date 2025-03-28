package gov.nist.csd.pm.pap.pml.compiler.visitor.function;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.operation.PMLNodeFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormalArgListVisitor extends PMLBaseVisitor<List<PMLFormalArg>> {

    public FormalArgListVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<PMLFormalArg> visitFormalArgList(PMLParser.FormalArgListContext ctx) {
        List<PMLFormalArg> formalArgs = new ArrayList<>();
        Set<String> argNames = new HashSet<>();
        for (int i = 0; i < ctx.formalArg().size(); i++) {
            PMLParser.FormalArgContext formalArgCtx = ctx.formalArg().get(i);
            String name = formalArgCtx.ID().getText();
            boolean isNodeop = formalArgCtx.NODE_ARG() != null;

            // check that two formal args dont have the same name and that there are no constants with the same name
            if (argNames.contains(name)) {
                throw new PMLCompilationRuntimeException(
                    formalArgCtx,
                    String.format("formal arg '%s' already defined in signature", name)
                );
            }

            // get arg type
            PMLParser.VariableTypeContext varTypeContext = formalArgCtx.variableType();
            Type type = Type.toType(varTypeContext);

            if (isNodeop) {
                formalArgs.add(new PMLNodeFormalArg(name, type));
            } else {
                formalArgs.add(new PMLFormalArg(name, type));
            }

            argNames.add(name);
        }

        return formalArgs;
    }
}
