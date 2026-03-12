package gastos.domain;

public class Encargo {
    private final String id;
    private final boolean activo;

    public Encargo(String id, boolean activo) {
        this.id = id;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public boolean isActivo() {
        return activo;
    }
}
