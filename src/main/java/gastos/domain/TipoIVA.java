package gastos.domain;

public class TipoIVA {

    private final int porcentaje;
    private final String descripcion;

    public TipoIVA(int porcentaje, String descripcion) {
        this.porcentaje = porcentaje;
        this.descripcion = descripcion;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public String getDescripcion() {
        return descripcion;
    }
}