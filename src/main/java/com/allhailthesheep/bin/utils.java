package com.allhailthesheep.bin;

import java.util.HashMap;
import java.util.Scanner;


public class utils {
    static final boolean DEBUG = true;

    private HashMap<String, String> mouse = new HashMap<String, String>();
    private HashMap<String, String> mouse_wheel = new HashMap<String, String>();
    private HashMap<String, String> keyboard = new HashMap<String, String>();

    /**
    * This function returns a string after prompting the user
    * if the input is not understood the function will re-run
    * @param prompt the promps to be presented to the user
    * @param in the pre-initialized scanner to read input from
    * @return the string the user inputed
    */
    public static String get_input(String prompt, Scanner in) {
        System.out.print(prompt);
        String result;
        try {
            result = in.nextLine();
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println(e.getStackTrace());
            } else {
                System.out.println("That didn't work, are you sure you entered a character or string?");
            }
            result = get_input(prompt, in);
        }
        return result;
    }

    /**
    * This function returns a char from the user after making sure that the char occurs in the parameter list a.
    * If it is not, it will re run itself until it does.
    * @param prompt the string prompt to be presented to the user
    * @param C a character array containing the possible answers
    * @return the character inputted by the user
    */
    public static char get_input_and_validate_char(String prompt, char[] C, Scanner in) {
        char res = get_input(prompt, in).toLowerCase().charAt(0);
        if (new String(C).indexOf(res) != -1) {
            return res;
        } else {
            System.out.println("Not a valid input. The valid inputs are as follows (not case sensitive):");
            for (char c : C) {
                System.out.println(c);
            }
            return get_input_and_validate_char(prompt, C, in);
        }
    }
}
