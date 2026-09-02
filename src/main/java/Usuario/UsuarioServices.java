package Usuario;


import Inspector.DTO.CrearInspectorDto;
import Inspector.Inspector;
import Perfil.Perfil;
import io.quarkus.panache.common.Page;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import Perfil.PerfilRepository;
import Inspector.InspectorRepository;
import Usuario.DTO.ObtenerUsuariosDto;

import java.time.Duration;
import java.util.*;

@ApplicationScoped
public class UsuarioServices {
    @Inject
    UsuarioRepository usuarioRepository;


    @Inject
    PerfilRepository perfilRepository;

    @Inject
    InspectorRepository inspectorRepository;


    @Transactional
    public Map<String, String> crearUsuario(Usuario nuevoUsuario){
        Optional<Usuario> existeUsername = this.usuarioRepository.find("username", nuevoUsuario.getUsername()).firstResultOptional();
        if(existeUsername.isPresent()){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Ya existe el usuario con el username: " + nuevoUsuario.getUsername())).build());
        }
        String PasswordActual = nuevoUsuario.getPassword();
        String PasswordEncriptada = BCrypt.hashpw(PasswordActual, BCrypt.gensalt(10));
        nuevoUsuario.setPassword(PasswordEncriptada);
        if(nuevoUsuario.getPerfil() != null){
            Perfil perfil = this.perfilRepository.findByIdOptional(nuevoUsuario.getPerfil().getId()).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Perfil no econtrado")).build()));
        }

        this.usuarioRepository.persist(nuevoUsuario);
        return Map.of("message", "ususario creado correctamente");
    }

    public Map<String, String> loginPaginaWeb(String username, String password){
        Usuario usuario = this.usuarioRepository.find("username", username).firstResultOptional().orElseThrow(() -> new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Usuario o contraseña incorrectos")).build()));
        if(usuario.getEs_inspector() == true){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "No se puede acceder a la pagina web con su perfil")).build());
        }

        boolean passwordMatch = BCrypt.checkpw(password, usuario.getPassword());
        if(!passwordMatch){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Usuario o contraseña incorrectos")).build());
        }
        if(usuario.getEstado() == false){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "No se puede acceder a la pagina, cuenta desactivada")).build());
        }

        String token = Jwt.issuer("https://multas.com/issuer")
                .upn(usuario.getUsername())
                .subject(usuario.getUsername())
                .claim("id_perfil",usuario.getPerfil().getId())
                .claim("id", usuario.getId())
                .groups(new HashSet<>(Set.of(usuario.getPerfil().getNombre())))
                .expiresIn(Duration.ofMinutes(15))
                .sign();
        return Map.of("token", token);
    }

    public Map<String, String> loginMovile(String username, String password){
        Usuario usuario = this.usuarioRepository.find("username", username).firstResultOptional().orElseThrow(() -> new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Usuario o contraseña incorrectos")).build()));
        if(usuario.getEs_inspector() == false){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "No se puede acceder a la pagina web con su perfil")).build());
        }
        if(usuario.getEstado() == false){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "No se puede acceder a la pagina, cuenta desactivada")).build());
        }
        boolean passwordMatch = BCrypt.checkpw(password, usuario.getPassword());
        if(!passwordMatch){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Usuario o contraseña incorrectos")).build());
        }

        String token = Jwt.issuer("https://multas.com/issuer")
                .upn(usuario.getUsername())
                .subject(usuario.getUsername())
                .claim("id_perfil",usuario.getPerfil().getId())
                .claim("id", usuario.getId())
                .groups(new HashSet<>(Set.of(usuario.getPerfil().getNombre())))
                .expiresIn(Duration.ofHours(24))
                .sign();
        return Map.of("token", token);
    }

    @Transactional
    public Map<String, String> crearInspector(CrearInspectorDto datosInspector){
        Perfil perfil = this.perfilRepository.findByIdOptional(3).orElseThrow(() -> new NotFoundException("No se encontró el perfil de inspector"));
        String passwordEncriptada = BCrypt.hashpw(datosInspector.getPassword(), BCrypt.gensalt(10));
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(datosInspector.getUsername());
        nuevoUsuario.setPassword(passwordEncriptada);
        nuevoUsuario.setEs_inspector(true);
        nuevoUsuario.setComuna(datosInspector.getComuna());
        nuevoUsuario.setPerfil(perfil);
        nuevoUsuario.setEmail(datosInspector.getEmail());
        Optional<Usuario> existeUsername = this.usuarioRepository.find("username", nuevoUsuario.getUsername()).firstResultOptional();
        if(existeUsername.isPresent()){
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Ya existe el usuario con el username: " + nuevoUsuario.getUsername())).build());
        }
        this.usuarioRepository.persist(nuevoUsuario);

        Inspector nuevoInspector = new Inspector();
        nuevoInspector.setNombre(datosInspector.getNombre());
        nuevoInspector.setApellido(datosInspector.getApellido());
        nuevoInspector.setComuna(datosInspector.getComuna());
        nuevoInspector.setEmail(datosInspector.getEmail());
        nuevoInspector.setUsuario(nuevoUsuario);

        this.inspectorRepository.persist(nuevoInspector);

        return Map.of("message", "Inspector creado correctamente");
    }

    public List<ObtenerUsuariosDto> obtenerUsuariosDtos(int pagina, int tamano){
        List<Usuario> usuarios = this.usuarioRepository.findAll().page(Page.of(pagina,tamano)).list();
        return usuarios.stream().map( u -> {
            ObtenerUsuariosDto obUsu = new ObtenerUsuariosDto();
            obUsu.setUsername(u.getUsername());
            obUsu.setComuna(u.getComuna());
            obUsu.setId(u.getId());
            obUsu.setEmail(u.getEmail());
            if(u.getPerfil() != null){
                Perfil perfil = this.perfilRepository.findByIdOptional(u.getPerfil().getId()).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Perfil no encontrado")).build()));
                obUsu.setIdPerfil(u.getPerfil().getId());
                obUsu.setNombrePerfil(u.getPerfil().getNombre());
            }
            obUsu.setEstado(u.getEstado());
            obUsu.setEs_inspector(u.getEs_inspector());
            return obUsu;
        }).toList();
    }


}
