package spell;

import java.io.IOException;
import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {

	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */

	public static void main(String[] args) throws IOException {

		String dictionaryFileName = args[0];
		String inputWord = args[1];

		SpellCorrector corrector = new SpellCorrector();

		corrector.useDictionary(dictionaryFileName);
		String suggestion = corrector.suggestSimilarWord(inputWord);
		if (suggestion == null) {
			suggestion = "No similar word found";
		}

		System.out.println("Suggestion is: " + suggestion);
	}
}

/*
	public static void main(String[] args) throws IOException {
		String s1 = args[0];
		String s2 = args[1];

		Trie t1 = new Trie();
		Trie t2 = new Trie();
		t1.add(s1);
		t1.add(s2);
		t2.add("dog");
		t2.add("cat");

		if(t1.equals(t2)) {
			System.out.println("equal!");
		}

	}

}
*/