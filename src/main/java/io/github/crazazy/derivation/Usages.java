package io.github.crazazy.derivation;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of ways you can use the regex DSL
 * What is peculiar is that the methods for validating and scanning for words will never require backtracking
 * This means we can leave the problems of having a DFA that is secretly not so deterministic after all behind
 * It should be noted that, as mentioned in the paper in {@link RegexElement}, there are ways of turning this DSL
 * into a DFA
 */
public class Usages {

    /**
     * checks if a word is in the language described by the regex
     *
     * @param regex a regex describing a language
     * @param word  the word to be checked against
     * @return true if the word is in the language, false if not
     */
    public static boolean valid(RegexElement regex, String word) {
        RegexElement current = regex;
        for (char i : word.toCharArray()) {
            current = current.derive(i);
            // make the regex minimal. this step is optional
            current = current.collapse();
        }
        return current.nullable();
    }

    /**
     * Finds most words that match the regex
     *
     * @param regex the regex to match against
     * @param words a sample string possibly containing words matching the regex
     * @return all words in the sample string that match the regex
     */
    public static List<String> naiveFindWords(RegexElement regex, String words) {
        List<String> result = new ArrayList<>();
        char[] charList = words.toCharArray();
        RegexElement current = regex;
        String currentWord = "";
        for (char i : charList) {
            RegexElement derived = current.derive(i);
            if (derived instanceof Nul) {
                if (current.nullable()) {
                    result.add(currentWord);
                }
                // extra check for if there isn't a new match right after a completed match
                if (regex.derive(i) instanceof Nul) {
                    current = regex;
                    currentWord = "";
                } else {
                    current = regex.derive(i);
                    currentWord = "" + i;
                }

            } else {
                currentWord += i;
                current = derived;
            }
        }
        // check if there was a match at the end of the string
        if (current.nullable()) {
            result.add(currentWord);
        }
        return result;
    }

    public static void main(String[] args) {
        RegexElement lang = new Union(
                new Kleene(Chr.fromString("ab")),
                new Chr('c')
        );

        RegexElement[] tests = new RegexElement[]{
                lang,
                lang.derive('a'),
                lang.derive('c'),
                lang.derive('d')
        };
        for (RegexElement i : tests) {
            System.out.printf("%s Nullability: %s\n", i.toString(), i.nullable());
        }
        String[] testWords = new String[]{
                "c",
                "ababab",
                "a",
                "ac"
        };
        for (String i : testWords) {
            System.out.printf("%s validity: %s\n", i, valid(lang, i));
        }

        System.out.println("Finding words");
        for (String i : naiveFindWords(lang, "abab cbab aaabccc")) {
            System.out.println(i);
        }

        RegexElement doubleQuote = new Prod(
                new Chr('"'),
                new Kleene(new Union(
                        Chr.fromString("\"\""),
                        new Chr('a'),
                        new Chr('b'),
                        new Chr('c')
                )),
                new Chr('"')
        );
        System.out.println(doubleQuote.toString());
        testWords = new String[]{
                "\"ab\"",
                "\"aab\"\"c\"",
                "\"aabc\"cc\""
        };
        for (String i : testWords) {
            System.out.printf("%s validity: %s\n", i, valid(doubleQuote, i));
        }

        for (String i : naiveFindWords(doubleQuote, "\"ab\" \"aab\"\"c\" \"aabc\"cc\"")) {
            System.out.println(i);
        }
    }

}
