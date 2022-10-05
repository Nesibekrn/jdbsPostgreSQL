import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        table tab=new table();
//        tab.createTable("teacher");

        try {
            //variable for making choice
            int choice=0;
            //calling the student class
            student s=new student();
            do {
                System.out.println("WELCOME TO STUDENT RECORD SYSTEM\n " +
                        "Please select the option from Menu\n " +
                        "1 - Student Registration\n " +
                        "2 - Password update\n " +
                        "3 - Delete Record\n " +
                        "4 - Search for a student\n " +
                        "5 - Show All Students\n " +
                        "6 - Exit Application");
                //input for selecting Menu
                Scanner ch=new Scanner(System.in);
                choice=ch.nextInt();
                switch (choice){
                    case 1:
                        s.getStudentDetails();
                        s.saveStudent();
                        break;
                    case 2:
                        s.updatePassword();
                        break;
                    case 3:
                        s.deleteStudent();
                        break;
                    case 4:
                        s.searchStudent();
                        break;
                    case 5:
                        s.showAllStudents();
                        break;
                    case 6:
                        //System.out.println("6 pressed");
                        break;
                    default:
                        System.out.println("Please enter the correct choice");
                }
            }while (choice!=6);{
                System.out.println("Thanks for using the Application!");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
//class for student record system
class student{
    private String name;
    private String email;
    private String password;
    private String country;
    private int marks;
    private int age;
    //making a method that takes user da= information
      public void getStudentDetails(){
        Scanner input=new Scanner(System.in);
        System.out.println("Enter Student Name: ");
        name=input.nextLine();
        System.out.println("Enter Student eMail: ");
        email=input.nextLine();
        System.out.println("Enter Student Password: ");
        password=input.nextLine();
        System.out.println("Enter Student Country: ");
        country=input.nextLine();
        System.out.println("Enter Student Marks: ");
        marks=input.nextInt();
        System.out.println("Enter Student Age: ");
        age=input.nextInt();
    }
    //making a method that takes the data from user and save it into database
    public void saveStudent() throws SQLException {
        //calling the database connection class
        dbmsconnection db=new dbmsconnection();
        Connection con=db.getConnection("postgres","Kahraman1.");

        String sql="insert into students values (?,?,?,?,?,?);";
        //preparedStatetments for getting the data from of the students repeatedly
        PreparedStatement stmt=con.prepareCall(sql);
        //saving the data to particular columns
        stmt.setString(1,name);
        stmt.setString(2,email);
        stmt.setString(3,password);
        stmt.setString(4,country);
        stmt.setInt(5,marks);
        stmt.setInt(6,age);

        //execute the statement
        stmt.executeUpdate();
        System.out.println("Data has been saved successfully!");
    }
//method for updating the password
    public void updatePassword() throws SQLException {
        //calling the database connection class
         dbmsconnection db=new dbmsconnection();
         Connection con=db.getConnection("postgres","Kahraman1.");


        //writing the sql query
        String sql="update students set password = ? where email = ?;";
        PreparedStatement stmt=con.prepareStatement(sql);
        Scanner input=new Scanner(System.in);

        System.out.println("Please enter your email");
        String input_email=input.nextLine();

        System.out.println("Enter new Password: ");
        String new_pass=input.nextLine();
        //saving the data to particular columns;
        stmt.setString(1,new_pass);
        stmt.setString(2,input_email);

        int i=stmt.executeUpdate();
        //checking if the email already exist in the database or not
        if(i>0){
            System.out.println("Password has been updated successfully!");
        }else{
            System.out.println("This email doesn't exist in the database");
        }
    }
    //making a method that delete the student record
    public void deleteStudent() throws SQLException {
        //calling the database connection class
        dbmsconnection db=new dbmsconnection();
        Connection con=db.getConnection("postgres","Kahraman1.");
        Scanner input=new Scanner(System.in);

        String sql="delete from students where email = ?;";
        PreparedStatement stmt=con.prepareStatement(sql);

        System.out.println("Please enter the students email");
        String input_email=input.nextLine();
        stmt.setString(1,input_email);

        int i=stmt.executeUpdate();
        //checking if the email already exist in the database or not
        if(i>0){
            System.out.println("Record has been deleted successfully!");
        }else{
            System.out.println("This email doesn't exist in the database");
        }
    }
    //making a method that search for a student
    public void searchStudent() throws SQLException {
        //calling the database connection class
        dbmsconnection db=new dbmsconnection();
        Connection con=db.getConnection("postgres","Kahraman1.");
        Scanner input=new Scanner(System.in);

        String sql="select * from students where name = ?;";
        PreparedStatement stmt=con.prepareStatement(sql);

        System.out.println("Please enter the Student Name");
        String input_name=input.nextLine();

        stmt.setString(1,input_name);
        ResultSet rs=stmt.executeQuery();
        while (rs.next()){
            //print all the columns
            System.out.print((rs.getString("name"))+" "+
                    (rs.getString("email"))+" "+
                    (rs.getString("password"))+" "+
                    (rs.getString("country"))+" "+
                    (rs.getInt("marks"))+" "+
                    (rs.getInt("age"))+"\n");
            }
    }
    //making a method that show all students
    public void showAllStudents() throws SQLException {
        //calling the database connection class
        dbmsconnection db=new dbmsconnection();
        Connection con=db.getConnection("postgres","Kahraman1.");

        String sql="select * from students;";
        PreparedStatement stmt=con.prepareStatement(sql);
        ResultSet rs=stmt.executeQuery();

        while (rs.next()){
            //print all the columns
            System.out.print((rs.getString("name"))+" "+
                    (rs.getString("email"))+" "+
                    (rs.getString("password"))+" "+
                    (rs.getString("country"))+" "+
                    (rs.getInt("marks"))+" "+
                    (rs.getInt("age"))+"\n");
        }
    }
}


class dbmsconnection{
    public Connection getConnection(String user,String pass){
        Connection conn = null;
        try{
            //load postgreSQL driver
            Class.forName("org.postgresql.Driver");
            //setting up the connection
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbc",user,pass);
            //passing condition to check the connection is successful or not
            if(conn !=null){
                System.out.println("Connection established!");
            }else{
                System.out.println("Connection failed");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }
}

class table{
    public void createTable(String table_name){
        dbmsconnection db=new dbmsconnection();
        Connection con=db.getConnection("postgres","Kahraman1.");

        Statement statement;
        try {
            String query="create table "+table_name+"(ID SERIAL , name varchar(200), email varchar(200), country varchar(200), primary key ( ID )) " ;
            statement=con.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table created!");
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}


