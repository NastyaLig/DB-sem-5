package by.bstu.fit.drugov.lab17;

import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper extends AsyncTask<String, String, String> {

    Connection connection;
    @Override
    protected String doInBackground(String... strings) {
        String address = "80.94.168.145";
        String port = "1433";
        String dbName = "lab17_JDBC";
        String user = "student";
        String password = "Pa$$w0rd";
        connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            System.out.println("Driver is registered!");

            ConnectionURL = "jdbc:jtds:sqlserver://"+ address+":"+port+"/"+dbName+";";

            connection = DriverManager.getConnection(ConnectionURL,"student","Pa$$w0rd");
            System.out.println("You are connected");

        } catch (ClassNotFoundException e) {
            System.out.println(e.getStackTrace());
        } catch (SQLException throwables) {
            System.out.println(throwables.getStackTrace());
        }

        return null;
    }

    public String selectFromTable(String query){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            String result = "";
            while(resultSet.next()){
                result+="id:"+resultSet.getString(1)+"; name:"+resultSet.getString(2)+"; surname:"+resultSet.getString(3)+";\n";
            }
            statement.close();
            resultSet.close();
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public String PrepareSelect(int param){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from table1 where id=?");
            statement.setInt(1,param);
            ResultSet resultSet = statement.executeQuery();
            String result="";
            while(resultSet.next()){
                result+="id: "+resultSet.getString(1)+"; name:"+resultSet.getString(2)+"; surname:"+resultSet.getString(3)+";";
            }
            statement.close();
            resultSet.close();
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void InsertBatch(int id, String fName, String sName){
        try {
            PreparedStatement statement = connection.prepareStatement("insert table1 values (?,?,?)");
            for(int i=0;i<10;i++) {
                statement.setInt(1, id);
                statement.setString(2, fName);
                statement.setString(3, sName);
                statement.addBatch();
            }
            statement.executeBatch();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void CancelBatch(){
        try {
            Statement statement = connection.createStatement();
            statement.execute("delete from table1 where FirstName is null");
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateInfo(){
        try {
            Statement statement = connection.createStatement();
            statement.execute("update table1 set id = '0' where FirstName = 'Дарья'");
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String ExecuteStoredProcedure(){
        try {
            CallableStatement callableStatement = connection.prepareCall("dbo.GetAllRows");
            ResultSet resultSet = callableStatement.executeQuery();
            String result = "";
            while(resultSet.next()){
                result+="id: "+resultSet.getString(1)+"; name:"+resultSet.getString(2)+"; surname:"+resultSet.getString(3)+";";
            }
            callableStatement.close();
            resultSet.close();
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public String ExecuteStoredFunction(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select dbo.GetCountRows()");
            String result ="";
            while(resultSet.next()){
                result+=resultSet.getString(1);
            }
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }


}