package edu.tarleton.checker.rename;

/**
 * The blind renaming strategy.
 *
 * @author Zdenek Tronicek
 */
public class BlindRename extends RenameStrategy {

    @Override
    public void enterBlock() {
    }

    @Override
    public void exitBlock() {
    }

    @Override
    public String declare(String name) {
        return "id";
    }

    @Override
    public String declareGlobal(String name) {
        return "id";
    }

    @Override
    public String rename(String name) {
        return "id";
    }
}
