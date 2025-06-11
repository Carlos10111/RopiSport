package com.ropisport.gestion.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.model.dto.request.PagoDetalleRequest;
import com.ropisport.gestion.model.dto.response.PagoDetalleResponse;
import com.ropisport.gestion.model.entity.Pago;
import com.ropisport.gestion.model.entity.PagoDetalle;
import com.ropisport.gestion.repository.PagoDetalleRepository;
import com.ropisport.gestion.repository.PagoRepository;
import com.ropisport.gestion.service.PagoDetalleService;

import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
@Service
public class PagoDetalleServiceImpl implements PagoDetalleService {

    @Autowired
    private PagoDetalleRepository pagoDetalleRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PagoDetalleResponse> getDetallesByPagoId(Integer pagoId) {
        return pagoDetalleRepository.findByPagoId(pagoId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagoDetalleResponse getDetalleById(Integer id) {
        PagoDetalle detalle = pagoDetalleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de pago no encontrado con ID: " + id));
        return mapToResponse(detalle);
    }

    @Override
    @Transactional
    public PagoDetalleResponse createDetalle(PagoDetalleRequest detalleRequest) {
        Pago pago = pagoRepository.findById(detalleRequest.getPagoId())
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + detalleRequest.getPagoId()));

        PagoDetalle detalle = new PagoDetalle();
        detalle.setPago(pago);
        detalle.setConcepto(detalleRequest.getConcepto());
        detalle.setMonto(detalleRequest.getMonto());
        detalle.setFechaDetalle(detalleRequest.getFechaDetalle() != null ?
                detalleRequest.getFechaDetalle() : LocalDateTime.now());
        detalle.setNotas(detalleRequest.getNotas());

        PagoDetalle savedDetalle = pagoDetalleRepository.save(detalle);
        return mapToResponse(savedDetalle);
    }

    @Override
    @Transactional
    public PagoDetalleResponse updateDetalle(Integer id, PagoDetalleRequest detalleRequest) {
        PagoDetalle detalle = pagoDetalleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de pago no encontrado con ID: " + id));

        // Verificar que el pago asociado no cambie
        if (!detalle.getPago().getId().equals(detalleRequest.getPagoId())) {
            throw new IllegalStateException("No se puede cambiar el pago asociado a un detalle");
        }

        detalle.setConcepto(detalleRequest.getConcepto());
        detalle.setMonto(detalleRequest.getMonto());
        detalle.setFechaDetalle(detalleRequest.getFechaDetalle());
        detalle.setNotas(detalleRequest.getNotas());

        PagoDetalle updatedDetalle = pagoDetalleRepository.save(detalle);
        return mapToResponse(updatedDetalle);
    }

    @Override
    @Transactional
    public void deleteDetalle(Integer id) {
        if (!pagoDetalleRepository.existsById(id)) {
            throw new EntityNotFoundException("Detalle de pago no encontrado con ID: " + id);
        }
        pagoDetalleRepository.deleteById(id);
    }

    private PagoDetalleResponse mapToResponse(PagoDetalle detalle) {
        return PagoDetalleResponse.builder()
                .id(detalle.getId())
                .pagoId(detalle.getPago().getId())
                .concepto(detalle.getConcepto())
                .monto(detalle.getMonto())
                .fechaDetalle(detalle.getFechaDetalle())
                .notas(detalle.getNotas())
                .createdAt(detalle.getCreatedAt())
                .updatedAt(detalle.getUpdatedAt())
                .build();
    }

@Override
public PaginatedResponse<PagoDetalleResponse> busquedaGeneral(
        String texto, Integer pagoId, int page, int size, String sort) {
    
    Pageable pageable = createPageable(page, size, sort);
    Page<PagoDetalle> detalles = pagoDetalleRepository.busquedaGeneral(texto, pagoId, pageable);
    
    List<PagoDetalleResponse> content = detalles.getContent().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    
    return createPaginatedResponse(content, detalles);
}

@Override
public PaginatedResponse<PagoDetalleResponse> busquedaAvanzada(
        String concepto,
        Integer pagoId,
        Integer sociaId,
        BigDecimal montoMin,
        BigDecimal montoMax,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        int page,
        int size,
        String sort) {
    
    Pageable pageable = createPageable(page, size, sort);
    Page<PagoDetalle> detalles = pagoDetalleRepository.busquedaAvanzada(
            concepto, pagoId, sociaId, montoMin, montoMax, fechaInicio, fechaFin, pageable);
    
    List<PagoDetalleResponse> content = detalles.getContent().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    
    return createPaginatedResponse(content, detalles);
}

@Override
public PaginatedResponse<PagoDetalleResponse> buscarPorConcepto(String concepto, int page, int size, String sort) {
    Pageable pageable = createPageable(page, size, sort);
    Page<PagoDetalle> detalles = pagoDetalleRepository.findByConceptoContaining(concepto, pageable);
    
    List<PagoDetalleResponse> content = detalles.getContent().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    
    return createPaginatedResponse(content, detalles);
}

@Override
public PaginatedResponse<PagoDetalleResponse> buscarPorSocia(Integer sociaId, int page, int size, String sort) {
    Pageable pageable = createPageable(page, size, sort);
    Page<PagoDetalle> detalles = pagoDetalleRepository.findBySociaId(sociaId, pageable);
    
    List<PagoDetalleResponse> content = detalles.getContent().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    
    return createPaginatedResponse(content, detalles);
}

@Override
public PaginatedResponse<PagoDetalleResponse> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin, int page, int size, String sort) {
    Pageable pageable = createPageable(page, size, sort);
    Page<PagoDetalle> detalles = pagoDetalleRepository.findByFechaDetalleBetween(fechaInicio, fechaFin, pageable);
    
    List<PagoDetalleResponse> content = detalles.getContent().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    
    return createPaginatedResponse(content, detalles);
}

// Métodos auxiliares
private Pageable createPageable(int page, int size, String sort) {
    String[] sortParams = sort.split(",");
    String sortField = sortParams[0];
    Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC : Sort.Direction.ASC;
    
    return PageRequest.of(page, size, Sort.by(direction, sortField));
}

private PaginatedResponse<PagoDetalleResponse> createPaginatedResponse(
        List<PagoDetalleResponse> content, Page<PagoDetalle> page) {
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