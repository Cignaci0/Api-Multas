package Usuario;


import Usuario.DTO.EditUsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "cdi",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UsuarioMapper {
    void editarUsuario(EditUsuarioDTO origen, @MappingTarget Usuario destino);
}
