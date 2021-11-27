package com.hs.analyser.reader;

import com.hs.analyser.analyser.Analyser;
import com.hs.analyser.analyser.HsAnalyser;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TxtFileReader implements HsFileReader {

    private String fileName;
    private Analyser analyser;

    public TxtFileReader(String fileName) {
        this.fileName = fileName;
        analyser = new HsAnalyser();
    }

    /**
     * This method will read data from file line by line
     * then call ana call analyse to get extract statistical data from file
     */
    @Override
    public void read() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("read: unable to read file: " + fileName);
            e.printStackTrace();
            return;
        }

        int countOfSpecialChar = 0;
        int countNoOfWords = 0;
        String[] split = fileName.split("\\\\");
        String fname = split[split.length - 1];
        try (Scanner fileScanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                countOfSpecialChar += analyser.countOfSpecialChar(line);
                countNoOfWords += analyser.countNoOfWords(line);
            }
            if (countOfSpecialChar == 0) {
                System.out.println("No Special Characters found in file : " + fname);
            } else {
                System.out.println("Special Characters found in file " + fname + " : " + countOfSpecialChar);
            }
            if (countNoOfWords == 0) {
                System.out.println("No word found in file : " + fname);
            } else {
                System.out.println("Total number of words present in file " + fname + " : " + countNoOfWords);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                System.out.println("read: fail to close the file");
                e.printStackTrace();
            }
        }

    }
}
