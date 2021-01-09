package edu.tarleton.checker;

import com.github.javaparser.JavaToken;
import java.util.List;

/**
 * The class that represents tokens after renaming.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class RenamedTokens {

    private final List<JavaToken> tokens;
    private final List<String> identifiers;
    private final boolean wasNormalized;

    public RenamedTokens(List<JavaToken> tokens, List<String> identifiers, boolean wasNormalized) {
        this.tokens = tokens;
        this.identifiers = identifiers;
        this.wasNormalized = wasNormalized;
    }

    public List<JavaToken> getTokens() {
        return tokens;
    }

    public JavaToken getToken(int index) {
        return tokens.get(index);
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public String getIdentifier(int index) {
        return identifiers.get(index);
    }

    public boolean wasNormalized() {
        return wasNormalized;
    }

    public int size() {
        return tokens.size();
    }
}
