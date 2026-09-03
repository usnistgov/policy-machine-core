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

package gov.nist.ngac.pm.core.pap.pml.compiler;

import java.util.Objects;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * A source text location, made of a line number and start/end character offsets.
 */
public class Position {

    private final int line;
    private final int start;
    private final int end;

    public Position(ParserRuleContext ctx) {
        this.line = ctx.start.getLine();
        this.start = ctx.start.getStartIndex();
        this.end = ctx.stop.getStopIndex();
    }

    public Position(int line, int start, int end) {
        this.line = line;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the 1-based source line.
     */
    public int line() {
        return line;
    }

    /**
     * Returns the start character offset within the source.
     */
    public int start() {
        return start;
    }

    /**
     * Returns the end character offset within the source.
     */
    public int end() {
        return end;
    }

    @Override
    public String toString() {
        return "Position{" +
                "line=" + line +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return line == position.line && start == position.start && end == position.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, start, end);
    }
}
