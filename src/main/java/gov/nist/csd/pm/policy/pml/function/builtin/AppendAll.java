package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;


import java.util.List;

public class AppendAll extends FunctionDefinitionStatement {
    public AppendAll() {
        super(new FunctionDefinitionStatement.Builder("appendAll")
                      .returns(Type.array(Type.any()))
                      .args(
                              new FormalArgument("dst", Type.array(Type.any())),
                              new FormalArgument("src", Type.array(Type.any()))
                      )
                      .executor((ctx, author) -> {
                          List<Value> dstValueArr = ctx.scope().getValue("dst").getArrayValue();
                          List<Value> srcValueArr = ctx.scope().getValue("src").getArrayValue();

                          dstValueArr.addAll(srcValueArr);

                          return new ArrayValue(dstValueArr, Type.array(Type.any()));
                      })
                      .build()
        );
    }
}
