package Modulo;

import Perfil.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "modulo")
@Getter
@Setter
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private Integer modulo_padre;

    @ManyToMany(mappedBy = "modulos")
    private Set<Perfil> perfiles;
}
//holas
