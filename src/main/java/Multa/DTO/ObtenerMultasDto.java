package Multa.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ObtenerMultasDto {

    private Integer id;
    private LocalDateTime fecha_creacion;
    private String ubicacion;
    private String foto1;
    private String foto2;
    private String foto3;
    private String patente;
    private String tipo_multa;
    private String direccion;
    private Integer idTipoMulta;
    private String motivoEliminacion;

}
