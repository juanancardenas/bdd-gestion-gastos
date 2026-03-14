package bdd.StepDefinitions;

import andamios.HttpClient;
import andamios.ServerSupport;

import gastos.service.GastoService;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class GastoSteps {

    private HttpClient http;
    private HttpResponse<String> response;

    private String encargoId;
    private String concepto;
    private String fecha;
    private double importeBase;
    private int iva;
    private double total;

    @Before
    public void setUp() {
        ServerSupport.startIfNeeded();
        http = new HttpClient();

        encargoId = "ENC-001";
        concepto = "Tasas judiciales";
        fecha = "2026-03-09";
        importeBase = 100.0;
        iva = 21;
    }

    @After
    public void tearDown() {
        ServerSupport.stopIfRunning();
    }

    /* ====================================================================
     * Escenario 1
     * ====================================================================*/

    @Given("existe un encargo activo")
    public void existe_un_encargo_activo() {
        // No quiero probar nada de Encargos en BDD/TDD de Gastos, por lo
        // que usamos un ID de encargo fijo que permita probar Gastos.
        encargoId = "ENC-001";
        assertNotNull(encargoId);
    }

    @When("el personal registra un gasto completo")
    public void el_personal_registra_un_gasto_completo() throws Exception {
        response = http.postForm("/gastos", buildForm());
        assertNotNull(response);
    }

    @Then("el gasto queda registrado")
    public void el_gasto_queda_registrado() {
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Gasto registrado"));
    }

    @And("el gasto queda asociado al encargo")
    public void el_gasto_queda_asociado_al_encargo() throws Exception {

        HttpResponse<String> res = http.get("/gastos?encargoId=" + encargoId);

        assertEquals(200, res.statusCode());
        assertTrue(res.body().contains(encargoId));
    }

    /* ====================================================================
     * Escenario 2
     * ====================================================================*/

    @Given("el personal introduce un importe base de {double} euros")
    public void base(double base) {
        this.importeBase = base;
        assertTrue(this.importeBase > 0, "Importe Base debe ser mayor que 0");
    }

    @And("selecciona un IVA del {int} por ciento")
    public void iva(int iva) {
        // En el escenario 7: ver TipoIVASteps se hará más elegantemente
        this.iva = iva;
        assertTrue(iva == 21 || iva == 10 || iva == 4 || iva == 0,"IVA no válido: " + iva);
    }

    @Then("el sistema calcula un total de {int} euros")
    public void total(double esperado) {
        // Es lógica de servicio, no lo responde el Servlet
        GastoService service = new GastoService();
        total = service.calcularTotal(importeBase, iva);
        assertEquals(esperado, total);
    }

    /* ====================================================================
     * Escenario 3
     * ====================================================================*/

    @When("el personal registra un gasto con importe base {int}")
    public void el_personal_registra_un_gasto_con_importe_base(double importe) throws Exception {
        importeBase = importe;
        response = http.postForm("/gastos", buildForm());
    }

    // Este paso también es parte de escenarios 4, 5 y 6
    @Then("el sistema muestra un mensaje de error")
    public void el_sistema_muestra_un_mensaje_de_error() {
        assertNotNull(response);
        assertEquals(400, response.statusCode());
        assertNotNull(response.body());
        assertFalse(response.body().isBlank());
    }

    // Este paso también es parte de escenarios 4, 5 y 6
    @And("el gasto no se guarda")
    public void el_gasto_no_se_guarda() {
        assertNotNull(response);
        assertEquals(400, response.statusCode());
    }

    /* ====================================================================
     * Escenario 4
     * ====================================================================*/

    @When("el personal intenta registrar un gasto sin encargo")
    public void el_personal_intenta_registrar_un_gasto_sin_encargo() throws Exception {
        encargoId = "";
        response = http.postForm("/gastos", buildForm());
    }


    /* ====================================================================
     * Escenario 5
     * ====================================================================*/

    @When("el personal registra un gasto sin fecha")
    public void el_personal_registra_un_gasto_sin_fecha() throws Exception {
        fecha = "";
        response = http.postForm("/gastos", buildForm());
    }

    /* ====================================================================
     * Escenario 6
     * ====================================================================*/
    @When("el personal registra un gasto sin concepto")
    public void el_personal_registra_un_gasto_sin_concepto() throws Exception {
        concepto = "";
        response = http.postForm("/gastos", buildForm());
    }

    /* ====================================================================
     * Steps específicos de mensaje
     * ====================================================================*/

    @And("el mensaje indica que el importe base debe ser mayor que 0")
    public void el_mensaje_indica_que_el_importe_base_debe_ser_mayor_que_0() {
        assertEquals("El importe base debe ser mayor que 0", response.body());
    }

    @And("el mensaje indica que debe existir un encargo activo")
    public void el_mensaje_indica_que_debe_existir_un_encargo_activo() {
        assertEquals("Debe existir un encargo activo", response.body());
    }

    @And("el mensaje indica que la fecha es obligatoria")
    public void el_mensaje_indica_que_la_fecha_es_obligatoria() {
        assertEquals("La fecha es obligatoria", response.body());
    }

    @Then("el mensaje indica que el concepto es obligatorio")
    public void el_mensaje_indica_que_el_concepto_es_obligatorio() {
        assertNotNull(response);
        assertEquals(400, response.statusCode());
        assertEquals("El concepto es obligatorio", response.body());
    }

    /* ====================================================================
     * Métodos privados y auxiliares, no implementan funcionalidad o steps
     * ====================================================================*/

    private String buildForm() {
        return "encargoId=" + encode(encargoId)
                + "&concepto=" + encode(concepto)
                + "&fecha=" + encode(fecha)
                + "&importeBase=" + encode(String.valueOf(importeBase))
                + "&iva=" + encode(String.valueOf(iva));
    }

    private String encode(String value) {
        return value == null ? "" : value.replace(" ", "+");
    }
}