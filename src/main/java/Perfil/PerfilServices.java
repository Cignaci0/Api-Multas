package Perfil;


import Modulo.DTO.ObtenerModulosPorPerfilDto;
import Perfil.DTO.ObtenerPerfilesDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PerfilServices {

    @Inject
    PerfilRepository perfilRepository;

    public List<ObtenerPerfilesDto> obtenerPerfiles(){
        List<Perfil> perfiles = this.perfilRepository.listAll();

        return perfiles.stream().map(p -> {
            ObtenerPerfilesDto obtenerPerfilesDto = new ObtenerPerfilesDto();
            obtenerPerfilesDto.setId(p.getId());
            obtenerPerfilesDto.setNombre(p.getNombre());
            return obtenerPerfilesDto;
        }).toList();
    }
}
