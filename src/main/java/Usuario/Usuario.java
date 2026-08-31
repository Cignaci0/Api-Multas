package Usuario;

import Multa.Multa;
import Perfil.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String email;
    private Integer codigo_recuperacion;
    private Boolean estado = true;
    private LocalDateTime fecha_exp_codigo;
    private Boolean es_inspector = false;

    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = true)
    private Perfil perfil;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Multa> multas;

}
