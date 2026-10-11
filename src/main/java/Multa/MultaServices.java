package Multa;


import Inspector.Inspector;
import Multa.DTO.*;
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
import Inspector.InspectorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class MultaServices {

    @Inject
    MultaRepository multaRepository;

    @Inject
    InspectorRepository inspectorRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    Tipo_multaRepository tipoMultaRepository;

    @Inject
    MultaMapper multaMapper;


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
        multa.setComuna(crearMultaDto.getComuna());
        multa.setDireccion(crearMultaDto.getDireccion());
        Object rawId = jwt.getClaim("id");
        Integer usuarioId = rawId != null ? Integer.valueOf(rawId.toString()) : null;
        Usuario usuario = usuarioRepository.findById(usuarioId);
        multa.setUsuario(usuario);
        multa.setPatente(crearMultaDto.getPatente().toUpperCase());
        if(crearMultaDto.getTipo_multa() != null){
            Tipo_multa tipoMulta = this.tipoMultaRepository.findByIdOptional(crearMultaDto.getTipo_multa()).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encotro el tipo de multa")).build()));
            multa.setTipoMulta(tipoMulta);
        }
        multa.setCortesia(crearMultaDto.getCortesia());
        this.multaRepository.persist(multa);

        return Map.of("message", "Multa creada con exito");
    }

    private List<ObtenerMultasDto> obtenerMultasPorEstado(int pagina, int tamano, Boolean estado){
        Object rawComuna = jwt.getClaim("comuna");
        String comuna = rawComuna != null ? rawComuna.toString() : null;

        Object rawId = jwt.getClaim("id_perfil");
        Integer perfilId = rawId != null ? Integer.valueOf(rawId.toString()) : null;

        List<Multa> multas;
        if (perfilId != 2) {
            multas = this.multaRepository.find("comuna = ?1 and estado = ?2 ORDER BY fecha_creacion DESC", comuna, estado).page(Page.of(pagina, tamano)).list();
        } else {
            multas = this.multaRepository.find("estado = ?1 ORDER BY fecha_creacion DESC", estado).page(Page.of(pagina, tamano)).list();
        }
        if(multas.isEmpty()){
            return List.of();
        }
        //distinc elimina los elementos repetidos
        List<Usuario> usuarios = multas.stream().map(Multa::getUsuario).distinct().toList();
        List<Inspector> inspectores = inspectorRepository.find("usuario in ?1", usuarios).list();

        Map<Integer, String > inpectorMap = inspectores.stream().filter(i -> i.getUsuario() != null).collect(Collectors.toMap(i -> i.getUsuario().getId(), i -> i.getNombre() + " " + i.getApellido()));

        return multas.stream().map(multa -> {
            ObtenerMultasDto dto = new ObtenerMultasDto();
            dto.setFecha_creacion(multa.getFecha_creacion());
            dto.setCortesia(multa.getCortesia());
            dto.setUbicacion(multa.getUbicacion());
            dto.setFoto1(multa.getFoto1());
            dto.setId(multa.getId());
            dto.setFoto2(multa.getFoto2());
            dto.setPagado(multa.getPagado());
            dto.setFoto3(multa.getFoto3());
            dto.setPatente(multa.getPatente());
            dto.setMotivoEliminacion(multa.getDescripcion_desactivada());
            dto.setDireccion(multa.getDireccion());
            if(multa.getUsuario() != null){
                dto.setInspector(inpectorMap.get(multa.getUsuario().getId()));
            }
            if (multa.getTipoMulta() != null) {
                dto.setTipo_multa(multa.getTipoMulta().getNombre());
                dto.setIdTipoMulta(multa.getTipoMulta().getId());
            }
            return dto;
        }).toList();
    }

    public List<ObtenerMultasDto> traeMultas(int pagina, int tamano){
        return obtenerMultasPorEstado(pagina,tamano,true);
    }

    public List<ObtenerMultasDto> traeMultasEliminadas(int pagina, int tamano){
        return obtenerMultasPorEstado(pagina,tamano,false);
    }



    public List<ObtenerMultasDto> multasPorPatente(String patente, int pagina, int tamanio, Boolean estado){
        Object rawComuna = jwt.getClaim("comuna");
        String comuna = rawComuna != null ? rawComuna.toString() : null;

        Object rawId = jwt.getClaim("id_perfil");
        Integer perfilId = rawId != null ? Integer.valueOf(rawId.toString()) : null;

        List<Multa> multas;

        if (perfilId != 2) {
           multas =  this.multaRepository.find("patente = ?1 and comuna = ?2 and estado = ?3 ORDER BY fecha_creacion DESC", patente.toUpperCase(), comuna, estado).page(Page.of(pagina, tamanio)).list();
        } else {
            multas = this.multaRepository.find("patente = ?1 and estado = ?2 ORDER BY fecha_creacion DESC", patente.toUpperCase(), estado).page(Page.of(pagina, tamanio)).list();
        }

        List<Usuario> usuarios = multas.stream().map(Multa::getUsuario).distinct().toList();
        List<Inspector> inspectores = inspectorRepository.find("usuario in ?1", usuarios).list();
        Map<Integer, String > inpectorMap = inspectores.stream().filter(i -> i.getUsuario() != null).collect(Collectors.toMap(i -> i.getUsuario().getId(), i -> i.getNombre() + " " + i.getApellido()));


        return multas.stream().map(m -> {
            ObtenerMultasDto obMultas = new ObtenerMultasDto();
            obMultas.setFecha_creacion(m.getFecha_creacion());
            obMultas.setFoto1(m.getFoto1());
            obMultas.setId(m.getId());
            obMultas.setFoto2(m.getFoto2());
            obMultas.setFoto3(m.getFoto3());
            obMultas.setIdTipoMulta(m.getTipoMulta().getId());
            obMultas.setUbicacion(m.getUbicacion());
            obMultas.setPatente(m.getPatente());
            obMultas.setPagado(m.getPagado());
            obMultas.setDireccion(m.getDireccion());
            obMultas.setMotivoEliminacion(m.getDescripcion_desactivada());
            obMultas.setCortesia(m.getCortesia());
            if(m.getUsuario() != null){
                obMultas.setInspector(inpectorMap.get(m.getUsuario().getId()));
            }
            obMultas.setTipo_multa(m.getTipoMulta().getNombre());
            return obMultas;
        }).toList();
    }

    public List<ObtenerMultasDto> traeMultasPorPatente(String patente, int pagina, int tamanio){
        return multasPorPatente(patente, pagina,tamanio,true);
    }

    public List<ObtenerMultasDto> traeMultasEliminadasPorPatente(String patente, int pagina, int tamanio){
        return multasPorPatente(patente, pagina,tamanio,false);
    }


    @Transactional
    public Map<String, String> ediatMulta(Integer idMulta, EditMultasDto editm){
        Multa multa = this.multaRepository.findByIdOptional(idMulta).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encontro la multa")).build()));
        if(editm.getIdTipoMulta() != null){
            Tipo_multa tipoMulta = this.tipoMultaRepository.findByIdOptional(editm.getIdTipoMulta()).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encontro el tipo de multa")).build()));
            multa.setTipoMulta(tipoMulta);
        }
        if(editm.getPagada() != null){
            multa.setPagado(editm.getPagada());
        }
        multaMapper.editarMulrta(editm, multa);
        this.multaRepository.persist(multa);
        return Map.of("message", "Multa actualizada con éxito");
    }

    @Transactional
    public Map<String, String> borrarMulta(Integer idMulta, BorrasMultaDto borrasMultaDto){
        Multa multa = this.multaRepository.findByIdOptional(idMulta).orElseThrow(() -> new NotFoundException(Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "No se encontro la multa")).build()));
        multa.setDescripcion_desactivada(borrasMultaDto.getDescripcion_desactivada());
        multa.setEstado(borrasMultaDto.getEstado());
        this.multaRepository.persist(multa);
        return Map.of("message", "Multa Eliminada con exito");
    }






}
