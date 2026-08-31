package Tipo_multa;

import Tipo_multa.DTO.ObtenerHijosMenuDto;
import Tipo_multa.DTO.ObtenerMenuTipoMultasDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Tipo_multaServices {

    @Inject
    Tipo_multaRepository tipoMultaRepository;

    @Transactional
    public Map<String, String> crearTipoMulta(Tipo_multa tipoMulta){
       if(tipoMulta.getModulo_padre() != null){
           Tipo_multa tipoMultaExiste = this.tipoMultaRepository.findByIdOptional(tipoMulta.getModulo_padre()).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No existe el tipo de multa con id: " +  tipoMulta.getModulo_padre())).build()));
           this.tipoMultaRepository.persist(tipoMultaExiste);
       }
       this.tipoMultaRepository.persist(tipoMulta);
       return Map.of("message", "Tipo de multa creada con exito");
    }

    public List<ObtenerMenuTipoMultasDto> traerMenu() {
        List<Tipo_multa> todasLasMultas = tipoMultaRepository.listAll();

        List<Tipo_multa> padres = todasLasMultas.stream()
                .filter(t -> t.getModulo_padre() == null)
                .toList();

        return padres.stream().map(padre -> {
            List<ObtenerHijosMenuDto> hijos = todasLasMultas.stream().filter(t -> padre.getId().equals(t.getModulo_padre())).map(t -> new ObtenerHijosMenuDto(t.getId(), t.getNombre())).toList();
            ObtenerMenuTipoMultasDto obtenerMenuTipoMultasDto = new ObtenerMenuTipoMultasDto();
            obtenerMenuTipoMultasDto.setPadre(padre.getNombre());
            obtenerMenuTipoMultasDto.setHijos(hijos);
            return obtenerMenuTipoMultasDto;
        }).toList();
    }

}
