package Tipo_multa;

import Multa.Multa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tipo_multa")
@Getter
@Setter
public class Tipo_multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private Integer modulo_padre;

    @OneToMany(mappedBy = "tipoMulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Multa> multas;
}
