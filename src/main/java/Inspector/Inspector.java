package Inspector;

import Perfil.Perfil;
import Usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inspector")
@Getter
@Setter
public class Inspector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String apellido;
    private String comuna;
    private String email;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuario usuario;

}
