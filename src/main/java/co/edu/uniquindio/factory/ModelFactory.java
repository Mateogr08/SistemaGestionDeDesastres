package co.edu.uniquindio.factory;

import co.edu.uniquindio.model.*;
/**
 * Fábrica central que proporciona instancias únicas de los gestores del sistema.
 * Implementa el patrón Singleton para asegurar una única instancia compartida.
 */
public class ModelFactory {

    private static ModelFactory instance;

    private final GestorUsuarios gestorUsuarios;
    private final GestorRecursos gestorRecursos;
    private final GestorEvacuacion gestorEvacuacion;
    private final GrafoRutas grafoRutas;

    /**
     * Constructor privado que inicializa los gestores del sistema.
     * Solo se invoca una vez como parte del patrón Singleton.
     */
    private ModelFactory() {
        this.gestorUsuarios = new GestorUsuarios();
        this.gestorRecursos = new GestorRecursos();
        this.gestorEvacuacion = new GestorEvacuacion();
        this.grafoRutas = new GrafoRutas();
    }

    /**
     * Devuelve la instancia única de ModelFactory.
     * Si no existe, la crea.
     *
     * @return instancia única de ModelFactory
     */
    public static ModelFactory getInstance() {
        if (instance == null) {
            instance = new ModelFactory();
        }
        return instance;
    }

    /**
     * Devuelve el gestor de usuarios.
     *
     * @return instancia de GestorUsuarios
     */
    public GestorUsuarios getGestorUsuarios() {
        return gestorUsuarios;
    }

    /**
     * Devuelve el gestor de recursos.
     *
     * @return instancia de GestorRecursos
     */
    public GestorRecursos getGestorRecursos() {
        return gestorRecursos;
    }

    /**
     * Devuelve el gestor de evacuación.
     *
     * @return instancia de GestorEvacuacion
     */
    public GestorEvacuacion getGestorEvacuacion() {
        return gestorEvacuacion;
    }

    /**
     * Devuelve el grafo de rutas.
     *
     * @return instancia de GrafoRutas
     */
    public GrafoRutas getGrafoRutas() {
        return grafoRutas;
    }
}