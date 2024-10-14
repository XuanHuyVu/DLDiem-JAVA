package GDiem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class GUI_insertHV extends JFrame implements ActionListener, MouseListener {
    private JTextField tfMaHV;
    private JTextField tfHoten;
    private JComboBox<String> cbLop;
    private JTextField tfDiem;
    private JButton btAdd;
    private JButton btEdit;
    private JButton btDelete;
    private JButton btSearch;
    private JButton btSave;
    private DefaultTableModel dfModel;
    private JTable tb;
    private int selectedRow = -1;

    public GUI_insertHV() {
        setTitle("CHƯƠNG TRÌNH QUẢN LÝ ĐIỂM");
        setSize(1000, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        BuildGUI();
    }

    private void BuildGUI() {
        JPanel pnLeft = new JPanel(new GridBagLayout());
        pnLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lbMaHV = new JLabel("Mã học viên: ");
        pnLeft.add(lbMaHV, gbc);

        gbc.gridx = 1;
        tfMaHV = new JTextField();
        tfMaHV.setPreferredSize(new Dimension(300, 30));
        pnLeft.add(tfMaHV, gbc);

        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lbHoten = new JLabel("Họ tên: ");
        pnLeft.add(lbHoten, gbc);

        gbc.gridx = 1;
        tfHoten = new JTextField();
        tfHoten.setPreferredSize(new Dimension(300, 30));
        pnLeft.add(tfHoten, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lbLop = new JLabel("Lớp: ");
        pnLeft.add(lbLop, gbc);

        gbc.gridx = 1;
        cbLop = new JComboBox<>(new String[] {"62TH1", "62PM1", "62PM2"});
        cbLop.setPreferredSize(new Dimension(300, 30));
        pnLeft.add(cbLop, gbc);

        // Điểm tổng kết
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lbDiem = new JLabel("Điểm tổng kết: ");
        pnLeft.add(lbDiem, gbc);

        gbc.gridx = 1;
        tfDiem = new JTextField();
        tfDiem.setPreferredSize(new Dimension(300, 30));
        pnLeft.add(tfDiem, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel pnLeftBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btAdd = new JButton("Thêm");
        btEdit = new JButton("Sửa");
        btDelete = new JButton("Xóa");
        btSearch = new JButton("Tìm kiếm");
        btSave = new JButton("Lưu");
        pnLeftBottom.add(btAdd);
        pnLeftBottom.add(btEdit);
        pnLeftBottom.add(btDelete);
        pnLeftBottom.add(btSearch);
        pnLeftBottom.add(btSave);
        pnLeft.add(pnLeftBottom, gbc);
        
        JPanel pnLeftContainer  = new JPanel(new BorderLayout());
        pnLeftContainer.add(pnLeft, BorderLayout.NORTH);

        JPanel pnRight = new JPanel(new GridLayout(1, 1));
        String[] headers = {"Mã học viên", "Họ tên", "Lớp", "Điểm"};
        dfModel = new DefaultTableModel(headers, 0);
        tb = new JTable(dfModel);
        tb.addMouseListener(this);
        pnRight.add(new JScrollPane(tb));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnLeftContainer, pnRight);
        splitPane.setDividerLocation(400);
        add(splitPane);

        // Bắt sự kiện thêm (không lưu database)
        btAdd.addActionListener((var e) -> {
            String maHV = tfMaHV.getText();
            String hoten = tfHoten.getText();
            String lop = cbLop.getSelectedItem().toString();
            String diem = tfDiem.getText();

            try {
                //float diemStr = Float.parseFloat(diem);  // Parse diem as float

                dfModel.addRow(new String[]{maHV, hoten, lop, String.valueOf(diem)});

                tfMaHV.setText("");
                tfHoten.setText("");
                cbLop.setSelectedIndex(0);
                tfDiem.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Điểm tổng kết phải là số thực hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Bắt sự kiện sửa
        btEdit.addActionListener((var e) -> {
            if (selectedRow != -1) {
                dfModel.setValueAt(tfMaHV.getText(), selectedRow, 0);
                dfModel.setValueAt(tfHoten.getText(), selectedRow, 1);
                dfModel.setValueAt(cbLop.getSelectedItem().toString(), selectedRow, 2);  // Changed to get from JComboBox
                dfModel.setValueAt(tfDiem.getText(), selectedRow, 3);

                tfMaHV.setText("");
                tfHoten.setText("");
                cbLop.setSelectedIndex(0);
                tfDiem.setText("");
                selectedRow = -1;
            } else {
                JOptionPane.showMessageDialog(null, "Hãy chọn một dòng để sửa.");
            }
        });

        // Bắt sự kiện xóa
        btDelete.addActionListener((var e) -> {
            if (selectedRow != -1) {
                dfModel.removeRow(selectedRow);
                tfMaHV.setText("");
                tfHoten.setText("");
                cbLop.setSelectedIndex(0);
                tfDiem.setText("");
                selectedRow = -1;
            } else {
                JOptionPane.showMessageDialog(null, "Hãy chọn một dòng để xóa.");
            }
        });

        // Bắt sự kiện tìm kiếm
        btSearch.addActionListener((var e) -> {
            String searchMaHV = tfMaHV.getText().toLowerCase();
            boolean found = false;

            for (int i = 0; i < dfModel.getRowCount(); i++) {
                String maHV = dfModel.getValueAt(i, 0).toString().toLowerCase();
                if (maHV.equals(searchMaHV)) {
                    tb.setRowSelectionInterval(i, i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy học viên với mã: " + searchMaHV);
            }
        });

        // Bắt sự kiện lưu
        btSave.addActionListener((var e) -> {
            try {
                for (int i = 0; i < dfModel.getRowCount(); i++) {
                    String maHV = dfModel.getValueAt(i, 0).toString();
                    String hoten = dfModel.getValueAt(i, 1).toString();
                    String lop = dfModel.getValueAt(i, 2).toString();
                    String diemStr = dfModel.getValueAt(i, 3).toString();

                    try {
                        float diem = Float.parseFloat(diemStr);
                        XLDiem.insertHV(new Hocvien(maHV, hoten, lop, diem));

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Điểm của học viên " + maHV + " không hợp lệ: " + diemStr, "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                    }
                }
                JOptionPane.showMessageDialog(null, "Lưu dữ liệu thành công!");
            } catch (HeadlessException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi lưu dữ liệu: " + ex.getMessage());
            }
        });

        loadData(dfModel);
    }

    public static void main(String[] args) {
        XLDiem.getCon();
        new GUI_insertHV().setVisible(true);
    }

    private void loadData(DefaultTableModel dfModel) {
        try {
            ResultSet res = XLDiem.getHV();
            dfModel.setRowCount(0);
            dfModel.fireTableDataChanged();
            if (res != null) {
                while (res.next()) {
                    dfModel.addRow(new String[]{
                            res.getString("MaHV"),
                            res.getString("Hoten"),
                            res.getString("Lop"),
                            res.getString("Diem"),
                    });
                }
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int rowIndex = tb.getSelectedRow();
        if (rowIndex != -1) {
            selectedRow = rowIndex;
            tfMaHV.setText(dfModel.getValueAt(rowIndex, 0).toString());
            tfHoten.setText(dfModel.getValueAt(rowIndex, 1).toString());
            cbLop.setSelectedItem(dfModel.getValueAt(rowIndex, 2).toString());
            tfDiem.setText(dfModel.getValueAt(rowIndex, 3).toString());
        }
    }

    // Other MouseListener events
    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void actionPerformed(ActionEvent e) { }
}
