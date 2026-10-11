package Zonas;

import ComunasRegiones.DTO.ObtenerZonas;
import Zonas.DTO.EditarZonaDto;
import Zonas.DTO.ObtenerZonasDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ZonasServices {

    @Inject
    Zonasrepository zonasrepository;

    @Inject
    JsonWebToken jwt;

    public List<ObtenerZonasDto> obtenerZonas(){
        Object rawComuna = jwt.getClaim("comuna");
        String comuna = rawComuna != null ? rawComuna.toString() : null;

        Object rawId = jwt.getClaim("id_perfil");
        Integer perfilId = rawId != null ? Integer.valueOf(rawId.toString()) : null;

        List<Zonas> zonas;

        if(perfilId != 2){

            zonas = this.zonasrepository.buscarPorNombreComuna(comuna);
        }else {
            zonas = this.zonasrepository.findAll().list();
        }

        return zonas.stream().map(zona -> {
            ObtenerZonasDto dto = new ObtenerZonasDto();
            dto.setId(zona.getId());
            dto.setNombreZona(zona.getNombre());
            dto.setIdComuna(zona.getComuna().getId());
            dto.setNombreComuna(zona.getComuna().getNombre());
            return dto;
        }).toList();
    }

    @Transactional
    public Map<String, String> editarZona(Integer id,  EditarZonaDto editZona){
        Zonas zona = this.zonasrepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Zona no encontrada")).build()));
        zona.setNombre(editZona.getNombre());
        return Map.of("message", "Zona editada con exito");
    }

    @Transactional
    public Map<String, String> crearZona(Zonas nuevaZona){
        if(nuevaZona.getComuna().getId() == null){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "No existe la comuna")).build());
        }
        this.zonasrepository.persist(nuevaZona);
        return Map.of("message", "zona creada correctamente");
    }

    @Transactional
    public Map<String, String> borrarZona(Integer id){
        Zonas zona = this.zonasrepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Zona no encontrada")).build()));
        this.zonasrepository.delete(zona);
        return Map.of("message", "zona borrada correctamente");
    }
}
