package Zonas.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerZonasDto {
    private Integer id;
    private String NombreZona;
    private Integer idComuna;
    private String nombreComuna;
}
