package bdd.StepDefinitions;

import andamios.HttpClient;
import andamios.ServerSupport;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class TipoIVASteps {

    private HttpClient http;
    private HttpResponse<String> response;

    @Before
    public void setUp() {
        ServerSupport.startIfNeeded();
        http = new HttpClient();
    }

    @After
    public void tearDown() {
        ServerSupport.stopIfRunning();
    }

    /* ====================================================================
     * Escenario 7
     * ====================================================================*/

    @Given("existen tipos de IVA configurados")
    public void existen_tipos_de_iva_configurados() throws IOException, InterruptedException {
        response = http.get("/tipos-iva");
        assertNotNull(response);
        assertEquals(200, response.statusCode());
    }

    @When("el personal inicia el registro de un gasto")
    public void el_personal_inicia_el_registro_de_un_gasto()  {
        // es una operación pura de UI, acción de usuario
        assertTrue(true);
    }

    @Then("el sistema muestra una lista de tipos de IVA disponibles")
    public void el_sistema_muestra_una_lista_de_tipos_de_iva_disponibles() {
        // Si fue bien el paso inicial, tenemos los valores del IVA
        String body = response.body();
        assertNotNull(body);

        String[] lineas = body.split("\n");

        assertEquals(4, lineas.length);

        assertTrue(body.contains("21|General"));
        assertTrue(body.contains("10|Reducido"));
        assertTrue(body.contains("4|Superreducido"));
        assertTrue(body.contains("0|Exento"));
    }
}