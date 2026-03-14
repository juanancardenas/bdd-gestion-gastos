package bdd.StepDefinitions;

import gastos.domain.TipoIVA;
import gastos.service.TipoIVAService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TipoIVASteps {

    private final TipoIVAService tipoIVAService = new TipoIVAService();
    private List<TipoIVA> tiposIVA;

    /* ====================================================================
     * Escenario 7
     * ====================================================================*/

    @Given("existen tipos de IVA configurados")
    public void existen_tipos_de_iva_configurados() {
        tiposIVA = tipoIVAService.obtenerTiposIVADisponibles();
        assertNotNull(tiposIVA);
        assertFalse(tiposIVA.isEmpty());
    }

    @When("el personal inicia el registro de un gasto")
    public void el_personal_inicia_el_registro_de_un_gasto() {
        tiposIVA = tipoIVAService.obtenerTiposIVADisponibles();
    }

    @Then("el sistema muestra una lista de tipos de IVA disponibles")
    public void el_sistema_muestra_una_lista_de_tipos_de_iva_disponibles() {
        assertNotNull(tiposIVA);
        assertFalse(tiposIVA.isEmpty());
        assertTrue(tiposIVA.stream().anyMatch(t -> t.getPorcentaje() == 21));
        assertTrue(tiposIVA.stream().anyMatch(t -> t.getPorcentaje() == 10));
    }
}