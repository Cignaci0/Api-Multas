package Reportes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class ReportesServices {

    @Inject
    ReportesRepository reportesRepository;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATO_FECHA_SIN_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_ARCHIVO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // ============================================================
    //  REPORTE: MULTAS POR RANGO DE FECHAS
    // ============================================================
    public byte[] generarReporteMultasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws DocumentException {
        List<ReporteMultaDto> multas = reportesRepository.getMultasPorRangoFechas(fechaInicio, fechaFin);

        Document document = new Document(PageSize.A4.rotate(), 36, 36, 72, 36);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Encabezado
        agregarEncabezado(document, "REPORTE DE MULTAS POR RANGO DE FECHAS",
            "Desde: " + fechaInicio.format(FORMATO_FECHA_SIN_HORA) + "  Hasta: " + fechaFin.format(FORMATO_FECHA_SIN_HORA));

        // Tabla de datos
        agregarTablaMultas(document, multas);

        // Pie de página
        agregarPiePagina(document, multas.size());

        document.close();
        return baos.toByteArray();
    }

    // ============================================================
    //  REPORTE: MULTAS POR INSPECTOR
    // ============================================================
    public byte[] generarReporteMultasPorInspector(Integer inspectorId) throws DocumentException {
        List<ReporteMultaDto> multas = reportesRepository.getMultasPorInspector(inspectorId);
        String nombreInspector = reportesRepository.getNombreInspector(inspectorId);

        Document document = new Document(PageSize.A4.rotate(), 36, 36, 72, 36);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        agregarEncabezado(document, "REPORTE DE MULTAS POR INSPECTOR",
            "Inspector: " + nombreInspector);

        agregarTablaMultas(document, multas);
        agregarPiePagina(document, multas.size());

        document.close();
        return baos.toByteArray();
    }

    // ============================================================
    //  REPORTE: MULTAS POR PATENTE
    // ============================================================
    public byte[] generarReporteMultasPorPatente(String patente) throws DocumentException {
        List<ReporteMultaDto> multas = reportesRepository.getMultasPorPatente(patente);

        Document document = new Document(PageSize.A4.rotate(), 36, 36, 72, 36);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        agregarEncabezado(document, "REPORTE DE MULTAS POR PATENTE",
            "Patente: " + patente);

        agregarTablaMultas(document, multas);
        agregarPiePagina(document, multas.size());

        document.close();
        return baos.toByteArray();
    }

    // ============================================================
    //  MÉTODOS AUXILIARES PARA EL PDF
    // ============================================================

    private void agregarEncabezado(Document document, String titulo, String subtitulo) throws DocumentException {
        // Título
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.WHITE);
        Paragraph pTitulo = new Paragraph(titulo, fontTitulo);
        pTitulo.setAlignment(Element.ALIGN_CENTER);

        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        PdfPCell cellTitulo = new PdfPCell(pTitulo);
        cellTitulo.setBackgroundColor(new Color(41, 128, 185));
        cellTitulo.setPadding(10);
        cellTitulo.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(cellTitulo);
        document.add(headerTable);

        document.add(new Paragraph(" "));

        // Subtítulo
        Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.DARK_GRAY);
        Paragraph pSubtitulo = new Paragraph(subtitulo, fontSubtitulo);
        pSubtitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(pSubtitulo);

        // Fecha de generación
        Paragraph pFecha = new Paragraph("Generado: " + LocalDateTime.now().format(FORMATO_FECHA),
            FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY));
        pFecha.setAlignment(Element.ALIGN_RIGHT);
        document.add(pFecha);

        document.add(new Paragraph(" "));
    }

    private void agregarTablaMultas(Document document, List<ReporteMultaDto> multas) throws DocumentException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.2f, 1.0f, 1.5f, 2.2f, 1.8f, 0.8f});

        // Estilos
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
        Font fontCell = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.BLACK);
        Font fontCellAlt = FontFactory.getFont(FontFactory.HELVETICA, 8, new Color(50, 50, 50));

        // Color de fondo para filas alternadas
        Color colorAlt = new Color(240, 248, 255);

        // Encabezados
        String[] headers = {"Fecha Creación", "Patente", "Tipo Multa", "Dirección", "Inspector", "Cortesía"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, fontHeader));
            cell.setBackgroundColor(new Color(41, 128, 185));
            cell.setPadding(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        // Filas de datos
        boolean alternar = false;
        for (ReporteMultaDto m : multas) {
            Color fondo = alternar ? colorAlt : Color.WHITE;
            Font fuente = alternar ? fontCellAlt : fontCell;

            agregarCelda(table, m.getFechaCreacion() != null ? m.getFechaCreacion().format(FORMATO_FECHA) : "-", fuente, fondo, Element.ALIGN_CENTER);
            agregarCelda(table, m.getPatente(), fuente, fondo, Element.ALIGN_CENTER);
            agregarCelda(table, m.getTipoMulta(), fuente, fondo, Element.ALIGN_LEFT);
            agregarCelda(table, m.getDireccion(), fuente, fondo, Element.ALIGN_LEFT);
            agregarCelda(table, m.getInspector() != null ? m.getInspector() : "-", fuente, fondo, Element.ALIGN_LEFT);
            agregarCelda(table, m.getCortesia() != null && m.getCortesia() ? "Sí" : "No", fuente, fondo, Element.ALIGN_CENTER);

            alternar = !alternar;
        }

        document.add(table);
    }

    private void agregarCelda(PdfPTable table, String texto, Font font, Color backgroundColor, int alineacion) {
        PdfPCell cell = new PdfPCell(new Paragraph(texto, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alineacion);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void agregarPiePagina(Document document, int totalRegistros) throws DocumentException {
        document.add(new Paragraph(" "));

        Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Paragraph pTotal = new Paragraph("Total de multas: " + totalRegistros, fontTotal);
        pTotal.setAlignment(Element.ALIGN_RIGHT);

        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        PdfPCell cellTotal = new PdfPCell(pTotal);
        cellTotal.setBackgroundColor(new Color(41, 128, 185));
        cellTotal.setPadding(8);
        cellTotal.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(cellTotal);
        document.add(footerTable);
    }
}
