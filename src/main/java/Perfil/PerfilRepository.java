package Perfil;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PerfilRepository implements PanacheRepositoryBase<Perfil, Integer> {
}
