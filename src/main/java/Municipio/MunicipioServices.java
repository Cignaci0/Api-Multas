package Municipio;


import Municipio.DTO.ObtenerMunicipiosDto;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MunicipioServices {

    @Inject
    MunicipioRepository municipioRepository;

    public List<ObtenerMunicipiosDto> municipios(String comuna, String nombre, int pagina, int tamanio){
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (comuna != null && !comuna.isBlank()) {
            query.append(" and comuna = :comuna");
            params.put("comuna", comuna);
        }

        if (nombre != null && !nombre.isBlank()) {
            query.append(" and nombre like :nombre");
            params.put("nombre", "%" + nombre + "%");
        }

        List<Municipio> municipios = this.municipioRepository.find(query.toString(), params).page(Page.of(pagina,tamanio)).list();

        return municipios.stream().map(m -> {
            ObtenerMunicipiosDto dto = new ObtenerMunicipiosDto();
            dto.setId(m.getId());
            dto.setNombre(m.getNombre());
            dto.setComuna(m.getComuna());
            return dto;
        }).toList();
    }

    @Transactional
    public Map<String, String> crearMuni (Municipio municipio){
        this.municipioRepository.persist(municipio);
        return Map.of("message", "Municipio creado con exito");
    }
}
