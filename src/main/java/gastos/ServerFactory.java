package gastos;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import gastos.service.GastoService;
import gastos.service.TipoIVAService;

import gastos.web.RegistrarGastosServlet;
import gastos.web.TiposIVAServlet;


public class ServerFactory {
    private ServerFactory() {
    }

    public static Server create(int port) {
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Servicios
        GastoService gastoService = new GastoService();
        TipoIVAService tipoIVAService = new TipoIVAService();

        // Servlets
        RegistrarGastosServlet registrarGastosServlet = new RegistrarGastosServlet(gastoService);
        TiposIVAServlet tiposIVAServlet = new TiposIVAServlet(tipoIVAService);

        context.addServlet(new ServletHolder(registrarGastosServlet), "/gastos");
        context.addServlet(new ServletHolder(tiposIVAServlet), "/tipos-iva");

        server.setHandler(context);

        return server;
    }
}
