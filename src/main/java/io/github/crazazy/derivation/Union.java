package io.github.crazazy.derivation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The union of multiple regexes
 */
public class Union implements RegexElement {
    private final RegexElement[] subElements;

    /**
     * Class constructor
     *
     * @param subElements regexes of which the union consists
     */
    public Union(RegexElement... subElements) {
        this.subElements = subElements;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("(");
        result.append(subElements[0].toString());
        for (RegexElement i : Arrays.copyOfRange(subElements, 1, subElements.length)) {
            result.append(" | ");
            result.append(i.toString());
        }
        result.append(")");

        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Union)) return false;
        Union union = (Union) o;
        return Arrays.equals(subElements, union.subElements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(subElements);
    }

    @Override
    public RegexElement derive(char input) {
        List<RegexElement> regexElementList = new ArrayList<>();
        for (RegexElement i : subElements) {
            RegexElement derived = i.derive(input);
            if (!(derived instanceof Nul)) {
                regexElementList.add(derived);
            }
        }
        if (regexElementList.size() == 0) {
            return new Nul();
        } else {
            return new Union(regexElementList.toArray(new RegexElement[0]));
        }
    }

    @Override
    public boolean nullable() {
        for (RegexElement i : subElements) {
            if (i.nullable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RegexElement collapse() {
        List<RegexElement> newSubElements = new ArrayList<>();
        for (RegexElement regex : subElements) {
            RegexElement newRegex = regex.collapse();
            if (!(newRegex instanceof Nul)) {
                if (newRegex instanceof Union) {
                    Collections.addAll(newSubElements, ((Union) newRegex).subElements);
                } else {
                    newSubElements.add(newRegex);
                }
            }
        }
        switch (newSubElements.size()) {
            case 0:
                return new Nul();
            case 1:
                return newSubElements.get(0);
            default:
                return new Union(newSubElements.toArray(new RegexElement[0]));

        }
    }
}
