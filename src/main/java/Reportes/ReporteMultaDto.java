package Reportes;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReporteMultaDto {

    private LocalDateTime fechaCreacion;
    private String patente;
    private String tipoMulta;
    private String direccion;
    private String inspector;
    private Boolean cortesia;

    public ReporteMultaDto() {
    }

    public ReporteMultaDto(LocalDateTime fechaCreacion, String patente,
                           String tipoMulta, String direccion,
                           String inspector, Boolean cortesia) {
        this.fechaCreacion = fechaCreacion;
        this.patente = patente;
        this.tipoMulta = tipoMulta;
        this.direccion = direccion;
        this.inspector = inspector;
        this.cortesia = cortesia;
    }
}
