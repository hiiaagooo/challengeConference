package br.com.talks.challengeconference.maintest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.talks.challengeconference.application.Conference;

class ConferenceTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	public void test() {
		Conference conference = new Conference();
		 File file = new File("resources/input.txt");
	        boolean expectedResult = true;
	        boolean actualResult = false;
	        if (file.exists()){
	            actualResult = true;
	        }
	        assertEquals(expectedResult, actualResult);
		
	}

}
