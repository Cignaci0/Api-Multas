package Usuario;


import Usuario.DTO.EditUsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "cdi",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)


public interface UsuarioMapper {
    @Mapping(target = "es_inspector", source = "es_inspector")
    @Mapping(target = "perfil", ignore = true)
    void editarUsuario(EditUsuarioDTO origen, @MappingTarget Usuario destino);
}
