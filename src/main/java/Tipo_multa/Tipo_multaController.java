package Tipo_multa;

import Tipo_multa.DTO.ObtenerMenuTipoMultasDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;


@Path("/tipoMulta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Tipo_multaController {

    @Inject
    Tipo_multaServices tipoMultaServices;

    @POST
    @RolesAllowed({"SUPERADMIN"})
    public Response crearTipoMulta(Tipo_multa tipoMulta){
        Map<String, String> respuesta = tipoMultaServices.crearTipoMulta(tipoMulta);
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }

    @GET
    @Path("/menu")
    @RolesAllowed({"SUPERADMIN", "ADMIN", "EMPLEADO"})
    public Response obtenerMenu() {
        List<ObtenerMenuTipoMultasDto> respuesta = tipoMultaServices.traerMenu();
        return Response.ok(respuesta).build();
    }

}
