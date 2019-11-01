package no.projectMembers.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpController {
    void handle(String requestPath, Map<String, String> socket, OutputStream outputStream) throws IOException;

}
