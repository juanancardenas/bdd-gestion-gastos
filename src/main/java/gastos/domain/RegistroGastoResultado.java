package gastos.domain;

public class RegistroGastoResultado {
    private final boolean exitoso;
    private final Gasto gasto;
    private final String mensajeError;

    private RegistroGastoResultado(boolean exitoso, Gasto gasto, String mensajeError) {
        this.exitoso = exitoso;
        this.gasto = gasto;
        this.mensajeError = mensajeError;
    }

    public static RegistroGastoResultado ok(Gasto gasto) {
        return new RegistroGastoResultado(true, gasto, null);
    }

    public static RegistroGastoResultado error(String mensajeError) {
        return new RegistroGastoResultado(false, null, mensajeError);
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public Gasto getGasto() {
        return gasto;
    }

    public String getMensajeError() {
        return mensajeError;
    }
}
