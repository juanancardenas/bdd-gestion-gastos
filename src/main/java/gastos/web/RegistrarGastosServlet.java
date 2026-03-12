package gastos.web;

import gastos.domain.Encargo;
import gastos.domain.RegistroGastoResultado;
import gastos.service.GastoService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RegistrarGastosServlet extends HttpServlet {

    private GastoService gastoService;

    public RegistrarGastosServlet(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String encargoId = request.getParameter("encargoId");

        if (encargoId == null || encargoId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("El encargo es obligatorio");
            return;
        }

        var gastos = gastoService.obtenerGastosPorEncargo(encargoId);

        response.setStatus(HttpServletResponse.SC_OK);
        if (gastos.isEmpty()) {
            response.getWriter().write("[]");
        } else {
            String body = gastos.stream()
                    .map(g -> g.getEncargoId() + "|" + g.getConcepto())
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("");
            response.getWriter().write(body);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String encargoId = request.getParameter("encargoId");
        String concepto = request.getParameter("concepto");
        String fecha = request.getParameter("fecha");
        String importeBaseParam = request.getParameter("importeBase");
        String ivaParam = request.getParameter("iva");

        double importeBase;
        int iva;

        // Control de importes
        try {
            importeBase = Double.parseDouble(importeBaseParam);
            iva = Integer.parseInt(ivaParam);
        } catch (NumberFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("El importe base debe ser numérico");
            return;
        }

        // Control para que encargo no sea ""
        if (encargoId == null || encargoId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Debe existir un encargo activo");
            return;
        }

        // Creo el encargo (valores mínimos, ya que estoy testeando Gastos, no Encargos)
        Encargo encargo = new Encargo(encargoId, true);

        // Registramos el Gasto
        RegistroGastoResultado resultado = gastoService.registrarGasto(
                encargo,
                concepto,
                fecha,
                importeBase,
                iva
        );

        // Control general de la respuesta del servicio
        if (resultado.isExitoso()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Gasto registrado");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(resultado.getMensajeError());
        }
    }
}
