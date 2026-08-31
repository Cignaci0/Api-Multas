package Inspector;


import Inspector.DTO.ObtenerInspectorDto;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class InspectorServices {

    @Inject
    InspectorRepository inspectorRepository;

    public List<ObtenerInspectorDto> obtenerInspectores(int pagina, int tamanio){
        List<Inspector> inspectores = this.inspectorRepository.findAll().page(Page.of(pagina,tamanio)).list();
            return inspectores.stream().map(i -> {
                ObtenerInspectorDto obIns = new ObtenerInspectorDto();
                obIns.setId(i.getId());
                obIns.setNombre(i.getNombre());
                obIns.setApeliido(i.getApellido());
                obIns.setIdMunicipio(i.getMunicipio().getId());
                obIns.setNombreMunicipio(i.getMunicipio().getNombre());
                obIns.setIdUsuario(i.getUsuario().getId());
                obIns.setUsernameUsuario(i.getUsuario().getUsername());
                return obIns;
            }).toList();
    }
}
