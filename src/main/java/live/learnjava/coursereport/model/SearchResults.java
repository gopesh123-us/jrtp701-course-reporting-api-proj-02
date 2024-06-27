package live.learnjava.coursereport.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResults {
	
	private Integer courseId;
	
	private String courseName;
	
	private String location;
	
	private String courseCategory;
	
	private String facultyName;
	
	private Double courseFee;
	
	private Long adminContact;
	
	private String modeOfTraining;
	
	private LocalDateTime startDate;
	
	private String courseStatus;

}
