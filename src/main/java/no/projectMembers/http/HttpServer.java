package no.projectMembers.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private HttpController defaultController;
    private ServerSocket serverSocket;
    private String fileLocation;
    private Map<String, HttpController> controllers = new HashMap<>();

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        defaultController = new FileHttpController(this);

        controllers.put("/echo", new EchoHttpController());
    }


    public static void main(String[] args) throws IOException {
        new HttpServer(8080).start();
    }

    public void start() {
        new Thread(() -> run()).start();
    }

    public void run() {

        /*
        boolean keepRunning = true;

        while(keepRunning) {

         */

        try {
            Socket socket = serverSocket.accept();

            String requestLine = HttpMessage.readLine(socket.getInputStream());
            while (!HttpMessage.readLine(socket.getInputStream()).isBlank()) {
            }

            String requestTarget = requestLine.split(" ")[1];
            int questionPos = requestTarget.indexOf('?');
            String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);

            Map<String, String> query = parseRequestParameters(requestLine);

            if (requestPath.equals("/echo")) {
                String status = query.getOrDefault("status", "200");
                String contentType = query.getOrDefault("content-type", "text/plain");
                String body = query.getOrDefault("body", "Hello World!");
                int contentLength = body.length();
                socket.getOutputStream().write(("HTTP/1.1 " + status + "OK\r\n" +
                        "Content-type " + contentType + "\r\n" +
                        "Content-length: " + contentLength + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        body).getBytes());
                controllers.get(requestPath).handle(requestPath, query, socket.getOutputStream());
            } else {
                defaultController.handle(requestPath, query, socket.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    if(requestLine.split(" ").length > 1) {
         requestTarget = requestLine.split(" ")[1];
    } else {requestTarget = "/";}

    int questionPos = requestTarget.indexOf('?');
    String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);
    Map<String, String> requestParameters = parseRequestParameters(requestLine);

    if(requestPath.equals("/")) {
        requestPath = "/index.html";
    }


    switch (requestPath) {
        case "/stop":
            System.exit(0);
            break;
        case "/tasksapi":
            String tasks = MemberDB.listAllTasks();

            socket.getOutputStream().write(("HTTP/1.0 200 OK\r\n" + "Content-length: " + tasks.length() + "\r\n" + "Connection: close\r\n"+ "\r\n" + tasks).getBytes());
            break;
        case "/membersapi":
            String members = MemberDB.listAllMembers();
            System.out.println(members);
            socket.getOutputStream().write(("HTTP/1.0 200 OK\r\n" + "Content-length: " + (members.length() + 1) + "\r\n" + "Connection: close\r\n"+ "\r\n" + members).getBytes());
            break;
        case "/echo":
            String statusCode = requestParameters.getOrDefault("status", "200");
            String location = requestParameters.getOrDefault("location", null);
            String body = requestParameters.getOrDefault("body", "Hello World!");

            socket.getOutputStream().write(("HTTP/1.0 " + statusCode + " OK\r\n" + "Content-length: " + body.length() + "\r\n" + "Connection: close\r\n" +
                    (location != null ? "Location: " + location + "\r\n" : "") + "\r\n" + body).getBytes());
            break;

        default:
            File file = new File(fileLocation + requestPath);
            if(file.isFile()) {
                socket.getOutputStream().write(("HTTP/1.1 200 OK\r\n" + "Content-Length: " + file.length() + "\r\n" + "Connection: close\r\n" + "\r\n").getBytes());

                new FileInputStream(file).transferTo(socket.getOutputStream());
            } else {
                socket.getOutputStream().write(("HTTP/1.1 404 NOT FOUND\r\n").getBytes());
            }
    }

    socket.getOutputStream().flush();

} catch (IOException | SQLException e) {
    e.printStackTrace();
}

}
}
     */

    private Map<String, String> parseRequestParameters(String requestLine) {
        String requestTarget;
        if(requestLine.split(" ").length > 1) {
            requestTarget = requestLine.split(" ")[1];
        } else {requestTarget = "/";}
        Map<String, String> requestParameters = new HashMap<>();
        int questionPos = requestTarget.indexOf('?');
        if (questionPos != -1){
            String query = requestTarget.substring(questionPos + 1);
            for (String parameter : query.split("&")) {

                int equalsPos = parameter.indexOf('=');
                String parameterName = parameter.substring(0, equalsPos);
                String parameterValue = parameter.substring(equalsPos + 1);

                requestParameters.put(parameterName, parameterValue);

            }

        }
        return requestParameters;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

}
