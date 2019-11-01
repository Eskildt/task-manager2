package no.projectMembers.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class EchoHttpController implements HttpController {
    @Override
    public void handle(String requestPath, Map<String, String> query, OutputStream outputStream) throws IOException {
        String status = query.getOrDefault("status", "200");
        String contentType = query.getOrDefault("content-type", "text/plain");
        String body = query.getOrDefault("body", "Hello World!");
        int contentLength = body.length();
        outputStream.write(("HTTP/1.1 " + status + "OK\r\n" +
                "Content-type " + contentType + "\r\n" +
                "Content-length: " + contentLength + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body).getBytes());
    }
}
