package Reportes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReportesRepository {

    @PersistenceContext
    EntityManager em;

    @Inject
    JsonWebToken jwt;

    public List<ReporteMultaDto> getMultasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Object rawComuna = jwt.getClaim("comuna");
        String comuna = rawComuna != null ? rawComuna.toString() : null;

        Object rawId = jwt.getClaim("id_perfil");
        Integer perfilId = rawId != null ? Integer.valueOf(rawId.toString()) : null;

        String jpql = """
            SELECT new Reportes.ReporteMultaDto(
                m.fecha_creacion, m.patente,
                tm.nombre, m.direccion,
                CONCAT(i.nombre, ' ', i.apellido), m.cortesia
            )
            FROM Multa m
            JOIN m.tipoMulta tm
            JOIN m.usuario u
            LEFT JOIN Inspector i ON i.usuario.id = u.id
            WHERE m.estado = true
            AND m.fecha_creacion BETWEEN :fechaInicio AND :fechaFin
        """;

        if (perfilId != null && perfilId != 2) {
            jpql += " AND m.comuna = :comuna";
        }

        jpql += " ORDER BY m.fecha_creacion DESC";

        TypedQuery<ReporteMultaDto> query = em.createQuery(jpql, ReporteMultaDto.class);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);

        if (perfilId != null && perfilId != 2) {
            query.setParameter("comuna", comuna);
        }

        return query.getResultList();
    }

    public List<ReporteMultaDto> getMultasPorInspector(Integer inspectorId) {
        Object rawComuna = jwt.getClaim("comuna");
        String comuna = rawComuna != null ? rawComuna.toString() : null;

        Object rawId = jwt.getClaim("id_perfil");
        Integer perfilId = rawId != null ? Integer.valueOf(rawId.toString()) : null;

        String jpql = """
            SELECT new Reportes.ReporteMultaDto(
                m.fecha_creacion, m.patente,
                tm.nombre, m.direccion,
                CONCAT(i.nombre, ' ', i.apellido), m.cortesia
            )
            FROM Multa m
            JOIN m.tipoMulta tm
            JOIN m.usuario u
            JOIN Inspector i ON i.usuario.id = u.id
            WHERE m.estado = true
            AND i.id = :inspectorId
        """;

        if (perfilId != null && perfilId != 2) {
            jpql += " AND m.comuna = :comuna";
        }

        jpql += " ORDER BY m.fecha_creacion DESC";

        TypedQuery<ReporteMultaDto> query = em.createQuery(jpql, ReporteMultaDto.class);
        query.setParameter("inspectorId", inspectorId);

        if (perfilId != null && perfilId != 2) {
            query.setParameter("comuna", comuna);
        }

        return query.getResultList();
    }

    public List<ReporteMultaDto> getMultasPorPatente(String patente) {
        Object rawComuna = jwt.getClaim("comuna");
        String comuna = rawComuna != null ? rawComuna.toString() : null;

        Object rawId = jwt.getClaim("id_perfil");
        Integer perfilId = rawId != null ? Integer.valueOf(rawId.toString()) : null;

        String jpql = """
            SELECT new Reportes.ReporteMultaDto(
                m.fecha_creacion, m.patente,
                tm.nombre, m.direccion,
                CONCAT(i.nombre, ' ', i.apellido), m.cortesia
            )
            FROM Multa m
            JOIN m.tipoMulta tm
            JOIN m.usuario u
            LEFT JOIN Inspector i ON i.usuario.id = u.id
            WHERE m.estado = true
            AND m.patente = :patente
        """;

        if (perfilId != null && perfilId != 2) {
            jpql += " AND m.comuna = :comuna";
        }

        jpql += " ORDER BY m.fecha_creacion DESC";

        TypedQuery<ReporteMultaDto> query = em.createQuery(jpql, ReporteMultaDto.class);
        query.setParameter("patente", patente);

        if (perfilId != null && perfilId != 2) {
            query.setParameter("comuna", comuna);
        }

        return query.getResultList();
    }

    public String getNombreInspector(Integer inspectorId) {
        String jpql = "SELECT CONCAT(i.nombre, ' ', i.apellido) FROM Inspector i WHERE i.id = :inspectorId";
        TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter("inspectorId", inspectorId);
        return query.getSingleResult();
    }
}
