package Inspector.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerInspectorDto {
    public Integer id;
    public String nombre;
    public String apeliido;
    public String comuna;
    public Integer idUsuario;
    public String usernameUsuario;
    public String email;
}
