package  com.emergentes.utiles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionDB {
    static String url = "jdbc:mysql://localhost:3306/bd_biblio";
    static String usuario = "root";
    static String password = "admin";
    
    protected Connection conn = null;
    
    public conexionDB() throws SQLException {
        try {
            //especificacion del driver
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = DriverManager.getConnection(url,usuario,password);
            if (conn != null) {
                System.out.println("Conexion ok: "+ conn);
            }
            
        }catch (ClassNotFoundException ex){
            System .out.println("falta driver: " + ex.getMessage());
        }catch (SQLException ex){
            System.out.println("Error de SQL: " + ex.getMessage());
        
        }       
    }
    
    public Connection conectar()
    {
        return conn;
    }
    
    public void desconectar()
    {
        try {
            conn.close();          
        } catch (SQLException ex){
            System.out.println("Error al cerrar la conexion: " + ex.getMessage());
        }
    }
}
