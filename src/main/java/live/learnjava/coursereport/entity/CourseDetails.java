package live.learnjava.coursereport.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="COURSE_DETAILS")
@Builder
public class CourseDetails {
	
	@Id
	@Column(name="course_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer courseId;
	
	@Column(name="course_name", length=50)
	private String courseName;
	
	@Column(name="course_category", length=50)
	private String courseCategory;
	
	@Column(name="faculty_name", length=50)
	private String facultyName;
	
	@Column(name="mode_of_training", length=50)
	private String modeOfTraining;
	
	@Column(name="location", length=50)
	private String location;
	
	@Column(name="start_date")
	private LocalDateTime startDate;
	
	@Column(name="course_fee", length=50)
	private Double courseFee;
	
	@Column(name="admin_contact")
	private Long adminContact;
	
	@Column(name="course_status", length=50)
	private String courseStatus;
		
	@CreationTimestamp
	@Column(name="created_at", insertable = true, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name="created_by", length=50)
	private String createdBy;
		
	@UpdateTimestamp
	@Column(name="upated_at", updatable = true, insertable = false)
	private LocalDateTime updatedAt;
	
	@Column(name="updated_by", length=50)
	private String updatedBy;
}
