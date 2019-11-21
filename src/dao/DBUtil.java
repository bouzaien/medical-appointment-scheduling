package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil
{
   public static void setUsername (String username) {
      DBUtil.username = username ;
   }

   public static void setPassword (String password) {
      DBUtil.password = password ;
   }

   public static void setHost (String host) {
      DBUtil.host = host ;
   }

   public static void setNumPort (String numPort) {
      DBUtil.numPort = numPort ;
   }

   public static void setBase (String base) {
      DBUtil.base = base ;
   }

//  /etc/init.d/postgresql start/stop/restart pour gérer le serveur postgres
   // sudo service  postgresql stop|start|restart fonctionne aussi
   //
  // before you must create the database
  // sudo -i -u postgres
  // postgres@IRON-MEN:~$ psql
  // postgres=# DROP DATABASE soins ;
	// DROP DATABASE
	// postgres=# CREATE USER user;
	// CREATE ROLE
	// postgres=# CREATE DATABASE soins OWNER user;
	// CREATE DATABASE
	// postgres=# ALTER USER user WITH ENCRYPTED PASSWORD 'usr';
	// ALTER ROLE
	// postgres=# \q
  // then connect and fill it : psql -U user -d soins -f install_database_soins.sql -p 5432 -h 127.0.0.1
  // Connection parameters to DB
  private static String username ; //= "user";
  private static String password ; //= "usr";
  private static String host     ; //= "localhost";
  private static String numPort  ; //= "5432";
  private static String base     ; //= "soins";
  static
  {
    try
    {
      DriverManager.registerDriver(new org.postgresql.Driver());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection("jdbc:postgresql://" + host + ":" + numPort + "/" + base, username, password);
  }
}
