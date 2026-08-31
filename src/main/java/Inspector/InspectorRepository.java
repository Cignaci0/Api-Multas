package Inspector;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InspectorRepository implements PanacheRepositoryBase<Inspector, Integer> {
}
