package projectDAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskModel {

	private static Logger l = Logger.getInstance();// will use this Logger ref throughout this class.

	// checks for duplicate category name...
	public static boolean doesCategoryExists(String catName) {
		TaskDAO dao = DAOFactory.getDAO();
		return dao.doesCategoryExists(catName);
	}

	// creates new category....
	public static String createCategory(String categoryName) {

		TaskDAO dao = DAOFactory.getDAO();
		return dao.createCategory(categoryName);

	}

	// creates new task
	public static String createTask(TaskBean task, String catName) {

		TaskDAO dao = DAOFactory.getDAO();
		return dao.createTask(task, catName);

	}

	// checks whether looking task exists in a category or not
	public static boolean doesTaskExists(String name, String catName) throws TaskException {
		
		TaskDAO dao = DAOFactory.getDAO();
		return dao.doesTaskExists(name, catName);

	}

	// updating existing task
	public static String updatingTask(TaskBean task, String catName, String oldTskName) {
		TaskDAO dao = DAOFactory.getDAO();
		return dao.updatingTask(task, catName, oldTskName);
	}

	// deleting existing task
	public static String deletingTask(String catName, String TaskName) {
		TaskDAO dao = DAOFactory.getDAO();
		return dao.deletingTask(catName, TaskName);
	}

	// Deleting a category
	public static String deleteCategory(String catName) {
		TaskDAO dao = DAOFactory.getDAO();
		return dao.deleteCategory(catName);
	}

	// listing tasks of all categories
	public static List<List<TaskBean>> listAll() throws TaskException {
		TaskDAO dao = DAOFactory.getDAO();
		return dao.listAll();
	}

	// Listing all tasks of a particular category
	public static List<TaskBean> taskList(String category) throws TaskException {

		TaskDAO dao = DAOFactory.getDAO();
		return dao.taskList(category);
	}

	// searching given words in all categories
	public static List<List<TaskBean>> searchAll(String words) throws TaskException {
		
		TaskDAO dao = DAOFactory.getDAO();
		return dao.searchAll(words);

	}

	// searching given words in a particular category
	public static List<TaskBean> search(String categoryName, String words) throws TaskException {

		TaskDAO dao = DAOFactory.getDAO();
		return dao.search(categoryName, words);
	}

}
