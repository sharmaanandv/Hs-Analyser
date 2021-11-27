package com.hs.analyser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.hs.analyser.analyser.Analyser;
import com.hs.analyser.analyser.HsAnalyser;
public class TestAnalyser {

    @Test
    public void testAnalyser1(){

        Analyser analyser = new HsAnalyser();
        assertEquals(1, analyser.countOfSpecialChar("!A"));
        assertEquals(2, analyser.countNoOfWords("one two"));
    }

    @Test
    public void testAnalyser2(){
        Analyser analyser = new HsAnalyser();
        assertEquals(0, analyser.countOfSpecialChar(""));
        assertEquals(1, analyser.countNoOfWords(""));
    }


}
