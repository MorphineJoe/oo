import com.mj.util.Dbutil;
import org.junit.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Demo {
    @Test
    public static void main(String[] args) {
        Dbutil du = new Dbutil();
        List list = du.query("st");
        System.out.println(list);
    }
/*    public void aa(){
        Properties pro = new Properties();
        try {
            pro.load(Demo.class.getClassLoader().getResourceAsStream("db.properties"));
            Class.forName(pro.getProperty("db.driver"));
            Connection conn = DriverManager.getConnection(pro.getProperty("db.url"),pro.getProperty("db.user"),pro.getProperty("db.password"));
            System.out.println(pro.getProperty("db.driver"));
            System.out.println(pro.getProperty("db.url"));
            System.out.println(pro.get("db.user"));
            System.out.println(pro.get("db.password"));
            System.out.println(conn);
            if (!conn.isClosed()){
                System.out.println("hjkhakha");
            }
        } catch (ClassNotFoundException e) {
           e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        }*/
    }

