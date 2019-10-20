package no.projectMembers.http;

import java.io.IOException;
import java.net.Socket;

public class HttpClient {

    private final String hostname;
    private final int port;
    private String requestTarget;

    public HttpClient(String hostname, int port, String requestTarget){

        this.hostname = hostname;
        this.port = port;
        this.requestTarget = requestTarget;
    }

    public static void
    main(String[] args) throws IOException {
        new HttpClient("urlecho.appspot.com", 80, "/echo?body=Hello+World!").execute();
    }

    public HttpClientResponse execute() throws IOException {
        Socket socket = new Socket(hostname, port);

        socket.getOutputStream().write(("GET " + requestTarget + " HTTP/1.1\r\n" + "Host: " + hostname + "\r\n" + "\r\n").getBytes());
        socket.getOutputStream().flush();

        return new HttpClientResponse(socket.getInputStream());

    }

}
