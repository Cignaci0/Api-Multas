package Inspector;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class InspectorZonasRepository implements PanacheRepositoryBase<InspectorZonas, Integer> {

    public List<InspectorZonas> findByInspector(Integer inspectorId) {
        return find("inspector.id", inspectorId).list();
    }
}
