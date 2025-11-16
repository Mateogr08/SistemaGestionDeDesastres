package co.edu.uniquindio.model;

import java.util.Objects;

public class Recurso {

    private String nombre;
    private TipoRecurso tipo;
    private int cantidadDisponible;

    public Recurso(String nombre, TipoRecurso tipo, int cantidadDisponible) {
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre del recurso no puede estar vacío");
        }

        if(tipo == null){
            throw new IllegalArgumentException("El tipo de recurso no puede ser nulo");
        }

        if(cantidadDisponible < 0){
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre del recurso no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public TipoRecurso getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecurso tipo) {
        if(tipo == null){
            throw new IllegalArgumentException("El tipo de recurso no puede ser nulo");
        }
        this.tipo = tipo;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        if(cantidadDisponible < 0){
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.cantidadDisponible = cantidadDisponible;
    }

    /**
    *Incrementa la cantiad del recurso
    *
    * Valida que la cantidad que se desea incrementar sea mayor que cero; de no ser así
    * lanza una excepción para evitar modificaciones inválidas
    *
    * @param cantidad es la cantidad que se desea sumar al inventario.
    * @throws illegalArgumentException Si la cantidad es menor o igual a cero.
    **/
    public void incrementarCantidad(int cantidad){
        if (cantidad <= 0){
            throw new IllegalArgumentException("La cantidad a incrementar debe ser mayor que 0");
        }
        this.cantidadDisponible += cantidad;
    }

    /**
     * Disminuye la cantidad disponible de recursos
     *
     * Verifica primero que la cantidad solicitada sea mayor que cero. Luego comprueba que
     * las unidades disponibles sean suficientes. Si alguna de las validaciones falla, muestra un mensaje y retorna false.
     * Si la operación es válida, se descuenta la cantidad y retorna true.
     *
     * @param cantidad La cantidad qur se desea restar del inventario
     * @return true si la cantidad se pudo disminuir correctamente;
     *          false si la cantidad es inválida o insuficiente
     */
    public boolean disminuirCantidad(int cantidad){
        if(cantidad <= 0){
            System.out.println("La cantidad debe ser mayor a 0");
            return false;
        }
        if(cantidad > cantidadDisponible){
            System.out.println("No hay unidades suficientes disponibles");
            return false;
        }

        cantidadDisponible -= cantidad;
        return true;
    }

    /**
     * Verifica si el recurso está agotado
     *
     * Un recurso se considera agotado cuando la cantidad disponibles es menor
     * o igual a cero.
     * @return true si ya  no hay unidades disponibles, false si es el caso contrario.
     */
    public boolean estaAgotado(){
        return cantidadDisponible <= 0;
    }

    /**
     * Genera un resumen en formato de texto del recurso
     *
     * Este resument incluye el nombre del recurso, su tipo y la cantidad
     * disponible
     *
     * @return Una cadena con la iformación del recurso en el formato: "nombre (tipo): canttidad"
     */
    public String resumen(){
        return String.format("%s (%s): %d unidades", nombre, tipo, cantidadDisponible);
    }

    /**
     * Compara el recurso con otro objeto para determinar si son iguales
     * Dos objetos (Recursos) se considera iguales cuando:
     * - Son la misma instancia
     * -Ambos son de tipo Recurso y tienen el mismo nombre y el mismo tipo
     *
     * Se sobreescribe el metodo equals heredado desde Object, permite comparar recursos por
     * el contenido lógico y no solo por la referencia en memoria
     *
     * @param o   El objeto con el que se compara el recurso
     * @return     true si ambos recursos son equivalentes, false si no lo son
     */
    @Override
    public boolean equals (Object o){
        if (this == o) return true;
        if(!(o instanceof Recurso)) return false;
        Recurso recurso = (Recurso) o;
        return Objects.equals(nombre, recurso.nombre) && tipo == recurso.tipo;
    }

    /**
     * Genera un código hash para el recurso
     *
     * Se asegura la coherencia con el metodo equals(), utilizando los mismos atributos
     * (nombre y tipo) para calcular el hash.
     *
     * @return Valor entero que representa el código hash del recurso
     */
    @Override
    public int hashCode(){
        return Objects.hash(nombre, tipo);
    }

    @Override
    public String toString(){
        return String.format("Recurso{nombre='%s', tipo='%s', disponible='%s'", nombre, tipo, cantidadDisponible);
    }
}
