package ComunasRegiones.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter

public class ObtenerComunas {
    private Integer id;
    private String comuna;
    private List<ObtenerZonas> zonas;
}
