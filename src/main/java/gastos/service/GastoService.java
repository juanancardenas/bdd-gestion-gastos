package gastos.service;

import gastos.domain.Encargo;
import gastos.domain.Gasto;
import gastos.domain.RegistroGastoResultado;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GastoService {

    private List<Gasto> gastos = new ArrayList<>();

    private TipoIVAService tipoIVAService;

    public GastoService() {
        this(new TipoIVAService());
    }

    public GastoService(TipoIVAService tipoIVAService) {
        this.tipoIVAService = tipoIVAService;
    }

    public RegistroGastoResultado registrarGasto(
            Encargo encargo,
            String concepto,
            String fecha,
            double importeBase,
            int iva
    ) {
        if (encargo == null || encargo.getId() == null || encargo.getId().trim().isEmpty()) {
            return RegistroGastoResultado.error("Debe existir un encargo activo");
        }

        if (concepto == null || concepto.trim().isEmpty()) {
            return RegistroGastoResultado.error("El concepto es obligatorio");
        }

        if (fecha == null || fecha.trim().isEmpty()) {
            return RegistroGastoResultado.error("La fecha es obligatoria");
        }

        if (importeBase <= 0) {
            return RegistroGastoResultado.error("El importe base debe ser mayor que 0");
        }

        if (!tipoIVAService.esTipoIVAValido(iva)) {
            return RegistroGastoResultado.error("El tipo de IVA no es válido");
        }

        Gasto gasto = new Gasto(
                concepto,
                fecha,
                importeBase,
                iva,
                encargo.getId()
        );

        gasto.calcularTotal();

        gastos.add(gasto);

        return RegistroGastoResultado.ok(gasto);
    }

    public double calcularTotal(double importeBase, int iva) {
        return importeBase + (importeBase * iva / 100.0);
    }

    public List<Gasto> obtenerGastosPorEncargo(String encargoId) {
        return gastos.stream()
                .filter(g -> g.getEncargoId().equals(encargoId))
                .collect(Collectors.toList());
    }
}
