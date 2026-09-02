package Inspector;


import Inspector.DTO.EditInspectorDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "cdi",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InspectorMapper {
    void editInspector(EditInspectorDto origen,@MappingTarget Inspector destino);
}
