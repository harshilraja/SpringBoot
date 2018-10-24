package com.titstory.heowc.component;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.titstory.heowc.constant.ExcelConstant;
import com.titstory.heowc.domain.KeyClass;
import com.titstory.heowc.domain.KeyClassSub;

public class ExcelWriteComponentKey {

	private Workbook workbook;
	private Map<String, Object> model;
	private HttpServletResponse response;

	public ExcelWriteComponentKey(Workbook workbook, Map<String, Object> model, HttpServletResponse response) {
		this.workbook = workbook;
		this.model = model;
		this.response = response;
	}

	public void create() {
		setFileName(response, mapToFileName());

		Sheet sheet = workbook.createSheet("Success Teams");
		sheet.setDefaultColumnWidth(15);
		
        createRowHeader(sheet,0,getHeaderStyle(workbook),"Know Your Customer");
        createRowHeader(sheet,1,getHeaderStyle(workbook),"April 23, 2018");
        createRowHeader(sheet,2,getHeaderStyle(workbook),"130 Edward Jones Blvd.");
        createRowHeader(sheet,3,getHeaderStyle(workbook),"St.Louis, MO");
       	createHead(sheet, 5, mapToHeadList(),getHeaderStyle(workbook),4);

       	createBody(sheet, 6,mapToBodyList(),4);
       	
       	int rowSubStarts = 8+ mapToBodyList().size();

       	createHead(sheet, rowSubStarts, mapToSubHeadList(),getHeaderStyle(workbook),0);
	
    	createBodySub(sheet, rowSubStarts,mapToBodySubList(),0);
    	
    	
	}
	
	private CellStyle getHeaderStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.BLACK.index);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
	}
	
	private CellStyle getSubHeaderStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        return style;
	}

	private String mapToFileName() {
		return (String) model.get(ExcelConstant.FILE_NAME);
	}

	private List<String> mapToHeadList() {
		return (List<String>) model.get(ExcelConstant.HEAD);
	}
	
	private List<String> mapToSubHeadList() {
		return (List<String>) model.get(ExcelConstant.SUB_HEAD);
	}

	private List<KeyClass> mapToBodyList() {
		return (List<KeyClass>) model.get(ExcelConstant.BODY);
	}
	
	private List<KeyClassSub> mapToBodySubList() {
		return (List<KeyClassSub>) model.get(ExcelConstant.SUB_BODY);
	}

	private void setFileName(HttpServletResponse response, String fileName) {
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + getFileExtension(fileName) + "\"");
	}

	private String getFileExtension(String fileName) {
		if (workbook instanceof XSSFWorkbook) {
			fileName += ".xlsx";
		}
		if (workbook instanceof SXSSFWorkbook) {
			fileName += ".xlsx";
		}
		if (workbook instanceof HSSFWorkbook) {
			fileName += ".xls";
		}

		return fileName;
	}

	private void createHead(Sheet sheet, int rowNum ,List<String> headList,CellStyle style, int columnStart) {
		createRow(sheet, headList, rowNum,style,columnStart);
	}

	private void createBody(Sheet sheet, int rowNum,List<KeyClass> bodyList,int columnStart) {
		int i=rowNum;
		for(KeyClass keySub:bodyList) {
			createRow(sheet, keySub, i + 1,columnStart);
			i++;
		}
	}
	
	private void createBodySub(Sheet sheet, int rowNum,List<KeyClassSub> bodyList,int columnStart) {
		int i=rowNum;
		int size = rowNum+bodyList.size();
		for(KeyClassSub keySub:bodyList) {
			createRow(sheet, keySub, i + 1,columnStart);
			i++;
		}
		
		CellRangeAddress ca =
			    new CellRangeAddress(rowNum, size,
			                 0,
			                 10);
		sheet.setAutoFilter(ca);
	}
	private void createRowHeader(Sheet sheet, int rowNum ,CellStyle style,String cellValue) {
		Row row = sheet.createRow(rowNum);
		row.createCell(3).setCellValue(cellValue);
		row.getCell(3).setCellStyle(style);
		/*row.createCell(4).setCellValue("");
		row.getCell(4).setCellStyle(style);
		row.createCell(5).setCellValue(cellValue);
		row.getCell(5).setCellStyle(style);
		row.createCell(6).setCellValue("");
		row.getCell(6).setCellStyle(style);
		row.createCell(7).setCellValue("");
		row.getCell(7).setCellStyle(style);
		row.createCell(8).setCellValue("");
		row.getCell(8).setCellStyle(style);
		row.createCell(9).setCellValue("");
		row.getCell(9).setCellStyle(style);*/
		setMerge(sheet,rowNum,rowNum,3,9,false);
	}
	private void createRow(Sheet sheet, List<String> cellList, int rowNum,CellStyle style,int columnStart) {
		int size = cellList.size();
		Row row = sheet.createRow(rowNum);
		for (int i = 0; i < size; i++) {
			row.createCell(columnStart).setCellValue(cellList.get(i));
			row.getCell(columnStart).setCellStyle(style);
			columnStart++;
		}
	}
	private void createRow(Sheet sheet, KeyClass keyClassSub, int rowNum,int columnstart) {
		
		Row row = sheet.createRow(rowNum);
		row.createCell(columnstart).setCellValue(keyClassSub.getRm()+rowNum);
		row.createCell(columnstart+1).setCellValue(keyClassSub.getPrimaryBtt()+rowNum);
		row.createCell(columnstart+2).setCellValue(keyClassSub.getPartnerBtt()+rowNum);
		row.createCell(columnstart+3).setCellValue(keyClassSub.getVisitingVeteran()+rowNum);
		row.createCell(columnstart+4).setCellValue(rowNum);
		
	}
	
