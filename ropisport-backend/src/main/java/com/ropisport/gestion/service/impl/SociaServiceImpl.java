package com.ropisport.gestion.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.model.dto.excel.SociaExcelDto;
import com.ropisport.gestion.model.dto.request.SociaRequest;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.dto.response.SociaResponse;
import com.ropisport.gestion.model.entity.CategoriaNegocio;
import com.ropisport.gestion.model.entity.Empresa;
import com.ropisport.gestion.model.entity.Socia;
import com.ropisport.gestion.repository.CategoriaNegocioRepository;
import com.ropisport.gestion.repository.SociaRepository;
import com.ropisport.gestion.service.SociaService;
import com.ropisport.gestion.util.ExcelHelper;
import com.ropisport.gestion.repository.EmpresaRepository;




@Service
public class SociaServiceImpl implements SociaService {

	@Autowired
	private EmpresaRepository empresaRepository;
	
    @Autowired
    private SociaRepository sociaRepository;

    @Autowired
    private CategoriaNegocioRepository categoriaNegocioRepository;

    @Autowired
    private ExcelHelper excelHelper;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public SociaResponse getSociaById(Integer id) {
        Socia socia = sociaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + id));
        return mapToResponse(socia);
    }

    @Override
    public PaginatedResponse<SociaResponse> getAllSocias(int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        Page<Socia> socias = sociaRepository.findAll(pageable);

        List<SociaResponse> content = socias.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return createPaginatedResponse(content, socias);
    }

    @Override
    @Transactional
    public SociaResponse createSocia(SociaRequest request) {
        Socia socia = new Socia();
        updateSociaFromRequest(socia, request);

        socia = sociaRepository.save(socia);
        return mapToResponse(socia);
    }

    @Override
    @Transactional
    public SociaResponse updateSocia(Integer id, SociaRequest request) {
        Socia socia = sociaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + id));

        updateSociaFromRequest(socia, request);
        socia = sociaRepository.save(socia);

        return mapToResponse(socia);
    }

    @Override
    @Transactional
    public void deleteSocia(Integer id) {
        if (!sociaRepository.existsById(id)) {
            throw new EntityNotFoundException("Socia no encontrada con ID: " + id);
        }
        sociaRepository.deleteById(id);
    }

    // Métodos de búsqueda
    @Override
    public PaginatedResponse<SociaResponse> busquedaGeneral(
            String texto, Boolean activa, int page, int size, String sort) {

        Pageable pageable = createPageable(page, size, sort);
        Page<Socia> socias = sociaRepository.busquedaGeneral(texto, activa, pageable);

        List<SociaResponse> content = socias.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return createPaginatedResponse(content, socias);
    }

    @Override
    public PaginatedResponse<SociaResponse> busquedaAvanzada(
            String nombre,
            String nombreNegocio,
            String email,
            String telefono,
            String cif,
            Integer categoriaId,
            Boolean activa,
            int page,
            int size,
            String sort) {

        Pageable pageable = createPageable(page, size, sort);
        Page<Socia> socias = sociaRepository.busquedaAvanzada(
                nombre, nombreNegocio, email, telefono, cif, categoriaId, activa, pageable);

        List<SociaResponse> content = socias.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return createPaginatedResponse(content, socias);
    }

    // Cambio de estado
    @Override
    @Transactional
    public SociaResponse cambiarEstado(Integer id, Boolean activa, String observaciones) {
        Socia socia = sociaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + id));

        socia.setActiva(activa);

        if (!activa && socia.getFechaBaja() == null) {
            socia.setFechaBaja(LocalDateTime.now());
        }

        if (activa && socia.getFechaBaja() != null) {
            socia.setFechaBaja(null);
        }

        if (observaciones != null && !observaciones.isEmpty()) {
            String currentObservaciones = socia.getObservaciones();
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            String newObservaciones = (currentObservaciones != null && !currentObservaciones.isEmpty())
                    ? currentObservaciones + "\n[" + dateTime + "] " + observaciones
                    : "[" + dateTime + "] " + observaciones;

            socia.setObservaciones(newObservaciones);
        }

        socia = sociaRepository.save(socia);
        return mapToResponse(socia);
    }

    @Override
    public byte[] exportToExcel(Map<String, String> parametros) {
        // Extracción de parámetros
        String texto = parametros.get("texto");
        String nombre = parametros.get("nombre");
        String nombreNegocio = parametros.get("nombreNegocio");
        String email = parametros.get("email");
        String telefono = parametros.get("telefono");
        String cif = parametros.get("cif");
        String estadoStr = parametros.get("estado");
        String categoriaStr = parametros.get("categoriaId");

        // Conversión de parámetros
        Boolean activa = null;
        if (estadoStr != null) {
            if (estadoStr.equalsIgnoreCase("true") || estadoStr.equalsIgnoreCase("activa")) {
                activa = true;
            } else if (estadoStr.equalsIgnoreCase("false") || estadoStr.equalsIgnoreCase("baja")) {
                activa = false;
            }
        }

        Integer categoriaId = null;
        if (categoriaStr != null && !categoriaStr.isEmpty()) {
            try {
                categoriaId = Integer.parseInt(categoriaStr);
            } catch (NumberFormatException e) {
            }
        }

        List<Socia> socias;

        if (texto != null && !texto.isEmpty()) {
            // Búsqueda general
            socias = sociaRepository.busquedaGeneral(texto, activa, Pageable.unpaged()).getContent();
        } else {
            // Búsqueda avanzada
            socias = sociaRepository.busquedaAvanzada(
                    nombre, nombreNegocio, email, telefono, cif, categoriaId, activa, Pageable.unpaged()).getContent();
        }

        // Conversión a DTOs para Excel
        List<SociaExcelDto> dtos = socias.stream()
                .map(this::mapToExcelDto)
                .collect(Collectors.toList());

        // Generación del Excel
        return excelHelper.sociasToExcel(dtos);
    }

    // Métodos de mapeo y utilidades
    private SociaResponse mapToResponse(Socia socia) {
        SociaResponse response = new SociaResponse();
        response.setId(socia.getId());
        response.setNumeroSocia(socia.getNumeroSocia());
        response.setNombre(socia.getNombre());
        response.setApellidos(socia.getApellidos());
        response.setNombreNegocio(socia.getNombreNegocio());
        response.setDescripcionNegocio(socia.getDescripcionNegocio());
        response.setDireccion(socia.getDireccion());
        response.setTelefonoPersonal(socia.getTelefonoPersonal());
        response.setTelefonoNegocio(socia.getTelefonoNegocio());
        response.setEmail(socia.getEmail());
        response.setCif(socia.getCif());
        response.setNumeroCuenta(socia.getNumeroCuenta());
        response.setEpigrafe(socia.getEpigrafe());
        response.setActiva(socia.getActiva());

        if (socia.getFechaInicio() != null) {
            response.setFechaInicio(socia.getFechaInicio().toString());
        }

        if (socia.getFechaBaja() != null) {
            response.setFechaBaja(socia.getFechaBaja().toString());
        }

        response.setObservaciones(socia.getObservaciones());

        if (socia.getCategoria() != null) {
            response.setCategoria(new SociaResponse.CategoriaDto(
                socia.getCategoria().getId(),
                socia.getCategoria().getNombre()
            ));
        }

        return response;
    }

    private SociaExcelDto mapToExcelDto(Socia socia) {
        SociaExcelDto dto = new SociaExcelDto();
        dto.setId(socia.getId());
        dto.setNumeroSocia(socia.getNumeroSocia());
        dto.setNombre(socia.getNombre());
        dto.setApellidos(socia.getApellidos());
        dto.setNombreNegocio(socia.getNombreNegocio());
        dto.setDescripcionNegocio(socia.getDescripcionNegocio());

        if (socia.getCategoria() != null) {
            dto.setCategoria(socia.getCategoria().getNombre());
        }

        dto.setDireccion(socia.getDireccion());
        dto.setTelefonoPersonal(socia.getTelefonoPersonal());
        dto.setTelefonoNegocio(socia.getTelefonoNegocio());
        dto.setEmail(socia.getEmail());
        dto.setCif(socia.getCif());
        dto.setNumeroCuenta(socia.getNumeroCuenta());
        dto.setEpigrafe(socia.getEpigrafe());
        dto.setEstado(socia.getActiva() ? "Activa" : "Baja");

        if (socia.getFechaInicio() != null) {
            dto.setFechaInicio(socia.getFechaInicio().format(dateFormatter));
        }

        if (socia.getFechaBaja() != null) {
            dto.setFechaBaja(socia.getFechaBaja().format(dateFormatter));
        }

        dto.setObservaciones(socia.getObservaciones());

        if (socia.getCreatedAt() != null) {
            dto.setFechaCreacion(socia.getCreatedAt().format(dateFormatter));
        }

        if (socia.getUpdatedAt() != null) {
            dto.setFechaActualizacion(socia.getUpdatedAt().format(dateFormatter));
        }

        return dto;
    }
