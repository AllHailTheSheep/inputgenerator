package com.allhailthesheep.bin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.Logger;

public class IO {
    private HashMap<String, String> mouse = new HashMap<String, String>();
    private HashMap<String, String> mouse_wheel = new HashMap<String, String>();
    private HashMap<String, String> keyboard = new HashMap<String, String>();

    private static String outputDest = null;
    

    public static void writeOutput(LinkedHashMap<Long, String> actions) throws IOException {
        PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(outputDest)));
        Set<Long> keys = actions.keySet();
        for (Long key : keys) {
           	w.write(key + " " + actions.get(key));
            w.println();
        }
        w.close();
    }

    /**
     * This function runs file checks against the string argument provided. The string is written to the class field
     * outputDest for use in the output function. The checks include if the file exists (will ask user for permission
     * to overwrite) as well read/write permissions.
     * @param fn the filename to create and run checks on
     * @param consoleIn a pre-initialized console scanner. This must be preinitialized as closing a console scanner will terminate the input stream as well.
     * @param LOG the logger to use for internal logging purposes
     */
    public static void fileChecks(String fn, Scanner consoleIn, Logger LOG) {
        // if the file exists, we need to overwrite it with the users permission
        outputDest = fn;
        File file = new File(fn);
        if (file.exists()) {
            LOG.warn("File already exists: " + file.getAbsolutePath()+ "!. Asking for permission to overwrite...");
            Character c = utils.get_input_and_validate_char("The file " + file.getAbsolutePath()+ " already exists. Permission to overwrite? (Y/N) ",
                            new char[] {'y', 'n'}, consoleIn);
            LOG.info("USER INPUT: " + c);

            if (Character.compare(c, 'y') == 0) {
                // if permission is granted, delete the file and recreate it
                LOG.info("Permission granted.");
                file.delete();
                LOG.info("File " + file.getAbsolutePath() +" deleted.");
                try {
                    file.createNewFile();
                    LOG.info("File " + file.getAbsolutePath() + " created.");
                    return;
                } catch (IOException e) {
                    // this should never happen but it is harmless to include
                    LOG.fatal("Unable to create file " + file.getAbsolutePath());
                    LOG.fatal(e.getStackTrace());
                    System.out.println("Unable to create file " + file.getAbsolutePath() + ". Try running as root.");
                    System.exit(1);
                }
            } else {
                // if the user doesn not give permission we exit.
                LOG.fatal("Permission denied. Exiting...");
                System.out.println("Permission denied. Exiting...");
                System.exit(1);
            }  
        }
        LOG.info("File does not yet exist: " + file.getAbsolutePath());
        try {
            file.createNewFile();
        } catch (IOException e) {
            // again, this should never happen
            LOG.fatal("Unable to create file " + file.getAbsolutePath());
            LOG.fatal(e.getStackTrace());
            System.out.println("Unable to create file " + file.getAbsolutePath() + ". Try running as root.");
            System.exit(1);
        }
        // check read capabilities
        if (file.canRead()) {
            LOG.info("File " + file.getAbsolutePath() + " is readable.");
        } else {
            LOG.fatal("File " + file.getAbsolutePath() + " is not readable.");
            System.out.println("File " + file.getAbsolutePath() + " is not readable. Exiting...");
            System.exit(1);
        }
        // check write capabilities
        if (file.canWrite()) {
            LOG.info("File " + file.getAbsolutePath() + " is writable.");
        } else {
            LOG.fatal("File " + file.getAbsolutePath() + " is not writable.");
            System.out.println("File " + file.getAbsolutePath() + " is not writable. Exiting...");
            System.exit(1);
        }
    }
}
