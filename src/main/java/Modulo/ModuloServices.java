package Modulo;

import Modulo.DTO.ObtenerHijosMenuDto;
import Modulo.DTO.ObtenerModulosPorPerfilDto;
import Modulo.DTO.ObtenerTodosLosModulosDto;
import Perfil.Perfil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import    Perfil.PerfilRepository;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class ModuloServices {

    @Inject
    ModuloRepository moduloRepository;

    @Inject
    PerfilRepository perfilRepository;

    @Inject
    JsonWebToken jwt;

    @Transactional
    public Map<String,String> asignarPerfilModulo(List<Integer> idsModulos, Integer idPerfil ){
        Perfil perfil = this.perfilRepository.findByIdOptional(idPerfil).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Perfil no encontrado")).build()));
        perfil.getModulos().clear();
        for (Integer idModulo : idsModulos){
            Modulo modulo = this.moduloRepository.findByIdOptional(idModulo).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Modulo no encontrado")).build()));
            perfil.getModulos().add(modulo);
        }
        return Map.of("message", "Modulos asignados con exito");
    }

    public List<ObtenerModulosPorPerfilDto> obtenerModulos(){
        Object rawId = jwt.getClaim("id_perfil");
        Integer id_perfil = rawId != null ? Integer.valueOf(rawId.toString()) : null;
        Perfil perfil = this.perfilRepository.findById(id_perfil);
        Set<Modulo> modulosDelPerfil = perfil.getModulos();
        Set<Integer> idsPadres = modulosDelPerfil.stream().filter(m -> m.getModulo_padre() != null).map(Modulo::getModulo_padre).collect(Collectors.toSet());
        List<Modulo> padres = this.moduloRepository.find("id in ?1", idsPadres).list();
        return padres.stream().map(padre -> {
            List<ObtenerHijosMenuDto> hijos = modulosDelPerfil.stream().filter(m -> padre.getId().equals(m.getModulo_padre())).map(m -> new ObtenerHijosMenuDto(m.getId(), m.getNombre())).toList();
            ObtenerModulosPorPerfilDto obtenerModulosPorPerfilDto =  new ObtenerModulosPorPerfilDto();
            obtenerModulosPorPerfilDto.setPadre(padre.getNombre());
            obtenerModulosPorPerfilDto.setHijos(hijos);
            return obtenerModulosPorPerfilDto;
        }).toList();
    }

    public List<ObtenerTodosLosModulosDto> obtenerModulosYaAsignados(Integer idPerfil){
        Perfil perfil = this.perfilRepository.findById(idPerfil);
        Set<Modulo> modulosDelPerfil = perfil.getModulos();
        return modulosDelPerfil.stream().map(m -> {
            ObtenerTodosLosModulosDto obMudulo = new ObtenerTodosLosModulosDto();
            obMudulo.setId(m.getId());
            obMudulo.setNombre(m.getNombre());
            return obMudulo;
        }).toList();
    }

    public List<ObtenerTodosLosModulosDto> obtenerTodosLosModulos () {
        List<Modulo> modulos = this.moduloRepository.listAll();
        return modulos.stream().filter(m -> m.getModulo_padre() != null).map(m -> {
            ObtenerTodosLosModulosDto obModulos = new ObtenerTodosLosModulosDto();
            obModulos.setId(m.getId());
            obModulos.setNombre(m.getNombre());
            return obModulos;
        }).toList();
    }

}
