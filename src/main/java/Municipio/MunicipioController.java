package Municipio;


import Municipio.DTO.ObtenerMunicipiosDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;


@Path("/municipio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MunicipioController {

    @Inject
    MunicipioServices municipioServices;

    @GET
    @RolesAllowed({"SUPERADMIN", "ADMIN", "EMPLEADO"})
    public Response obtenerMunicipios(
            @QueryParam("pagina") @DefaultValue("0") int pagina,
            @QueryParam("tamanio") @DefaultValue("10") int tamanio,
            @QueryParam("comuna") String comuna,
            @QueryParam("nombre") String nombre) {

        List<ObtenerMunicipiosDto> lista = municipioServices.municipios(comuna, nombre, pagina, tamanio);
        return Response.ok(lista).build();
    }

    @POST
    public Response crearMunicipio(Municipio municipio){
        Map<String, String> respuesta = municipioServices.crearMuni(municipio);
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }


}
