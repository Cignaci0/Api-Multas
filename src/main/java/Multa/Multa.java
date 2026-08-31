package Multa;


import Tipo_multa.Tipo_multa;
import Usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "multa")
@Getter
@Setter
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime fecha_creacion;
    private String ubicacion;
    private String foto1;
    private String foto2;
    private String foto3;
    private String patente;

    @ManyToOne
    @JoinColumn(name = "id_usuario_creador", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_multa", nullable = false)
    private Tipo_multa tipoMulta;

}
