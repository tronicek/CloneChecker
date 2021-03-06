package edu.tarleton.checker;

import java.util.Set;

/**
 * The class that represents the result of comparison of clone candidates.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Result {

    public enum Diff {
        IDENTIFIERS,
        LITERALS,
        NORMALIZATION
    }

    private final boolean clone;
    private final Set<Diff> diff;

    public Result(boolean clone, Set<Diff> diff) {
        this(clone, false, diff);
    }

    public Result(boolean clone, boolean uncertain, Set<Diff> diff) {
        this.clone = clone;
        this.diff = diff;
    }

    public boolean isClone() {
        return clone;
    }

    public Set<Diff> getDiff() {
        return diff;
    }

    public String getDiffAsString() {
        StringBuilder sb = new StringBuilder();
        if (diff.contains(Diff.IDENTIFIERS)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("identifiers");
        }
        if (diff.contains(Diff.LITERALS)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("literals");
        }
        if (diff.contains(Diff.NORMALIZATION)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("normalization");
        }
        return sb.toString();
    }
}
