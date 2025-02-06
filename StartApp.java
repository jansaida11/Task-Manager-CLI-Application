package projectDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StartApp {
	private static Scanner sc1 = new Scanner(System.in);
	private static Scanner sc2 = new Scanner(System.in);
	private static Logger l = Logger.getInstance();//will use this ref to log.
	private static StartApp st = new StartApp();
	public static void main(String[] args) {
		try {
			l.log("Application is started...");
			System.out.println("Welcome to Personel TaskManager Application...!");
			
			int ch1 = 0; //options of main menu	
			
			while(ch1!=6) {
				l.log("Firing the main menu",2,st);
				System.out.println("press 1 to create category");
				System.out.println("press 2 to load category");
				System.out.println("press 3 to delete category");
				System.out.println("press 4 to list");
				System.out.println("press 5 to search");
				System.out.println("press 6 to exit");
				System.out.print("enter your choice : ");
				try {
					ch1=sc1.nextInt();
					sc1.nextLine(); // Clears the leftover newline after reading the integer
				}
				catch(InputMismatchException i) {
					System.out.println("please check your provided input.");
					System.out.println("Enter an integer between 1 to 6.");
					sc1.nextLine();// clears the inavalid input.
					continue;// Restart the loop after invalid input.
				}
				if(Utility.isValid(ch1)) {
					switch(ch1) {
						case 1:
							StartApp.categoryMenu();
							break;
						case 2:
							System.out.println("Enter name of category to load");
							String catName=sc2.nextLine();
							if(TaskModel.doesCategoryExists(catName)) {
								l.log("Loading the category "+catName);
								StartApp.taskMenu(catName);
							}
							else {
								System.out.println("Category "+catName+" doesn't exists");
//								System.out.println("If you want to create category "+catName+" press 0");
//								int ch = sc1.nextInt();
//								if(ch==0) {
//									StartApp.categoryMenu();
//								}
							}
							
							break;
						case 3:
							System.out.println("Enter name of category to remove");
							String catN = sc2.nextLine();
							if(TaskModel.doesCategoryExists(catN)) {
								
								String message = TaskModel.deleteCategory(catN);
								if(message.equals(Constants.result))
									System.out.println("Category deleted succesfully");
								else
									System.out.println(message);
							}
							else {
								System.out.println("Category "+catN+" doesn't exists");
							}
							break;
						case 4:
							List<List<TaskBean>> list = TaskModel.listAll();
							if(list==null) {
								System.out.println("There are no categories to list tasks...First create Categories then add tasks");
							}
							if(list.size()==0) {
								System.out.println("There are no tasks in categories.Kindly add tasks to list them");
							}
							else {
								System.out.println("Listing all tasks of all categories..");
								for(List<TaskBean> li : list) {
									
									for(TaskBean tasks : li) {
										
										System.out.println("Task name = "+tasks.getName() + "," +"Task Desc = "+ tasks.getDescription() 
										+ "," +"Task pln date = "+ tasks.getPlannedDate() + "," +"Task priority = "+ tasks.getPriority() 
										+ "," +"Task tags = "+ tasks.getTags() + "," +"Task creation date = "+ tasks.getCreationTime());

									}
								}
							}
							
							break;
						case 5:
							System.out.println("Enter words to search..");
							String word = sc2.nextLine();
							List<List<TaskBean>> lists = TaskModel.searchAll(word);
							if(lists==null) {
								System.out.println("There are no categories");
								System.out.println("First create Categories then add tasks then search for words");
							}	
							else if(lists.size()==0) {
								System.out.println("There are no tasks that contains : "+word);
							}
							else {
								System.out.println("Listing all tasks those contains words : "+word);
								
								for(List<TaskBean> li : lists) {
									
									for(TaskBean tasks : li) {
										
										System.out.println("Task name = "+tasks.getName() + "," +"Task Desc = "+ tasks.getDescription() 
										+ "," +"Task pln date = "+ tasks.getPlannedDate() + "," +"Task priority = "+ tasks.getPriority() 
										+ "," +"Task tags = "+ tasks.getTags() + "," +"Task creation date = "+ tasks.getCreationTime());

									}
								}
							}
							break;
						case 6:
							System.out.println("Thank you for your time.Have a nice day !!");
							break;
						
					}
				}
				else {
					
					System.out.println("Incorrect input,Enter a number between 1 to 6");
					System.out.println();
				}
				
			}
		}
		catch(Throwable t) {
			
			System.out.println("Something went wrong : "+t.getMessage());
			System.out.println("If your issue is not resolved yet, Please contact developer Jans Saida Shaik.");
			Logger.getInstance().log(""+t);
		}
		Logger.getInstance().log("Application is shutting down.");

	}
	
	//code to create new category
	public static void categoryMenu() {
		
		System.out.println("Enter your category name ");
		String categoryName = sc2.nextLine();
		
		while(!Utility.isValidName(categoryName)) {
			
			System.out.println("catName must be 1 word,no spl chars,Start with alph followed by alphanumerics");
			System.out.println("Enter again");
			categoryName = sc2.nextLine();
		}
		if(!TaskModel.doesCategoryExists(categoryName)) {
			
			String res = TaskModel.createCategory(categoryName);	
			if(res.equals(Constants.result))
				StartApp.taskMenu(categoryName);
			else
				System.out.println(res);
		}
		else {
			System.out.println("category already exists..");
		}
	}
	
	//code of task menu
	public static void taskMenu(String categoryName){
		Logger.getInstance().log("Firing the task menu...",2,st);
		int ch2 = 0; //options of task menu	
		while(ch2!=6) {
			System.out.println("press 1 to create task");
			System.out.println("press 2 to update task");
			System.out.println("press 3 to delete task");
			System.out.println("press 4 to list tasks");
			System.out.println("press 5 to search task");
			System.out.println("press 6 to go back");
			System.out.print("Enter your choice : ");
			try {
				ch2=sc1.nextInt();
				sc1.nextLine();// Clear the newline character after nextInt()
			}
			catch(InputMismatchException i) {
				System.out.println("Please check your input.");
				System.out.println("Enter a integer between 1 to 6.");
				sc1.nextLine();// clears the inavalid input.
				continue;// Restart the loop after invalid input.
			}
			if(Utility.isValid(ch2)) {
				switch(ch2) {
				case 1:	
					TaskBean tsk = creatingTask(categoryName);
					if(tsk!=null) {
						String isAdded = TaskModel.createTask(tsk,categoryName);
						if((isAdded.equals(Constants.result)))
								System.out.println("Task "+tsk.getName()+" added succesfully");
						else
							System.out.println(isAdded);	
					}
					break;
				case 2:
					updatingTask(categoryName);
					break;
				case 3:
					deleteTask(categoryName);
					break;
				case 4:				
					if(TaskModel.doesCategoryExists(categoryName)) {
						List<TaskBean> list;
						try {
							list = TaskModel.taskList(categoryName);
							if(list.size()==0)
								System.out.println("category "+categoryName+" is empty.");
							else {
								System.out.println("listing all tasks of "+categoryName);
								for(TaskBean tasks : list) {
									System.out.println("Task name = "+tasks.getName() + "," +"Task Desc = "+ tasks.getDescription() 
									+ "," +"Task pln date = "+ tasks.getPlannedDate() + "," +"Task priority = "+ tasks.getPriority() 
									+ "," +"Task tags = "+ tasks.getTags() + "," +"Task creation date = "+ tasks.getCreationTime());
									//System.out.println(tasks);
								}
							}
						} 
						catch (TaskException e) {
							System.out.println(e.getMessage());
							Logger.getInstance().log(""+e);
						}		
					}
					else {
						System.out.println("category "+categoryName+" does not exists");
					}
					break;
				case 5:
					System.out.println("Enter words to search");
					String words = sc2.nextLine();
					try {
						List<TaskBean> list = TaskModel.search(categoryName,words);
						if(list.isEmpty()) {
							System.out.println("There is no task that contains words : "+words);
						}
						else {
							System.out.println("Search results of category "+categoryName);
							for(TaskBean tasks : list) {
								System.out.println("Task name = "+tasks.getName() + "," +"Task Desc = "+ tasks.getDescription() + "," +"Task pln date = "+ tasks.getPlannedDate() + "," +"Task priority = "+ tasks.getPriority() + "," +"Task tags = "+ tasks.getTags() + "," +"Task creation date = "+ tasks.getCreationTime());
							}
						}
					} catch (TaskException e) {
						System.out.println(e.getMessage());
						Logger.getInstance().log(""+e);
					}
					
					break;
				default:				
					System.out.println("Going to main menu");
					break;
			}
			
			}
			else {
				System.out.println("Incorrect input,Enter a number between 1 to 6 ");
				System.out.println();
			}
		}
		Logger.getInstance().log("Exiting the task menu...",1,st);
	}

	//creating a new task
	public static TaskBean creatingTask(String categoryName) {
		
		String tName;
		String tDesc;
		String tags;
		Date plnDt = null;
		int priority = 0;		
		TaskBean task=null;	

		Logger.getInstance().log("Adding a task to "+categoryName);
		System.out.println();
		System.out.println("Enter task name...");
		tName=sc2.nextLine();
		System.out.println("Enter task description...");
		tDesc=sc2.nextLine();				
		System.out.println("Enter planned task completion date...(dd/mm/yyyy)");
		String date = sc1.next();
		
		try {
			plnDt=new SimpleDateFormat("dd/MM/yyyy").parse(date);
		} catch (ParseException e) {
			Logger.getInstance().log("ParseException "+e.getMessage());
			System.out.println(e.getMessage());
			System.out.println("Invalid date format. Please use dd/MM/yyyy.");		
			return null;
		}
		do {
			System.out.println("Enter priority(1-low__10-high)...");
			try {
				priority=sc1.nextInt();
				if(!Utility.isValidPiority(priority)) {
					System.out.println("Priority must be between 1 and 10.");
				}
			}
			catch(InputMismatchException i) {
				System.out.println("Enter a valid integer for priority.");
				sc1.next();
			}
		}
		while(!Utility.isValidPiority(priority));
	
		System.out.println("Enter tags(comma seperated)...");
		tags=sc2.nextLine();				
		task = new TaskBean(tName,tDesc,plnDt,priority,tags,new Date(new Date().getTime()));		
		return task;
	}
	
	//updating a task 
	public static void updatingTask(String categoryName) {
		
		System.out.println("Enter task name that you want to update");
		String tskName=sc2.nextLine();
		try {
			if(!TaskModel.doesTaskExists(tskName,categoryName)) {
				System.out.println("Task doesn't exists");
			}
			else {
				System.out.println("Enter new details of task");
				TaskBean task = StartApp.creatingTask(categoryName);
				if(task!=null) {
					String update = TaskModel.updatingTask(task, categoryName, tskName);
					if(update.equals(Constants.result)) {
						System.out.println("Task updated successfully");
					}
					else {
						System.out.println("Task updation failed "+update);
					}
				}
			}
		} catch (TaskException e) {
			System.out.println(e.getMessage());
			Logger.getInstance().log(""+e);
		}
	}
	
	//deleting a task
	public static void deleteTask(String categoryName) {
		
		System.out.println("Enter task name that you want to delete");
		String taskName=sc2.nextLine();
		try {
			if(!TaskModel.doesTaskExists(taskName,categoryName)) {
				System.out.println("Task doesn't exists");
			}
			else {
				String delete = TaskModel.deletingTask(categoryName, taskName);
				if(delete.equals(Constants.result)) {
					System.out.println("Task removed succesfully...");
				}
				else {
					System.out.println("Task removal failed "+delete);
				}
			}
		} catch (TaskException e) {
			System.out.println(e.getMessage());
			Logger.getInstance().log(""+e);
		}
	}
}
