package GDiem;

import java.sql.*;

public class XLDiem {
    private static Connection cn;

    // Kết nối SQL Server
    public static void getCon() {
        if (cn == null) {
            try {
                cn = DriverManager.getConnection("jdbc:sqlserver://XUY\\SQLEXPRESS;database=DLDiem;user=sa;password=12345678;trustServerCertificate=true");
                System.out.println("Connected");
            } catch (SQLException e) {
                System.out.println("Not Connected: " + e.getMessage());
            }
        }
    }

    public static ResultSet getHV() {
        getCon();
        try {
            Statement st = cn.createStatement();
            return st.executeQuery("SELECT * FROM tbHocvien");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static boolean insertHV(Hocvien hv) {
        getCon();
        try (PreparedStatement checkStmt = cn.prepareStatement("SELECT COUNT(*) FROM tbHocvien WHERE MaHV = ?")) {
            checkStmt.setString(1, hv.getMaHV());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Duplicate entry found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error checking for duplicates
        }

        // Proceed to insert if no duplicates
        try (PreparedStatement insertStmt = cn.prepareStatement("INSERT INTO tbHocvien (MaHV, Hoten, Lop, Diem) VALUES (?, ?, ?, ?)")) {
            insertStmt.setString(1, hv.getMaHV());
            insertStmt.setString(2, hv.getHoten());
            insertStmt.setString(3, hv.getLop());
            insertStmt.setFloat(4, hv.getDiem());
            insertStmt.executeUpdate();
            return true; // Insertion successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Insertion failed
        }
    }
}
