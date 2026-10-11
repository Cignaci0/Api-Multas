package ComunasRegiones;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "regiones")
@Getter
@Setter
public class Region {
    @Id
    String id;
    String nombre;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comuna> comunas;
}
