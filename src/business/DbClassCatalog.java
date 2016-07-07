package business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;
import java.util.*;


public class DbClassCatalog implements DbClass {
	enum QueryType {READ_ALL, INSERT, DELETE};
	QueryType queryType;


	String readQuery = "SELECT * from catalogtype";
	String insertQuery = "INSERT into catalogtype " +
    		"(catalogname) " +
    		"VALUES(?)";
	String deleteQuery = "DELETE from catalogtype where catalogid = ?";

	Object[] readParams = null;
	int[] readParamTypes = null;
	Object[] insertParams = null;
	int[] insertParamTypes = null;
	Object[] deleteParams = null;
	int[] deleteParamTypes = null;

	private static final String DB_URL_PROD = "jdbc:mysql:///ProductsDb";

	List<Catalog> catalogList = new ArrayList<>();

	public List<Catalog> getAllCatalogs() throws DatabaseException {
		queryType = QueryType.READ_ALL;
		readParams = new Object[] {};
		readParamTypes = new int[] {};
		DataAccessSubsystemFacade dss = new DataAccessSubsystemFacade();
		dss.establishConnection(this);
		dss.read();
		dss.releaseConnection();
		return catalogList;
	}

	public int insertNewCatalog(String catalogName) throws DatabaseException {
		queryType = QueryType.INSERT;
		insertParams = new Object[] {catalogName};
		insertParamTypes = new int[] {Types.VARCHAR};
		DataAccessSubsystemFacade dss = new DataAccessSubsystemFacade();
		int autoVal = dss.insertWithinTransaction(this);
		return autoVal;
	}

	public int deleteCatalog(int id) throws DatabaseException {
		queryType = QueryType.DELETE;
		deleteParams = new Object[]{id};
		deleteParamTypes = new int[]{Types.SMALLINT};
		DataAccessSubsystemFacade dss = new DataAccessSubsystemFacade();
		int numrows = dss.deleteWithinTransaction(this);
		return numrows;
	}

	///interface implementations
	@Override
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		catalogList = new ArrayList<>();
		try {
			String name = null;
			int id;
			while(resultSet.next()) {
				name = resultSet.getString("catalogname").trim();
				id= resultSet.getInt("catalogid");
				catalogList.add(new Catalog(id,name));
			}
		} catch(SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public String getDbUrl() {
		return DB_URL_PROD;
	}

	@Override
	public String getQuery() {
		switch(queryType) {
		case READ_ALL :
			return readQuery;
		case INSERT :
			return insertQuery;
		case DELETE :
			return deleteQuery;
		default :
			return null;
		}
	}

	@Override
	public Object[] getQueryParams() {
		switch(queryType) {
		case READ_ALL :
			return readParams;
		case INSERT :
			return insertParams;
		case DELETE :
			return deleteParams;
		default :
			return null;

	}
	}

	@Override
	public int[] getParamTypes() {
		switch(queryType) {
		case READ_ALL :
			return readParamTypes;
		case INSERT :
			return insertParamTypes;
		case DELETE :
			return deleteParamTypes;
		default :
			return null;

	}
	}

}
