package Multa;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MultaRepository implements PanacheRepositoryBase<Multa, Integer> {
}
