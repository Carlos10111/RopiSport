package com.ropisport.gestion.service;

import java.io.IOException;
import java.io.OutputStream;

public interface ExcelExportService {
	
	void exportSociasToExcel(OutputStream outputStream) throws IOException;
	void exportEmpresasToExcel(OutputStream outputStream) throws IOException;
	void exportPagosToExcel(OutputStream outputStream) throws IOException;
	void exportInstitucionesToExcel(OutputStream outputStream) throws IOException;
	

}
