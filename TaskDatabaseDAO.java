package projectDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projectDAO.Logger;
import projectDAO.TaskException;

public class TaskDatabaseDAO implements TaskDAO {

	@Override
	public boolean doesCategoryExists(String catName) {

		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement psl = con.prepareStatement("Select catName from category where catName = ?")) {

			psl.setString(1, catName);
			psl.executeQuery();
			try (ResultSet rs = psl.getResultSet()) {
				return rs.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String createCategory(String categoryName) {
		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement pins = con.prepareStatement("insert into category(catName) values(?)");) {

			pins.setString(1, categoryName);
			pins.execute();
			return Constants.result;
		} catch (SQLException e) {
			Logger.getInstance().log("Exception in Method createCategory " + e);
			return "category creation failed " + e.getMessage();
		}

	}

	@Override
	public String createTask(TaskBean task, String catName) {
		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement pins = con.prepareStatement(
						"insert into task(cat_sl_no,name,descr,priority,plndt,crtdt) values(?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement psl = con.prepareStatement("Select sl_no from category where catName = ?");
				PreparedStatement pns = con.prepareStatement("insert into tags (tasksl_no,tag) values (?,?)")) {

			psl.setString(1, catName);
			psl.execute();
			int sl = 0;

			try (ResultSet rs = psl.getResultSet()) {
				if (rs.next())
					sl = rs.getInt("sl_no");
			}

			pins.setInt(1, sl);
			pins.setString(2, task.getName());
			pins.setString(3, task.getDescription());
			pins.setInt(4, task.getPriority());

			// converting java.util.Date to java.sql.Date
			java.sql.Date plnDt = new java.sql.Date(task.getPlannedDate().getTime());
			java.sql.Date crtDt = new java.sql.Date(task.getCreationTime().getTime());

			pins.setDate(5, plnDt);
			pins.setDate(6, crtDt);
			pins.execute();

			int tsl = 0;
			try (ResultSet rset = pins.getGeneratedKeys()) {
				if (rset.next())
					tsl = rset.getInt(1);
			}

			// null check and empty string check
			String tagsString = task.getTags();
			if (tagsString != null && !tagsString.isEmpty()) {
				String[] tags = tagsString.split(",");
				for (String tag : tags) {
					pns.setInt(1, tsl);
					pns.setString(2, tag.trim());// trimming trailing spaces
					pns.addBatch();
				}
				pns.executeBatch(); // executing once as a batch.
			}
			return Constants.result;
		} catch (SQLException e) {
			Logger.getInstance().log("" + e);
			return "task addition failed " + e.getMessage();
		}
	}

	@Override
	public boolean doesTaskExists(String name, String catName) throws TaskException {

		try (Connection con = JDBCHelper.getConnection();
				// PreparedStatement psl1 = con.prepareStatement("SELECT sl_no FROM category
				// WHERE catName = ?");
				// PreparedStatement psl2 = con.prepareStatement("select task.name from task
				// where name = ? and task.cat_sl_no = ?");
				PreparedStatement psl = con.prepareStatement(
						"SELECT task.name FROM task JOIN category ON task.cat_sl_no = category.sl_no WHERE task.name = ? AND category.catName = ?")) {

			psl.setString(1, name);
			psl.setString(2, catName);
			try (ResultSet rs = psl.executeQuery()) {
				return rs.next();
			}

			/*
			 * psl1.setString(1, catName); psl1.execute();
			 * 
			 * int sl =0; try (ResultSet rs = psl1.getResultSet()) { if(rs.next()) sl =
			 * rs.getInt("sl_no"); }
			 * 
			 * psl2.setString(1, name); psl2.setInt(2, sl); psl2.execute(); try (ResultSet
			 * rs = psl2.getResultSet()) { return rs.next(); }
			 */

		} catch (SQLException e) {
			Logger.getInstance().log("" + e);
			throw new TaskException("Something went wrong " + e.getMessage());

		}

	}

	@Override
	public String updatingTask(TaskBean task, String catName, String oldTskName) {
		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement pupd = con.prepareStatement(
						"update task set name = ?,descr =?,priority =?,plndt =?,crtdt =? where name = ?;")) {

			pupd.setString(1, task.getName());
			pupd.setString(2, task.getDescription());
			pupd.setInt(3, task.getPriority());

			// util date to sql date
			java.sql.Date plnDt = new java.sql.Date(task.getPlannedDate().getTime());
			java.sql.Date crtDt = new java.sql.Date(task.getCreationTime().getTime());

			pupd.setDate(4, plnDt);
			pupd.setDate(5, crtDt);
			pupd.setString(6, oldTskName);
			int rowsAffected = pupd.executeUpdate();
			if (rowsAffected == 0)
				return "Task with name " + oldTskName + " not found.";

		} catch (SQLException e) {
			Logger.getInstance().log("" + e);
		}
		return Constants.result;
	}

