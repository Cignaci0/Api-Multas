package Multa;


import Multa.DTO.BorrasMultaDto;
import Multa.DTO.CrearMultaDto;
import Multa.DTO.EditMultasDto;
import Multa.DTO.ObtenerMultasDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;


@Path("/multa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MultaController {

    @Inject
    MultaServices multaServices;

    @POST
    @RolesAllowed({"EMPLEADO"})
    public Response crearMulta(CrearMultaDto multaBody) {
        Map<String, String> respuesta = multaServices.crearMulta(multaBody);
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }

    @PATCH
    @Path("/borrar/{idMulta}")
    public Response borrarMulta(@PathParam("idMulta") Integer idMulta, BorrasMultaDto borrasMultaDto) {
        Map<String, String> respuesta = multaServices.borrarMulta(idMulta, borrasMultaDto);
        return Response.status(Response.Status.OK).entity(respuesta).build();
    }

    @PATCH
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    @Path("/edit/{idMulta}")
    public Response editarMulta(@PathParam("idMulta") Integer idMulta, EditMultasDto editm) {
        Map<String, String> respuesta = multaServices.ediatMulta(idMulta, editm);
        return Response.status(Response.Status.OK).entity(respuesta).build();
    }


    @GET
    @RolesAllowed({"SUPERADMIN", "ADMIN", "EMPLEADO"})
    public Response obtenerMultas(
            @QueryParam("pagina") @DefaultValue("0") int pagina,
            @QueryParam("tamanio") @DefaultValue("10") int tamanio) {

        List<ObtenerMultasDto> listaMultas = multaServices.traeMultas(pagina, tamanio);
        return Response.ok(listaMultas).build();
    }

    @GET
    @Path("/{patente}")
    @RolesAllowed({"SUPERADMIN", "ADMIN", "EMPLEADO"})
    public Response obtenerMultasPorPatente(
            @PathParam("patente") String patente,
            @QueryParam("pagina") @DefaultValue("0") int pagina,
            @QueryParam("tamanio") @DefaultValue("10") int tamanio) {

        List<ObtenerMultasDto> listaMultas = multaServices.multasPorPatente(patente, pagina, tamanio);
        return Response.ok(listaMultas).build();
    }
}
