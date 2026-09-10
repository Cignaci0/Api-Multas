package Tipo_multa;

import Tipo_multa.DTO.ObtenerTipoMultas;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Tipo_multaServices {

    @Inject
    Tipo_multaRepository tipoMultaRepository;

    public List<ObtenerTipoMultas> obtenerTipoMulta(String query, int pagina, int tamanio ){
        return this.tipoMultaRepository.find("Lower(nombre) like ?1", "%" + query.toLowerCase() + "%").stream().map(t -> {
            ObtenerTipoMultas obt = new ObtenerTipoMultas();
            obt.setId(t.getId());
            obt.setNombre(t.getNombre());
            return  obt;
        }).toList();
    }

}
