package com.mycompany.dongho;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;

public class Dongho {
   
    private String name;
    private String time;
    private int duration;
    private static Connection connection;
    private static Timer timer; // cho dong ho
    
    public static void main(String[] args) {
        connectDB();
        UI();
    }
    
    public static void connectDB() {
        String url = "jdbc:mysql://127.0.0.1:3306/dongho";
        try {
            connection = DriverManager.getConnection(url, "root", "yuetl123");
            if (connection != null) {
                System.out.println("Connected!");
            } else {
                System.out.println("Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Dongho> getData(){
        List<Dongho> dataList = new ArrayList<>();
        String url = "jdbc:mysql://127.0.0.1:3306/dongho";
        
       try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Dongho")) {
            while (rs.next()) {
                Dongho dongho = new Dongho();
                dongho.name = rs.getString("Name");
                dongho.time = rs.getString("Time");
                dongho.duration = rs.getInt("Duration");
                dataList.add(dongho);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    
    public static void UI(){

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Dong Ho");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 450);
        frame.setLocationRelativeTo(null); // Đặt frame giữa màn hình
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(800, 450));

        // Clock panel placeholder
        JPanel clockContainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Sử dụng BorderLayout cho clockContainPanel
        clockContainPanel.setPreferredSize(new Dimension(450, 450)); 
        JPanel clockPanel = new JPanel() {
        @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                DongHo(g); // Gọi phương thức DongHo(Graphics g) để vẽ đồng hồ
            }
        };
        clockPanel.setPreferredSize(new Dimension(400, 400));
        clockPanel.setBackground(Color.WHITE);
        
        clockPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); 
        clockContainPanel.add(clockPanel);
        
        mainPanel.add(clockContainPanel, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setPreferredSize(new Dimension(320, 0)); 

        // Import data 
        JLabel importLabel = new JLabel("Import data");
        importLabel.setBounds(0, 20, 70, 25);
        controlPanel.add(importLabel);

        JTextField importTextField = new JTextField();
        importTextField.setBounds(80, 20, 100, 25);
        controlPanel.add(importTextField);

        JButton importButton = new JButton("Import file");
        importButton.setBounds(190, 20, 105, 25);
        controlPanel.add(importButton);

        // Keyword 
        JLabel keywordLabel = new JLabel("Keyword");
        keywordLabel.setBounds(0, 60, 70, 25);
        controlPanel.add(keywordLabel);

        JTextField keywordTextField = new JTextField();
        keywordTextField.setBounds(80, 60, 215, 25);
        controlPanel.add(keywordTextField);

        // Buttons section
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(0, 100, 95, 25);
        controlPanel.add(deleteButton);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(100, 100, 95, 25);
        controlPanel.add(searchButton);

        JButton showAllButton = new JButton("Show All");
        showAllButton.setBounds(200, 100, 95, 25);
        controlPanel.add(showAllButton);

        JTextArea searchResultsTextArea = new JTextArea(" Search result:");
        searchResultsTextArea.setBounds(0, 140, 295, 250);
        searchResultsTextArea.setEditable(false);
        controlPanel.add(searchResultsTextArea);

        mainPanel.add(controlPanel, BorderLayout.EAST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        startTimer(clockPanel);
    }
    
     public static void DongHo(Graphics g){
        int r = 120;
        int fs = 30;
        int of = 50;
        int cx = r+fs+of;
        int cy = r+fs+of;
        //Ve hinh tron
        g.setColor(Color.BLACK);
        g.drawOval(cx-r, cy-r, r*2, r*2);
        //Ve chu so the hien gio
        g.setFont(new Font("Arial",Font.BOLD,fs));
        FontMetrics fm = g.getFontMetrics();
        for (int i=0;i<12;i++) {
            double alpha = Math.PI*2/12*i - Math.PI/2;
            int x = (int)((r+fs)*Math.cos(alpha)) + cx;
            int y = (int)((r+fs)*Math.sin(alpha)) + cy;
            String str = i+"";
            if (i==0) str = "12";
            int w = fm.stringWidth(str);
            int h = fm.getHeight();
            g.drawString(str, x-w/2, y+h/3);
        }
        // Ve kim gio phut giay
        Date d = new Date();
        double s = d.getSeconds();
        double m = d.getMinutes()+s/60;
        double h = d.getHours()+m/60;
        double hms[] = {h/12.0,m/60.0,s/60.0};
        double len[] = {0.6,0.8,0.95};
        Color c[] = {Color.RED,Color.GREEN,Color.BLUE};
        for (int i=0;i<3;i++){
            double alpha = Math.PI*2*hms[i] - Math.PI/2;
            int x = (int)((r*len[i])*Math.cos(alpha)) + cx;
            int y = (int)((r*len[i])*Math.sin(alpha)) + cy;
            //Giay
            g.setColor(c[i]);
            g.drawLine(cx, cy, x, y);
        }
    }
    public static void startTimer(JPanel clockPanel) {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clockPanel.repaint(); 
            }
        });
        timer.start(); 
    }
}
