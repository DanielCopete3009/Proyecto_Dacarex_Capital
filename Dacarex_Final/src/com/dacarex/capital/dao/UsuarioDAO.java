package com.dacarex.capital.dao;

import com.dacarex.capital.modelo.Usuario;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

// Esta clase hereda de DAOBase, especializándose en la clase "Usuario".
// Al hacer esto, hereda automáticamente funciones como guardar(), actualizar() o eliminar() sin tener que reescribirlas.
public class UsuarioDAO extends DAOBase<Usuario> {

    // Este método es obligatorio porque en DAOBase se declaró como "abstract".
    // Se encarga de traer absolutamente todos los usuarios registrados, ordenados alfabéticamente por su nombre.
    @Override
    public List<Usuario> buscarTodos() {
        // Creamos una consulta de tipo TypedQuery (consulta tipada), asegurándole a Java que el resultado serán objetos "Usuario".
        TypedQuery<Usuario> q = getEm().createQuery(
            "SELECT u FROM Usuario u ORDER BY u.nombre",
            Usuario.class
        );
        // Ejecutamos la consulta y devolvemos la lista con los resultados obtenidos.
        return q.getResultList();
    }

    // Busca a un usuario específico utilizando su dirección de correo electrónico (clave para el Login).
    public Optional<Usuario> buscarPorEmail(String email) {
        // Preparamos la consulta filtrando por el campo email empleando un parámetro dinámico (:email)
        TypedQuery<Usuario> q = getEm().createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :email",
            Usuario.class
        );
        // Inyectamos de forma segura el email que recibimos por parámetro para sustituir el ":email" de la consulta
        q.setParameter("email", email);
        
        // Guardamos el resultado en una lista temporal
        List<Usuario> resultado = q.getResultList();
        
        // El "Optional" es un contenedor seguro: 
        // Si la lista está vacía, devolvemos un contenedor vacío (Optional.empty()).
        // Si tiene datos, sacamos el primer usuario de la lista (posición 0) y lo envolvemos en el contenedor (Optional.of(...)).
        return resultado.isEmpty() ? Optional.empty() : Optional.of(resultado.get(0));
    }

    // Método de seguridad para el primer arranque del programa.
    // Evita que la base de datos se quede sin usuarios administradores bloqueando el acceso al Login.
    public void crearUsuarioDemoSiVacio() {
        // Llama al método buscarTodos() que programamos arriba para comprobar si la base de datos no tiene cuentas creadas.
        if (buscarTodos().isEmpty()) {
            // Si está completamente vacía, crea un usuario con credenciales por defecto:
            // Nombre: "Demo", Email: "demo@dacarex.com", Contraseña: "demo1234"
            // y lo guarda usando el método guardar() que heredó de la clase madre DAOBase.
            guardar(new Usuario("Demo", "demo@dacarex.com", "demo1234"));
            System.out.println("Usuario demo creado.");
        }
    }
}