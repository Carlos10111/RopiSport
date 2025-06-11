package com.ropisport.gestion.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.model.dto.request.PagoDetalleRequest;
import com.ropisport.gestion.model.dto.request.PagoRequest;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.dto.response.PagoDetalleResponse;
import com.ropisport.gestion.model.dto.response.PagoResponse;
import com.ropisport.gestion.model.entity.Pago;
import com.ropisport.gestion.model.entity.PagoDetalle;
import com.ropisport.gestion.model.entity.Socia;
import com.ropisport.gestion.repository.PagoRepository;
import com.ropisport.gestion.repository.SociaRepository;
import com.ropisport.gestion.service.PagoService;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private SociaRepository sociaRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> getAllPagos(Pageable pageable) {
        return pagoRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse getPagoById(Integer id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));
        return mapToResponse(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> getPagosBySociaId(Integer sociaId) {
        return pagoRepository.findBySociaId(sociaId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> getPagosByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return pagoRepository.findByFechaPagoBetween(inicio, fin).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> getPagosByConfirmado(Boolean confirmado) {
        return pagoRepository.findByConfirmado(confirmado).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PagoResponse createPago(PagoRequest pagoRequest) {
        Socia socia = sociaRepository.findById(pagoRequest.getSociaId())
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + pagoRequest.getSociaId()));

        Pago pago = new Pago();
        pago.setSocia(socia);
        pago.setMonto(pagoRequest.getMonto());
        pago.setFechaPago(pagoRequest.getFechaPago());
        pago.setConcepto(pagoRequest.getConcepto());
        pago.setMetodoPago(pagoRequest.getMetodoPago());
        pago.setConfirmado(pagoRequest.getConfirmado() != null ? pagoRequest.getConfirmado() : false);

        if (pagoRequest.getDetalles() != null && !pagoRequest.getDetalles().isEmpty()) {
            for (PagoDetalleRequest detalleRequest : pagoRequest.getDetalles()) {
                PagoDetalle detalle = new PagoDetalle();
                detalle.setConcepto(detalleRequest.getConcepto());
                detalle.setMonto(detalleRequest.getMonto());
                detalle.setFechaDetalle(detalleRequest.getFechaDetalle() != null ?
                        detalleRequest.getFechaDetalle() : LocalDateTime.now());
                detalle.setNotas(detalleRequest.getNotas());
                pago.addDetalle(detalle);
            }
        }

        Pago savedPago = pagoRepository.save(pago);
        return mapToResponse(savedPago);
    }

    @Override
    @Transactional
    public PagoResponse updatePago(Integer id, PagoRequest pagoRequest) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));

        if (!pago.getSocia().getId().equals(pagoRequest.getSociaId())) {
            throw new IllegalStateException("No se puede cambiar la socia asociada a un pago");
        }

        pago.setMonto(pagoRequest.getMonto());
        pago.setFechaPago(pagoRequest.getFechaPago());
        pago.setConcepto(pagoRequest.getConcepto());
        pago.setMetodoPago(pagoRequest.getMetodoPago());
        pago.setConfirmado(pagoRequest.getConfirmado() != null ? pagoRequest.getConfirmado() : pago.getConfirmado());

    
        pago.getDetalles().clear();

        if (pagoRequest.getDetalles() != null && !pagoRequest.getDetalles().isEmpty()) {
            for (PagoDetalleRequest detalleRequest : pagoRequest.getDetalles()) {
                PagoDetalle detalle = new PagoDetalle();
                detalle.setConcepto(detalleRequest.getConcepto());
                detalle.setMonto(detalleRequest.getMonto());
                detalle.setFechaDetalle(detalleRequest.getFechaDetalle() != null ?
                        detalleRequest.getFechaDetalle() : LocalDateTime.now());
                detalle.setNotas(detalleRequest.getNotas());
                pago.addDetalle(detalle);
            }
        }

        Pago updatedPago = pagoRepository.save(pago);
        return mapToResponse(updatedPago);
    }

    @Override
    @Transactional
    public void deletePago(Integer id) {
        if (!pagoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pago no encontrado con ID: " + id);
        }
        pagoRepository.deleteById(id);
    }

    private PagoResponse mapToResponse(Pago pago) {
        List<PagoDetalleResponse> detallesResponse = new ArrayList<>();

        if (pago.getDetalles() != null) {
            detallesResponse = pago.getDetalles().stream()
                    .map(this::mapDetalleToResponse)
                    .collect(Collectors.toList());
        }

        return PagoResponse.builder()
                .id(pago.getId())
                .sociaId(pago.getSocia().getId())
                .nombreSocia(pago.getSocia().getNombre() + " " + pago.getSocia().getApellidos())
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .concepto(pago.getConcepto())
                .metodoPago(pago.getMetodoPago())
                .confirmado(pago.getConfirmado())
                .detalles(detallesResponse)
                .createdAt(pago.getCreatedAt())
                .updatedAt(pago.getUpdatedAt())
                .build();
    }

    private PagoDetalleResponse mapDetalleToResponse(PagoDetalle detalle) {
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
    public PaginatedResponse<PagoResponse> busquedaGeneral(
            String texto, Boolean confirmado, int page, int size, String sort) {
        
        Pageable pageable = createPageable(page, size, sort);
        Page<Pago> pagos = pagoRepository.busquedaGeneral(texto, confirmado, pageable);
        
        List<PagoResponse> content = pagos.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return createPaginatedResponse(content, pagos);
    }

    @Override
    public PaginatedResponse<PagoResponse> busquedaAvanzada(
            String concepto, 
            String referencia,  
            Integer sociaId, 
            BigDecimal importeMin, 
            BigDecimal importeMax, 
            Boolean confirmado,
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin,
            int page, int size, String sort) {
        
        Pageable pageable = createPageable(page, size, sort);
        Page<Pago> pagos = pagoRepository.busquedaAvanzada(
                concepto, sociaId, importeMin, importeMax, confirmado, 
                fechaInicio, fechaFin, pageable);
        
        List<PagoResponse> content = pagos.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return createPaginatedResponse(content, pagos);
    }

    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }

    private PaginatedResponse<PagoResponse> createPaginatedResponse(
            List<PagoResponse> content, Page<Pago> page) {
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