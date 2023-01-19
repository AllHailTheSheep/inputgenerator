package com.allhailthesheep.InputCapture;

// java system imports
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	/* This class uses a hashmap with types <long, string>. the long is the date and the string is the type of
	* action and the action.
	* for buttons, this takes the format of <identifier>.<button>.<action>.
	* for mouse actions, the format is <identifier>.<X>.<Y>.
	* for mouseWheel actions, the format is <identifier>.<direction>, where 1 is down (irl towards the user).
	* for each identifier there is a corresponding
	* hashmap with the action commands in the IO.java file (we don't use it here but it is relevant nonetheless).
	*/

	private static final Logger LOG = LogManager.getLogger(InputCapture.class);
	
	private LinkedHashMap<Long, String> actions = new LinkedHashMap<Long, String>();
	private Long start = Long.MAX_VALUE;
	public int esc = 0;


    public void nativeKeyPressed(NativeKeyEvent e) {
		actions.put(System.currentTimeMillis() - start, "keyboard." + NativeKeyEvent.getKeyText(e.getKeyCode()) + ".press");

		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			// TODO: make first on release and second on press (possible other config option?)
			if (esc == 0) {
				LOG.info("ESC pressed. Starting recording...");
				System.out.println("ESC pressed. Starting recording...");
				esc++;
				setStart();
			} else if (esc == 1) {
				LOG.info("ESC pressed. Finishing up...");
				System.out.println("ESC pressed. Finishing up...");
				try {
            		GlobalScreen.unregisterNativeHook();
            	} catch (NativeHookException nativeHookException) {
        			nativeHookException.printStackTrace();
        		}
				// TODO: add cleaning of negative times from map
				// TODO: write to file here!
				Set<Long> keys = actions.keySet();
        		for (Long key : keys) {
            		System.out.println(key + ", " + actions.get(key));
        		}
				System.out.println("Finished listener.");
				LOG.debug("Finished listener.");
			}
        }
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		actions.put(System.currentTimeMillis() - start, "keyboard." + NativeKeyEvent.getKeyText(e.getKeyCode()) + ".release");
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
}
