package edu.tarleton.checker;

import java.io.FileReader;
import java.util.Properties;

/**
 * The entry class.
 *
 * @author Zdenek Tronicek
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("expected argument: properties file (e.g. JavaAPI.properties)");
            System.exit(0);
        }
        Properties conf = new Properties();
        try (FileReader in = new FileReader(args[0])) {
            conf.load(in);
        }
        String type = conf.getProperty("type", "2");
        switch (type) {
            case "2": {
                CloneChecker checker = CloneChecker.instance(conf);
                checker.process();
                break;
            }
            case "3": {
                Clone3Checker checker = Clone3Checker.instance(conf);
                checker.process();
                break;
            }
        }
    }
}
