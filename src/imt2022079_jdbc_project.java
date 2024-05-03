import java.sql.*;
import java.util.*;
public class imt2022079_jdbc_project {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/companydb?allowPublicKeyRetrieval=true&useSSL=false";
    static final String USER = "root";
    static final String PASSWORD = "12345";
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Connection conn = null;
        Statement stmt = null;
        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            department.init_update(stmt);
            while(true){
                System.out.println("1 -> employee\n2 -> department\n3 -> dept_loaction\n4 -> project\n5 -> works_on\n-1 -> exit");
                System.out.print("enter a table to perform operation : ");
                int choice = scan.nextInt();
                if(choice == -1){
                    break;
                }
                switch (choice) {
                    case 1:
                        employee.choose(scan, stmt);
                        break;
                    case 2:
                        department.choose(scan, stmt);
                        break;
                    case 3:
                        dept_loc.choose(scan, stmt);
                        break;
                    case 4:
                        project.choose(scan, stmt);
                        break;
                    case 5:
                        works.choose(scan, stmt);
                        break;
                    default:
                        System.out.println("enter a valid option");
                        break;
                }
                conn.commit();
            }
        }catch(SQLException se){
            se.printStackTrace();
            if(conn != null){
                try{
                    conn.rollback();
                }catch(SQLException se2){
                    se2.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(conn != null){conn.close();}
                if(stmt != null){stmt.close();}
                scan.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }
        }
    }
}
class employee {
    public employee(){}
    public static void choose(Scanner scan,Statement stmt) throws SQLException{
        System.out.println("1 -> insert\n2 -> update\n3 -> delete\n4 -> show all projects of employee\n5 -> see employee info");
        System.out.print("choose a operation : ");
        int c = scan.nextInt();
        String ssn;
        double salary = 0;
        int dno = 0;
        switch (c) {
            case 1:
                System.out.print("enter a name : ");
                scan.nextLine();
                String name = scan.nextLine();
                System.out.print("enter ssn : ");
                ssn = scan.next();
                System.out.print("enter address : ");
                scan.nextLine();
                String addr = scan.nextLine();
                System.out.print("enter sex [M/F] : ");
                char sex = scan.next().charAt(0);
                System.out.print("enter salary : ");
                salary = scan.nextDouble();
                System.out.print("enter department number : ");
                dno = scan.nextInt();
                System.out.print("enter if supervisor ssn must be null of not [1(yes)/anything else (no)] : ");
                if(scan.nextInt() == 1){
                    insert(name, ssn, addr, sex, salary, Optional.empty(), dno, stmt);
                }else{
                    System.out.print("enter supervisor's ssn : ");
                    insert(name, ssn, addr, sex, salary, Optional.of(scan.next()), dno, stmt);
                }
                break;
            case 2:
                System.out.print("enter ssn : ");
                ssn = scan.next();
                System.out.println("1 -> name\n2 -> address\n3 -> supervisor_ssn\n4 -> salary\n5 -> dno");
                System.out.print("choose what to change : ");
                int choice = scan.nextInt();
                String val = null;
                System.out.print("enter : ");
                if(choice == 4){
                    salary = scan.nextDouble();
                }else if(choice == 5){
                    dno = scan.nextInt();
                }else if(choice < 4 && choice > 0){
                    scan.nextLine();
                    val = scan.nextLine();
                }else{
                    System.out.println("enter valid choice");
                    return;
                }
                update(ssn, val, salary, dno, choice, stmt);
                break;
            case 3:
                System.out.print("enter ssn : ");
                ssn = scan.next();
                delete(ssn, stmt);
                break;
            case 4:
                System.out.print("enter ssn : ");
                ssn = scan.next();
                show_all_projects(ssn, stmt);
                break;
            case 5:
                System.out.print("enter ssn : ");
                ssn = scan.next();
                select(ssn, stmt);
                break;
            default:
                System.out.println("choose valid option");
                break;
        }
    }
    public static void insert(String name,String ssn,String addr,char sex,double salary,Optional <String> s_ssn,int dno,Statement stmt) throws SQLException {
        stmt.executeUpdate("insert into employee values('" + name+ "','"+ ssn + "','"+ addr +"','" + sex + "'," + String.format("%.2f",salary) + ", NULL ," + dno + ")");
        if(s_ssn.isPresent()){
            stmt.executeUpdate("update employee set super_ssn = '" + s_ssn.get() + "' where ssn = '" + ssn + "'");
        }
        stmt.executeUpdate("update department set no_of_employee = no_of_employee + 1 where dnumber = " + dno);
    }
    public static void update(String ssn,String val_str,double salary,int dno,int k,Statement stmt) throws SQLException {
        switch (k) {
            case 1:
                stmt.executeUpdate("update employee set name = '" + val_str + "' where ssn = '" + ssn + "'");
                break;
            case 2:
                stmt.executeUpdate("update employee set address = '" + val_str + "' where ssn = '" + ssn + "'");
                break;
            case 3:
                stmt.executeUpdate("update employee set super_ssn = '" + val_str + "' where ssn = '" + ssn + "'");
                break;
            case 4:
                stmt.executeUpdate("update employee set salary = " + String.format("%.2f", salary) + " where ssn = '" + ssn + "'");
                break;
            case 5:
                stmt.executeUpdate("update employee set dno = " + dno + " where ssn = '" + ssn + "'");
                break;
            default:
                System.out.println("choose an valid option");
                break;
        }
    }
    public static void delete(String ssn,Statement stmt) throws SQLException{
        ResultSet r = stmt.executeQuery("select dno from employee where ssn = '" + ssn + "'");
        r.next();
        int dno = r.getInt("dno");
        stmt.executeUpdate("update department set no_of_employee = no_of_employee - 1 where dnumber = " + dno);
        stmt.executeUpdate("delete from employee where ssn = '" + ssn + "'");
    }
    public static void show_all_projects(String e_ssn,Statement stmt) throws SQLException{
        ResultSet res = stmt.executeQuery("select w.pno,w.hours,p.pname,p.plocation from employee e inner join works_on w inner join project p on p.pnumber = w.pno on e.ssn = w.essn where e.ssn = '" + e_ssn + "'");
        while(res.next()){
            int pno = res.getInt("pno");
            double hours = res.getDouble("hours");
            String pname = res.getString("pname");
            String pLoc = res.getString("plocation");
            System.out.printf("%d | %-7s | %-20s | %-20s\n",pno,String.format("%.2f",hours),pname,pLoc);
        }
    }
    public static void select(String ssn,Statement stmt) throws SQLException {
        ResultSet r = stmt.executeQuery("select * from employee where ssn = '" + ssn + "'");
        r.next();
        String name = r.getString("name");
        String addr = r.getString("address");
        String sex = r.getString("sex");
        double salary = r.getDouble("salary");
        int dno = r.getInt("dno");
        String super_ssn = r.getString("super_ssn");
        System.out.printf("%-20s | %s | %-40s | %-2s | %-10s | %-10s | %d\n",name,ssn,addr,sex,String.format("%.2f", salary),super_ssn,dno);
    }
}
class department{
    public department(){}
    public static void choose(Scanner scan,Statement stmt) throws SQLException {
        System.out.println("1 -> insert\n2 -> update\n3 -> show info\n4 -> show employees of department");
        System.out.print("eneter your choice : ");
        int c = scan.nextInt();
        int dno = 0;
        switch (c) {
            case 1:
                System.out.print("enter department name : ");
                scan.nextLine();
                String name = scan.nextLine();
                System.out.print("enter department number : ");
                dno = scan.nextInt();
                System.out.print("enter department manager's ssn : ");
                String mssn = scan.next();
                insert(name, dno, mssn, stmt);
                break;
            case 2:
                System.out.print("enter department number : ");
                dno = scan.nextInt();
                System.out.println("1 -> dname\n2 -> mgr_ssn");
                System.out.print("choose what to modify : ");
                int choice = scan.nextInt();
                System.out.print("enter value : ");
                scan.nextLine();
                String val = scan.nextLine();
                update(dno, val, choice, stmt);
                break;
            case 3:
                System.out.print("enter department number : ");
                dno = scan.nextInt();
                select(dno, stmt);
                break;
            case 4:
                System.out.print("enter department number : ");
                dno = scan.nextInt();
                show_all_employees(dno, stmt);
                break;
            default:
                System.out.println("choose a valid option");
                break;
        }
    }
    public static void show_all_employees(int dno,Statement stmt) throws SQLException{
        ResultSet r = stmt.executeQuery("select ssn from employee where dno = " + dno );
        ArrayList <String> ssn_arr = new ArrayList<>();
        while(r.next()){
            ssn_arr.add(r.getString("ssn"));
        }
        for(String ssn : ssn_arr){
            employee.select(ssn, stmt);
        }
    }
    public static void init_update(Statement stmt) throws SQLException{
        ResultSet r = stmt.executeQuery("select dno,count(ssn) as 'ct' from employee group by dno");
        ArrayList <Integer> arr_dno = new ArrayList<>();
        ArrayList <Integer> arr_ct = new ArrayList<>();
        while(r.next()){
            arr_dno.add(r.getInt("dno"));
            arr_ct.add(r.getInt("ct"));
        }
        for(int i = 0;i<arr_ct.size();i++){
            stmt.executeUpdate("update department set no_of_employee = " + arr_ct.get(i) + " where dnumber = " + arr_dno.get(i));
        }
    }
    public static void update(int dno,String val_str,int k,Statement stmt) throws SQLException{
        switch (k) {
            case 1:
                stmt.executeUpdate("update department set dname = '" + val_str + "' where dnumber = " + dno);
                break;
            case 2:
                stmt.executeUpdate("update department set mgr_ssn = '" + val_str + "' where dnumber = " + dno );
                break;
            default :
                System.out.println("enter a valid option");
                return;
        }
    }
    public static void insert(String dname,int dno,String mssn,Statement stmt) throws SQLException{
        stmt.executeUpdate("insert into department values( '" + dname + "'," + dno + ",'" + mssn  + "',0)");
    }
    public static void select(int dno,Statement stmt) throws SQLException{
        ResultSet r = stmt.executeQuery("select dname,mgr_ssn,no_of_employee from department where dnumber = " + dno);
        r.next();
        String name = r.getString("dname");
        String ssn = r.getString("mgr_ssn");
        int no_of_employee = r.getInt("no_of_employee");
        System.out.printf("%-20s | %-9s | %d\n",name,ssn,no_of_employee);
    }
}
class dept_loc{
    public dept_loc(){}
    public static void choose(Scanner scan,Statement stmt) throws SQLException{
        System.out.println("1 -> insert\n2 -> delete\n3 -> show all locations of a department");
        System.out.printf("choose option :");
        int choice = scan.nextInt();
        int dno = 0;
        String loc = null;
        switch (choice) {
            case 1:
                System.out.print("enter the department number : ");
                dno = scan.nextInt();
                System.out.print("enter location : ");
                loc = scan.next();
                insert(dno, loc, stmt);
                break;
            case 2:
                System.out.print("enter the department number : ");
                dno = scan.nextInt();
                System.out.print("enter location : ");
                loc = scan.next();
                delete(dno, loc, stmt);
                break;
            case 3:
                System.out.print("enter the department number : ");
                dno = scan.nextInt();
                show_all_locs(dno, stmt);
                break;
            default:
                System.out.println("choose a valid option");
                break;
        }
    }
    public static void show_all_locs(int dno,Statement stmt) throws SQLException{
        ResultSet r = stmt.executeQuery("select dlocation from dept_locations where dnumber = " + dno); 
        while(r.next()){
            String loc = r.getString("dlocation");
            System.out.println(loc);
        }
    }
    public static void insert(int dno,String loc,Statement stmt) throws SQLException{
        stmt.executeUpdate("insert into dept_locations values( " + dno + ",'" + loc +"')");
    }
    public static void delete(int dno,String loc,Statement stmt) throws SQLException{
        stmt.executeUpdate("delete from dept_locations where dnumber = " + dno + " and dlocation = '" + loc +"'");
    }
}
class project {
    public project(){}
    public static void choose(Scanner scan,Statement stmt) throws SQLException {
        System.out.println("1 -> insert\n2 -> delete\n3 -> show all employees on a project\n4 -> project info");
        System.out.printf("choose option : ");
        int choose = scan.nextInt();
        int pid = 0;
        switch (choose) {
            case 1:
                System.out.printf("enter project name : ");
                String name = scan.next();
                System.out.printf("enter project number : ");
                pid = scan.nextInt();
                System.out.printf("enter project location : ");
                String loc = scan.next();
                System.out.printf("enter project department number :");
                int dno = scan.nextInt();
                insert(name, pid, loc, dno, stmt);
                break;
            case 2:
                System.out.printf("enter project number : ");
                pid = scan.nextInt();
                delete(pid, stmt);
                break;
            case 3:
                System.out.printf("enter project number : ");
                pid = scan.nextInt();
                show_all_employees(pid, stmt);
                break;
            case 4:
                System.out.printf("enter project number : ");
                pid = scan.nextInt();
                select(pid, stmt);
                break;
            default:
                System.out.println("enter a valid option");
                break;
        }
    }
    public static void insert(String name,int number,String loc,int dno,Statement stmt) throws SQLException{
        stmt.executeUpdate("insert into project values('" + name + "'," + number + ",'" + loc + "'," + dno + ")");
    }
    public static void show_all_employees(int pno,Statement stmt) throws SQLException{
        ResultSet r = stmt.executeQuery("select e.name,e.ssn,e.dno,w.hours,p.pname,p.plocation from employee e inner join works_on w inner join project p on p.pnumber = w.pno on w.essn = e.ssn where p.pnumber = " + pno);
        while(r.next()){
            String ename = r.getString("name");
            String ssn = r.getString("ssn");
            int dno = r.getInt("dno");
            double hours = r.getDouble("hours");
            String pname = r.getString("pname");
            String loc = r.getString("plocation");
            System.out.printf("%-20s | %-9s | %d | %-5s | %-15s | %s\n",ename,ssn,dno,String.format("%.2f", hours),pname,loc);
        }
    }
    public static void delete(int pno,Statement stmt) throws SQLException{
        stmt.executeUpdate("delete from project where pnumber = " + pno);
    }
    public static void select(int pno,Statement stmt) throws SQLException{
        ResultSet r = stmt.executeQuery("select pname,plocation,dnum from project where pnumber = " + pno);
        r.next();
        System.out.printf("%-20s | %-20s | %d\n",r.getString("pname"),r.getString("plocation"),r.getInt("dnum"));
    }
}
class works{
    public works(){};
    public static void choose(Scanner scan,Statement stmt) throws SQLException {
        System.out.println("1 -> insert\n2 -> delete\n3 -> update hours");
        System.out.printf("choose option : ");
        int choose = scan.nextInt();
        String ssn = null;
        int pno = 0;
        double hrs = 0.0;
        switch (choose) {
            case 1:
                System.out.printf("enter ssn : ");
                ssn = scan.next();
                System.out.printf("enter project number : ");
                pno = scan.nextInt();
                System.out.printf("enter number of hours : ");
                hrs = scan.nextDouble();
                insert(ssn, pno, hrs, stmt);
                break;
            case 2:
                System.out.printf("enter ssn : ");
                ssn = scan.next();
                System.out.printf("enter project number : ");
                pno = scan.nextInt();
                delete(ssn, pno, stmt);
                break;
            case 3:
                System.out.printf("enter ssn : ");
                ssn = scan.next();
                System.out.printf("enter project number : ");
                pno = scan.nextInt();
                System.out.printf("enter number of hours (new) : ");
                hrs = scan.nextDouble();
                edit_hours(ssn, pno, hrs, stmt);
                break;
            default:
                System.out.println("enter a valid operation");
                break;
        }
    }
    public static void insert(String ssn,int pno,double hrs,Statement stmt) throws SQLException {
        stmt.executeUpdate("insert into works_on values( '"+ ssn + "'," + pno + "," + String.format("%.2f", hrs) + ")");
    }
    public static void delete(String ssn,int pno,Statement stmt) throws SQLException{
        stmt.executeUpdate("delete from works_on where essn = '" + ssn + "' and pno = " + pno);
    }
    public static void edit_hours(String ssn,int pno,double hrs,Statement stmt) throws SQLException {
        stmt.executeUpdate("update works_on set hours = " + String.format("%.2f", hrs) + " where essn = '" + ssn + "' and pno = " + pno);
    }
}
