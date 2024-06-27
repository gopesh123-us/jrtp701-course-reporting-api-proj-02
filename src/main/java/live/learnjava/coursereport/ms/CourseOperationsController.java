package live.learnjava.coursereport.ms;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import live.learnjava.coursereport.model.SearchInputs;
import live.learnjava.coursereport.model.SearchResults;
import live.learnjava.coursereport.service.ICourseManagementService;

@RestController
@RequestMapping("/reporting/api")
@OpenAPIDefinition (
		info = @Info(
				title = "Courses Reports API",
				version = "1.0",
				description = "Course Reporting API supporting file download options in Excel and PDF formats",
				license = @License(name="Gopesh Sharma", url="https://www.acmesoftware.com"),
				contact = @Contact(url="http://www.acmesoftwaregt.com", name="Gopesh Sharma", email="gopesh.sharma@gmail.com")
))
public class CourseOperationsController {

	@Autowired
	ICourseManagementService service;

	@GetMapping("/allcategories")
	public ResponseEntity<?> getUniqueCourseCategories() {
		// use service
		try {
			Set<String> uniqueCategories = service.getAllCourseCategories();
			return new ResponseEntity<Set<String>>(uniqueCategories, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Problem getting course categories", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/allfaculties")
	public ResponseEntity<?> getUniqueFacultyNames() {
		// use service
		try {

			Set<String> allFacultyMembers = service.getAllFacultyNames();
			return new ResponseEntity<Set<String>>(allFacultyMembers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Problem getting faculty members.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/allmodesOfTraining")
	public ResponseEntity<?> getUniqueTrainingModes() {
		// use service
		try {

			Set<String> allUniqueTrainingMode = service.getAllModesOfTraining();
			return new ResponseEntity<Set<String>>(allUniqueTrainingMode, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Problem getting Modes of Trainings", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/searchByFilters")
	public ResponseEntity<?> search(@RequestBody SearchInputs inputs) {
		// use service
		try {

			List<SearchResults> filteredSearch = service.showCoursesByFilters(inputs);
			return new ResponseEntity<List<SearchResults>>(filteredSearch, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Problem getting courses based on search criteria",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/downloadExcel")
	public ResponseEntity<?> generateExcel(@RequestBody SearchInputs inputs) {

		String fileName = "courseDetails.xlsx";

		ByteArrayInputStream excelData = null;

		try {
			excelData = service.generateExcelReport(inputs, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		InputStreamResource file = new InputStreamResource(excelData);
		ResponseEntity<InputStreamResource> body = ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
		return body;
	}

	@GetMapping("/downloadPdf")
	public ResponseEntity<?> generatePdf(@RequestBody SearchInputs inputs) {
		String pdfFileName = "courseDetails.pdf";
		ByteArrayInputStream pdfDataInputStream = null;
		try {
			pdfDataInputStream = service.generatePdfReport(inputs, pdfFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		InputStreamResource pdfFileResource = new InputStreamResource(pdfDataInputStream);

		ResponseEntity<InputStreamResource> body = ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + pdfFileName)
				.contentType(MediaType.APPLICATION_PDF).body(pdfFileResource);

		return body;
	}

}
