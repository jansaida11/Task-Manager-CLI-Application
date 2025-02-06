package projectDAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskFileDAO implements TaskDAO {

	@Override
	public boolean doesCategoryExists(String catName) {
		File f = new File("D:\\Task\\" + catName + ".todo");
		return f.exists();
	}

	@Override
	public String createCategory(String categoryName) {
		// try-with-resources block automatically closes the resources without finally
		// even an exception occured.
		try (BufferedWriter bfw = new BufferedWriter(new FileWriter("D:\\Task\\" + categoryName + ".todo", true))) {
		//	Logger.getInstance().log("Created a category called " + categoryName);
			return Constants.result;
		} catch (IOException i) {
			Logger.getInstance().log("Exception in Method createCategory " + i, 3, new TaskModel());
			return "category creation failed " + i.getMessage();
		}
	}

	@Override
	public String createTask(TaskBean task, String catName) {
		try(BufferedWriter bfw = new BufferedWriter(new FileWriter("D:\\Task\\"+catName+".todo",true))) 
		{
			bfw.write(task.getName()+"-"+task.getDescription()+"-"+task.getPlannedDate()+"-"+task.getPriority()+"-"+task.getTags()+"-"+task.getCreationTime());
			bfw.newLine();
			return Constants.result;
		}
		catch(IOException i) {
			Logger.getInstance().log(""+i);
			return "task addition failed "+i.getMessage();
		}
	}

	@Override
	public boolean doesTaskExists(String name, String catName) throws TaskException {
		String line;
		String [] sr = null;
			try(BufferedReader br = new BufferedReader(new FileReader("D:\\Task\\"+catName+".todo"))) {
				while((line=br.readLine())!=null) {
					sr = line.split("-");
					if(sr[0].equals(name)) {
						return true;
					}
				}	
			} catch (IOException e) {
				Logger.getInstance().log(""+e);
				throw new TaskException("Something went wrong "+e.getMessage());				
			}
			return false;
	}

	@Override
	public String updatingTask(TaskBean task, String catName, String oldTskName) {
		 String filePath = "D:\\Task\\" + catName + ".todo";
		    String lineToUpdate = task.getName() + "-" + task.getDescription() + "-" + task.getPlannedDate() + "-" + task.getPriority() + "-" + task.getTags() + "-" + task.getCreationTime();
		    List<String> lines = new ArrayList<>();
		    boolean taskUpdated = false;

		    // Step 1: Reading contents into a collection
		    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
		        String line;
		        while ((line = br.readLine()) != null) {
		            String[] sr = line.split("-");
		            if (sr[0].equals(oldTskName)) {
		                // Update the task in the collection
		                lines.add(lineToUpdate);
		                taskUpdated = true;
		            } else {
		                lines.add(line); // Retain unchanged lines
		            }
		        }
		    } catch (IOException e) {
		        Logger.getInstance().log(""+e);
		        return "Error reading file: " + e.getMessage();
		    }

		    if (!taskUpdated) {
		        return "Task with name " + oldTskName + " not found.";
		    }

		    // Step 2: Clear the file contents
		    try (FileWriter fw = new FileWriter(filePath, false)) {
		        // Opening in write mode with `false` clears the file
		        // File is now empty
		    } catch (IOException e) {
		        Logger.getInstance().log(""+e);
		        return "Error clearing file: " + e.getMessage();
		    }

		    // Step 3: Write updated content back to the file from the collection
		    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
		        for (String updatedLine : lines) {
		            bw.write(updatedLine);
		            bw.newLine();
		        }
		    } catch (IOException e) {
		    	Logger.getInstance().log(""+e);
		        return "Error writing to file: " + e.getMessage();
		    }

		    return Constants.result;
	}

	@Override
	public String deletingTask(String catName, String TaskName) {
		String filePath = "D:\\Task\\" + catName + ".todo";
	    List<String> lines = new ArrayList<>();

	    // Step 1: Reading contents into a collection
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] sr = line.split("-");
	            if (sr[0].equals(TaskName)) {
	            	continue; // skipping the line to be deleted
	            } else {
	                lines.add(line); // Retain unchanged lines
	            }
	        }
	    } catch (IOException e) {
	    	Logger.getInstance().log(""+e);
	        return "Error reading file: " + e.getMessage();
	    }

	    // Step 2: Clear the file contents
	    try (FileWriter fw = new FileWriter(filePath, false)) {
	        // Opening in write mode with `false` clears the file
	        // File is now empty
	    } catch (IOException e) {
	    	Logger.getInstance().log(""+e);
	        return "Error clearing file: " + e.getMessage();
	    }

	    // Step 3: Write updated content back to the file from the collection
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
	        for (String updatedLine : lines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	    } catch (IOException e) {
	    	Logger.getInstance().log(""+e);
	        return "Error writing to file: " + e.getMessage();
	    }

	    return Constants.result;
	}

	@Override
	public String deleteCategory(String catName) {
		String filePath = "D:\\Task\\" + catName + ".todo";
		File f = new File(filePath);
		if(f.delete())
			return Constants.result;
		return "Category deletion failed";
	}

	@Override
	public List<List<TaskBean>> listAll() throws TaskException {
		List<List<TaskBean>> li = new ArrayList<List<TaskBean>>();
		File f = new File("D:\\Task");
		File [] fa = f.listFiles();
		if(fa.length==0) {
			return null;
		}
		for(File fi : fa) {
			if(fi.length()!=0) {
				List<TaskBean> l = TaskModel.taskList(fi.getName());
				if(!l.isEmpty())
					li.add(l);			
			}
		}
		return li;
	}

	@Override
	public List<TaskBean> taskList(String category) throws TaskException {
		if(!category.contains(".todo")) {
			category+=".todo";
		}
		String filePath = "D:\\Task\\" + category;
		List<TaskBean> list = new ArrayList<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while((line=br.readLine())!=null) {
				String sa[] = line.split("-");
				Date pln = null;
				Date cr = null;
				try {
					pln = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(sa[2]);
					cr = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(sa[5]);
				} catch (ParseException e) {
					Logger.getInstance().log(""+e);
					throw new TaskException("Something went wrong "+e.getMessage());
				}
				TaskBean t = new TaskBean(sa[0],sa[1],pln,Integer.parseInt(sa[3]),sa[4],cr);
				list.add(t);
			}
		}
		catch(IOException i) {
			Logger.getInstance().log(""+i);
			throw new TaskException("Something went wrong "+i.getMessage());
		}
		return list;
	}

	@Override
	public List<List<TaskBean>> searchAll(String words) throws TaskException {
		List<List<TaskBean>> list = new ArrayList<List<TaskBean>>();
		File f = new File("D:\\Task");
		File fa [] = f.listFiles();
		if(fa.length==0) {
			return null;
		}
		for(File fi : fa) {
			List<TaskBean> l = null;
				l = search(fi.getName(), words);
				if(!l.isEmpty())
					list.add(l);
		}
		return list;
	}

	@Override
	public List<TaskBean> search(String categoryName, String words) throws TaskException {
		if(!categoryName.contains(".todo"))
			categoryName+=".todo";
		String filePath = "D:\\Task\\" + categoryName;
		List<TaskBean> list = new ArrayList<TaskBean>();
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while((line=br.readLine())!=null) {
				if(line.contains(words)) {
					String sa[] = line.split("-");
					Date pln = null;
					Date cr = null;
					try {
						pln = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(sa[2]);
						cr = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(sa[5]);
					} catch (ParseException e) {
						Logger.getInstance().log(""+e);
						throw new TaskException("Something went wrong "+e.getMessage());
						
					}
					TaskBean t = new TaskBean(sa[0],sa[1],pln,Integer.parseInt(sa[3]),sa[4],cr);
					list.add(t);
				}
			}
		}
		catch(IOException i) {
			Logger.getInstance().log(""+i);
			throw new TaskException("Something went wrong "+i.getMessage());
		}
		return list;
	}

}
