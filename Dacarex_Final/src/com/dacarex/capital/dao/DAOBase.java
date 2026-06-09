package com.dacarex.capital.dao;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

// "abstract" significa que esta clase no se puede usar por sí sola con un "new".
// Sirve como una plantilla o "madre" para que otras clases (como CategoriaDAO) hereden de ella.
// El "<T>" es un "Genérico". Significa que "T" se adaptará al tipo de objeto que uses (Usuario, Categoria, Movimiento, etc.).
public abstract class DAOBase<T> {

    // Método interno para obtener el gestor de la base de datos (EntityManager).
    // Al usar "protected", solo esta clase y sus hijas (las que heredan de ella) pueden usarlo.
    protected EntityManager getEm() {
        return ConexionDB.getInstance().getEm();
    }

    // Guarda un objeto nuevo en la base de datos
    public void guardar(T entidad) {
        EntityManager em = getEm();
        // Las transacciones son obligatorias en JPA para modificar datos. 
        // Imagínalo como abrir una caja fuerte, meter el dato y cerrarla.
        em.getTransaction().begin(); // Abre la transacción
        em.persist(entidad);         // Prepara el objeto para guardarlo
        em.getTransaction().commit(); // Confirma los cambios y los graba permanentemente
    }

    // Actualiza los datos de un objeto que ya existía en la base de datos
    public void actualizar(T entidad) {
        EntityManager em = getEm();
        em.getTransaction().begin(); // Abre la transacción
        em.merge(entidad);           // Busca el objeto existente y actualiza sus campos
        em.getTransaction().commit(); // Confirma los cambios
    }

    // Elimina un registro de la base de datos usando su ID y el tipo de clase
    public void eliminar(Object id, Class<T> clase) {
        EntityManager em = getEm();
        // Primero, busca si el objeto realmente existe en la base de datos
        T entidad = em.find(clase, id);
        
        // Si lo encuentra (no es nulo), procede a borrarlo
        if (entidad != null) {
            em.getTransaction().begin(); // Abre transacción
            em.remove(entidad);          // Ordena eliminar el objeto encontrado
            em.getTransaction().commit(); // Confirma el borrado
        }
    }

    // Busca un solo registro por su ID de forma segura
    public Optional<T> buscarPorId(Object id, Class<T> clase) {
        // "Optional.ofNullable" evita que el programa se rompa si no encuentra nada.
        // Si el registro existe, te lo devuelve dentro del Optional; si no existe, devuelve un Optional vacío.
        return Optional.ofNullable(getEm().find(clase, id));
    }

    // Al ser un método "abstract", no tiene código aquí dentro (no tiene llaves {}).
    // Obliga a todas las clases hijas a escribir su propia versión de cómo buscar todos los registros,
    // ya que cada tabla se consulta con un nombre diferente (ej: "SELECT c FROM Categoria c").
    public abstract List<T> buscarTodos();
}