package Inspector;

import Municipio.Municipio;
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

    @ManyToOne
    @JoinColumn(name = "id_municipio", nullable = true)
    private Municipio municipio;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuario usuario;

}
