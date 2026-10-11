package ComunasRegiones;


import ComunasRegiones.DTO.ObtenerRegiones;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/regiones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComunasRegionesController {

    @Inject
    ComunasRegionesServices comunasRegionesServices;

    @GET
    @RolesAllowed({"SUPERADMIN", "ADMIN", "USUARIO"})
    public Response obtenerRegiones(){
        List<ObtenerRegiones> respuesta = comunasRegionesServices.ObetenerRegiones();
        return Response.ok(respuesta).build();
    }

}
