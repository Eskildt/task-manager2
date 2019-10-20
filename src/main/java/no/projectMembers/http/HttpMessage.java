package no.projectMembers.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpMessage {


    protected String body;
    protected String startLine;
    protected Map<String, String> headers = new HashMap<>();

    public HttpMessage(InputStream inputStream) throws IOException {

        startLine = readLine(inputStream);
        String headerLine;
        while (!(headerLine = readLine(inputStream)).isBlank()) {
            int colonPos = headerLine.indexOf(':');
            String headerName = headerLine.substring(0, colonPos).trim();
            String headerValue = headerLine.substring(colonPos + 1).trim();
            headers.put(headerName.toLowerCase(), headerValue);
        }

        if (getHeader("Content-Length") != null) {
            this.body = readBytes(inputStream, getContentLength());
        }

    }

    public String getHeader(String headerName) {
        return headers.get(headerName.toLowerCase());
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public static String readLine(InputStream inputStream) throws IOException {

        StringBuilder line = new StringBuilder();

        int c;
        while ((c = inputStream.read()) != -1) {
            if (c == '\r') {
                //noinspection ResultOfMethodCallIgnored
                inputStream.read(); // the follow \n
                break;
            }

            line.append((char)c);
        }

        return line.toString();

    }

    protected String readBytes(InputStream inputStream, int contentLength) throws IOException {
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            body.append((char)inputStream.read());
        }
        return body.toString();
    }

    public String getStartLine() {
        return startLine;
    }


}