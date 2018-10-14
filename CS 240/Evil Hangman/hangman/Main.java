package hangman;

import java.io.IOException;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws IOException {
        String fileName = args[0];

        File inputFile = new File(fileName);

        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        if (wordLength < 2) {
            throw new NoSuchElementException();
        }

        if (guesses < 1) {
            throw new NoSuchElementException();
        }

        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();
        evilHangmanGame.startGame(inputFile, wordLength);
        playGame(evilHangmanGame, guesses);

    }

    public static void playGame(EvilHangmanGame evilHangmanGame, int guesses) {
        Set<String> enteredList = evilHangmanGame.enteredList;
        String pattern = evilHangmanGame.getPattern();
        int leftGuesses = guesses;
        printString(leftGuesses, enteredList, evilHangmanGame.pattern);
        Scanner input = new Scanner(System.in);
        String inputChar = "";
        char letter = 0;

        while(leftGuesses != 0) {
            inputChar = input.nextLine().toLowerCase();
            Set<String> newSet = new TreeSet<String>();
            if(inputChar.length() > 1 || inputChar.length() < 1) {
                System.out.println("Invalid input");
                continue;
            }
            letter = inputChar.charAt(0);
            if(Character.isSpaceChar(letter) || !Character.isLetter(letter)) {
                System.out.println("Invalid input");
                continue;
            }
            try {
                newSet = evilHangmanGame.makeGuess(letter);
                pattern = evilHangmanGame.getPattern();
                int matchNum = 0;
                if(pattern.contains(Character.toString(letter))){
                    leftGuesses++;
                    for(int i = 0; i < pattern.length(); i++) {
                        if(pattern.charAt(i) == letter) {
                            matchNum++;
                        }
                    }
                }
                int numDash = 0;
                for(int i = 0; i < pattern.length(); i++) {
                    if(pattern.charAt(i) == '-') {
                        numDash++;
                    }
                }
                if(numDash == 0) {
                    System.out.println("You win! " + newSet.toString());
                    System.exit(0);
                }
                printAnswer(letter, matchNum);
            }
            catch (IEvilHangmanGame.GuessAlreadyMadeException e) {
                System.out.println("You already used that letter");
                continue;
            }
            --leftGuesses;
            if(leftGuesses == 0) {
                checkWin(pattern, newSet);
                break;
            }
            printString(leftGuesses, enteredList, pattern);
        }
        input.close();
    }


    public static void printString(int guesses, Set<String> enteredList, String pattern) {
        System.out.println("you have " + guesses + " guesses left");
        System.out.print("Used letters: ");
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = enteredList.iterator(); it.hasNext(); ) {
            String s = it.next();
            sb.append(s + " ");
        }
        System.out.println(sb.toString());
        System.out.println("Word: " + pattern);
        System.out.print("Enter guess: ");
    }

    public static void printAnswer(char letter, int matchNum) {
        if(matchNum > 0){
            System.out.println("Yes, there is "+matchNum+" "+letter+"\n");
        }
        else{
            System.out.println("Sorry, there are no " + letter + "'s"+"\n");
        }
    }

    public static void checkWin(String pattern, Set<String> newSet) {
        int numDash = 0;
        for(int i = 0; i < pattern.length(); i++) {
            if(pattern.charAt(i) == '-') {
                numDash++;
            }
        }
        printResult(newSet, numDash);
    }

    public static void printResult(Set<String> word, int numDash) {
        if(numDash == 0) {
            System.out.println("You win! " + word.toString());
            System.exit(0);
        }
        else {
            if(word.size() > 1) {
                String randomWord = "";
                for(String random : word) {
                    randomWord = random;
                    break;
                }
                System.out.println("You lose! \n" + "The word was: " + randomWord);
                System.exit(0);
            }else {
                System.out.println("You lose! \n" + "The word was: " + word.toString());
                System.exit(0);
            }
        }

    }

}