	@Override
	public String deletingTask(String catName, String TaskName) {
		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement pdl = con.prepareStatement("delete from task where name = ?")) {
			pdl.setString(1, TaskName);
			int rowsAffected = pdl.executeUpdate();
			if (rowsAffected == 0)
				return "Task with name " + TaskName + " not found.";

		} catch (SQLException e) {
			Logger.getInstance().log("" + e);
		}
		return Constants.result;
	}

	@Override
	public String deleteCategory(String catName) {
		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement pdl = con.prepareStatement("delete from category where catName = ?")) {
			pdl.setString(1, catName);
			int rowsAffected = pdl.executeUpdate();
			if (rowsAffected == 0)
				return "Category deletion failed";
		} catch (SQLException e) {
			Logger.getInstance().log("" + e);
		}
		return Constants.result;

	}

	@Override
	public List<TaskBean> taskList(String category) throws TaskException {
		List<TaskBean> list = new ArrayList<>();

		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement psl = con.prepareStatement(
						"SELECT task.sl_no,task.name, task.descr, task.plndt, task.crtdt, task.priority FROM category, task WHERE category.sl_no = task.cat_sl_no and category.catName = ?");
				PreparedStatement psl2 = con.prepareStatement(
						"SELECT tags.tag FROM task, tags WHERE task.sl_no = tags.tasksl_no and task.sl_no = ?")) {
			psl.setString(1, category);
			
			try(ResultSet rs1 = psl.executeQuery()) // closing ResultSet with try-with-resources
			{
				while (rs1.next()) {
				String name = rs1.getString("name");
				String descr = rs1.getString("descr");
				Date plndt = rs1.getDate("plndt");
				Date crtdt = rs1.getDate("crtdt");
				int priority = rs1.getInt("priority");
				int tsl = rs1.getInt("sl_no");
				psl2.setInt(1, tsl);
				
				StringBuilder tags = new StringBuilder();
				
				try(ResultSet rs2 = psl2.executeQuery()) //closing ResultSet with try-with-resources
				{
					
					while (rs2.next()) {
						if (tags.length() > 0) {
							tags.append(", ");
						}
						tags.append(rs2.getString("tag"));
					}
				}
				TaskBean task = new TaskBean(name, descr, plndt, priority, tags.toString(), crtdt);
				list.add(task);
				}
			}
			return list;
		} catch (SQLException e) {
			Logger.getInstance().log(""+e);
			throw new TaskException("Something went wrong "+e.getMessage());
		}
	}

	@Override
	public List<List<TaskBean>> listAll() throws TaskException {

		List<List<TaskBean>> li = new ArrayList<List<TaskBean>>();

		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement psl = con.prepareStatement("SELECT catName FROM category")) {
			
			try(ResultSet rs = psl.executeQuery())
			{
				int count = 0;
				while (rs.next()) {
					count++;
					List<TaskBean> l = taskList(rs.getString("catName"));
					if (!l.isEmpty())
						li.add(l);
				}
				if (count == 0)
					return null;
			}
			
		} catch (SQLException s) {
			Logger.getInstance().log("" + s);
			throw new TaskException("Something went wrong " + s.getMessage());
		}

		return li;

	}

	@Override
	public List<TaskBean> search(String categoryName, String words) throws TaskException {
		List<TaskBean> list = new ArrayList<>();

		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement psl = con.prepareStatement(
						"SELECT task.sl_no,task.name, task.descr, task.plndt, task.crtdt, task.priority FROM category, task WHERE category.sl_no = task.cat_sl_no and category.catName = ?");
				PreparedStatement psl2 = con.prepareStatement(
						"SELECT tags.tag FROM task, tags WHERE task.sl_no = tags.tasksl_no and task.sl_no = ?")) {
			psl.setString(1, categoryName);
			
			try(ResultSet rs1 = psl.executeQuery()) // closing ResultSet with try-with-resources
			{
				while (rs1.next()) {
					String name = rs1.getString("name");
					String descr = rs1.getString("descr");
					Date plndt = rs1.getDate("plndt");
					Date crtdt = rs1.getDate("crtdt");
					int priority = rs1.getInt("priority");
					int tsl = rs1.getInt("sl_no");
					psl2.setInt(1, tsl);
					
					StringBuilder tags = new StringBuilder();
					
					try(ResultSet rs2 = psl2.executeQuery()) // closing ResultSet with try-with-resources
					{
						while (rs2.next()) {
							if (tags.length() > 0) {
								tags.append(", ");
							}
							tags.append(rs2.getString("tag"));
						}
					}

					String cont = name + descr + plndt + crtdt + priority + tags;
					if (cont.contains(words)) {
						TaskBean task = new TaskBean(name, descr, plndt, priority, tags.toString(), crtdt);
						list.add(task);
					}
				}
			}
			
			return list;
			
		} catch (SQLException e) {
			Logger.getInstance().log("" + e);
			throw new TaskException("Something went wrong " + e.getMessage());
		}
	}

	@Override
	public List<List<TaskBean>> searchAll(String words) throws TaskException {

		List<List<TaskBean>> list = new ArrayList<List<TaskBean>>();

		try (Connection con = JDBCHelper.getConnection();
				PreparedStatement psl = con.prepareStatement("SELECT catName FROM category")) {
			
			try(ResultSet rs = psl.executeQuery()) // closing ResultSet with try-with-resources
			{
				int count = 0;
				while (rs.next()) {
					count++;
					List<TaskBean> l = search(rs.getString("catName"), words);
					if (!l.isEmpty())
						list.add(l);
				}
				if (count == 0)
					return null;
			}
			
		} catch (SQLException s) {
			Logger.getInstance().log("" + s);
			throw new TaskException("Something went wrong " + s.getMessage());
		}

		return list;
	}

}
