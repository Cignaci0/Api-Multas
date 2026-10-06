package Reportes;

import com.lowagie.text.DocumentException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Path("/reportes")
@Produces(MediaType.APPLICATION_JSON)
public class ReportesController {

    @Inject
    ReportesServices reportesServices;

    // ============================================================
    //  REPORTE: MULTAS POR RANGO DE FECHAS
    //  GET /reportes/multas/rango-fechas?fechaInicio=2024-01-01&fechaFin=2024-12-31
    // ============================================================
    @GET
    @Path("/multas/rango-fechas")
    @Produces("application/pdf")
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    public Response reporteMultasPorRangoFechas(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate inicio = LocalDate.parse(fechaInicio, formatter);
            LocalDate fin = LocalDate.parse(fechaFin, formatter);

            LocalDateTime inicioDateTime = inicio.atStartOfDay();
            LocalDateTime finDateTime = fin.atTime(LocalTime.MAX);

            byte[] pdf = reportesServices.generarReporteMultasPorRangoFechas(inicioDateTime, finDateTime);

            String filename = "reporte_multas_rango_fechas_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

            return Response.ok(pdf)
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .build();

        } catch (DocumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar el reporte: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Use: yyyy-MM-dd")
                    .build();
        }
    }

    // ============================================================
    //  REPORTE: MULTAS POR INSPECTOR
    //  GET /reportes/multas/inspector/{inspectorId}
    // ============================================================
    @GET
    @Path("/multas/inspector/{inspectorId}")
    @Produces("application/pdf")
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    public Response reporteMultasPorInspector(@PathParam("inspectorId") Integer inspectorId) {
        try {
            byte[] pdf = reportesServices.generarReporteMultasPorInspector(inspectorId);

            String filename = "reporte_multas_inspector_" + inspectorId + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

            return Response.ok(pdf)
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .build();

        } catch (DocumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar el reporte: " + e.getMessage())
                    .build();
        }
    }

    // ============================================================
    //  REPORTE: MULTAS POR PATENTE
    //  GET /reportes/multas/patente/{patente}
    // ============================================================
    @GET
    @Path("/multas/patente/{patente}")
    @Produces("application/pdf")
    @RolesAllowed({"SUPERADMIN", "ADMIN"})
    public Response reporteMultasPorPatente(@PathParam("patente") String patente) {
        try {
            byte[] pdf = reportesServices.generarReporteMultasPorPatente(patente);

            String filename = "reporte_multas_patente_" + patente + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

            return Response.ok(pdf)
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .build();

        } catch (DocumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar el reporte: " + e.getMessage())
                    .build();
        }
    }
}
