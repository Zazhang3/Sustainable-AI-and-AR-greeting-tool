package com.example;

import java.util.Scanner;

public class TestTextToSpeechConverter {
    /** A piece of test code
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Enter the text to be converted to speech
        System.out.print("Enter the text to convert to speech: ");
        String text = scanner.nextLine();

        // Set a default outputFilePath for this test
        String outputFilePath = "HelloWorld.wav";

        // Reference a TextToSpeechConverter Class
        TextToSpeechConverter converter = new TextToSpeechConverter();

        converter.convertTextToSpeech(text, outputFilePath);

        scanner.close();
        // A signal that the convert has finished successfully
        System.out.println("Text has been converted to speech and saved as " + outputFilePath);
    }
}
