package Modulo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ObtenerModulosPorPerfilDto {
    public String padre;
    public List<ObtenerHijosMenuDto> hijos;
}
