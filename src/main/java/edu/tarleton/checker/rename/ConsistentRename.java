package edu.tarleton.checker.rename;

import edu.tarleton.checker.symtab.SymbolTable;

/**
 * The consistent renaming strategy.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class ConsistentRename extends RenameStrategy {

    private SymbolTable symbolTable = new SymbolTable(true, null);

    @Override
    public void enterBlock() {
        symbolTable = new SymbolTable(false, symbolTable);
    }

    @Override
    public void exitBlock() {
        symbolTable = symbolTable.getNext();
    }

    @Override
    public String declare(String name) {
        return symbolTable.declare(name);
    }

    @Override
    public String declareGlobal(String name) {
        return symbolTable.declareGlobal(name);
    }

    @Override
    public String rename(String name) {
        return symbolTable.lookup(name);
    }
}
