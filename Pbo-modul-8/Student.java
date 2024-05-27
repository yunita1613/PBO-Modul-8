/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Student extends JFrame {
    private JTextField tfNamaMK, tfSKS, tfNilai;
    private JTextField tfNama, tfNIM, tfProdi;
    private JButton btnAdd, btnUpdate, btnDelete, btnView;
    private JTable table;
    private DefaultTableModel tableModel;

    public Student() {
        setTitle("Data Student");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupUI();
        setupEventHandlers();
    }

    private void setupUI() {
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Data Mahasiswa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nama
        gbc.gridx = 0; gbc.gridy = 0;
        panelInput.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        tfNama = new JTextField(20);
        panelInput.add(tfNama, gbc);

        // NIM
        gbc.gridx = 0; gbc.gridy = 1;
        panelInput.add(new JLabel("NIM:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        tfNIM = new JTextField(20);
        panelInput.add(tfNIM, gbc);

        // Prodi
        gbc.gridx = 0; gbc.gridy = 2;
        panelInput.add(new JLabel("Prodi:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        tfProdi = new JTextField(20);
        panelInput.add(tfProdi, gbc);

        // Nama Mata Kuliah
        gbc.gridx = 0; gbc.gridy = 3;
        panelInput.add(new JLabel("Nama Mata Kuliah:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        tfNamaMK = new JTextField(20);
        panelInput.add(tfNamaMK, gbc);

        // SKS
        gbc.gridx = 0; gbc.gridy = 4;
        panelInput.add(new JLabel("SKS:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        tfSKS = new JTextField(20);
        panelInput.add(tfSKS, gbc);

        // Nilai
        gbc.gridx = 0; gbc.gridy = 5;
        panelInput.add(new JLabel("Nilai:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        tfNilai = new JTextField(20);
        panelInput.add(tfNilai, gbc);

        // Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnView = new JButton("View");

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnView);

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(panelInput, BorderLayout.CENTER);
        panelTop.add(panelButtons, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "NIM", "Prodi", "Nama Mata Kuliah", "SKS", "Nilai"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnView.addActionListener(e -> viewData());
    }

    private void addStudent() {
        try (Connection con = Connection1.getConnection1()) {
            String sqlStudent = "INSERT INTO Student (nama, nim, prodi) VALUES (?, ?, ?)";
            try (PreparedStatement psStudent = con.prepareStatement(sqlStudent, Statement.RETURN_GENERATED_KEYS)) {
                psStudent.setString(1, tfNama.getText());
                psStudent.setString(2, tfNIM.getText());
                psStudent.setString(3, tfProdi.getText());
                psStudent.executeUpdate();

                ResultSet rs = psStudent.getGeneratedKeys();
                int mahasiswaId = -1;
                if (rs.next()) {
                    mahasiswaId = rs.getInt(1);
                }

                String sqlMatakuliah = "INSERT INTO Matakuliah (nama_mk, sks) VALUES (?, ?)";
                try (PreparedStatement psMatakuliah = con.prepareStatement(sqlMatakuliah, Statement.RETURN_GENERATED_KEYS)) {
                    psMatakuliah.setString(1, tfNamaMK.getText());
                    psMatakuliah.setString(2, tfSKS.getText());
                    psMatakuliah.executeUpdate();

                    rs = psMatakuliah.getGeneratedKeys();
                    int matakuliahId = -1;
                    if (rs.next()) {
                        matakuliahId = rs.getInt(1);
                    }

                    String sqlNilai = "INSERT INTO Nilai (mahasiswa_id, matakuliah_id, nilai) VALUES (?, ?, ?)";
                    try (PreparedStatement psNilai = con.prepareStatement(sqlNilai)) {
                        psNilai.setInt(1, mahasiswaId);
                        psNilai.setInt(2, matakuliahId);
                        psNilai.setInt(3, Integer.parseInt(tfNilai.getText()));
                        psNilai.executeUpdate();
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");
            viewData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateStudent() {
        try (Connection con = Connection1.getConnection1()) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih Data yang Akan Diupdate");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);

            String sqlStudent = "UPDATE Student SET nama = ?, nim = ?, prodi = ? WHERE id = ?";
            try (PreparedStatement psStudent = con.prepareStatement(sqlStudent)) {
                psStudent.setString(1, tfNama.getText());
                psStudent.setString(2, tfNIM.getText());
                psStudent.setString(3, tfProdi.getText());
                psStudent.setInt(4, id);
                psStudent.executeUpdate();
            }

            String sqlMatakuliah = "UPDATE Matakuliah SET nama_mk = ?, sks = ? WHERE id = (SELECT matakuliah_id FROM nilai WHERE mahasiswa_id = ?)";
            try (PreparedStatement psMatakuliah = con.prepareStatement(sqlMatakuliah)) {
                psMatakuliah.setString(1, tfNamaMK.getText());
                psMatakuliah.setString(2, tfSKS.getText());
                psMatakuliah.setInt(3, id);
                psMatakuliah.executeUpdate();
            }

            String sqlNilai = "UPDATE Nilai SET nilai = ? WHERE mahasiswa_id = ?";
            try (PreparedStatement psNilai = con.prepareStatement(sqlNilai)) {
                psNilai.setInt(1, Integer.parseInt(tfNilai.getText()));
                psNilai.setInt(2, id);
                psNilai.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate");
            viewData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteStudent() {
        try (Connection con = Connection1.getConnection1()) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih Data yang Akan Dihapus");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);

            String sqlNilai = "DELETE FROM Nilai WHERE mahasiswa_id = ?";
            try (PreparedStatement psNilai = con.prepareStatement(sqlNilai)) {
                psNilai.setInt(1, id);
                psNilai.executeUpdate();
            }

            String sqlMatakuliah = "DELETE FROM Matakuliah WHERE id = (SELECT matakuliah_id FROM nilai WHERE mahasiswa_id = ?)";
            try (PreparedStatement psMatakuliah = con.prepareStatement(sqlMatakuliah)) {
                psMatakuliah.setInt(1, id);
                psMatakuliah.executeUpdate();
            }

            String sqlStudent = "DELETE FROM Student WHERE id = ?";
            try (PreparedStatement psStudent = con.prepareStatement(sqlStudent)) {
                psStudent.setInt(1, id);
                psStudent.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
            viewData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void viewData() {
        try (Connection con = Connection1.getConnection1();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT m.id, m.nama, m.nim, m.prodi, mk.nama_mk, mk.sks, n.nilai " +
                     "FROM Student m " +
                     "JOIN Nilai n ON m.id = n.mahasiswa_id " +
                     "JOIN Matakuliah mk ON n.matakuliah_id = mk.id")) {
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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Student().setVisible(true));
    }
}

class Connection1 {
    public static Connection getConnection1() throws SQLException {
        // Implement the connection logic
        return DriverManager.getConnection("jdbc:pbo", "username", "password");
    }
}
