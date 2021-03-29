package sk.kosickaakademia.company.database;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.util.Util;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {
    Log log = new Log();
    private final String INSERTQUERY ="INSERT INTO user (fname, lname, age, gender) " +
            " VALUES ( ?, ?, ?, ?)";
    public Connection getConnection(){
        try {
            Properties props = new Properties();
            InputStream loader = getClass().getClassLoader().getResourceAsStream("database.properties");
            if(loader==null){
                log.error("null");
                return null;
            }
            props.load(loader);
            String url = props.getProperty("url");
            String username=props.getProperty("username");
            String password=props.getProperty("password");
            Connection con = DriverManager.getConnection(url, username, password);
            log.print("Connected");
            return con;
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return null;
    }

    public void closeConnection(Connection con)  {

        if(con!=null) {
            try {
                con.close();
                log.print("Connection closed");
            }catch(SQLException e){
                log.error(e.toString());
            }
        }
    }
    public boolean insertNewUser(User user){
        if(user==null)
            return false;
        String fname = new Util().normalizeName(user.getFname());
        String lname = new Util().normalizeName(user.getLname());
        Connection con = getConnection();
        if(con!=null){
            try{
                PreparedStatement ps = con.prepareStatement(INSERTQUERY);
                ps.setString(1,fname);
                ps.setString(2,lname);
                ps.setInt(3,user.getAge());
                ps.setInt(4,user.getGender().getValue());
                int result = ps.executeUpdate();
                closeConnection(con);
                log.print("New user has been added to the DB");
                return result==1;
            }catch(SQLException ex){
                log.error(ex.toString());
            }
        }
        return false;
    }
    public List<User> getFemales(){
        log.info("Executing: getFemales()");
        String sql = "SELECT * FROM user WHERE gender = 1";
        try (Connection con = getConnection()) {
            if(con!=null) {
                PreparedStatement ps = con.prepareStatement(sql);
                return executeSelect(ps);
            }
        }catch(Exception ex){
            log.error(ex.toString());
        }
        return null;
    }
    public List<User> getMales(){
        String sql = "SELECT * FROM user WHERE gender = 0 SELECT *";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            return executeSelect(ps);
        }catch(Exception ex){
            log.error(ex.toString());
        }
        return null;
    }
    public List<User> getUsersByAge(int from, int to){

        if(to<from)
            return null;
        try {
            String sql = "SELECT * FROM user WHERE age >= ? AND age <= ?";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1,from);
            ps.setInt(2, to);

            return executeSelect(ps);
        }catch(Exception ex){
            log.error(ex.toString());
        }
        return null;
    }
    private List<User> executeSelect(PreparedStatement ps) throws SQLException {
        ResultSet rs =  ps.executeQuery();
        List<User> list = new ArrayList<>();
        int count = 0;
        while(rs.next()){
            count ++;
            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            int age = rs.getInt("age");
            int id = rs.getInt("id");
            int gender = rs.getInt("gender");
            User u=new User(id,fname,lname,age,gender);
            list.add(u);
        }
        log.info("Number of records: "+ count);
        return list;
    }

    public List<User> getAllUsers(){
        String sql = "SELECT * FROM user";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            return executeSelect(ps);
        }catch(Exception ex){
            log.error(ex.toString());
        }
        return null;

    }

    public User getUserById(int id){
        String sql = "SELECT * FROM user WHERE id = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1,id);
            List<User> list = executeSelect(ps);
            if(list.isEmpty())
                return null;
            else
                list.get(0);
        }catch(Exception ex){
            log.error(ex.toString());
        }
        return null;

    }
    public boolean changeAge(int id, int newAge){
        return false;
    }
    public List<User> getUser(String pattern) {
        List<User> users = getAllUsers();
        List<User> list = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getFname().contains(pattern)) {
                list.add(users.get(i));
            }
        }
        return list;
    }

}

