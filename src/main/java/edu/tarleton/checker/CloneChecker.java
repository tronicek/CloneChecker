package edu.tarleton.checker;

import edu.tarleton.drdup2.nicad.NiCadClone;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * The common parent of checkers.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public abstract class CloneChecker {

    protected final Properties conf;
    protected final List<NiCadClone> type1 = new ArrayList<>();
    protected final List<NiCadClone> type2 = new ArrayList<>();
    protected final List<NiCadClone> notClones = new ArrayList<>();
    protected final List<NiCadClone> invalid = new ArrayList<>();
    protected final Map<NiCadClone, String> comments = new HashMap<>();
    protected final boolean treatNullAsLiteral;
    protected final boolean treatSuperThisAsIdentifier;

    public static CloneChecker instance(Properties conf) {
        String rename = conf.getProperty("rename", "blind");
        switch (rename) {
            case "blind":
                return new BlindCloneChecker(conf);
            case "consistent":
                return new ConsistentCloneChecker(conf);
            default:
                throw new AssertionError("invalid rename: " + rename);
        }
    }

    protected CloneChecker(Properties conf) {
        this.conf = conf;
        treatNullAsLiteral = Boolean.parseBoolean(conf.getProperty("treatNullAsLiteral", "false"));
        treatSuperThisAsIdentifier = Boolean.parseBoolean(conf.getProperty("treatSuperThisAsIdentifier", "false"));
    }

    public List<NiCadClone> getType1() {
        return type1;
    }

    public List<NiCadClone> getType2() {
        return type2;
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
