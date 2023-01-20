package com.allhailthesheep.InputCapture;

import java.util.LinkedHashMap;
import java.util.Set;
// java system imports
import java.util.concurrent.Callable;

import picocli.CommandLine;
// picocli imports
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

// TODO: make an option for the hotkey to end capture
// TODO: add touch support?
// TODO: so, no log? *curbstomps keyboard*

/**
 * This class triggers the InputCapture class to begin capturing user input. InputCapture then utilizes the JNativeHook library to capture the input.
 * The output is stored in a text file until the user decides to reproduce it with the Generator half of this project.
 * PARAMS:
 *     output_file - The file to store the program output in. If it already exists the program will ask if it's ok to overwrite it.
 */
@Command(name = "input-capture", mixinStandardHelpOptions = true, version = "input-capture v0.0.1", description = 
        "Captures user input from keyboard and mouse and writes them to a file to be later repreduced by input-generator.")
public class InputCaptureLauncher implements Callable<Integer> {

    @Parameters(index = "0", description = "The output file to be written to.")
    private String output_file;

    @Override
    public Integer call() throws Exception {
        // create the InputCapture object
        InputCapture inputCapture = new InputCapture(output_file);
        inputCapture.run();
        return 0;
    }
    
    public static void main(String[] args) {
        String[] argsPlaceHolder = {"output.txt"};
        int exitCode = new CommandLine(new InputCaptureLauncher()).execute(argsPlaceHolder);
        return;
    }
}
