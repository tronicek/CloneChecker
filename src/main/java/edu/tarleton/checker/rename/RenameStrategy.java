package edu.tarleton.checker.rename;

/**
 * The renaming strategy.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public abstract class RenameStrategy {

    public static RenameStrategy instance(String name) {
        switch (name) {
            case "blind":
                return new BlindRename();
            case "consistent":
                return new ConsistentRename();
        }
        System.out.println("missing rename strategy, using default...");
        return new BlindRename();
    }

    public abstract void enterBlock();

    public abstract void exitBlock();

    public abstract String declare(String name);

    public abstract String declareGlobal(String name);

    public abstract String rename(String name);
}
