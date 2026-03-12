package gastos.service;

import gastos.domain.TipoIVA;

import java.util.List;

public class TipoIVAService {

    public List<TipoIVA> obtenerTiposIVADisponibles() {
        return List.of(
                new TipoIVA(21, "General"),
                new TipoIVA(10, "Reducido"),
                new TipoIVA(4, "Superreducido"),
                new TipoIVA(0, "Exento")
        );
    }

    public boolean esTipoIVAValido(int porcentaje) {
        return obtenerTiposIVADisponibles()
                .stream()
                .anyMatch(tipo -> tipo.getPorcentaje() == porcentaje);
    }
}