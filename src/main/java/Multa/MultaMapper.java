package Multa;


import Multa.DTO.EditMultasDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "cdi",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MultaMapper {
    void editarMulrta(EditMultasDto origen, @MappingTarget Multa destino);
}
