package Perfil;


import Perfil.DTO.ObtenerPerfilesDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/perfil")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PerfilController {

    @Inject
    PerfilServices perfilServices;

    @GET
    @RolesAllowed({"SUPERADMIN", "ADMIN", "USUARIO"})
    public Response obtenerPerfiles(){
        List<ObtenerPerfilesDto> perfiles = perfilServices.obtenerPerfiles();
        return Response.ok(perfiles).build();
    }
}
