package ComunasRegiones;

import ComunasRegiones.DTO.ObtenerComunas;
import ComunasRegiones.DTO.ObtenerRegiones;
import ComunasRegiones.DTO.ObtenerZonas;
import Modulo.DTO.ObtenerModulosPorPerfilDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ComunasRegionesServices {

    @Inject
    ComunasRegionesRepository comunasRegionesRepository;


    public List<ObtenerRegiones> ObetenerRegiones(){
        List<Region> regiones = this.comunasRegionesRepository.obtenerRegionesConCOmunasYZonas();
        return regiones.stream().map(region -> {
            ObtenerRegiones dto = new ObtenerRegiones();
            dto.setId(region.getId());
            dto.setRegion(region.getNombre());

            //Mapear Comunas
            List<ObtenerComunas> comunasDto = region.getComunas().stream().map(comuna -> {
                ObtenerComunas comunaDto = new ObtenerComunas();
                comunaDto.setId(comuna.getId());
                comunaDto.setComuna(comuna.getNombre());

                //Maper Zonas
                List<ObtenerZonas> zonasDto = comuna.getZonas().stream().map(zona -> {
                    ObtenerZonas zonaDto = new ObtenerZonas();
                    zonaDto.setId(zona.getId());
                    zonaDto.setNombre(zona.getNombre());
                    return zonaDto;
                }).collect(Collectors.toList());

                comunaDto.setZonas(zonasDto);
                return comunaDto;
            }).collect(Collectors.toList());
            dto.setComunas(comunasDto);
            return dto;
        }).collect(Collectors.toList());
    }
}
