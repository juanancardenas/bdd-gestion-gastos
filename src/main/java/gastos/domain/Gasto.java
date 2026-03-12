package gastos.domain;

public class Gasto {

    private final String concepto;
    private final String fecha;
    private final double importeBase;
    private final int iva;
    private final String encargoId;
    private double total;

    public Gasto(String concepto, String fecha, double importeBase, int iva, String encargoId) {
        this.concepto = concepto;
        this.fecha = fecha;
        this.importeBase = importeBase;
        this.iva = iva;
        this.encargoId = encargoId;
    }

    public void calcularTotal() {
        this.total = importeBase + (importeBase * iva / 100.0);
    }

    public String getConcepto() {
        return concepto;
    }

    public String getFecha() {
        return fecha;
    }

    public double getImporteBase() {
        return importeBase;
    }

    public int getIva() {
        return iva;
    }

    public String getEncargoId() {
        return encargoId;
    }

    public double getTotal() {
        return total;
    }
}
