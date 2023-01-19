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

@Command(name = "input-capture", mixinStandardHelpOptions = true, version = "input-capture v0.0.1", description = 
        "Captures user input from keyboard and mouse and writes them to a file to be later repreduced by input-generator.")
public class InputCaptureLauncher implements Callable<Integer> {

    @Parameters(index = "0", description = "The output file to be written to.")
    private String output_file;

    @Override
    public Integer call() throws Exception {
        // create the InputCapture object
        InputCapture inputCapture = new InputCapture(output_file);
        // TODO: maybe write/print in the listener?
        LinkedHashMap<Long, String> actions = inputCapture.run();
        // Set<Long> keys = actions.keySet();
        // for (Long key : keys) {
        //     System.out.println(key + ", " + actions.get(key));
        // }
        return 0;
    }
    
    public static void main(String[] args) {
        String[] argsPlaceHolder = {"output.txt"};
        int exitCode = new CommandLine(new InputCaptureLauncher()).execute(argsPlaceHolder);
        return;
    }
}
