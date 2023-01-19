package com.allhailthesheep.InputCapture;

// java system imports
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Scanner;

// log4j2 imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// jnativehook imports
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

// local imports
import com.allhailthesheep.bin.IO;


/**
 * This class controls the Listener interface made with JNativeHook.
 */
public class InputCapture {
    private static final Logger LOG = LogManager.getLogger(InputCapture.class);

    private static final Scanner consoleIn = new Scanner(System.in);
    private File file;

    public InputCapture(String fn) {
        this.file = new File(fn);
        LOG.info("Starting file checks...");
        IO.fileChecks(file, consoleIn, LOG);
        LOG.info("File checks completed successfully.");
    }

    public void run() {
        System.out.println("Registering native hook...");
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            LOG.fatal("There was a problem registering the native hook. Exiting...");
            System.out.println("There was a problem registering the native hook. Exiting...");
            System.exit(1);
        }
        Listener l = new Listener();
        GlobalScreen.addNativeMouseListener(l);
        GlobalScreen.addNativeMouseMotionListener(l);
        GlobalScreen.addNativeMouseWheelListener(l);
        GlobalScreen.addNativeKeyListener(l);
        return;
    }
}
