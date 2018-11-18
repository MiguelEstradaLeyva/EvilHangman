/**
 * EvilHangman project
 * @Author Miguel Estrada
 *
 */
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class EvilHangman {
    private static int guesses;//holds the guesses
    private static int secretWordLength;// word length
    public static LinkedList<String> words = new LinkedList<String>();//all possible words
    public static LinkedList<Character> guessedLetters = new LinkedList<>();//guessed letters
    private static TreeSet<String> currentWords;// current words size of secretWordLength
    private static String pattern;//to be the hold of the pattern

    public static void main(String[] args) {
        //beginning of the game
        System.out.println("Welcome to Evil Hangman");
        System.out.println();
        String dictionary = "google-10000-english.txt";
        readFile(dictionary);
        Scanner console = new Scanner(System.in);
        System.out.println("Enter the length of word to choose : ");
        secretWordLength = console.nextInt();
        System.out.println("How many guesses do you want to have: ");
        guesses = console.nextInt();
        System.out.println();

        possibleWords(secretWordLength, guesses, words);
        //playing the game until there are no more guesses
        while (guesses > 0) {
            System.out.println("Possible words = " + currentWords.size());
            System.out.println("Guesses remaining :" + guesses);
            System.out.println("Guessed letters " + guessedLetters);
            System.out.println("Current State = " + pattern);
            System.out.println("Please enter your guess:");
            char ch = console.next().toUpperCase().charAt(0);
            if (guessedLetters.contains(ch)) {
                System.out.println("You already guessed that");
            } else {
                int count = MethodForGuess(ch);
                if (count == 0) {
                    System.out.println("Sorry there are no " + ch + " 's");
                } else if (count == 1) {
                    System.out.println("Yes there is one " + ch);

                } else {
                    System.out.println("Yes there are " + count + " " + ch + " 's");
                }
            }
        }//choosing an answer at the end
        String answer = currentWords.iterator().next();
        System.out.println("The answer is : " + answer);
        if (guesses > 0) {
            System.out.println("You win!!");
        } else {
            System.out.println("You loose");
        }
    }

    //method to read the file in
    public static LinkedList<String> readFile(String file) {
        try {
            Scanner input = new Scanner(new File(file));
            while (input.hasNext()) {
                words.add(input.next().toUpperCase());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return words;
    }

    //method to get the words based on the user length
    public static void possibleWords(int length, int Numguess, LinkedList<String> dic) {
        if (length < 1 || Numguess < 0) {
            throw new IllegalArgumentException();
        }
        //guesses = Numguess;
        currentWords = new TreeSet<String>();
       // MostCurrentWords = new LinkedList<String>();//added this to maybe use a linkedList

        for (String potentialWord : dic) {
            if (potentialWord.length() == length) {
                currentWords.add(potentialWord);
            }
        }
        pattern = "";
        for (int i = 0; i < length; i++) {
            pattern += "-";
        }
    }

    //method that thows exceptions if is empty or guesses are less than 1
    //help with the guess to update the pattern based on the guess, chooses the longest family.
    public static int MethodForGuess(char guess) {
        if (currentWords.isEmpty() || guesses < 1) {
            throw new IllegalStateException();
        }
        if (guessedLetters.contains(guess)) {
            throw new IllegalStateException();
        }
        guessedLetters.add(guess);

        Map<String, TreeSet<String>> letterMap = new TreeMap<String, TreeSet<String>>();

        for (String Currwords : currentWords) {
            getPatternForWord(Currwords, guess);
            getPatternMapForGuess(letterMap, Currwords, pattern);
        }
        int maxS = 0;
        String maxkeyP = "";
        for (String currentkey : letterMap.keySet()) {
            Set<String> temp = letterMap.get(currentkey);
            if (temp.size() > maxS) {
                maxS = letterMap.get(currentkey).size();
                maxkeyP = currentkey;
            }
        }
        currentWords = letterMap.get(maxkeyP);
        pattern = maxkeyP;
        return KeepCount(maxkeyP, guess);
    }
    //metjod to help with the pattern and strings returning the given pattern.
    private static int KeepCount(String maxKeyP, char guess) {
        int guessCount = 0;
        for (int i = 0; i < maxKeyP.length(); i++) {
            if (maxKeyP.charAt(i) == guess) {
                guessCount++;
            }
        }
        if (guessCount == 0) {
            guesses--;
        }
        return guessCount;
    }

    //method to help with the pattern of the given guess and compares it with the current words.
    public static void getPatternForWord(String Currword, char guess) {
        int length = pattern.length();
        pattern = "";
        for (int i = 0; i < length; i++) {//had guessed letters
            if (guessedLetters.contains(Currword.charAt(i))) {
                pattern += Currword.charAt(i);
            } else if (Currword.charAt(i) == guess) {
                pattern += guess;
            } else {
                pattern += "-";
            }
        }
    }

    // method to help with storing the corresponding list of words.
    public static void getPatternMapForGuess(Map<String, TreeSet<String>> letterMap, String value, String newPattern) {

        if (!letterMap.containsKey(newPattern)) {
            letterMap.put(newPattern, new TreeSet<String>());
        }
        letterMap.get(newPattern).add(value);
    }
}

