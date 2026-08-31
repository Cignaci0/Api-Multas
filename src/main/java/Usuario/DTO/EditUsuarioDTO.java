package Usuario.DTO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EditUsuarioDTO {
    private String email;
    private Boolean estado;
}
