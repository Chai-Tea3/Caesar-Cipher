/*
 * Author: Riley Chai 
 * Class: ICS4U
 * Program: Caesar Cipher
 * Description: Encodes, decodes and can brute force messages.
 */
package chairileycaesarcipher;

import java.util.Scanner;

/**
 *
 * @author 335480661
 */
public class ChaiRileyCaesarCipher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        String userString = "";//Stores the user input.
        String shift = "";//Stores the desired shift. Needs to be verified.
        String output = "";//Stores the output from the encode/decode method.
        int shiftNum = 0;//Stores the verified shift number.
        boolean isValid = false;//Loops until the the user enters the correct input.
        boolean run = true;//Keeps the program running.

        while (run == true) {//Loops until a 'q' is entered to quit the program.
            isValid = false;
            System.out.println("Please select one option: Encode(e), Decode(d), Brute Force(b) or Quit(q)");//Encode, Decode, Brute Force, Quit.
            userString = keyboard.nextLine();
            if (userString.length() > 0) {//Ensures the string is not empty.
                switch (userString) {//Checks if the input matches any of the options
                    case "e"://Encode
                        while (!isValid) {//Loops until a non-empty input is entered.
                            System.out.println("Please enter the message you would like to encode: ");
                            userString = keyboard.nextLine();
                            if (userString.length() > 0) {//Ensures string is not empty.
                                while (!isValid) {//Loops until a valid integer is entered.
                                    System.out.println("Please enter the shift: ");
                                    shift = keyboard.nextLine();
                                    isValid = inputValidation(shift);//Ensures the shift is an integer and between 1 and 25.
                                    if (isValid == true) {
                                        shiftNum = Integer.parseInt(shift);
                                        output = changeIt(userString, shiftNum);//Encodes the string with the given shift.
                                        System.out.println(output);//Outputs the encoded string.
                                    }
                                }
                            } else {
                                System.out.println("**Empty input. Please try again**");
                            }
                        }
                        break;

                    case "d"://Decode
                        while (!isValid) {//Loops until a non-empty input is entered.
                            System.out.println("Please enter the message you would like to encode: ");
                            userString = keyboard.nextLine();
                            if (userString.length() > 0) {//Ensures the string is not empty.
                                while (!isValid) {//Loops until a valid integer is entered.
                                    System.out.println("Please enter the shift: ");
                                    shift = keyboard.nextLine();
                                    isValid = inputValidation(shift);//Ensures the shift is an integer and between 1 and 25.
                                    if (isValid == true) {
                                        shiftNum = Integer.parseInt(shift);
                                        output = changeIt(userString, -shiftNum);//Changes the shift to a negative to decode.
                                        System.out.println(output);//Outputs the decoded string.
                                    }
                                }
                            } else {
                                System.out.println("**Empty input. Please try again**");
                            }
                        }
                        break;

                    case "b"://Brute force
                        while (!isValid) {
                            System.out.println("Please enter the message you would like to brute force: ");
                            userString = keyboard.nextLine();
                            if (userString.length() > 0) {//Ensures the string is not empty.
                                isValid = true;
                                String[] allChoices = BruteForce(userString);//Stores all of the possible shifts in an array.
                                for (String str : allChoices) {//Loops through the array and outputs all possible options.
                                    System.out.println(str);
                                }
                                int bestIndex = BestChoice(allChoices);//Checks for the best string and stores the index.
                                System.out.println("The best String is: " + allChoices[bestIndex]);
                                System.out.println("Shift used: "+bestIndex);
                            } else {
                                System.out.println("**Empty input. Please try again**");
                                isValid = false;
                            }
                        }
                        break;

                    case "q"://Quit
                        run = false;
                        break;
                    default://Runs if the input does not match any of the options.
                        System.out.println("**Please choose one of the options**");
                        break;
                }
            } else {
                System.out.println("**Please choose one of the options**");
            }
        }
        keyboard.close();//Closes the scanner.
    }

    /**
     * Checks every shift for the most common bigrams in the English language. 
     * The sentence with the most matches will be chosen as the best choice.
     * Bigrams are a pair of 2 letters and some common ones that are found in 
     * majority of sentences are "th", "to", and "an".
     * 
     * @param input (Receives all possible shifts from the brute force method)
     * @return int bestIndex
     */
    public static int BestChoice(String[] input) {
        String[] bigrams = new String[]{"th", "he", "in", "en", "nt", "re", "er", "an", "ti", "es", "on", "at", "se", "nd", "or", "ar", "al", "te", "co", "de", "to", "ra", "et", "ed", "it", "sa", "em", "ro"};
        int stringScore = 0;//Current amount of matches for the given string.
        int bestScore = 0;//Highest amount of matches found so far.
        int index = 0;//Used to keep track of the current position in the string.
        int bestIndex = 0;//The best string.
        for (int i = 0; i < 26; i++) {//Loops through all indexes of the input array.
            stringScore = 0;//Resets the score for each string.
            for (String bigram : bigrams) {//Loops through the array of bigrams.
                index = 0;//Resets the starting location.
                while (input[i].indexOf(bigram, index) != -1) {//If a bigram is found, 
                    stringScore++;//Add one to the string score.
                    index = input[i].indexOf(bigram, index) + 1;//Move the starting location to where the last bigram was found
                    //If a string has multiple of the same bigram. This will find all of them and add to the total score.
                }
            }
            if (stringScore > bestScore) {//If the current string has more matches than previous attempts.
                bestIndex = i;//Updates the best choice.
                bestScore = stringScore;//Updates the amount of matches required.
            }
        }
        return bestIndex;//Returns the shift that gave the best result.
    }

    public static String[] BruteForce(String input) {//Performs 0 to 25 shift on the encoded string.
        String[] allChoices = new String[26];
        for (int i = 0; i < 26; i++) {
            allChoices[i] = changeIt(input, -i);//Calls the changeIt method to decode the string with each shift.
        }
        return allChoices;//Returns an array of all attempts.
    }

    public static String changeIt(String input, int shift) {//Encode if the shift is positive, Decode if the shift is negative.
        char curChar;//Holds the current character that is being shifted
        String output = "";
        for (int i = 0; i < input.length(); i++) {//Loops through the entire sentence
            curChar = input.charAt(i);
            if (curChar >= 65 && curChar <= 90) {//Between uppercase a to z.
                curChar = (char) (curChar + shift);//Shifts the character.
                if (curChar > 90) {//If it exceeds the range
                    curChar = (char) (curChar - 26);//Loops back to the start.
                } else if (curChar < 65) {
                    curChar = (char) (curChar + 26);//Loops to Z when decoding.
                }
                output += curChar;//Appends the shifted character to the encoded string.
            } else if (curChar >= 97 && curChar <= 122) {//Between lowercase a to z.
                curChar = (char) (curChar + shift);//Shifts the character.
                if (curChar > 122) {//If it exceeds the range
                    curChar = (char) (curChar - 26);//Loops back to the start.
                } else if (curChar < 97) {
                    curChar = (char) (curChar + 26);//Loops to z when decoding.
                }
                output += curChar;//Appends the shifted character to the encoded string.
            } else {//If the current character is not a letter.
                output += curChar;//Appends the non-shifted character to the encoded string.
            }
        }
        return output;
    }

    public static boolean inputValidation(String input) {
        boolean isValid = true;
        try {
            int validInt = Integer.parseInt(input);//Tries to parse the input to an integer.
            if (validInt >= 1 && validInt <= 25) {//Ensures the desired shift is between 1 and 25.
                isValid = true;
            } else {
                System.out.println("**Shift must be between 1 and 25**");
                isValid = false;
            }
        } catch (NumberFormatException e) {//If it is unable to parse.
            System.out.println("**Value should be an INTEGER**");
            isValid = false;
        }
        return isValid;
    }
}
