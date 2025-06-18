// ========= File: ExcelExportServiceImpl.java =========
package com.ropisport.gestion.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Autowired private SociaRepository sociaRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private InstitucionRepository institucionRepository;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /* ───────────────────── SOCIAS ───────────────────── */
    @Override
    public void exportSociasToExcel(OutputStream outputStream) throws IOException {
        List<SociaExcelDto> sociasDto = sociaRepository.findAll().stream()
                .map(this::convertToSociaDto)
                .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Socias");
            CellStyle headerStyle = ExcelHelper.createDateStyle(workbook);

            String[] columns = {"ID","Número","Nombre","Apellidos","Nombre Negocio",
                    "Categoría","Teléfono Personal","Teléfono Negocio","Email",
                    "CIF","Estado","Fecha Inicio","Fecha Baja","Observaciones"};
            createHeader(sheet, headerStyle, columns);

            int rowNum = 1;
            for (SociaExcelDto s : sociasDto) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(defaultLong(s.getId()));
                row.createCell(1).setCellValue(defaultString(s.getNumeroSocia()));
                row.createCell(2).setCellValue(defaultString(s.getNombre()));
                row.createCell(3).setCellValue(defaultString(s.getApellidos()));
                row.createCell(4).setCellValue(defaultString(s.getNombreNegocio()));
                row.createCell(5).setCellValue(defaultString(s.getCategoria()));
                row.createCell(6).setCellValue(defaultString(s.getTelefonoPersonal()));
                row.createCell(7).setCellValue(defaultString(s.getTelefonoNegocio()));
                row.createCell(8).setCellValue(defaultString(s.getEmail()));
                row.createCell(9).setCellValue(defaultString(s.getCif()));
                row.createCell(10).setCellValue(defaultString(s.getEstado()));
                row.createCell(11).setCellValue(defaultString(s.getFechaInicio()));
                row.createCell(12).setCellValue(defaultString(s.getFechaBaja()));
                row.createCell(13).setCellValue(defaultString(s.getObservaciones()));
            }
            autoSize(sheet, columns.length);
            workbook.write(outputStream);
        }
    }

    /* ───────────────────── EMPRESAS ───────────────────── */
    @Override
    public void exportEmpresasToExcel(OutputStream outputStream) throws IOException {
        List<EmpresaExcelDto> empresasDto = empresaRepository.findAll().stream()
                .map(this::convertToEmpresaDto)
                .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Empresas");
            CellStyle headerStyle = ExcelHelper.createDateStyle(workbook);
            String[] columns = {"ID","Socia","Nombre Negocio","Categoría","Teléfono",
                    "Email","CIF","Web","Instagram","Facebook","LinkedIn"};
            createHeader(sheet, headerStyle, columns);

            int rowNum = 1;
            for (EmpresaExcelDto e : empresasDto) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(e.getId());
                row.createCell(1).setCellValue(e.getNombreSocia());
                row.createCell(2).setCellValue(e.getNombreNegocio());
                row.createCell(3).setCellValue(e.getNombreCategoria());
                row.createCell(4).setCellValue(e.getTelefonoNegocio());
                row.createCell(5).setCellValue(e.getEmailNegocio());
                row.createCell(6).setCellValue(e.getCif());
                row.createCell(7).setCellValue(e.getWeb());
                row.createCell(8).setCellValue(e.getInstagram());
                row.createCell(9).setCellValue(e.getFacebook());
                row.createCell(10).setCellValue(e.getLinkedin());
            }
            autoSize(sheet, columns.length);
            workbook.write(outputStream);
        }
    }

    /* ───────────────────── PAGOS ───────────────────── */
    @Override
    public void exportPagosToExcel(OutputStream outputStream) throws IOException {
        List<PagoExcelDto> pagosDto = pagoRepository.findAll().stream()
                .map(this::convertToPagoDto)
                .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pagos");
            CellStyle headerStyle = ExcelHelper.createDateStyle(workbook);
            String[] columns = {"ID","Socia","Monto","Fecha Pago","Concepto",
                    "Método de Pago","Confirmado"};
            createHeader(sheet, headerStyle, columns);

            int rowNum = 1;
            for (PagoExcelDto p : pagosDto) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNombreSocia());
                row.createCell(2).setCellValue(p.getMonto().doubleValue());
                row.createCell(3).setCellValue(p.getFechaPago().format(DATE_FORMATTER));
                row.createCell(4).setCellValue(p.getConcepto());
                row.createCell(5).setCellValue(p.getMetodoPago().name());
                row.createCell(6).setCellValue(p.getConfirmado() ? "Sí" : "No");
            }
            autoSize(sheet, columns.length);
            workbook.write(outputStream);
        }
    }

    /* ─────────────────── INSTITUCIONES ─────────────────── */
    @Override
    public void exportInstitucionesToExcel(OutputStream outputStream) throws IOException {
        List<Institucion> instituciones = institucionRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Instituciones");
            CellStyle headerStyle = ExcelHelper.createDateStyle(workbook);
            String[] columns = {"ID","Nombre Institución","Tipo","Persona Contacto",
                    "Cargo","Teléfono","Email","Web"};
            createHeader(sheet, headerStyle, columns);
            int rowNum = 1;
            for (Institucion i : instituciones) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(i.getId());
                row.createCell(1).setCellValue(i.getNombreInstitucion());
                row.createCell(2).setCellValue(i.getTipoInstitucion()!=null? i.getTipoInstitucion().getNombre():"");
                row.createCell(3).setCellValue(i.getPersonaContacto());
                row.createCell(4).setCellValue(i.getCargo());
                row.createCell(5).setCellValue(i.getTelefono());
                row.createCell(6).setCellValue(i.getEmail());
                row.createCell(7).setCellValue(i.getWeb());
            }
            autoSize(sheet, columns.length);
            workbook.write(outputStream);
        }
    }

    /* ─────────────────── helpers ─────────────────── */
    private static void createHeader(Sheet sheet, CellStyle style, String[] columns) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(style);
        }
    }

    private static void autoSize(Sheet sheet, int cols) {
        for (int i = 0; i < cols; i++) {
			sheet.autoSizeColumn(i);
		}
    }

    private static long defaultLong(Number n) {
        return n != null ? n.longValue() : 0L;
    }

    private static String defaultString(String v) { return v != null ? v : ""; }

    /* ───────────── DTO mappers ───────────── */
    private SociaExcelDto convertToSociaDto(Socia s) {
        SociaExcelDto dto=new SociaExcelDto();
        dto.setId(s.getId());
        dto.setNumeroSocia(s.getNumeroSocia());
        dto.setNombre(s.getNombre());
        dto.setApellidos(s.getApellidos());
        dto.setNombreNegocio(s.getNombreNegocio());
        dto.setCategoria(s.getCategoria()!=null? s.getCategoria().getNombre():"");
        dto.setTelefonoPersonal(s.getTelefonoPersonal());
        dto.setTelefonoNegocio(s.getTelefonoNegocio());
        dto.setEmail(s.getEmail());
        dto.setCif(s.getCif());
        dto.setEstado(s.getActiva()!=null&&s.getActiva()?"Activa":"Baja");
        dto.setFechaInicio(s.getFechaInicio()!=null? s.getFechaInicio().format(DATE_FORMATTER):"");
        dto.setFechaBaja(s.getFechaBaja()!=null? s.getFechaBaja().format(DATE_FORMATTER):"");
        dto.setObservaciones(s.getObservaciones());
        return dto;
    }

    private EmpresaExcelDto convertToEmpresaDto(Empresa e) {
        EmpresaExcelDto dto=new EmpresaExcelDto();
        dto.setId(e.getId());
        dto.setNombreSocia(((SociaExcelDto) e.getSocias()).getNombre()+" "+((SociaExcelDto) e.getSocias()).getApellidos());
        dto.setNombreNegocio(e.getNombreNegocio());
        dto.setNombreCategoria(e.getCategoria()!=null? e.getCategoria().getNombre():"");
        dto.setTelefonoNegocio(e.getTelefonoNegocio());
        dto.setEmailNegocio(e.getEmailNegocio());
        dto.setCif(e.getCif());
        dto.setWeb(e.getWeb());
        dto.setInstagram(e.getInstagram());
        dto.setFacebook(e.getFacebook());
        dto.setLinkedin(e.getLinkedin());
        return dto;
    }

    private PagoExcelDto convertToPagoDto(Pago p) {
        PagoExcelDto dto=new PagoExcelDto();
        dto.setId(p.getId());
        dto.setNombreSocia(p.getSocia().getNombre()+" "+p.getSocia().getApellidos());
        dto.setMonto(p.getMonto());
        dto.setFechaPago(p.getFechaPago());
        dto.setConcepto(p.getConcepto());
        dto.setMetodoPago(p.getMetodoPago());
        dto.setConfirmado(p.getConfirmado());
        return dto;
    }
}
