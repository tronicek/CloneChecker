package edu.tarleton.checker.symtab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class that works as a flat symbol table.
 *
 * @author Zdenek Tronicek
 */
public class SymbolTable {

    private final List<Entry> entries = new ArrayList<>();
    private final boolean global;
    private final SymbolTable next;
    private final Map<String, Integer> countMap;

    public SymbolTable(boolean global, SymbolTable next) {
        this.global = global;
        this.next = next;
        countMap = global ? new HashMap<>() : next.countMap;
    }

    private int getCount() {
        Integer c = countMap.get("id");
        if (c == null) {
            c = 0;
        }
        return c;
    }

    private void putCount(int c) {
        countMap.put("id", c);
    }

    public String declare(String name) {
        int count = getCount();
        Entry entry = new Entry(name, null, count);
        entries.add(entry);
        putCount(count + 1);
        return entry.norm();
    }

    public String declareVar(String name, String type) {
        int count = getCount();
        Entry entry = new Entry(name, type, count);
        entries.add(entry);
        putCount(count + 1);
        return entry.norm();
    }

    public String getVarType(String name) {
        for (Entry e : entries) {
            if (name.equals(e.getName())) {
                return e.getType();
            }
        }
        if (!global) {
            return next.getVarType(name);
        }
        return null;
    }

    public String declareGlobal(String name) {
        if (!global) {
            return next.declareGlobal(name);
        }
        int count = getCount();
        Entry entry = new Entry(name, null, count);
        entries.add(entry);
        putCount(count + 1);
        return entry.norm();
    }

    public String lookup(String name) {
        for (Entry e : entries) {
            if (name.equals(e.getName())) {
                return e.norm();
            }
        }
        if (!global) {
            return next.lookup(name);
        }
        return null;
    }

    public void print() {
        System.out.println("symbol table:");
        for (Entry e : entries) {
            System.out.println(e);
        }
        System.out.println("----------------");
    }

    public SymbolTable getNext() {
        return next;
    }
}
