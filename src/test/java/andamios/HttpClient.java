package andamios;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClient {
    private final String base;
    private final java.net.http.HttpClient client;

    public HttpClient() {
        ServerSupport.startIfNeeded();
        this.base = ServerSupport.baseUrl();
        CookieManager cm = new CookieManager();
        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = java.net.http.HttpClient.newBuilder().cookieHandler(cm).followRedirects(java.net.http.HttpClient.Redirect.NEVER).build();
    }

    public HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(base + path)).GET().build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> postForm(String path, String form) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(base + path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }
}
