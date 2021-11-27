package com.hs.analyser.analyser;

/**
 * This class can be enhance to provide various statistical data
 */
public class HsAnalyser implements Analyser {

    /**
     * This method can provide countOfSpecialChar for given input string
     *
     * @param line data to be analyse
     * @return countOfSpecialChar
     */
    @Override
    public int countOfSpecialChar(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isDigit(line.charAt(i))
                    && !Character.isLetter(line.charAt(i))
                    && !Character.isWhitespace(line.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * This method can provide countNoOfWords for given input string
     *
     * @param line data to be analyse
     * @return countNoOfWords
     */
    @Override
    public int countNoOfWords(String line) {
        String[] split = line.split(" ");
        return split.length;
    }
}
