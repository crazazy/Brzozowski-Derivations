package io.github.crazazy.derivation;

/**
 * The base element of the custom regex eDSL.
 * The DSL implements Brzozowski derivations, verifying words in a language with it
 * should be trivial
 * for more info see <a href="http://www.ccs.neu.edu/home/turon/re-deriv.pdf">a paper</a> implementing the algorithm
 * in a haskell-like pseudo language
 */
public interface RegexElement {
    /**
     * Derive a regular expression on a sample character
     * Formally, a derivation on a character is described as Ð[u](L) = [v | {@link Prod}(u, v) = L]
     * In practice, it's kind of difficult for me personally to explain. I highly recommend you read the paper
     * I mentioned if you want to get a deeper understanding of what is going on.
     *
     * @param input the character to derive on
     * @return the Regex derivation of the current instance
     */
    RegexElement derive(char input);

    /**
     * nullability means whether the language described in the regex contains the empty string
     *
     * @return whether the regex is nullable or not
     */
    boolean nullable();

    /**
     * Tries to remove excessive constructs in regexes
     * This function should be idempotent
     *
     * @return a simplified regex
     */
    RegexElement collapse();


}
