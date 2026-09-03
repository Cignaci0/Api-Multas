package Usuario.DTO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EditUsuarioDTO {
    private String email;
    private Boolean estado;
    private String username;
    private Integer perfil;
    private Boolean es_inspector;
    private String comuna;
}
