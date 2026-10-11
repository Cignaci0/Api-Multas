package ComunasRegiones;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.w3c.dom.Text;

import java.util.List;

@ApplicationScoped
public class ComunasRegionesRepository implements PanacheRepositoryBase<Region, Integer> {
    public List<Region> obtenerRegionesConCOmunasYZonas(){
        return list("SELECT DISTINCT r FROM Region r " + "LEFT JOIN FETCH r.comunas c " + "LEFT JOIN FETCH c.zonas");
    }
}
