package gov.nist.csd.pm.policy.author.pal.compiler;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

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

    public int line() {
        return line;
    }

    public int start() {
        return start;
    }

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
