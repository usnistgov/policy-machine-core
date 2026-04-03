package gov.nist.csd.pm.core.pap.serialization.json;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserIdContext;
import gov.nist.csd.pm.core.pap.query.model.context.UsernameContext;
import java.util.Objects;

public class JSONObligation {

    public static JSONObligation fromObligation(Obligation o, GraphQuery graphQuery) throws PMException {
        long authorId = switch (o.getAuthor()) {
            case UserIdContext c -> c.userId();
            case UsernameContext c -> graphQuery.getNodeByName(c.username()).getId();
        };
        return new JSONObligation(o.getName(), authorId, o.toString());
    }

    private String name;
    private long author;
    private String pml;

    public JSONObligation() {
    }

    public JSONObligation(String name, long author, String pml) {
        this.name = name;
        this.author = author;
        this.pml = pml;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public String getPml() {
        return pml;
    }

    public void setPml(String pml) {
        this.pml = pml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSONObligation that)) return false;
        return author == that.author && 
               Objects.equals(name, that.name) && 
               Objects.equals(pml, that.pml);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, pml);
    }

    @Override
    public String toString() {
        return "JSONObligation{" +
                "name='" + name + '\'' +
                ", author=" + author +
                ", pml='" + pml + '\'' +
                '}';
    }
} 