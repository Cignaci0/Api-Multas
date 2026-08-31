package Inspector.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerInspectorDto {
    public Integer id;
    public String nombre;
    public String apeliido;
    public Integer idMunicipio;
    public String nombreMunicipio;
    public Integer idUsuario;
    public String usernameUsuario;
}
