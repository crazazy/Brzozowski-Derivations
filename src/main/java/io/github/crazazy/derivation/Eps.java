package io.github.crazazy.derivation;

/**
 * Epsilon class: represents the empty string
 */
public class Eps implements RegexElement {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Eps;
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
        return true;
    }

    @Override
    public RegexElement collapse() {
        return this;
    }
}
