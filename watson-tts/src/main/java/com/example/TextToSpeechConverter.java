package com.example;

import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TextToSpeechConverter {

    private TextToSpeech textToSpeech;

    /** Get an IAMAuthenticator via given apiKey
     *  Get textToSpeech via IAMAuthenticator and Url
     */
    public TextToSpeechConverter() {
        IamAuthenticator authenticator = new IamAuthenticator("a8EJRzum-gl06iJIXU91DVvz_KPMuYIFhliMyb8066CF");
        this.textToSpeech = new TextToSpeech(authenticator);
        this.textToSpeech.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/59e94c02-8277-4f64-912c-354645efc801");
    }
    /**Convert String to .wav file at given file path
     * @param text, outputFilePath
    */
    public void convertTextToSpeech(String text, String outputFilePath) {
        try {
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
                    .text(text)
                    .accept("audio/wav") // Set the audio format
                    .voice("en-US_AllisonV3Voice") // Set the type of voice
                    .build();
            // Operations related to file stream
            InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute().getResult();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

            OutputStream out = new FileOutputStream(outputFilePath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            // Close file stream
            out.close();
            in.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

