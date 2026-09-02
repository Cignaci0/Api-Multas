package Inspector;


import Inspector.DTO.EditInspectorDto;
import Inspector.DTO.ObtenerInspectorDto;
import Multa.DTO.ObtenerMultasDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;


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

    @PATCH
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    @Path("/{idInspector}")
    public Response editInspector(@PathParam("idInspector") Integer idInspector, EditInspectorDto editInspectorDto){
        Map<String, String> respuesta = inspectorServices.editIsnpector(idInspector, editInspectorDto);
        return Response.status(Response.Status.OK).entity(respuesta).build();
    }

}
