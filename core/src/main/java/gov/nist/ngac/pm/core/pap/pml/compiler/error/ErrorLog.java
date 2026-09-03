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

package gov.nist.ngac.pm.core.pap.pml.compiler.error;

import gov.nist.ngac.pm.core.pap.pml.compiler.Position;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Aggregates the {@link CompileError}s found during one PML compilation, deduplicating identical errors.
 */
public class ErrorLog {

    private final List<CompileError> errors;

    public ErrorLog() {
        this.errors = new ArrayList<>();
    }

    /**
     * Adds an error at the given parse context's position, unless an identical error is already
     * recorded.
     *
     * @return this instance, for chaining
     */
    public ErrorLog addError(ParserRuleContext ctx, String message) {
        CompileError compileError = CompileError.fromParserRuleContext(ctx, message);

        addError(compileError);

        return this;
    }

    /**
     * Adds an error at the given explicit position, unless an identical error is already recorded.
     *
     * @return this instance, for chaining
     */
    public ErrorLog addError(int line, int charPos, int end, String msg) {
        CompileError compileError = new CompileError(new Position(line, charPos, end), msg);

        addError(compileError);

        return this;
    }

    /**
     * Adds every error in the given list, ignoring a null list.
     */
    public void addErrors(List<CompileError> errors) {
        if (errors == null) {
            return;
        }

        this.errors.addAll(errors);
    }

    private void addError(CompileError error) {
        if (this.errors.contains(error)) {
            return;
        }

        this.errors.add(error);
    }

    public List<CompileError> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("errors: \n");
        for (CompileError error : errors) {
            s.append(error.toString()).append("\n");
        }
        return s.toString();
    }
}
