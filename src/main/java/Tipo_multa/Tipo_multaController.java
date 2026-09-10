package Tipo_multa;

import Tipo_multa.DTO.ObtenerTipoMultas;
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

    @GET
    public Response traerTiposMultas(
        @QueryParam("pagina") @DefaultValue("0") int pagina,
        @QueryParam("tamanio") @DefaultValue("10") int tamanio,
        @QueryParam("query")@DefaultValue("") String query){

        List<ObtenerTipoMultas> listaTipoMultas = tipoMultaServices.obtenerTipoMulta(query,pagina,tamanio);
        return Response.ok(listaTipoMultas).build();
    }

}
