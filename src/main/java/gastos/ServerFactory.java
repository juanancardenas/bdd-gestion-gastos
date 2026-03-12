package gastos;

import gastos.service.GastoService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import gastos.web.RegistrarGastosServlet;

public class ServerFactory {
    private ServerFactory() {
    }

    public static Server create(int port) {
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Servicio
        GastoService gastoService = new GastoService();

        // Servlet
        RegistrarGastosServlet registrarGastosServlet = new RegistrarGastosServlet(gastoService);

        context.addServlet(new ServletHolder(registrarGastosServlet), "/gastos");

        server.setHandler(context);

        return server;
    }
}
