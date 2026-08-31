package Modulo;

import Modulo.DTO.AsignarModulosDto;
import Modulo.DTO.ObtenerModulosPorPerfilDto;
import Modulo.DTO.ObtenerTodosLosModulosDto;
import Usuario.Usuario;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/modulo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModuloController {

    @Inject
    ModuloServices moduloServices;

    @POST
    @Path("/asignar/{idPerfil}")
    @RolesAllowed({"SUPERADMIN"})
    public Response asignarModulo(AsignarModulosDto idsModulos, @PathParam("idPerfil") Integer idPerfil){
        Map<String, String> respuesta = moduloServices.asignarPerfilModulo(idsModulos.idsModulos, idPerfil);
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }

    @GET
    @Path("/menu")
    @RolesAllowed({"SUPERADMIN", "ADMIN", "EMPLEADO"})
    public Response menu(){
        List<ObtenerModulosPorPerfilDto> listaModulos = moduloServices.obtenerModulos();
        return Response.ok(listaModulos).build();
    }

    @GET
    @RolesAllowed({"SUPERADMIN"})
    public Response todosLosModulos(){
        List<ObtenerTodosLosModulosDto> modulos = moduloServices.obtenerTodosLosModulos();
        return Response.ok(modulos).build();
    }

    @GET
    @Path("/asignados/{idPerfil}")
    @RolesAllowed({"SUPERADMIN"})
    public Response modulosYaAsignados(@PathParam("idPerfil") Integer idPerfil){
        List<ObtenerTodosLosModulosDto> modulos = moduloServices.obtenerModulosYaAsignados(idPerfil);
        return Response.ok(modulos).build();
    }





}
