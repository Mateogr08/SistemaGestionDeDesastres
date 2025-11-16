package co.edu.uniquindio.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ubicacion {

    private String nombre;
    private String tipo; //Ciudad, Refugio, Centro de Ayuda
    private int personasAfectadas;
    private int nivelUrgencia;
    private final Map<Recurso, Integer> recursos;

    public Ubicacion(String nombre, String tipo, int personasAfectadas, int nivelUrgencia, Map<Recurso, Integer> recursos) {
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre de la ubicación no puede estar vacía");
        }
        if(tipo == null || tipo.isBlank()){
            throw new IllegalArgumentException("El tipo de ubicación no puede estar vacío");
        }
        if(personasAfectadas < 0){
            throw new IllegalArgumentException("El número de personas no puede ser negativo");
        }
        this.nombre = nombre;
        this.tipo = tipo;
        this.personasAfectadas = personasAfectadas;
        this.nivelUrgencia = validarNivelUrgencia(nivelUrgencia);
        this.recursos = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre de la ubicación no puede estar vacía");
        }
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if(tipo == null || tipo.isBlank()){
            throw new IllegalArgumentException("El tipo de ubicación no puede estar vacío");
        }
        this.tipo = tipo;
    }

    public int getPersonasAfectadas() {
        return personasAfectadas;
    }

    public void setPersonasAfectadas(int personasAfectadas) {
        if(personasAfectadas < 0){
            throw new IllegalArgumentException("El número de personas no puede ser negativo");
        }
        this.personasAfectadas = personasAfectadas;
    }

    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(int nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public Map<Recurso, Integer> getRecursos() {
        return recursos;
    }

    /**
     * Agrega un cantidad específica de un recurso al inventario
     *
     * Se valida que el recurso no sea nulo y que la cantidad a agregar sea mayor
     * a 0. Si el recurso ya existe en el mapa, se suma esa cantidad a la ya existente usando el
     * metodo merge(); de lo contrario, se inserta con la cantidad inicial
     *
     * @param recurso Recurso que se dese agregar o actualizar en el inventario
     * @param cantidad La cantidad a añadir al recurso
     */
    public void agregarRecurso(Recurso recurso, int cantidad){
        if(recurso == null)
            throw new IllegalArgumentException("El recurso no puede ser nulo");
        if(cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        recursos.merge(recurso, cantidad, Integer::sum);
    }

    /**
     * Consume una cantidad del recurso en el inventario
     *
     * Si el recurso es nulo, la cantidad no es válida o el recurso mo existe
     * en el inventario, el metodo no realiza ninguna operación
     *
     * Se calcula la cantidad restante despues del consumo:
     *  - Si la cantidad restante es menor o igual a 0, el recurso se elimina
     *  - Si aun queda inventario, se actualiza la nueva cantidad
     *
     * @param recurso Recurso que se desea consumir
     * @param cantidad Cantidad a descontar
     */
    public void consumirRecurso(Recurso recurso,  int cantidad){
        if(recurso == null || cantidad <= 0 || !recursos.containsKey(recurso)) return;

        int restante = recursos.get(recurso) - cantidad;
        if (restante <= 0){
            recursos.remove(recurso);
        } else {
            recursos.put(recurso, restante);
        }
    }

    /**
     * Determina si la zona es considerada crítica según el nivel de urgencia
     *
     * La zona es clasificada como crítica cuando el nivel de urgencia es mayor o igual a 7.
     *
     * @return true si la zona es crtica, false si no lo es
     */
    public boolean esZonaCritica(){
        return nivelUrgencia >= 7;
    }

    public String resumen(){
        return String.format("%s (%s) | Afectados: %d | Urgencia: %d | Recursos: %d",
                nombre, tipo, personasAfectadas, nivelUrgencia, recursos.size());
    }

    /**
     * Valida y ajusta el nivel de urgencia dentro del rango permitido
     *
     * Este nivel debe estar entre 1 y 10. Si el valor que se recibe es menor a 1, retorna 1.
     * Si es mayor a 10, retorna 10. Y si es un número detro del rango, retorna el valor.
     *
     * @param nivel Nivel de urgencia que se desea validar
     * @return Valor entre 1 y 10 que representa el nivel de urgencia válido.
     */
    public int validarNivelUrgencia(int nivel){
        return Math.max(1, Math.min(nivel, 10));
    }

    /**
     * Compara la ubicación con otro objeto para saber si son iguales
     *
     * Dos objetos Ubicacion son iguales:
     *  - Son la misma instancia
     *  - El objeto recibido tambien es una Ubicación y tienen el mismo nombre
     *
     *  Esta implementación sobreescribe el metodo equals heredado de object, spermite comparar las ubicaciones por
     *  su nombre y no por su referencia
     *
     * @param o   the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Ubicacion)) return false;
        Ubicacion that = (Ubicacion) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode(){
        return Objects.hash(nombre);
    }

    @Override
    public String toString(){
        return String.format("Ubicacion{nombre='%s', tipo='%s', personas=%d, urgencia=%d, recursos=%d}",
                nombre, tipo, personasAfectadas, nivelUrgencia, recursos.size());
    }


}
