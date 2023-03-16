package com.example.familymapclient.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Serializer {
    public static String readString(InputStream inputStream) throws IOException {
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];
        int len;
        while ((len = inputReader.read(buffer)) > 0) {
            builder.append(buffer, 0, len);
        }

        return builder.toString();
    }

    public static void writeString(String str, OutputStream outputStream) throws IOException {
        OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
        outWriter.write(str);
        outWriter.flush();
    }
}
