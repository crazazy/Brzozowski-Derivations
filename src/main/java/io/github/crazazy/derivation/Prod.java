package io.github.crazazy.derivation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A production of multiple regexes
 */
public class Prod implements RegexElement {

    private final RegexElement[] subElements;

    /**
     * Class constructor
     *
     * @param subElements regexes of which the production consists
     */
    public Prod(RegexElement... subElements) {
        this.subElements = subElements;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (RegexElement i : subElements) {
            result.append(i.toString());
        }
        return result.toString();
    }

    @Override
    public RegexElement derive(char input) {
        if (subElements.length == 0) {
            return new Nul();
        }
        RegexElement first = subElements[0];
        RegexElement derived = first.derive(input);
        RegexElement derivedRest;
        if (first.nullable()) {
            derivedRest = new Prod(Arrays.copyOfRange(subElements, 1, subElements.length)).derive(input);
        } else {
            derivedRest = new Nul();
        }
        RegexElement[] rest = Arrays.copyOf(subElements, subElements.length);
        rest[0] = derived;
        RegexElement result;
        if (derived instanceof Nul) {
            result = new Nul();
        } else {
            result = new Prod(rest);
        }
        if (derivedRest instanceof Nul && result instanceof Nul) {
            return new Nul();
        } else {
            return new Union(result, derivedRest);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prod prod = (Prod) o;
        return Arrays.equals(subElements, prod.subElements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(subElements);
    }

    @Override
    public boolean nullable() {
        for (RegexElement i : subElements) {
            if (!i.nullable()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public RegexElement collapse() {
        List<RegexElement> newSubElements = new ArrayList<>();
        for (RegexElement regex : subElements) {
            RegexElement newRegex = regex.collapse();
            if (!(newRegex instanceof Eps)) {
                if (newRegex instanceof Prod) {
                    for (RegexElement i : ((Prod) newRegex).subElements) {
                        newSubElements.add(i);
                    }
                } else {
                    newSubElements.add(newRegex);
                }
            }
        }

        switch (newSubElements.size()) {
            case 0:
                return new Eps();
            case 1:
                return newSubElements.get(0);
            default:
                if (newSubElements.get(0) instanceof Nul) {
                    return new Nul();
                } else {
                    return new Prod(newSubElements.toArray(new RegexElement[newSubElements.size()]));
                }
        }
    }
}
