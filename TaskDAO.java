package projectDAO;

import java.util.List;

public interface TaskDAO {
	
	boolean doesCategoryExists(String catName);
	
	String createCategory(String categoryName);
	
	String createTask(TaskBean task,String catName);
	
	boolean doesTaskExists(String name,String catName) throws TaskException;
	
	String updatingTask(TaskBean task, String catName, String oldTskName);
	
	String deletingTask(String catName, String TaskName);
	
	String deleteCategory(String catName);
	
	List<List<TaskBean>> listAll() throws TaskException;
	
	List<TaskBean> taskList(String category) throws TaskException;
	
	List<List<TaskBean>> searchAll(String words) throws TaskException;
	
	List<TaskBean> search(String categoryName, String words) throws TaskException;
	
	
}
