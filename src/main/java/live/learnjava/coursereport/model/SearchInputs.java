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
public class SearchInputs {
	
	private String courseCatetory;
	private String facultyName;
	private String modeOfTraining;
	private LocalDateTime startsOnDate;
}