private void createRow(Sheet sheet, KeyClassSub keyClassSub, int rowNum,int columnstart) {
		
		Row row = sheet.createRow(rowNum);
		row.createCell(columnstart).setCellValue(keyClassSub.getEid()+rowNum);
		row.createCell(columnstart+1).setCellValue(keyClassSub.getFa()+rowNum);
		row.createCell(columnstart+2).setCellValue(keyClassSub.getPj()+rowNum);
		row.createCell(columnstart+3).setCellValue(keyClassSub.getRm()+rowNum);
		row.createCell(columnstart+4).setCellValue(keyClassSub.getFirstName()+rowNum);
		row.createCell(columnstart+5).setCellValue(keyClassSub.getLastName()+rowNum);
		row.createCell(columnstart+6).setCellValue(keyClassSub.getSt()+rowNum);
		row.createCell(columnstart+7).setCellValue(keyClassSub.getArea()+rowNum);
		row.createCell(columnstart+8).setCellValue(keyClassSub.getRegion()+rowNum);
		row.createCell(columnstart+9).setCellValue(keyClassSub.getStatus()+rowNum);
		row.createCell(columnstart+10).setCellValue(keyClassSub.getKycBtt()+rowNum);
	}
	
	protected void setMerge(Sheet sheet, int numRow, int untilRow, int numCol, int untilCol, boolean border) {
	    CellRangeAddress cellMerge = new CellRangeAddress(numRow, untilRow, numCol, untilCol);
	    sheet.addMergedRegion(cellMerge);
	    if (border) {
	        setBordersToMergedCells(sheet, cellMerge);
	    }
	}
	
	protected void setBordersToMergedCells(Sheet sheet, CellRangeAddress rangeAddress) {
	    RegionUtil.setBorderTop(BorderStyle.MEDIUM, rangeAddress, sheet);
	    RegionUtil.setBorderLeft(BorderStyle.MEDIUM, rangeAddress, sheet);
	    RegionUtil.setBorderRight(BorderStyle.MEDIUM, rangeAddress, sheet);
	    RegionUtil.setBorderBottom(BorderStyle.MEDIUM, rangeAddress, sheet);
	}
}