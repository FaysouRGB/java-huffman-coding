package com.faycal.huffman;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for creating histograms from text.
 */
public class Histogram {

    /**
     * Creates a histogram from the given text.
     *
     * @param text the input text
     * @return a map where keys are characters and values are their frequencies
     */
    public static Map<Character, Integer> createHistogram(String text) {
        Map<Character, Integer> histogram = new HashMap<>();
        for (char character : text.toCharArray()) {
            histogram.compute(character, (k, v) -> (v == null) ? 1 : v + 1);
        }
        return histogram;
    }
}
