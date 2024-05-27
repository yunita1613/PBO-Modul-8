/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pbo;

/**
 *
 * @author ASUS
 */
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Crud extends JFrame {
    private JTextField tfNamaMK, tfSKS, tfNilai;
    private JTextField tfNama, tfNIM, tfProdi;
    private JButton btnAdd, btnUpdate, btnDelete, btnView;
    private JTable table;
    private DefaultTableModel tableModel;

    public Crud() {
        setTitle("Data Student");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(20, 20, 100, 25);
        add(lblNama);

        tfNama = new JTextField();
        tfNama.setBounds(120, 20, 200, 25);
        add(tfNama);

        JLabel lblNIM = new JLabel("NIM:");
        lblNIM.setBounds(20, 60, 100, 25);
        add(lblNIM);

        tfNIM = new JTextField();
        tfNIM.setBounds(120, 60, 200, 25);
        add(tfNIM);

        JLabel lblNamaMK = new JLabel("Nama Mata Kuliah:");
        lblNamaMK.setBounds(20, 100, 150, 25);
        add(lblNamaMK);

        tfNamaMK = new JTextField();
        tfNamaMK.setBounds(170, 100, 200, 25);
        add(tfNamaMK);

        JLabel lblSKS = new JLabel("SKS:");
        lblSKS.setBounds(20, 140, 100, 25);
        add(lblSKS);

        tfSKS = new JTextField();
        tfSKS.setBounds(170, 140, 200, 25);
        add(tfSKS);

        JLabel lblProdi = new JLabel("Prodi:");
        lblProdi.setBounds(20, 180, 100, 25);
        add(lblProdi);

        tfProdi = new JTextField();
        tfProdi.setBounds(120, 180, 200, 25);
        add(tfProdi);

        JLabel lblNilai = new JLabel("Nilai:");
        lblNilai.setBounds(20, 220, 100, 25);
        add(lblNilai);

        tfNilai = new JTextField();
        tfNilai.setBounds(120, 220, 200, 25);
        add(tfNilai);

        btnAdd = new JButton("Add");
        btnAdd.setBounds(20, 260, 80, 25);
        add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(110, 260, 80, 25);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(200, 260, 80, 25);
        add(btnDelete);

        btnView = new JButton("View");
        btnView.setBounds(290, 260, 80, 25);
        add(btnView);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "NIM", "Prodi", "Nama Mata Kuliah", "SKS", "Nilai"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 300, 740, 250);
        add(scrollPane);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = Connection1.getConnection1();
                    String sqlStudent = "INSERT INTO student (nama, nim, prodi) VALUES (?, ?, ?)";
                    String sqlMatakuliah = "INSERT INTO matakuliah (nama_mk, sks) VALUES (?, ?)";
                    String sqlNilai = "INSERT INTO nilai (mahasiswa_id, matakuliah_id, nilai) VALUES (?, ?, ?)";
                    
                    try (PreparedStatement psStudent = con.prepareStatement(sqlStudent, Statement.RETURN_GENERATED_KEYS);
                         PreparedStatement psMatakuliah = con.prepareStatement(sqlMatakuliah, Statement.RETURN_GENERATED_KEYS);
                         PreparedStatement psNilai = con.prepareStatement(sqlNilai)) {
                        
                        psStudent.setString(1, tfNama.getText());
                        psStudent.setString(2, tfNIM.getText());
                        psStudent.setString(3, tfProdi.getText());
                        psStudent.executeUpdate();
                        
                        ResultSet rsStudent = psStudent.getGeneratedKeys();
                        int mahasiswaId = -1;
                        if (rsStudent.next()) {
                            mahasiswaId = rsStudent.getInt(1);
                        }
                        
                        psMatakuliah.setString(1, tfNamaMK.getText());
                        psMatakuliah.setString(2, tfSKS.getText());
                        psMatakuliah.executeUpdate();
                        
                        ResultSet rsMatakuliah = psMatakuliah.getGeneratedKeys();
                        int matakuliahId = -1;
                        if (rsMatakuliah.next()) {
                            matakuliahId = rsMatakuliah.getInt(1);
                        }
                        
                        psNilai.setInt(1, mahasiswaId);
                        psNilai.setInt(2, matakuliahId);
                        psNilai.setInt(3, Integer.parseInt(tfNilai.getText()));
                        psNilai.executeUpdate();
                    }
                    
                    JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");
                    viewData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    System.out.println(ex.getMessage());
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Pilih Data yang Akan Diupdate");
                        return;
                    }
                    int id = (int) tableModel.getValueAt(row, 0);
                    Connection con = Connection1.getConnection1();
                    
                    String sqlStudent = "UPDATE Student SET nama = ?, nim = ?, prodi = ? WHERE id = ?";
                    String sqlMatakuliah = "UPDATE matakuliah SET nama_mk = ?, sks = ? WHERE id = (SELECT matakuliah_id FROM nilai WHERE mahasiswa_id = ?)";
                    String sqlNilai = "UPDATE nilai SET nilai = ? WHERE mahasiswa_id = ?";
                    
                    try (PreparedStatement psStudent = con.prepareStatement(sqlStudent);
                         PreparedStatement psMatakuliah = con.prepareStatement(sqlMatakuliah);
                         PreparedStatement psNilai = con.prepareStatement(sqlNilai)) {
                        
                        psStudent.setString(1, tfNama.getText());
                        psStudent.setString(2, tfNIM.getText());
                        psStudent.setString(3, tfProdi.getText());
                        psStudent.setInt(4, id);
                        psStudent.executeUpdate();
                        
                        psMatakuliah.setString(1, tfNamaMK.getText());
                        psMatakuliah.setString(2, tfSKS.getText());
                        psMatakuliah.setInt(3, id);
                        psMatakuliah.executeUpdate();
                        
                        psNilai.setInt(1, Integer.parseInt(tfNilai.getText()));
                        psNilai.setInt(2, id);
                        psNilai.executeUpdate();
                    }
                    
                    JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate");
                    viewData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Pilih Data yang Akan Dihapus");
                        return;
                    }
                    int id = (int) tableModel.getValueAt(row, 0);
                    Connection con = Connection1.getConnection1();
                    
                    String sqlNilai = "DELETE FROM nilai WHERE mahasiswa_id = ?";
                    String sqlMatakuliah = "DELETE FROM matakuliah WHERE id = (SELECT matakuliah_id FROM nilai WHERE mahasiswa_id = ?)";
                    String sqlStudent = "DELETE FROM mahasiswa WHERE id = ?";
                    
                    try (PreparedStatement psNilai = con.prepareStatement(sqlNilai);
                         PreparedStatement psMatakuliah = con.prepareStatement(sqlMatakuliah);
                         PreparedStatement psStudent = con.prepareStatement(sqlStudent)) {
                        
                        psNilai.setInt(1, id);
                        psNilai.executeUpdate();
                        
                        psMatakuliah.setInt(1, id);
                        psMatakuliah.executeUpdate();
                        
                        psStudent.setInt(1, id);
                        psStudent.executeUpdate();
                    }
                    
                    JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
                    viewData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        btnView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewData();
            }
        });
    }

    private void viewData() {
        try {
            Connection con = Connection1.getConnection1();
            String sql = "SELECT m.id, m.nama, m.nim, m.prodi, mk.nama_mk, mk.sks, n.nilai " +
                         "FROM Student m " +
                         "JOIN Nilai n ON m.id = n.mahasiswa_id " +
                         "JOIN Matakuliah mk ON n.matakuliah_id = mk.id";
            
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                tableModel.setRowCount(0);
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("nim"),
                        rs.getString("prodi"),
                        rs.getString("nama_mk"),
                        rs.getString("sks"),
                        rs.getInt("nilai")
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Crud().setVisible(true);
            }
        });
    }
}
