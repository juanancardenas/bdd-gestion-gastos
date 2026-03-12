package andamios;

import gastos.ServerFactory;
import org.eclipse.jetty.server.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSupport {
    private static Server server;
    private static int port;

    public static synchronized void startIfNeeded() {
        if (server != null && server.isStarted()) return;
        try {
            port = findFreePort();
            server = ServerFactory.create(port);
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void stopIfRunning() {
        if (server != null) {
            try { server.stop(); } catch (Exception ignored) {}
            server = null;
        }
    }

    public static String baseUrl() {
        startIfNeeded();
        return "http://localhost:" + port;
    }

    private static int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (IOException e) {
            return 8082; // fallback distinto de TDD
        }
    }
}
