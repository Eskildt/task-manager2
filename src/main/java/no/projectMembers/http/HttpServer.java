package no.projectMembers.http;

import taskManager.MemberDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private int port;
    private ServerSocket serverSocket;
    private String fileLocation;

    public HttpServer(int port) throws IOException {

        this.port = port;
        serverSocket = new ServerSocket(port);

    }


    public static void main(String[] args) throws IOException {

        HttpServer httpServer = new HttpServer(8080);

        httpServer.setFileLocation("src/main/resources");
        httpServer.start();

    }

    public void start() {
        new Thread(this::run).start();
    }

    public void run() {

        boolean keepRunning = true;

        while(keepRunning) {

            try {
                Socket socket = serverSocket.accept();

                HttpServerRequest request = new HttpServerRequest(socket.getInputStream());
                String requestLine = request.getStartLine();

                System.out.println(requestLine);
                String requestTarget;
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

}

