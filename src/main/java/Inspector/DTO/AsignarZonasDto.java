package Inspector.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AsignarZonasDto {
    private Integer inspectorId;
    private List<Integer> zonaIds;
}
