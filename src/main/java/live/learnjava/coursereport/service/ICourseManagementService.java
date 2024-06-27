package live.learnjava.coursereport.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletResponse;
import live.learnjava.coursereport.model.SearchInputs;
import live.learnjava.coursereport.model.SearchResults;

public interface ICourseManagementService {

	public Set<String> getAllCourseCategories();

	public Set<String> getAllFacultyNames();

	public Set<String> getAllModesOfTraining();

	public List<SearchResults> showCoursesByFilters(SearchInputs inputs);

	public ByteArrayInputStream generatePdfReport(SearchInputs inputs, String pdfFileName) throws IOException;

	public ByteArrayInputStream generateExcelReport(SearchInputs inputs, String sheetName) throws IOException;
}
