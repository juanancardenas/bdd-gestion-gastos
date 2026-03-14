package gastos.web;

import gastos.domain.TipoIVA;
import gastos.service.TipoIVAService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TiposIVAServlet extends HttpServlet {

    private final TipoIVAService tipoIVAService;

    public TiposIVAServlet(TipoIVAService tipoIVAService) {
        this.tipoIVAService = tipoIVAService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        List<TipoIVA> tipos = tipoIVAService.obtenerTiposIVADisponibles();

        String body = tipos.stream()
                .map(tipo -> tipo.getPorcentaje() + "|" + tipo.getDescripcion())
                .collect(Collectors.joining("\n"));

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(body);
    }
}
