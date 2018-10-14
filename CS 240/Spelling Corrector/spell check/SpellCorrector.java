package spell;

import java.io.IOException;
import java.io.*;
import java.lang.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    private Trie dictionary;

    public Trie getDictionary() {
        return dictionary;
    }

    public SpellCorrector() {
        dictionary = new Trie();
    }

    public void useDictionary(String dictionaryFileName) throws IOException {//making dictionary
        String word;
        File inputFile = new File(dictionaryFileName);
        FileReader fileReader = new FileReader(inputFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNext()) {
            word = scanner.next().toLowerCase();//all lower case
            dictionary.add(word);//actual function making dictionary
        }
        scanner.close();
    }

    public String suggestSimilarWord(String inputWord){// throws IOException {
       /* if (inputWord.equals("")) {
            throw new IOException();
        }*/
        inputWord = inputWord.toLowerCase();
        if (dictionary.find(inputWord) != null) {//correct word
            return inputWord;
        }
        else {
            //Set<String> modifiedWords = distanceOne(inputWord);
            Set<String> edited = new TreeSet<String>();
            insertion(inputWord, edited);
            deletion(inputWord, edited);
            transposition(inputWord, edited);
            alteration(inputWord, edited);

            String suggestedWord = suggestWord(edited);
            if(suggestedWord == null) {//there is no match
                Set<String> edited2 = distanceTwo(edited);
                suggestedWord = suggestWord(edited2);
                if(suggestedWord == null) {
                    //throw new IOException();
                }
            }
            return suggestedWord;
        }
    }

    public Set<String> distanceTwo(Set<String> edited) {
        Set<String> modified = new TreeSet<String>();
        for(String string : edited) {
            insertion(string, modified);
            deletion(string, modified);
            transposition(string, modified);
            alteration(string, modified);
        }
        return modified;

    }
    public void insertion(String word, Set<String> edited) {
        for(int i = 0; i < word.length() + 1; i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                StringBuilder sb = new StringBuilder(word);
                sb.insert(i, c);
                edited.add(sb.toString());
            }
        }
    }
    public void deletion(String word, Set<String> edited) {
        for(int i = 0; i < word.length(); i++) {
            StringBuilder sb = new StringBuilder(word);
            sb.deleteCharAt(i);
            edited.add(sb.toString());
        }
    }
    public void transposition(String word, Set<String> edited) {
        for(int i = 0; i < word.length() - 1; i++) {
            char[] c = word.toCharArray();
            char temp = c[i];
            c[i] = c[i+1];
            c[i+1] = temp;
            String transposed = new String(c);
            edited.add(transposed);
        }
    }
    public void alteration(String word, Set<String> edited) {
        for(int i = 0; i < word.length(); i++) {
            char[] alter = word.toCharArray();
            for (char c = 'a'; c <= 'z'; c++) {
                alter[i] = c;
                String altered = new String(alter);
                edited.add(altered);
            }
        }
    }

    public String suggestWord(Set<String> edited) {
        String suggest = null;
        int maxValue = 0;
        for(String string : edited) {
            if(dictionary.find(string) != null) {
                Trie.INode n = dictionary.find(string);
                if(n.getValue() > maxValue) {
                    maxValue = n.getValue();
                    suggest = string;
                }
            }
        }
        return suggest;
    }

}