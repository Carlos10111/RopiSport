package com.ropisport.gestion.service.impl;

import com.ropisport.gestion.model.dto.excel.EmpresaExcelDto;
import com.ropisport.gestion.model.dto.excel.PagoExcelDto;
import com.ropisport.gestion.model.dto.excel.SociaExcelDto;
import com.ropisport.gestion.model.entity.Empresa;
import com.ropisport.gestion.model.entity.Institucion;
import com.ropisport.gestion.model.entity.Pago;
import com.ropisport.gestion.model.entity.Socia;
import com.ropisport.gestion.repository.EmpresaRepository;
import com.ropisport.gestion.repository.InstitucionRepository;
import com.ropisport.gestion.repository.PagoRepository;
import com.ropisport.gestion.repository.SociaRepository;
import com.ropisport.gestion.service.ExcelExportService;
import com.ropisport.gestion.util.ExcelHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired
    private SociaRepository sociaRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private InstitucionRepository institucionRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void exportSociasToExcel(OutputStream outputStream) throws IOException {
        List<Socia> socias = sociaRepository.findAll();
        List<SociaExcelDto> sociasDto = socias.stream()
                .map(this::convertToSociaDto)
                .collect(Collectors.toList());
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Socias");
            
            // Crear estilos
            CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
            
            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Número", "Nombre", "Apellidos", "Usuario", "Negocio", 
                               "Categoría", "Teléfono Personal", "Teléfono Negocio", "Email", 
                               "CIF", "Activa", "Fecha Inicio", "Fecha Baja"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            int rowNum = 1;
            for (SociaExcelDto socia : sociasDto) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(socia.getId());
                row.createCell(1).setCellValue(socia.getNumeroSocia());
                row.createCell(2).setCellValue(socia.getNombre());
                row.createCell(3).setCellValue(socia.getApellidos());
                row.createCell(4).setCellValue(socia.getUsername());
                row.createCell(5).setCellValue(socia.getNombreNegocio());
                row.createCell(6).setCellValue(socia.getNombreCategoria());
                row.createCell(7).setCellValue(socia.getTelefonoPersonal());
                row.createCell(8).setCellValue(socia.getTelefonoNegocio());
                row.createCell(9).setCellValue(socia.getEmail());
                row.createCell(10).setCellValue(socia.getCif());
                row.createCell(11).setCellValue(socia.getActiva() ? "Sí" : "No");
                row.createCell(12).setCellValue(socia.getFechaInicio() != null ? 
                        socia.getFechaInicio().format(DATE_FORMATTER) : "");
                row.createCell(13).setCellValue(socia.getFechaBaja() != null ? 
                        socia.getFechaBaja().format(DATE_FORMATTER) : "");
            }
            
            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
        }
    }

    @Override
    public void exportEmpresasToExcel(OutputStream outputStream) throws IOException {
        List<Empresa> empresas = empresaRepository.findAll();
        List<EmpresaExcelDto> empresasDto = empresas.stream()
                .map(this::convertToEmpresaDto)
                .collect(Collectors.toList());
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Empresas");
            
            // Crear estilos
            CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
            
            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Socia", "Nombre Negocio", "Categoría", "Teléfono", 
                               "Email", "CIF", "Web", "Instagram", "Facebook", "LinkedIn"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            int rowNum = 1;
            for (EmpresaExcelDto empresa : empresasDto) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(empresa.getId());
                row.createCell(1).setCellValue(empresa.getNombreSocia());
                row.createCell(2).setCellValue(empresa.getNombreNegocio());
                row.createCell(3).setCellValue(empresa.getNombreCategoria());
                row.createCell(4).setCellValue(empresa.getTelefonoNegocio());
                row.createCell(5).setCellValue(empresa.getEmailNegocio());
                row.createCell(6).setCellValue(empresa.getCif());
                row.createCell(7).setCellValue(empresa.getWeb());
                row.createCell(8).setCellValue(empresa.getInstagram());
                row.createCell(9).setCellValue(empresa.getFacebook());
                row.createCell(10).setCellValue(empresa.getLinkedin());
            }
            
            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
        }
    }

    @Override
    public void exportPagosToExcel(OutputStream outputStream) throws IOException {
        List<Pago> pagos = pagoRepository.findAll();
        List<PagoExcelDto> pagosDto = pagos.stream()
                .map(this::convertToPagoDto)
                .collect(Collectors.toList());
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pagos");
            
            // Crear estilos
            CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
            
            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Socia", "Monto", "Fecha Pago", "Concepto", 
                               "Método de Pago", "Confirmado"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            int rowNum = 1;
            for (PagoExcelDto pago : pagosDto) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(pago.getId());
                row.createCell(1).setCellValue(pago.getNombreSocia());
                row.createCell(2).setCellValue(pago.getMonto().doubleValue());
                row.createCell(3).setCellValue(pago.getFechaPago().format(DATE_FORMATTER));
                row.createCell(4).setCellValue(pago.getConcepto());
                row.createCell(5).setCellValue(pago.getMetodoPago().name());
                row.createCell(6).setCellValue(pago.getConfirmado() ? "Sí" : "No");
            }
            
            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
        }
    }

    @Override
    public void exportInstitucionesToExcel(OutputStream outputStream) throws IOException {
        List<Institucion> instituciones = institucionRepository.findAll();
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Instituciones");
            
            // Crear estilos
            CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
            
            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Nombre Institución", "Tipo", "Persona Contacto", 
                               "Cargo", "Teléfono", "Email", "Web"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            int rowNum = 1;
            for (Institucion institucion : instituciones) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(institucion.getId());
                row.createCell(1).setCellValue(institucion.getNombreInstitucion());
                row.createCell(2).setCellValue(institucion.getTipoInstitucion() != null ? 
                        institucion.getTipoInstitucion().getNombre() : "");
                row.createCell(3).setCellValue(institucion.getPersonaContacto());
                row.createCell(4).setCellValue(institucion.getCargo());
                row.createCell(5).setCellValue(institucion.getTelefono());
                row.createCell(6).setCellValue(institucion.getEmail());
                row.createCell(7).setCellValue(institucion.getWeb());
            }
            
            // Autoajustar columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
        }
    }
    
    private SociaExcelDto convertToSociaDto(Socia socia) {
        SociaExcelDto dto = new SociaExcelDto();
        dto.setId(socia.getId());
        dto.setNumeroSocia(socia.getNumeroSocia());
        dto.setNombre(socia.getNombre());
        dto.setApellidos(socia.getApellidos());
        dto.setUsername(socia.getUsuario() != null ? socia.getUsuario().getUsername() : "");
        dto.setNombreNegocio(socia.getNombreNegocio());
        dto.setNombreCategoria(socia.getCategoria() != null ? socia.getCategoria().getNombre() : "");
        dto.setTelefonoPersonal(socia.getTelefonoPersonal());
        dto.setTelefonoNegocio(socia.getTelefonoNegocio());
        dto.setEmail(socia.getEmail());
        dto.setCif(socia.getCif());
        dto.setActiva(socia.getActiva());
        dto.setFechaInicio(socia.getFechaInicio());
        dto.setFechaBaja(socia.getFechaBaja());
        return dto;
    }
    
    private EmpresaExcelDto convertToEmpresaDto(Empresa empresa) {
        EmpresaExcelDto dto = new EmpresaExcelDto();
        dto.setId(empresa.getId());
        dto.setNombreSocia(empresa.getSocia().getNombre() + " " + empresa.getSocia().getApellidos());
        dto.setNombreNegocio(empresa.getNombreNegocio());
        dto.setNombreCategoria(empresa.getCategoria() != null ? empresa.getCategoria().getNombre() : "");
        dto.setTelefonoNegocio(empresa.getTelefonoNegocio());
        dto.setEmailNegocio(empresa.getEmailNegocio());
        dto.setCif(empresa.getCif());
        dto.setWeb(empresa.getWeb());
        dto.setInstagram(empresa.getInstagram());
        dto.setFacebook(empresa.getFacebook());
        dto.setLinkedin(empresa.getLinkedin());
        return dto;
    }
    
    private PagoExcelDto convertToPagoDto(Pago pago) {
        PagoExcelDto dto = new PagoExcelDto();
        dto.setId(pago.getId());
        dto.setNombreSocia(pago.getSocia().getNombre() + " " + pago.getSocia().getApellidos());
        dto.setMonto(pago.getMonto());
        dto.setFechaPago(pago.getFechaPago());
        dto.setConcepto(pago.getConcepto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setConfirmado(pago.getConfirmado());
        return dto;
    }
}