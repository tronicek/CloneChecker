package edu.tarleton.checker;

import edu.tarleton.drdup2.nicad.NiCadClone;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * The common parent of Type-3 checkers.
 *
 * @author Zdenek Tronicek
 */
public abstract class Clone3Checker {

    protected final Properties conf;
    protected final List<NiCadClone> type1 = new ArrayList<>();
    protected final List<NiCadClone> type2 = new ArrayList<>();
    protected final List<NiCadClone> type3 = new ArrayList<>();
    protected final List<NiCadClone> notClones = new ArrayList<>();
    protected final List<NiCadClone> invalid = new ArrayList<>();
    protected final Map<NiCadClone, String> comments = new HashMap<>();
    protected final boolean treatNullAsLiteral;
    protected final boolean treatSuperThisAsIdentifier;
    protected final String distance;

    public static Clone3Checker instance(Properties conf) {
        String rename = conf.getProperty("rename", "blind");
        switch (rename) {
            case "blind":
                return new BlindClone3Checker(conf);
            case "consistent":
                throw new AssertionError("consistent renaming for Type-3 clones is not supported");
            default:
                throw new AssertionError("invalid rename: " + rename);
        }
    }

    protected Clone3Checker(Properties conf) {
        this.conf = conf;
        treatNullAsLiteral = Boolean.parseBoolean(conf.getProperty("treatNullAsLiteral", "false"));
        treatSuperThisAsIdentifier = Boolean.parseBoolean(conf.getProperty("treatSuperThisAsIdentifier", "false"));
        distance = conf.getProperty("distance", "Levenshtein");
    }

    public List<NiCadClone> getType1() {
        return type1;
    }

    public List<NiCadClone> getType2() {
        return type2;
    }

    public List<NiCadClone> getType3() {
        return type3;
    }

    public List<NiCadClone> getNotClones() {
        return notClones;
    }

    public List<NiCadClone> getInvalid() {
        return invalid;
    }

    public Map<NiCadClone, String> getComments() {
        return comments;
    }

    public abstract void process() throws Exception;
}
