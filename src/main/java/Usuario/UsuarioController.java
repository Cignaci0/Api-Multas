package Usuario;


import Inspector.DTO.CrearInspectorDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {

    @Inject
    UsuarioServices usuarioServices;


    @POST
    @RolesAllowed({"SUPERADMIN"})
    public Response crearUsuario(Usuario usuarioBody){
        Map<String, String> respuesta = usuarioServices.crearUsuario(usuarioBody);
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }

    @POST
    @Path("/loginWeb")
    public Response loginPaginaWeb(Usuario loginBody){
        Map<String, String> respuesta = usuarioServices.loginPaginaWeb(loginBody.getUsername(), loginBody.getPassword());
        return Response.status(Response.Status.OK).entity(respuesta).build();
    }

    @POST
    @Path("/loginMovile")
    public Response loginMovile(Usuario loginBody){
        Map<String, String> respuesta = usuarioServices.loginMovile(loginBody.getUsername(), loginBody.getPassword());
        return Response.status(Response.Status.OK).entity(respuesta).build();
    }

    @POST
    @Path("/inspector")
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    public Response crearInspector(CrearInspectorDto inspectorBody){
        Map<String, String> respuesta = usuarioServices.crearInspector(inspectorBody);
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }
}
