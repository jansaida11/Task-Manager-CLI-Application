package projectDAO;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/*
 *This is a perfect javaBean class as it built with all convetions of a javaBean.
 *
 * It is inside a package(jan.project).
 * have a public no arg constructor.
 * have private inst variables with full names.
 * have setters and getters with proper naming conventions.
 * No business logic, only acts as a data holder.
 * implemented Serializable(i) for future object writing and reading.
 * For convinience @overrided hashcode(),toString() and equals() as my TaskBean is added to collections.
 */

public class TaskBean implements Serializable {
	private String name;
	private String description;
	private Date plannedDate;
	private String tags;
	private int priority;
	private Date creationTime;
	
	public TaskBean() {
		//Empty constructor
	}
	
	public TaskBean(String name, String description, Date plannedDate, int priority, String tags,Date creationTime) {
		super();
		this.name = name;
		this.plannedDate = plannedDate;
		this.tags = tags;
		this.priority = priority;
		this.creationTime = creationTime;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creationTime, description, name, plannedDate, priority, tags);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskBean other = (TaskBean) obj;
		return Objects.equals(creationTime, other.creationTime) && Objects.equals(description, other.description)
				&& Objects.equals(name, other.name) && Objects.equals(plannedDate, other.plannedDate)
				&& priority == other.priority && Objects.equals(tags, other.tags);
	}

	@Override
	public String toString() {
		return "TaskBean [name=" + name + ", description=" + description + ", plannedDate=" + plannedDate + ", tags="
				+ tags + ", priority=" + priority + ", creationTime=" + creationTime + "]";
	}
	
	
		
}
