package Zonas;

import ComunasRegiones.Comuna;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class Zonasrepository implements PanacheRepositoryBase<Zonas, Integer> {

    public List<Zonas> buscarPorNombreComuna(String nombreComuna) {
        return find("SELECT z FROM Zonas z JOIN z.comuna c WHERE c.nombre = ?1", nombreComuna).list();
    }
}
