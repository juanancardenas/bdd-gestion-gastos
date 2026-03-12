package gastos;

import org.eclipse.jetty.server.Server;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        Server server = ServerFactory.create(port);
        server.start();
        System.out.println("Server started on http://localhost:" + port);
        server.join();
    }
}
