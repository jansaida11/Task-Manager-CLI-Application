package projectDAO;

public class DAOFactory {
	public static TaskDAO getDAO() {
		switch(Constants.DS) {
		case 1:
			return new TaskFileDAO();
		case 2:
			return new TaskDatabaseDAO();
		}
		return null;
	}
}
