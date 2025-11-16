package co.edu.uniquindio.model;

public enum TipoRecurso {

    ALIMENTOS("Alimentos y víveres"),
    MEDICINAS("Medicinas y suministros médicos"),
    EQUIPO_RESCATE("Equipos de rescate y protección"),
    AGUA("Agua potable y líquidos"),
    REFUGIO("Materiales para refugio y abrigo"),
    OTRO("Otros recursos");

    private final String descripcion;

    TipoRecurso(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }

    @Override
    public String toString(){
        return descripcion;
    }
}
