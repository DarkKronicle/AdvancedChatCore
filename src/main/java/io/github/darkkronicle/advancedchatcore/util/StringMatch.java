package io.github.darkkronicle.advancedchatcore.util;


import lombok.AllArgsConstructor;

/**
 * A class to store data about a match.
 *
 * This class is comparable based on where it starts.
 */
@AllArgsConstructor
public class StringMatch implements Comparable<StringMatch> {
    /**
     * The content that was matched
     */
    public String match;

    /**
     * The index of the start of the match
     */
    public Integer start;

    /**
     * The index of the end of the match
     */
    public Integer end;

    @Override
    public int compareTo(StringMatch o) {
        return start.compareTo(o.start);
    }

}
