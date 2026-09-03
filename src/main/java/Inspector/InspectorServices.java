package Inspector;


import Inspector.DTO.EditInspectorDto;
import Inspector.DTO.ObtenerInspectorDto;
import Usuario.Usuario;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import Usuario.UsuarioRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class InspectorServices {

    @Inject
    InspectorRepository inspectorRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    InspectorMapper inspectorMapper;

    @Inject
    UsuarioRepository usuarioRepository;

    public List<ObtenerInspectorDto> obtenerInspectores(int pagina, int tamanio){

        Object rawComuna = jwt.getClaim("comuna");
        String comuna = rawComuna != null ? rawComuna.toString() : null;

        Object rawId = jwt.getClaim("id_perfil");
        Integer perfilId = rawId != null ? Integer.valueOf(rawId.toString()) : null;

        List<Inspector> inspectores;

        if (perfilId != 2) {
            inspectores =  this.inspectorRepository.find("comuna = ?1", comuna).page(Page.of(pagina,tamanio)).list();
        } else {
            inspectores =  this.inspectorRepository.findAll().page(Page.of(pagina,tamanio)).list();
        }
            return inspectores.stream().map(i -> {
                ObtenerInspectorDto obIns = new ObtenerInspectorDto();
                obIns.setId(i.getId());
                obIns.setNombre(i.getNombre());
                obIns.setApeliido(i.getApellido());
                obIns.setComuna(i.getComuna());
                obIns.setEmail(i.getEmail());
                obIns.setIdUsuario(i.getUsuario().getId());
                obIns.setUsernameUsuario(i.getUsuario().getUsername());
                return obIns;
            }).toList();
    }

    @Transactional
    public Map<String, String> editIsnpector(Integer idInspector, EditInspectorDto editIns){
        Inspector inspector = this.inspectorRepository.findByIdOptional(idInspector).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Inpector no encontrado")).build()));
        if(inspector.getUsuario() != null){
            inspector.getUsuario().setEmail(editIns.getEmail());
            inspector.getUsuario().setComuna(editIns.getComuna());
            this.usuarioRepository.persist(inspector.getUsuario());
        }
        inspectorMapper.editInspector(editIns, inspector);
        this.inspectorRepository.persist(inspector);
        return Map.of("message", "Inspector actualizado con éxito");
    }
}
