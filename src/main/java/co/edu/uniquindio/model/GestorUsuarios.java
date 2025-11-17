package co.edu.uniquindio.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorUsuarios {

    private final List<Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
    }

    public boolean registrarUsuario(Usuario usuario) {
        if (buscarPorNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            return false;
        }
        usuarios.add(usuario);
        return true;
    }

    //Autentica un usuario por nombre y contraseña
    public Usuario autenticar(String nombreUsuario, String contrasena) {
        return usuarios.stream()
                .filter(u -> u.getNombreUsuario().equals(nombreUsuario)
                        && u.getContrasena().equals(contrasena))
                .findFirst()
                .orElse(null);
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios); // se devuelve copia para evitar modificaciones externas
    }

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarios.stream()
                .filter(u -> u.getNombreUsuario().equalsIgnoreCase(nombreUsuario))
                .findFirst();
    }

    public boolean eliminarUsuario(String nombreUsuario) {
        return usuarios.removeIf(u -> u.getNombreUsuario().equalsIgnoreCase(nombreUsuario));
    }

    public List<Usuario> getNombreUsuarios() {
        return new ArrayList<>(usuarios);
    }
}