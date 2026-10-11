package Inspector;

import Zonas.Zonas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inpectores_zonas")
@Getter
@Setter
public class InspectorZonas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_inspector")
    Inspector inspector;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_zona")
    Zonas zona;
}
