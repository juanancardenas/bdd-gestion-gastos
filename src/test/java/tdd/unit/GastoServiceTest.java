package tdd.unit;

import org.junit.jupiter.api.Test;

import gastos.domain.Encargo;
import gastos.domain.RegistroGastoResultado;
import gastos.service.GastoService;

import static org.junit.jupiter.api.Assertions.*;

class GastoServiceTest {

    private final GastoService gastoService = new GastoService();

    @Test
    void deberia_registrar_un_gasto_valido() {
        Encargo encargo = new Encargo("ENC-001", true);

        RegistroGastoResultado resultado = gastoService.registrarGasto(
                encargo,
                "Tasas judiciales",
                "2026-03-09",
                100.0,
                21
        );

        assertTrue(resultado.isExitoso());
        assertNotNull(resultado.getGasto());
        assertNull(resultado.getMensajeError());

        assertEquals("ENC-001", resultado.getGasto().getEncargoId());
        assertEquals("Tasas judiciales", resultado.getGasto().getConcepto());
        assertEquals("2026-03-09", resultado.getGasto().getFecha());
        assertEquals(100.0, resultado.getGasto().getImporteBase());
        assertEquals(21, resultado.getGasto().getIva());
        assertEquals(121.0, resultado.getGasto().getTotal());
    }

    @Test
    void no_deberia_registrar_un_gasto_sin_encargo() {
        RegistroGastoResultado resultado = gastoService.registrarGasto(
                null,
                "Tasas judiciales",
                "2026-03-13",
                100.0,
                21
        );

        assertFalse(resultado.isExitoso());
        assertNull(resultado.getGasto());
        assertEquals("Debe existir un encargo activo", resultado.getMensajeError());
    }

    @Test
    void no_deberia_registrar_un_gasto_sin_concepto() {
        Encargo encargo = new Encargo("ENC-001", true);

        RegistroGastoResultado resultado = gastoService.registrarGasto(
                encargo,
                null,
                "2026-03-12",
                100.0,
                21
        );

        assertFalse(resultado.isExitoso());
        assertNull(resultado.getGasto());
        assertEquals("El concepto es obligatorio", resultado.getMensajeError());
    }

    @Test
    void no_deberia_registrar_un_gasto_sin_fecha() {
        Encargo encargo = new Encargo("ENC-001", true);

        RegistroGastoResultado resultado = gastoService.registrarGasto(
                encargo,
                "Tasas judiciales",
                null,
                100.0,
                21
        );

        assertFalse(resultado.isExitoso());
        assertNull(resultado.getGasto());
        assertEquals("La fecha es obligatoria", resultado.getMensajeError());
    }

    @Test
    void no_deberia_registrar_un_gasto_con_importe_base_cero() {
        Encargo encargo = new Encargo("ENC-001", true);

        RegistroGastoResultado resultado = gastoService.registrarGasto(
                encargo,
                "Tasas judiciales",
                "2026-03-12",
                0,
                21
        );

        assertFalse(resultado.isExitoso());
        assertNull(resultado.getGasto());
        assertEquals("El importe base debe ser mayor que 0", resultado.getMensajeError());
    }

    @Test
    void deberia_calcular_total_con_iva_10() {
        GastoService gastoService = new GastoService();
        double gasto = gastoService.calcularTotal(100.0,10);
        assertEquals(110.0, gasto);
    }

    @Test
    void deberia_calcular_total_con_iva_0() {
        GastoService gastoService = new GastoService();
        double gasto = gastoService.calcularTotal(100.0,0);
        assertEquals(100.0, gasto);
    }
}
