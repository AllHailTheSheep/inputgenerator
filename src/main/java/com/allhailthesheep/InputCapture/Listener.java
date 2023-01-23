package com.allhailthesheep.InputCapture;

import java.io.IOException;
// java system imports
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.allhailthesheep.bin.IO;
// jnativehook imports
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;

public class Listener implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {
	/* This class uses a hashmap with types <long, string>. the long is the time after start (in ms) and the string is the type of
	* action and the action itself.
	* for buttons, this takes the format of <identifier>.<button>.<action>.
	* for mouse actions, the format is <identifier>.<X>.<Y>.
	* for mouseWheel actions, the format is <identifier>.<direction>, where 1 is down (irl towards the user).
	*/

	private static final Logger LOG = LogManager.getLogger(InputCapture.class);
	private LinkedHashMap<Long, String> actions = new LinkedHashMap<Long, String>();
	private Long start = Long.MAX_VALUE;
	public int esc = 0;

	// TODO: use bufferedfilewriter and dont keep track of actions in a map, just write them directly to file

    public void nativeKeyPressed(NativeKeyEvent e) {
		actions.put(System.currentTimeMillis() - start, "keyboard." + NativeKeyEvent.getKeyText(e.getKeyCode()) + ".press");
		// if its the second time escape is presed we finish up, clean the output, and write it to file.
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE && esc == 1) {
			LOG.info("ESC pressed. Second esc pressed. Finishing up...");
			System.out.println("Second esc pressed. Finishing up...");
			try {
            	GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException nativeHookException) {
        		nativeHookException.printStackTrace();
        	}
			clean();
			try {
				IO.writeOutput(actions);
				LOG.info("File writing finished.");
			} catch (IOException e1) {
				System.out.println("Could not write to file!");
				e1.printStackTrace();
				LOG.fatal("Could not write to file!");
				LOG.debug(e1.getMessage());
			}
			System.out.println("Finished listener.");
			LOG.debug("Finished listener.");
    	}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		actions.put(System.currentTimeMillis() - start, "keyboard." + NativeKeyEvent.getKeyText(e.getKeyCode()) + ".release");
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE && esc == 0) {
			LOG.info("First ESC released. Starting recording...");
			System.out.println("First ESC released. Starting recording...");
			esc++;
			setStart();
		}
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		actions.put(System.currentTimeMillis() - start, "mouse." + e.getButton() + ".press");
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		actions.put(System.currentTimeMillis() - start, "mouse." + e.getButton() + ".release");
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		actions.put(System.currentTimeMillis()- start, "mouse." + e.getX() + "." + e.getY());
	}

	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		actions.put(System.currentTimeMillis() - start, "mouseWheel." + e.getWheelRotation());
	}

	public void setStart() {
		start = System.currentTimeMillis();
	}

	private void clean() {
		LinkedList<Long> toRemove = new LinkedList<Long>();
		LinkedList<Long> every = new LinkedList<Long>();
		for (Long key : actions.keySet()) {
            if (key < 0) {
                toRemove.add(key);
            }
			every.add(key);
        }
		toRemove.forEach((i) -> actions.remove(i));
		actions.remove(every.getLast());
	}
}
