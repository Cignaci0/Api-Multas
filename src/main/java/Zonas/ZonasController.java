package Zonas;

import Usuario.Usuario;
import Zonas.DTO.EditarZonaDto;
import Zonas.DTO.ObtenerZonasDto;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/zonas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ZonasController {

    @Inject
    ZonasServices zonasServices;

    @GET
    @RolesAllowed({"SUPERADMIN", "ADMIN", "USUARIO"})
    public Response obtenerZonas(){
        List<ObtenerZonasDto> respuesta = zonasServices.obtenerZonas();
        return Response.ok(respuesta).build();
    }

    @PATCH
    @Path("/{idZona}")
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    public Response editarZona(@PathParam("idZona") Integer idZona, EditarZonaDto editZona){
        Map<String, String> respuesta = zonasServices.editarZona(idZona, editZona);
        return Response.status(Response.Status.OK).entity(respuesta).build();
    }

    @POST
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    public Response crearZona(Zonas zona){
        Map<String, String> respuesta = zonasServices.crearZona(zona);
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }

    @DELETE
    @Path("/{idZona}")
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    public Response borrarZona(@PathParam("idZona") Integer idZona){
        Map<String, String> respuesta = zonasServices.borrarZona(idZona);
        return Response.status(Response.Status.OK).entity(respuesta).build();
    }

}
