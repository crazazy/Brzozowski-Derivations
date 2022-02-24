package io.github.crazazy.derivation;

/**
 * Class representing the empty set
 */
public class Nul implements RegexElement {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Nul;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public RegexElement derive(char input) {
        return new Nul();
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
