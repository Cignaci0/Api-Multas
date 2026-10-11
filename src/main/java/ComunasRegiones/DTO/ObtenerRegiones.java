package ComunasRegiones.DTO;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Set;

@Getter
@Setter

public class ObtenerRegiones {
    private String id;
    private String region;
    private List<ObtenerComunas> comunas;

}
