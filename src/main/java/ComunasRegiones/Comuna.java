package ComunasRegiones;


import Zonas.Zonas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "comunas")
@Getter
@Setter
public class Comuna {

    @Id
    Integer id;
    String nombre;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "comuna", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Zonas> zonas;
}
