package com.allhailthesheep.bin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;

public class IO {
    private HashMap<String, String> mouse = new HashMap<String, String>();
    private HashMap<String, String> mouse_wheel = new HashMap<String, String>();
    private HashMap<String, String> keyboard = new HashMap<String, String>();
    
    public static void fileChecks(File file, Scanner consoleIn, Logger LOG) {
        // if the file exists, we need to overwrite it with the users permission
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
