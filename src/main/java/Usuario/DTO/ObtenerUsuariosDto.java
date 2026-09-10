package Usuario.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerUsuariosDto {
    private Integer id;
    private String username;
    private Boolean estado;
    private Boolean es_inspector;
    private String comuna;
    private Integer idPerfil;
    private String nombrePerfil;
    private String email;


}
