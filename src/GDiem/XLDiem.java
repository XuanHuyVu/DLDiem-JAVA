package GDiem;

import java.sql.*;

public class XLDiem {
    private static Connection cn;
    
    //Kết nối sql server
    public static void getCon() {
        try {
            cn = DriverManager.getConnection("jdbc:sqlserver://XUY\\SQLEXPRESS;database=DLDiem;user=sa;password=12345678;trustServerCertificate=true");
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println("Not Connected: " + e.getMessage());
        }
    }
    
    public static ResultSet getHV() {
        try {
            Statement st = cn.createStatement();
            return st.executeQuery("SELECT * FROM tbHocvien");
        } catch (SQLException e) {
            System.out.println("Error: "+ e.getMessage());
            return null;
        }
    }
    
    public static boolean insertHV(Hocvien hv) {
        try {
            PreparedStatement pst = cn.prepareStatement("Insert into tbHocvien (MaHV,Hoten,Lop,Diem) VALUES (?,?,?,?)");
            pst.setString(1, hv.getMaHV());
            pst.setString(2, hv.getHoten());
            pst.setString(3, hv.getLop());
            pst.setFloat(4, hv.getDiem());
            int res = pst.executeUpdate();
            return res>0;
        } catch (SQLException e) {
            System.out.println("Error: "+ e.getMessage());
            return false;
        }
    }
   
}
