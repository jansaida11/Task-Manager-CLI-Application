package projectDAO;

public class DAOFactory {
	public static TaskDAO getDAO() {
		switch(Constants.DS) {
		case 1:
			return new TaskFileDAO();
		}
		return null;
	}
}
