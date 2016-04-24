package movies;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


@Named
@SessionScoped
public class MovieDB implements Serializable{
	private static final String connection_url = "jdbc:mysql://localhost:3306/web_shopdb";
	private Movie temp = new Movie();
	
	public Movie getTemp() {
		return temp;
	}
	public void setTemp(Movie temp) {
		this.temp = temp;
	}
	
	/**
	 * Returns a list with all available movies
	 * 
	 * @return a list with all available movies
	 */
	public List<Movie> getMovies() {
		List<Movie> result = new ArrayList<>();

		try {

			Connection conn = newConnection();

			try {
				String sql = "SELECT * FROM web_shopdb.movies";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Movie m = new Movie();
					m.setId(rs.getInt(1));
					m.setTitle(rs.getString(2));
					m.setGenre(rs.getString(3));
					m.setDescription(rs.getString(4));
					m.setImgPath(rs.getString(5));
					m.setQuantity(rs.getInt(6));
					m.setPrice(rs.getFloat(7));
					result.add(m);
				}

			} finally {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Adds a new movie to the database
	 * 
	 * @return return page url
	 */
	public String add() {

		try {
			Connection conn = newConnection();
			try {
				String sql = "INSERT INTO web_shopdb.movies (title, genre, description, image_path, quantity, price) VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setString(1, temp.getTitle());
				stat.setString(2, temp.getGenre());
				stat.setString(3, temp.getDescription());
				stat.setString(4, temp.getImgPath());
				stat.setInt(5, temp.getQuantity());
				stat.setFloat(6, temp.getPrice());
				stat.executeUpdate();

			} finally {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return "manage_movies";
	}
	
	
	public String edit(Movie m) {
		this.temp = m;
		return "edit_movie";
	}
	
	public String save(){
		try {
			Connection conn = newConnection();
			try {
				String sql = "UPDATE web_shopdb.movies SET title = ?, genre = ?, description = ?, image_path = ?, quantity = ?, price = ?  WHERE id = ?";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setString(1, temp.getTitle());
				stat.setString(2, temp.getGenre());
				stat.setString(3, temp.getDescription());
				stat.setString(4, temp.getImgPath());
				stat.setInt(5, temp.getQuantity());
				stat.setFloat(6, temp.getPrice());
				stat.setInt(7, temp.getId());
				stat.executeUpdate();
				
			} finally {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return "manage_movies";
		
	}
	
	public String delete(Movie x) {
		this.temp = x;

		try {
			Connection conn = newConnection();
			try {
				String sql = "DELETE FROM web_shopdb.movies WHERE id = ?";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setInt(1, temp.getId());
				stat.executeUpdate();

				String sql1 = "ALTER TABLE web_shopdb.movies AUTO_INCREMENT = ?";
				PreparedStatement stat1 = conn.prepareStatement(sql1);
				List<Movie> result = getMovies();
				int increment = result.get(result.size() - 1).getId() + 1;
				stat1.setInt(1, increment);
				stat1.executeUpdate();

			} finally {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return "manage_movies";
	}
	
	
	private Connection newConnection()
			throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Properties user = new Properties();
		user.put("user", "group1");
		user.put("password", "UltrabookGroup1!");
		Connection conn = DriverManager.getConnection(connection_url, user);

		return conn;
	}
	

}
