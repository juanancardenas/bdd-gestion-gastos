package tdd.flow;

import andamios.HttpClient;
import andamios.ServerSupport;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrarGastosFlowIT {

    static HttpClient http;

    @BeforeAll
    static void setup() {
        ServerSupport.startIfNeeded();
        http = new HttpClient();
    }

    @AfterAll
    static void tearDown() {
        ServerSupport.stopIfRunning();
    }

    @Test
    @DisplayName("POST /gastos registra un gasto válido")
    void deberiaRegistrarUnGastoValidoPorHttp() throws Exception {
        String form = "encargoId=ENC-001" +
                "&concepto=Tasas+judiciales" +
                "&fecha=2026-03-09" +
                "&importeBase=100.0" +
                "&iva=21";

        var res = http.postForm("/gastos", form);

        assertEquals(200, res.statusCode());
        assertTrue(res.body().contains("Gasto registrado"));
    }

    @Test
    @DisplayName("POST /gastos devuelve error si el concepto está vacío")
    void deberiaDevolverErrorSiElConceptoEsVacio() throws Exception {
        String form = "encargoId=ENC-001" +
                "&concepto=" +
                "&fecha=2026-03-09" +
                "&importeBase=100.0" +
                "&iva=21";

        var res = http.postForm("/gastos", form);

        assertEquals(400, res.statusCode());
        assertTrue(res.body().contains("El concepto es obligatorio"));
    }

    @Test
    @DisplayName("POST /gastos devuelve error si la fecha falta")
    void deberiaDevolverErrorSiLaFechaFalta() throws Exception {
        String form = "encargoId=ENC-001" +
                "&concepto=Tasas+judiciales" +
                "&fecha=" +
                "&importeBase=100.0" +
                "&iva=21";

        var res = http.postForm("/gastos", form);

        assertEquals(400, res.statusCode());
        assertTrue(res.body().contains("La fecha es obligatoria"));
    }

    @Test
    @DisplayName("POST /gastos devuelve error si el importe es 0")
    void deberiaDevolverErrorSiElImporteEsCero() throws Exception {
        String form = "encargoId=ENC-001" +
                "&concepto=Tasas+judiciales" +
                "&fecha=2026-03-09" +
                "&importeBase=0" +
                "&iva=21";

        var res = http.postForm("/gastos", form);

        assertEquals(400, res.statusCode());
        assertTrue(res.body().contains("El importe base debe ser mayor que 0"));
    }

    @Test
    @DisplayName("POST /gastos devuelve error si el importe no es numérico")
    void deberiaDevolverErrorSiElImporteNoEsNumerico() throws Exception {
        String form = "encargoId=ENC-001" +
                "&concepto=Tasas+judiciales" +
                "&fecha=2026-03-09" +
                "&importeBase=abc" +
                "&iva=21";

        var res = http.postForm("/gastos", form);

        assertEquals(400, res.statusCode());
        assertTrue(res.body().contains("El importe base debe ser numérico"));
    }
}