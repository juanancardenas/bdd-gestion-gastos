package tdd.unit;

import gastos.domain.Encargo;
import gastos.domain.Gasto;
import gastos.domain.RegistroGastoResultado;
import gastos.service.GastoService;
import gastos.web.RegistrarGastosServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RegistrarGastosServletTest {

    @Test
    void deberia_registrar_un_gasto_valido_y_responder_ok() throws Exception {
        GastoService gastoService = new GastoService();
        RegistrarGastosServlet servlet = new RegistrarGastosServlet(gastoService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter body = new StringWriter();
        PrintWriter writer = new PrintWriter(body);

        when(request.getParameter("encargoId")).thenReturn("ENC-001");
        when(request.getParameter("concepto")).thenReturn("Tasas judiciales");
        when(request.getParameter("fecha")).thenReturn("2026-03-09");
        when(request.getParameter("importeBase")).thenReturn("100.0");
        when(request.getParameter("iva")).thenReturn("21");
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        writer.flush();
        assertTrue(body.toString().contains("Gasto registrado"));
    }

    @Test
    void deberia_responder_bad_request_si_el_servicio_devuelve_error() throws Exception {
        GastoService gastoService = new GastoService() {
            @Override
            public RegistroGastoResultado registrarGasto(
                    Encargo encargo,
                    String concepto,
                    String fecha,
                    double importeBase,
                    int iva
            ) {
                return RegistroGastoResultado.error("El concepto es obligatorio");
            }
        };

        RegistrarGastosServlet servlet = new RegistrarGastosServlet(gastoService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter body = new StringWriter();
        PrintWriter writer = new PrintWriter(body);

        when(request.getParameter("encargoId")).thenReturn("ENC-001");
        when(request.getParameter("concepto")).thenReturn("");
        when(request.getParameter("fecha")).thenReturn("2026-03-09");
        when(request.getParameter("importeBase")).thenReturn("100.0");
        when(request.getParameter("iva")).thenReturn("21");
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writer.flush();
        assertTrue(body.toString().contains("El concepto es obligatorio"));
    }
/*
    void deberia_responder_bad_request_si_el_servicio_devuelve_error() throws Exception {
        GastoService gastoService = mock(GastoService.class);
        RegistrarGastosServlet servlet = new RegistrarGastosServlet(gastoService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter body = new StringWriter();
        PrintWriter writer = new PrintWriter(body);

        when(request.getParameter("encargoId")).thenReturn("ENC-001");
        when(request.getParameter("concepto")).thenReturn("");
        when(request.getParameter("fecha")).thenReturn("2026-03-09");
        when(request.getParameter("importeBase")).thenReturn("100.0");
        when(request.getParameter("iva")).thenReturn("21");
        when(response.getWriter()).thenReturn(writer);

        when(gastoService.registrarGasto(
                any(Encargo.class),
                eq(""),
                eq("2026-03-09"),
                eq(100.0),
                eq(21)
        )).thenReturn(RegistroGastoResultado.error("El concepto es obligatorio"));

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writer.flush();
        assertTrue(body.toString().contains("El concepto es obligatorio"));
    }
*/

    @Test
    void deberia_devolver_bad_request_si_importe_base_no_es_numerico() throws Exception {
        GastoService gastoService = new GastoService();
        RegistrarGastosServlet servlet = new RegistrarGastosServlet(gastoService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter body = new StringWriter();
        PrintWriter writer = new PrintWriter(body);

        when(request.getParameter("encargoId")).thenReturn("ENC-001");
        when(request.getParameter("concepto")).thenReturn("Tasas judiciales");
        when(request.getParameter("fecha")).thenReturn("2026-03-09");
        when(request.getParameter("importeBase")).thenReturn("abc");
        when(request.getParameter("iva")).thenReturn("21");
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writer.flush();
        assertTrue(body.toString().contains("El importe base debe ser numérico"));
    }

/*
    void deberia_devolver_bad_request_si_importe_base_no_es_numerico() throws Exception {
        GastoService gastoService = mock(GastoService.class);
        RegistrarGastosServlet servlet = new RegistrarGastosServlet(gastoService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter body = new StringWriter();
        PrintWriter writer = new PrintWriter(body);

        when(request.getParameter("encargoId")).thenReturn("ENC-001");
        when(request.getParameter("concepto")).thenReturn("Tasas judiciales");
        when(request.getParameter("fecha")).thenReturn("2026-03-09");
        when(request.getParameter("importeBase")).thenReturn("abc");
        when(request.getParameter("iva")).thenReturn("21");
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        writer.flush();
        assertTrue(body.toString().contains("El importe base debe ser numérico"));

        verifyNoInteractions(gastoService);
    }*/
}
