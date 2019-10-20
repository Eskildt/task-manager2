package no.projectMembers.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {

    String URLECHO = "urlecho.appspot.com";

    @Test
    void shouldExecuteHttpRequest() throws IOException {
        HttpClient client = new HttpClient(URLECHO, 80, "/echo");
        assertEquals(200, client.execute().getStatusCode());
    }

    @Test
    void shouldReadHttpRequest() throws IOException {
        HttpClient client = new HttpClient(URLECHO, 80, "/echo?status=401");
        assertEquals(401, client.execute().getStatusCode());
    }

    @Test
    void shouldReadHttpHeaders() throws IOException {

        HttpClient client = new HttpClient(URLECHO, 80, "/echo?content-type=text/html");
        assertEquals("text/html; charset=utf-8", client.execute().getHeader("Content-type"));
    }

    @Test
    void shouldReadContentLength() throws IOException {

        HttpClient client = new HttpClient(URLECHO, 80, "/echo?body=hello+world!");
        assertEquals(12, client.execute().getContentLength());
    }

    @Test
    void shouldReadBody() throws IOException {

        HttpClient client = new HttpClient(URLECHO, 80, "/echo?body=hello+world!");
        assertEquals("hello world!", client.execute().getBody());
    }

}
