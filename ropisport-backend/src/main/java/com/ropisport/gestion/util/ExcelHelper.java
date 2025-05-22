
// ========= File: ExcelHelper.java =========
package com.ropisport.gestion.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.ropisport.gestion.model.dto.excel.SociaExcelDto;

@Component
public class ExcelHelper {
    /* ───── Estilo de fecha ───── */
    public static CellStyle createDateStyle(Workbook wb){
        CellStyle style=createBorderStyle(wb);
        style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("dd/MM/yyyy HH:mm"));
        return style;
    }

    /* ───── Socias a Excel ───── */
    public byte[] sociasToExcel(List<SociaExcelDto> socias){
        try(Workbook wb=new XSSFWorkbook(); ByteArrayOutputStream out=new ByteArrayOutputStream()){
            Sheet sheet=wb.createSheet("Socias");
            String[] headers={"ID","Número Socia","Nombre","Apellidos","Nombre Negocio","Descripción Negocio","Categoría","Dirección","Teléfono Personal","Teléfono Negocio","Email","CIF","Número Cuenta","Epígrafe","Estado","Fecha Inicio","Fecha Baja","Observaciones","Fecha Creación","Fecha Actualización"};
            createHeader(sheet,createHeaderStyle(wb),headers);
            CellStyle data=createBorderStyle(wb);
            CellStyle activa=createStatusStyle(wb,IndexedColors.LIGHT_GREEN);
            CellStyle baja=createStatusStyle(wb,IndexedColors.ROSE);
            int r=1;
            for(SociaExcelDto s:socias){
                Row row=sheet.createRow(r++);
                setCell(row,0,defaultLong(s.getId()),data);
                setCell(row,1,defaultString(s.getNumeroSocia()),data);
                setCell(row,2,defaultString(s.getNombre()),data);
                setCell(row,3,defaultString(s.getApellidos()),data);
                setCell(row,4,defaultString(s.getNombreNegocio()),data);
                setCell(row,5,defaultString(s.getDescripcionNegocio()),data);
                setCell(row,6,defaultString(s.getCategoria()),data);
                setCell(row,7,defaultString(s.getDireccion()),data);
                setCell(row,8,defaultString(s.getTelefonoPersonal()),data);
                setCell(row,9,defaultString(s.getTelefonoNegocio()),data);
                setCell(row,10,defaultString(s.getEmail()),data);
                setCell(row,11,defaultString(s.getCif()),data);
                setCell(row,12,defaultString(s.getNumeroCuenta()),data);
                setCell(row,13,defaultString(s.getEpigrafe()),data);
                Cell estado=row.createCell(14);estado.setCellValue(defaultString(s.getEstado()));estado.setCellStyle("Activa".equals(s.getEstado())?activa:baja);
                setCell(row,15,defaultString(s.getFechaInicio()),data);
                setCell(row,16,defaultString(s.getFechaBaja()),data);
                setCell(row,17,defaultString(s.getObservaciones()),data);
                setCell(row,18,defaultString(s.getFechaCreacion()),data);
                setCell(row,19,defaultString(s.getFechaActualizacion()),data);
            }
            autoSize(sheet,headers.length);
            wb.write(out);return out.toByteArray();
        }catch(IOException ex){throw new RuntimeException("Error al generar Excel: "+ex.getMessage(),ex);}    }

    /* ───── helpers ───── */
    private static void createHeader(Sheet sheet,CellStyle style,String[] headers){
        Row h=sheet.createRow(0);
        for(int i=0;i<headers.length;i++){Cell c=h.createCell(i);c.setCellValue(headers[i]);c.setCellStyle(style);} }

    private static CellStyle createHeaderStyle(Workbook wb){
        CellStyle s=wb.createCellStyle();Font f=wb.createFont();f.setBold(true);s.setFont(f);s.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());s.setFillPattern(FillPatternType.SOLID_FOREGROUND);return s;}

    private static CellStyle createBorderStyle(Workbook wb){CellStyle s=wb.createCellStyle();s.setBorderBottom(BorderStyle.THIN);s.setBorderTop(BorderStyle.THIN);s.setBorderLeft(BorderStyle.THIN);s.setBorderRight(BorderStyle.THIN);return s;}

    private static CellStyle createStatusStyle(Workbook wb,IndexedColors col){CellStyle s=createBorderStyle(wb);s.setFillForegroundColor(col.getIndex());s.setFillPattern(FillPatternType.SOLID_FOREGROUND);return s;}

    private static void setCell(Row row,int idx,String val,CellStyle st){Cell c=row.createCell(idx);c.setCellValue(val);c.setCellStyle(st);}    private static void setCell(Row row,int idx,long val,CellStyle st){Cell c=row.createCell(idx);c.setCellValue(val);c.setCellStyle(st);}    private static void autoSize(Sheet s,int n){for(int i=0;i<n;i++) {
		s.autoSizeColumn(i);
	}}    private static long defaultLong(Number n){return n!=null?n.longValue():0L;}    private static String defaultString(String v){return v!=null?v:"";}
}
