package Inspector.DTO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class EditInspectorDto {
    private String email;
    private String nombre;
    private String apellido;
    private String comuna;
}
