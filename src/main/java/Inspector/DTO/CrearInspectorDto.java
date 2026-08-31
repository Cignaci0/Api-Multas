package Inspector.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearInspectorDto {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private Integer id_municipio;
}
