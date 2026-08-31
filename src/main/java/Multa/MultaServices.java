package Multa;


import Multa.DTO.CrearMultaDto;
import Multa.DTO.ObtenerMultasDto;
import Tipo_multa.Tipo_multa;
import Tipo_multa.Tipo_multaRepository;
import Usuario.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import   Usuario.UsuarioRepository;
import io.quarkus.panache.common.Page;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MultaServices {

    @Inject
    MultaRepository multaRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    Tipo_multaRepository tipoMultaRepository;


    @Transactional
    public Map<String, String> crearMulta(CrearMultaDto crearMultaDto){
        Multa multa = new Multa();
        multa.setFecha_creacion(LocalDateTime.now());
        if (crearMultaDto.getLatitud() != null && crearMultaDto.getLongitud() != null) {
            String coordenadasExactas = crearMultaDto.getLatitud() + "," + crearMultaDto.getLongitud();
            multa.setUbicacion(coordenadasExactas);
        }
        multa.setFoto1(crearMultaDto.getFoto1());
        multa.setFoto2(crearMultaDto.getFoto2());
        multa.setFoto3(crearMultaDto.getFoto3());
        Object rawId = jwt.getClaim("id");
        Integer usuarioId = rawId != null ? Integer.valueOf(rawId.toString()) : null;
        Usuario usuario = usuarioRepository.findById(usuarioId);
        multa.setUsuario(usuario);
        multa.setPatente(crearMultaDto.getPatente().toUpperCase());
        if(crearMultaDto.getTipo_multa() != null){
            Tipo_multa tipoMulta = this.tipoMultaRepository.findByIdOptional(crearMultaDto.getTipo_multa()).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encotro el tipo de multa")).build()));
            multa.setTipoMulta(tipoMulta);
        }
        this.multaRepository.persist(multa);
        return Map.of("message", "Multa creada con exito");
    }

    public List<ObtenerMultasDto> traeMultas(int pagina, int tamano){
        List<Multa> multas = this.multaRepository.findAll().page(Page.of(pagina, tamano)).list();
        return multas.stream().map(multa -> {
            ObtenerMultasDto dto = new ObtenerMultasDto();
            dto.setFecha_creacion(multa.getFecha_creacion());
            dto.setUbicacion(multa.getUbicacion());
            dto.setFoto1(multa.getFoto1());
            dto.setFoto2(multa.getFoto2());
            dto.setFoto3(multa.getFoto3());
            dto.setPatente(multa.getPatente());
            if (multa.getTipoMulta() != null) {
                dto.setTipo_multa(multa.getTipoMulta().getNombre());
            }
            return dto;
        }).toList();
    }

    public List<ObtenerMultasDto> multasPorPatente(String patente, int pagina, int tamanio){
        List<Multa> multas = this.multaRepository.find("patente = ?1 ORDER BY fecha_creacion DESC", patente.toUpperCase()).page(Page.of(pagina,tamanio)).list();
        return multas.stream().map(m -> {
            ObtenerMultasDto obMultas = new ObtenerMultasDto();
            obMultas.setFecha_creacion(m.getFecha_creacion());
            obMultas.setFoto1(m.getFoto1());
            obMultas.setFoto2(m.getFoto2());
            obMultas.setFoto3(m.getFoto3());
            obMultas.setUbicacion(m.getUbicacion());
            obMultas.setPatente(m.getPatente());
            obMultas.setTipo_multa(m.getTipoMulta().getNombre());
            return obMultas;
        }).toList();
    }


}
