package bdd.StepDefinitions;

import gastos.service.GastoService;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculoGastoSteps {

    private double base;
    private int iva;
    private double total;

    private final GastoService service = new GastoService();

    @Given("el personal introduce un importe base de {int} euros")
    public void base(int base) {
        this.base = base;
    }

    @And("selecciona un IVA del {int} por ciento")
    public void iva(int iva) {
        this.iva = iva;
    }

    @Then("el sistema calcula un total de {int} euros")
    public void total(int esperado) {
        total = service.calcularTotal(base, iva);
        assertEquals(esperado, (int) total);
    }
}