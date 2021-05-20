package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.MovieBean;

/*
 * Class that handles the SQL connection with the database.
 */
public class SQLconnection {

	// set up the connection as static
	static Connection conn = null;
	static PreparedStatement stmt = null;
	static ResultSet rs = null;

	public static boolean connectSQL() {

		try {

			// driver setup
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception ex) {
			// handle the error
			System.out.println("Exception Driver: " + ex);
			return false;
		}

		try {

			// keep the pass and username in its own file
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_db?serverTimezone=UTC",
					DatabaseLogin.getuName(), DatabaseLogin.getuPass());
			return true;

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}

	}
	
	
	/*
	 * Method for adding a movie to the database.
	 */
	public static void insertMovie(String movieName, String movieDir) {

		try {
			if(movieName != null && !movieName.isEmpty()) {
				String[] params = {movieName, movieDir};
				
				// SQLqueryString with parameters
				String requestQuery = 
						"SELECT "
								+ "movies.movies_name "
								+ "FROM movies "
								+ "WHERE "
								+ "movies.movies_name = ? AND movies.movies_director = ? ";
				
				// Run query
				rs = runQuery(params, requestQuery);
				
				// Run insert update if the movie does not exists in database.
				if(!rs.next()) {
					
					// SQLqueryString with parameters
					requestQuery = 
							"INSERT INTO movies("
									+ "movies.movies_name, "
									+ "movies.movies_director )"
									+ "VALUES ( ?, ? )";
					
					// Update
					runUpdate(params, requestQuery);
					
				}
				
				// Close ResultSet and request.
				rs.close();
				conn.endRequest();	
			}

		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}

	}
	
	/*
	 * Method for adding actors to the database.
	 */
	public static void insertActors(String actors, String movieName) {

		try {
			if(actors != null && !actors.isEmpty()) {
				String[] params = {movieName};
				
				// SQLqueryString with parameters
				String requestQuery = "SELECT movies.movies_id FROM movies WHERE movies.movies_name = ? ";
				
				// Run query
				rs = runQuery(params, requestQuery);
				
				if(rs.next()) {
					String movieId = rs.getString("movies_id");
					String[] actorArray = actors.split(",");
					
					// Loop through the array of actors.
					for(int i = 0; i < actorArray.length; i++) {
						params = new String[] {actorArray[i], movieId};
						
						// SQLqueryString with parameters
						requestQuery = 
								"SELECT "
										+ "actors.actors_name "
										+ "FROM actors "
										+ "WHERE "
										+ "actors.actors_name = ? AND actors.movies_id = ? ";
						
						// Run query
						rs = runQuery(params, requestQuery);
						
						// Run insert update if the movie does not exists in database.
						if(!rs.next()) {
							
							// SQLqueryString with parameters
							requestQuery = 
									"INSERT INTO actors("
											+ "actors.actors_name, "
											+ "actors.movies_id )"
											+ "VALUES ( ?, ? )";
							
							// Update
							runUpdate(params, requestQuery);
							
						}
					}
					
					// Close ResultSet and request.
					rs.close();
					conn.endRequest();			
				}
	
			}

		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		
	}

	/*
	 * Method for adding awards to the database.
	 */
	public static void insertAward(String award, String actorName, String movieName) {
		
		try {
			if(award != null && !award.isEmpty()) {
				String[] params = {movieName};
				
				// SQLqueryString with parameters
				String requestQuery = "SELECT movies.movies_id FROM movies WHERE movies.movies_name = ? ";
				
				// Run query
				rs = runQuery(params, requestQuery);
				
				if(rs.next()) {
					params = new String[] {award, actorName, rs.getString("movies_id")};
					
					// SQLqueryString with parameters
					requestQuery = 
							"SELECT "
									+ "awards.awards_award "
									+ "FROM awards "
									+ "WHERE "
									+ "awards.awards_award = ? AND awards.actors_id = (SELECT actors.actors_id FROM actors WHERE actors.actors_name = ?) "
									+ "AND awards.movies_id = ? ";
					
					// Run query
					rs = runQuery(params, requestQuery);
					
					// Run insert update if the movie does not exists in database.
					if(!rs.next()) {
						
						// SQLqueryString with parameters
						requestQuery = 
								"INSERT INTO awards("
										+ "awards.awards_award, "
										+ "awards.actors_id, "
										+ "awards.movies_id )"
										+ "VALUES ( ?, (SELECT actors.actors_id FROM actors WHERE actors.actors_name = ? ), ? )";
						
						// Update
						runUpdate(params, requestQuery);
						
					}
					
					// Close ResultSet and request.
					rs.close();
					conn.endRequest();
					
				}
					
			}		

		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
	}

	/*
	 * Method for getting movies with the provided actor.
	 */
	public static ArrayList<MovieBean> movieAwardsWithActor(String actorName, ArrayList<MovieBean> movies) {

		try {
			String[] params = {"%" + actorName + "%"};
		
			// SQLqueryString with parameter
			String requestQuery = 
					"SELECT "
					+ "actors.actors_name, "
					+ "movies.movies_name, "
					+ "movies.movies_director, "
					+ "awards.awards_award "
					+ "FROM movies "
					+ "JOIN actors ON movies.movies_id = actors.movies_id "
					+ "JOIN awards ON movies.movies_id = awards.movies_id "
					+ "WHERE actors.actors_name LIKE ?";
			
			// Run query
			rs = runQuery(params, requestQuery);
			
			while (rs.next()) {
				// Add data to the bean
				MovieBean movie = new MovieBean();
				movie.setActorName(rs.getString("actors_name"));
				movie.setMovieName(rs.getString("movies_name"));
				movie.setMovieDirector(rs.getString("movies_director"));
				movie.setAwardName(rs.getString("awards_award"));
				
				movies.add(movie);
				
			}
			
			// Close ResultSet and request.
			rs.close();
			conn.endRequest();

			// return the results after the ResultSet is done and closed
			return movies;

		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		return null;

	}

	/*
	 * Method for getting all actors in a provided movie.
	 */
	public static ArrayList<MovieBean> actorsInMovie(String movieName, ArrayList<MovieBean> movies) {		

		try {
			String[] params = {"%" + movieName + "%"};
		
			// SQLqueryString with parameter
			String requestQuery = 
					"SELECT "
					+ "movies.movies_name, "
					+ "actors.actors_name "
					+ "FROM movies "
					+ "JOIN actors ON movies.movies_id = actors.movies_id "
					+ "WHERE movies.movies_name LIKE ?";
			
			// Run query
			rs = runQuery(params, requestQuery);
			
			while (rs.next()) {
				// Add data to the bean
				MovieBean movie = new MovieBean();
				movie.setActorName(rs.getString("actors_name"));
				movie.setMovieName(rs.getString("movies_name"));
				
				movies.add(movie);
				
			}
			
			// Close ResultSet and request.
			rs.close();
			conn.endRequest();

			// return the results after the ResultSet is done and closed
			return movies;

		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		
		return null;
	}
	
	/*
	 * Method for getting all movies with the provided actor.
	 */
	public static ArrayList<MovieBean> moviesWithActor(String actorName, ArrayList<MovieBean> movies) {		

		try {
			String[] params = {"%" + actorName + "%"};
			
			// SQLqueryString with parameter
			String requestQuery = 
					"SELECT "
					+ "actors.actors_name, "
					+ "movies.movies_name "
					+ "FROM actors "
					+ "JOIN movies ON movies.movies_id = actors.movies_id "
					+ "WHERE actors.actors_name LIKE ?";
			
			// Run query
			rs = runQuery(params, requestQuery);
			
			while (rs.next()) {
				// Add data to the bean
				MovieBean movie = new MovieBean();
				movie.setActorName(rs.getString("actors_name"));
				movie.setMovieName(rs.getString("movies_name"));
				
				movies.add(movie);
				
			}
			
			// Close ResultSet and request.
			rs.close();
			conn.endRequest();

			// return the results after the ResultSet is done and closed
			return movies;

		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		
		return null;
	}
	
	/*
	 * Method for getting all movies with the provided award.
	 */
	public static ArrayList<MovieBean> movieAwards(String awardName, ArrayList<MovieBean> movies) {
		
		try {
			String[] params = {"%" + awardName + "%"};

			// SQLqueryString with parameter
			String requestQuery = 
					"SELECT "
					+ "awards.awards_award, "
					+ "movies.movies_name, "
					+ "movies.movies_director "
					+ "FROM awards "
					+ "JOIN movies ON movies.movies_id = awards.movies_id "
					+ "WHERE awards.awards_award LIKE ?";
			
			// Run query
			rs = runQuery(params, requestQuery);
			
			while (rs.next()) {
				// Add data to the bean
				MovieBean movie = new MovieBean();
				movie.setAwardName(rs.getString("awards_award"));
				movie.setMovieName(rs.getString("movies_name"));
				movie.setMovieDirector(rs.getString("movies_director"));
				
				movies.add(movie);
				
			}
			
			// Close ResultSet and request.
			rs.close();
			conn.endRequest();
			
			// return the results after the ResultSet is done and closed
			return movies;

		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		
		return null;
	}
	
	/*
	 * Method for executing query with parameters.
	 */
	private static ResultSet runQuery(String[] params, String requestQuery) {
		
		try {
			// Prepare statement
			stmt = conn.prepareStatement(requestQuery);
		

			// Set parameter values
			for(int i = 0; i < params.length; i++) {
				stmt.setString(i + 1, params[i]);			
			}

			// Run query and return ResultSet.
			return stmt.executeQuery();
			
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		return null;
	}

	/*
	 * Method for executing update with parameters.
	 */
	private static void runUpdate(String[] params, String requestQuery) {
		
		try {
			// Prepare statement
			stmt = conn.prepareStatement(requestQuery);
		

			// Set parameter values
			for(int i = 0; i < params.length; i++) {
				stmt.setString(i + 1, params[i]);			
			}

			// Update database.
			stmt.executeUpdate();
			
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}
		
	}
	
	/*
	 * Method for closing the connection to the database.
	 */
	public static void closeConnection() {
		
		try {
			conn.close();
		
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}	
	}

}