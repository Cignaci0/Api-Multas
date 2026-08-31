package Inspector;


import Inspector.DTO.ObtenerInspectorDto;
import Multa.DTO.ObtenerMultasDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/inspector")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InspectorController {

    @Inject
    InspectorServices inspectorServices;

    @GET
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    public Response obtenerInspectores(
            @QueryParam("pagina") @DefaultValue("0") int pagina,
            @QueryParam("tamanio") @DefaultValue("10") int tamanio) {

        List<ObtenerInspectorDto> listaMultas = inspectorServices.obtenerInspectores(pagina, tamanio);
        return Response.ok(listaMultas).build();
    }

}
