package io.github.crazazy.derivation;

import java.util.Objects;

/**
 * class for the kleene star.
 * regexes under a kleene star can be derived 0 or more times
 */
public class Kleene implements RegexElement {
    private final RegexElement inner;

    /**
     * Class constructor
     *
     * @param inner regex under the kleene star
     */
    public Kleene(RegexElement inner) {
        this.inner = inner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kleene kleene = (Kleene) o;
        return inner.equals(kleene.inner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inner);
    }

    @Override
    public String toString() {
        return "(" + inner.toString() + ")*";
    }

    @Override
    public RegexElement derive(char input) {
        RegexElement derived = inner.derive(input);
        if (derived instanceof Nul) {
            return new Nul();
        } else {
            return new Prod(derived, this);
        }
    }

    @Override
    public boolean nullable() {
        return true;
    }

    @Override
    public RegexElement collapse() {
        return new Kleene(inner.collapse());
    }
}