// AÑADIR ESTE CÓDIGO AL FINAL del método updateSociaFromRequest en SociaServiceImpl

private void updateSociaFromRequest(Socia socia, SociaRequest request) {
    try {
        socia.setNumeroSocia(request.getNumeroSocia());
        socia.setNombre(request.getNombre());
        socia.setApellidos(request.getApellidos());
        socia.setNombreNegocio(request.getNombreNegocio());
        socia.setDescripcionNegocio(request.getDescripcionNegocio());
        socia.setDireccion(request.getDireccion());
        socia.setTelefonoPersonal(request.getTelefonoPersonal());
        socia.setTelefonoNegocio(request.getTelefonoNegocio());
        socia.setEmail(request.getEmail());
        socia.setCif(request.getCif());
        socia.setNumeroCuenta(request.getNumeroCuenta());
        socia.setEpigrafe(request.getEpigrafe());
        socia.setActiva(request.getActiva());
        socia.setObservaciones(request.getObservaciones());

        // Manejar fechaInicio (código existente...)
        if (request.getFechaInicio() != null && !request.getFechaInicio().isEmpty()) {
            try {
                String fechaStr = request.getFechaInicio();
                if (fechaStr.endsWith("Z")) {
                    fechaStr = fechaStr.substring(0, fechaStr.length() - 1);
                }
                LocalDateTime fechaInicio = LocalDateTime.parse(fechaStr);
                socia.setFechaInicio(fechaInicio);
                System.out.println("✅ FechaInicio parseada: " + fechaInicio);
            } catch (Exception e) {
                System.err.println("❌ Error parsing fechaInicio: " + e.getMessage());
                socia.setFechaInicio(LocalDateTime.now());
            }
        }

        // Manejar fechaBaja (código existente...)
        if (request.getFechaBaja() != null && !request.getFechaBaja().isEmpty()) {
            try {
                String fechaStr = request.getFechaBaja();
                if (fechaStr.endsWith("Z")) {
                    fechaStr = fechaStr.substring(0, fechaStr.length() - 1);
                }
                LocalDateTime fechaBaja = LocalDateTime.parse(fechaStr);
                socia.setFechaBaja(fechaBaja);
                System.out.println("✅ FechaBaja parseada: " + fechaBaja);
            } catch (Exception e) {
                System.err.println("❌ Error parsing fechaBaja: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ FechaBaja no proporcionada o vacía");
        }

        // Asignar categoría (código existente...)
        if (request.getCategoriaId() != null) {
            CategoriaNegocio categoria = categoriaNegocioRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + request.getCategoriaId()));
            socia.setCategoria(categoria);
        }

        // ✅ AÑADIR ESTO NUEVO - Manejar empresas asociadas
        if (request.getEmpresaIds() != null) {
            // Limpiar empresas actuales
            List<Empresa> empresasActuales = new ArrayList<>(socia.getEmpresas());
            for (Empresa empresa : empresasActuales) {
                empresa.getSocias().remove(socia);
            }
            socia.getEmpresas().clear();

            // Añadir nuevas empresas
            if (!request.getEmpresaIds().isEmpty()) {
                List<Empresa> empresas = empresaRepository.findAllById(request.getEmpresaIds());
                if (empresas.size() != request.getEmpresaIds().size()) {
                    throw new EntityNotFoundException("Una o más empresas no fueron encontradas");
                }
                
                for (Empresa empresa : empresas) {
                    socia.getEmpresas().add(empresa);
                    empresa.getSocias().add(socia);
                }
            }
        }
        
    } catch (Exception e) {
        System.err.println("❌ Error en updateSociaFromRequest: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}
private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }

    private PaginatedResponse<SociaResponse> createPaginatedResponse(
            List<SociaResponse> content, Page<Socia> page) {
        return new PaginatedResponse<>(
            content,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
    }
}