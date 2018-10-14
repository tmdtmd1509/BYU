package hangman;

import java.io.IOException;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.Map;




public class EvilHangmanGame implements IEvilHangmanGame {

    public EvilHangmanGame() {
        /*
        Map<String, Set<String>> map;
        String pattern;
        Set<String> words = new TreeSet<String>();//origianl
        Set<String> enteredList = new TreeSet<String>();
*/
    }

    public HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
    public String pattern;
    public Set<String> words = new TreeSet<String>();//origianl
    public Set<String> enteredList = new TreeSet<String>();
    //public char[] enteredList;

    @Override
    public void startGame(File dictionary, int wordLength) {
        Scanner scanner = null;
        pattern = initialPattern(wordLength);
        try {
            FileReader fileReader = new FileReader(dictionary);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            scanner = new Scanner(bufferedReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String curWord = null;
        while (scanner.hasNext()) {
            curWord = scanner.next().toLowerCase();
            if (wordLength == curWord.length()) {
                words.add(curWord);
            }
        }
        scanner.close();
    }


    public String initialPattern(int wordLength) {
        StringBuilder sb = new StringBuilder();
        //sb.append('u');
        for (int i = 0; i < wordLength; i++) {
            sb.append('-');
        }
        return sb.toString();
    }

    public static class GuessAlreadyMadeException extends Exception {
    }

    @Override
    public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException {
        String letter = Character.toString(guess);
        if (enteredList.contains(letter)) {
            throw new IEvilHangmanGame.GuessAlreadyMadeException();
        }
        enteredList.add(letter);
        buildMap(guess);
        String key = choosePattern();
        words = map.get(key);
        setPattern(key);
        return words;
    }

    public void setPattern(String key) {
        pattern = key;
    }

    public String getPattern() {
        System.out.println("choosed one " + pattern);
        return pattern;
    }

    public void buildMap(char guess) {
        map.clear();
        for (String word : words) {
            //Iterator<String> it = words.iterator();
            //while(it.hasNext()) {
            //    String word = it.next();
            String pattern = buildPattern(word, guess);
            if (map.containsKey(pattern)) {
                map.get(pattern).add(word);
            } else {
                Set<String> patternWords = new TreeSet<String>();
                patternWords.add(word);
                map.put(pattern, patternWords);
            }
        }
    }

    public String buildPattern(String word, char guess) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                //System.out.println("match!!!!!");
                sb.append(guess);
            } else if (pattern.charAt(i) != '-') {
                sb.append(pattern.charAt(i));
            } else {
                sb.append('-');
            }
        }
        return sb.toString();
    }

    public String choosePattern() {
        Set<String> set = map.keySet();
        String pattern = "";
        int maxSize = 0;
        for (String s : set) {
            if (maxSize < map.get(s).size()) {
                maxSize = map.get(s).size();
                //System.out.println(maxSize+" "+s);
                pattern = s;
            } else if (maxSize == map.get(s).size()) {
                pattern = compareDash(pattern, s);
            }
        }
        return pattern;
    }

    public String compareDash(String pattern, String s) {
        int numDash1 = 0;
        int numDash2 = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '-') {
                numDash1++;
            }
            if (s.charAt(i) == '-') {
                numDash2++;
            }
        }
        if (numDash1 > numDash2) {
            return pattern;
        } else if (numDash1 < numDash2) {
            return s;
        } else {
            return finalcompare1(pattern, s);
        }
    }
/*
    public String finalcompare(String pattern, String s) {
        int elseadded1 = 0;
        int elseadded2 = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '-') {
                elseadded1 = +i;
                System.out.println("final works1" + elseadded1);

            }
            if (s.charAt(i) == '-') {
                elseadded2 = +i;
                System.out.println("final works2" + elseadded2);

            }
        }

        if (elseadded1 < elseadded2) {
            return pattern;
        } else if (elseadded1 > elseadded2) {
            return s;
        } else {
            return finalcompare(pattern, s);
        }
    }
*/
    public String finalcompare1(String pattern, String s) {
        for (int i = pattern.length()-1; i > 0; i--) {
            if (pattern.charAt(i) != s.charAt(i)) {
                if (pattern.charAt(i) != '-') {
                    return pattern;
                } else if (s.charAt(i) != '-') {
                    return s;
                }
            }
        }
        return finalcompare1(pattern, s);
    }
}
