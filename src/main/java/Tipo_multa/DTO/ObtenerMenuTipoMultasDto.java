package Tipo_multa.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ObtenerMenuTipoMultasDto {
    private String padre;
    private List<ObtenerHijosMenuDto> hijos;
}
