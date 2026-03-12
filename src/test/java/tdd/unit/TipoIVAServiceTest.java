package tdd.unit;

import gastos.domain.TipoIVA;
import gastos.service.TipoIVAService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TipoIVAServiceTest {

    private final TipoIVAService service = new TipoIVAService();

    @Test
    void deberia_devolver_los_tipos_de_iva_configurados() {
        List<TipoIVA> tipos = service.obtenerTiposIVADisponibles();

        assertNotNull(tipos);
        assertFalse(tipos.isEmpty());
        assertEquals(4, tipos.size());

        assertEquals(21, tipos.get(0).getPorcentaje());
        assertEquals("General", tipos.get(0).getDescripcion());
    }

    @Test
    void deberia_indicar_que_21_es_un_tipo_de_iva_valido() {
        assertTrue(service.esTipoIVAValido(21));
    }

    @Test
    void deberia_indicar_que_10_es_un_tipo_de_iva_valido() {
        assertTrue(service.esTipoIVAValido(10));
    }

    @Test
    void deberia_indicar_que_7_no_es_un_tipo_de_iva_valido() {
        assertFalse(service.esTipoIVAValido(7));
    }
}