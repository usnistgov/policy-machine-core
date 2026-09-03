/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.exception;

import gov.nist.ngac.pm.core.pap.pml.compiler.Position;
import gov.nist.ngac.pm.core.pap.pml.compiler.error.CompileError;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Unchecked counterpart to {@link PMLCompilationException}, used inside compiler visitors.
 */
public class PMLCompilationRuntimeException extends RuntimeException {

    private List<CompileError> errors;

    public PMLCompilationRuntimeException(ParserRuleContext ctx, String message) {
        super(message);
        this.errors = List.of(CompileError.fromParserRuleContext(ctx, message));
    }

    public PMLCompilationRuntimeException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(new CompileError(new Position(0, 0, 0), message));
    }

    public PMLCompilationRuntimeException(List<CompileError> errors) {
        this.errors = errors;
    }

    public PMLCompilationRuntimeException(Throwable cause) {
        super(cause);
    }

    public List<CompileError> getErrors() {
        return errors;
    }
}
