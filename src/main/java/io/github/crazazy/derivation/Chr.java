package io.github.crazazy.derivation;

import java.util.Objects;

/**
 * Regex class for a single character
 */
public class Chr implements RegexElement {
    private final char input;

    /**
     * Class constructor. see <code>Chr.fromString</code> for a way of inputting multiple letters into the regex
     *
     * @param input character to be used as regex
     */
    public Chr(char input) {
        this.input = input;
    }

    /**
     * because having to type "new Chr('x')" for every letter in a string is tedious there is also a utility tool for
     * getting multiple characters, represented as a production of characters
     *
     * @param string the words to use for the regex
     * @return a <code>Production</code> of characters
     */
    public static RegexElement fromString(String string) {
        RegexElement[] charList = new RegexElement[string.length()];
        char[] chars = string.toCharArray();
        for (int i = 0; i < charList.length; i++) {
            charList[i] = new Chr(chars[i]);
        }
        return new Prod(charList);
    }

    @Override
    public String toString() {
        return "" + input;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chr)) return false;
        Chr chr = (Chr) o;
        return input == chr.input;
    }

    @Override
    public int hashCode() {
        return Objects.hash(input);
    }

    @Override
    public RegexElement derive(char input) {
        if (this.input == input) {
            return new Eps();
        } else {
            return new Nul();
        }
    }

    @Override
    public boolean nullable() {
        return false;
    }

    @Override
    public RegexElement collapse() {
        return this;
    }
}
