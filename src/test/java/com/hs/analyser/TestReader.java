package com.hs.analyser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hs.analyser.reader.HsFileReader;
import com.hs.analyser.reader.TxtFileReader;

public class TestReader {

    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testReader() throws IOException {
        File file = File.createTempFile("test-", "txt");
        HsFileReader reader = new TxtFileReader(file.getAbsolutePath());
        reader.read();
        file.deleteOnExit();
        String expectedResult0 = "No Special Characters found in file";
        String expectedResult1 = "No word found in file";
        String[] result = outContent.toString().split("\n");
        System.out.println("Result is :"+result.length);
        for(String res: result){
            System.out.println("1 "+res);
        }
        System.out.println("end");

        assertEquals(0, 0);
        assertEquals(0, result[0].toString().indexOf(expectedResult0));
        assertEquals(0, result[1].toString().indexOf(expectedResult1));
    }

}
