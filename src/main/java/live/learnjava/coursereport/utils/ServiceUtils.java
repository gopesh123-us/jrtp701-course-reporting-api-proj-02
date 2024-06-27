package live.learnjava.coursereport.utils;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import live.learnjava.coursereport.model.SearchResults;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceUtils {

	private Logger logger = LoggerFactory.getLogger(ServiceUtils.class);

	public static ByteArrayInputStream dataToExcel(List<SearchResults> listResults, String[] theHeaders,
			String theSheetName) throws IOException, FileNotFoundException {
		logger.info("inside dataToExcel");
		// get data and worksheet headers

		// create an empty new ByeArrayOutputStream object
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// create workbook
		Workbook workbook = new XSSFWorkbook();
		logger.info("A new workbook created");

		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 13);
		font.setColor(IndexedColors.WHITE.index);
		font.setBold(false);
		style.setFont(font);

		try {
			Sheet sheet = workbook.createSheet(theSheetName);
			Row row = sheet.createRow(0);
			for (int i = 0; i < theHeaders.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(theHeaders[i]);
				cell.setCellStyle(style);
			}

			for (int i = 0; i < listResults.size(); i++) {
				row = sheet.createRow(i+1);
				SearchResults details = listResults.get(i);
				row.createCell(0).setCellValue(details.getCourseId());
				row.createCell(1).setCellValue(details.getCourseName());
				row.createCell(2).setCellValue(details.getCourseCategory());
				row.createCell(3).setCellValue(details.getFacultyName());
				row.createCell(4).setCellValue(details.getModeOfTraining());
				row.createCell(5).setCellValue(details.getLocation());
				row.createCell(6).setCellValue(details.getStartDate());
				row.createCell(7).setCellValue("INR "+details.getCourseFee());
				row.createCell(8).setCellValue("+91-"+details.getAdminContact());
				row.createCell(9).setCellValue(details.getCourseStatus());
			}

			// write workbook to and OutputStream
			workbook.write(out);

			// create a ByteArrayInputStream from the OutputStream
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());

			// return ByteArrayInputStream
			return byteArrayInputStream;

		} catch (

		Exception e) {
			e.printStackTrace();
			System.out.println("Failed to import data in excel");
			return new ByteArrayInputStream(null);
		} finally {
			workbook.close();
			out.close();
		}

	}

	public static ByteArrayInputStream dataToPdf(List<SearchResults> listResults, String[] theHeaders,
			String thePdfFileName) throws IOException {
		logger.info("inside dataToPdf");

		// start and put the document in landscape mode
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate(), 25, 25, 25, 25);

		// create page writer
		PdfWriter writer = PdfWriter.getInstance(document, out);

		// various fonts
		BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
		BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
		BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER, "Cp1252", false);
		BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL, "Cp1252", false);

		// set headers - must be set before the document is opened
		com.lowagie.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
		HeaderFooter header = new HeaderFooter(new Phrase("Courses Search Report", headerFont), false);
		header.setAlignment(Element.ALIGN_CENTER);
		header.setBackgroundColor(Color.ORANGE);
		document.setHeader(header);
		// end set headers

		// now open document
		document.open();

		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 2.0f, 2.5f, 3.0f, 3.0f, 2.0f, 3.0f, 2.0f });
		table.setSpacingBefore(0.5f);
		table.setHorizontalAlignment(100);

		// heading row cell in the pdf table
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.ORANGE);
		cell.setPadding(5);
		com.lowagie.text.Font cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
		cellFont.setColor(Color.WHITE);

		// fill header row in cells
		for (int i = 0; i < theHeaders.length; i++) {
			cell.setPhrase(new Phrase(theHeaders[i], cellFont));
			table.addCell(cell);
		}
		com.lowagie.text.Font dataCellFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
		dataCellFont.setColor(Color.BLACK);
		// add data to cells of table
		listResults.forEach(result -> {
			table.addCell(new PdfPCell(new Phrase(String.valueOf(result.getCourseId()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(result.getCourseName()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(result.getCourseCategory()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(result.getFacultyName()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(result.getModeOfTraining()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(result.getLocation()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(result.getStartDate()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf("INR " + result.getCourseFee()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf("+91-" + result.getAdminContact()), dataCellFont)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(result.getCourseStatus()), dataCellFont)));
		});
		document.add(table);
		document.close();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
		out.close();
		writer.close();
		return inputStream;

		// end

	}
}
