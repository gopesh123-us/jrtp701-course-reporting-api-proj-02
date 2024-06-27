package live.learnjava.coursereport.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import live.learnjava.coursereport.entity.CourseDetails;
import live.learnjava.coursereport.model.SearchInputs;
import live.learnjava.coursereport.model.SearchResults;
import live.learnjava.coursereport.repository.ICourseDetailsRepository;
import live.learnjava.coursereport.utils.ServiceUtils;

@Service
public class CourseManagementServiceImpl implements ICourseManagementService {

	private static String[] headers = { "ID", "Course Name", "Course Category", "Faculty", "Training Mode",
			"Location", "Start Date", "Course Fee", "Admin Contact", "Status" };
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ICourseDetailsRepository courseRepo;

	@Override
	public Set<String> getAllCourseCategories() {
		Set<String> courseCategories = courseRepo.getUniqueCourseCategories();
		return courseCategories;
	}

	@Override
	public Set<String> getAllFacultyNames() {
		Set<String> facultyNames = courseRepo.getUniqueFacultyNames();
		return facultyNames;
	}

	@Override
	public Set<String> getAllModesOfTraining() {
		Set<String> modesOfTrainings = courseRepo.getUniqueModeOfTraining();
		return modesOfTrainings;
	}

	@Override
	public List<SearchResults> showCoursesByFilters(SearchInputs inputs) {
		logger.info("Search Inputs: " + inputs);
		// create a domain object
		CourseDetails courseDetails = new CourseDetails();
		// get data from inputs and populate an domain object based on data from inputs
		// using StringUtils, and ObjectUtils class of Spring Framework
		String category = inputs.getCourseCatetory();
		if (StringUtils.hasLength(category)) {
			courseDetails.setCourseCategory(category);
		}

		String facultyName = inputs.getFacultyName();
		if (StringUtils.hasLength(facultyName)) {
			courseDetails.setFacultyName(facultyName);
		}

		String modeOfTraining = inputs.getModeOfTraining();
		if (StringUtils.hasLength(modeOfTraining)) {
			courseDetails.setModeOfTraining(modeOfTraining);
		}

		LocalDateTime startOnDate = inputs.getStartsOnDate();
		if (!ObjectUtils.isEmpty(startOnDate)) {
			courseDetails.setStartDate(startOnDate);
		}

		// create an example object of the domain object.
		Example<CourseDetails> exampleObj = Example.of(courseDetails);

		// search by example object and get results
		List<CourseDetails> listCourseDetails = courseRepo.findAll(exampleObj);

		// create a new list of SearchResults based by using BeanUtils.copyProperties
		List<SearchResults> listResults = new ArrayList<>();
		listCourseDetails.forEach(course -> {
			SearchResults result = new SearchResults();
			BeanUtils.copyProperties(course, result);
			listResults.add(result);
		});
		// return new list of search results
		return listResults;
	}

	@Override
	public ByteArrayInputStream generatePdfReport(SearchInputs inputs, String pdfFileName) throws IOException {

		List<SearchResults> listSearchResults = showCoursesByFilters(inputs);
		logger.info("Generating PDF report...");

		// return BytInputStream to the client (RestController)
		ByteArrayInputStream in = ServiceUtils.dataToPdf(listSearchResults, CourseManagementServiceImpl.headers,
				pdfFileName);
		return in;
	}

	@Override
	public ByteArrayInputStream generateExcelReport(SearchInputs inputs, String sheetName) throws IOException {
		List<SearchResults> listResults = showCoursesByFilters(inputs);
		logger.info("Generating Excel report...");
		ByteArrayInputStream in = ServiceUtils.dataToExcel(listResults, CourseManagementServiceImpl.headers, sheetName);
		return in;
	}

}
