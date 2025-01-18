package FrontEnd;


import backEnd.decompress;
import backEnd.compress;
import backEnd.huffmanNode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class driver extends JFrame implements ActionListener {
    static public File opened_file;
    static long past, future;
    static double percentage;
    static JLabel curFile, afterzip, cursize, aftersize;
    static JPanel buttonPanel, titlePanel, scorePanel;
    static JButton ZH, UH, EX;
    static decompress d1;
    static compress h1;
    static JLabel perc1,perc2;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    public JPanel createContentPane() {
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);

        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setLocation(100, 20);
        titlePanel.setSize(200, 100);
        totalGUI.add(titlePanel);

        curFile = new JLabel("Selected File Size: ");
        curFile.setLocation(43, 30);
        curFile.setSize(150, 30);
        curFile.setHorizontalAlignment(0);
        titlePanel.add(curFile);

        perc1 = new JLabel("Percentage : ");
        perc1.setLocation(50,5);
        perc1.setSize(100,28);
        perc1.setHorizontalAlignment(0);
        titlePanel.add(perc1);

        afterzip = new JLabel("After zip/unzip the file size: ");
        afterzip.setLocation(10, 60);
        afterzip.setSize(200, 30);
        afterzip.setHorizontalAlignment(0);
        titlePanel.add(afterzip);

        scorePanel = new JPanel();
        scorePanel.setLayout(null);
        scorePanel.setLocation(320, 20);
        scorePanel.setSize(600, 100);
        totalGUI.add(scorePanel);

        cursize = new JLabel("");
        cursize.setLocation(0, 30);
        cursize.setSize(100, 30);
        cursize.setHorizontalAlignment(0);
        scorePanel.add(cursize);

        aftersize = new JLabel("");
        aftersize.setLocation(0, 60);
        aftersize.setSize(100, 30);
        aftersize.setHorizontalAlignment(0);
        scorePanel.add(aftersize);

        perc2 = new JLabel("");
        perc2.setLocation(0,0);
        perc2.setSize(100,28);
        perc2.setHorizontalAlignment(0);
        scorePanel.add(perc2);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(10, 130);
        buttonPanel.setSize(5200, 150);
        totalGUI.add(buttonPanel);

        ZH = new JButton("ZIP");
        ZH.setLocation(95, 20);
        ZH.setSize(200, 30);
        ZH.addActionListener(this);
        buttonPanel.add(ZH);

        UH = new JButton("UNZIP");
        UH.setLocation(290, 20);
        UH.setSize(200, 30);
        UH.addActionListener(this);
        buttonPanel.add(UH);

        EX = new JButton("EXIT");
        EX.setLocation(150, 100);
        EX.setSize(250, 30);
        EX.addActionListener(this);
        buttonPanel.add(EX);

        totalGUI.setOpaque(true);
        return totalGUI;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ZH) {
            try {
                h1 = new compress(opened_file.getPath());
                h1.Compress(opened_file.getPath());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(null, "Zipping Finished");
            future =h1.fileLength ;
            aftersize.setText(future + "Bytes");
            cursize.setText(opened_file.length() + "Bytes");
            percentage = 100 - ((double) future /opened_file.length()) *100;

            perc2.setText(df.format(percentage)+"% comp");
            System.out.println(percentage);
        }  else if (e.getSource() == UH) {
            JOptionPane.showMessageDialog(null,
                    "UnZipping Finished");
            try {
                d1 = new decompress(opened_file.getPath());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            cursize.setText(opened_file.length() + "Bytes");
            String s = opened_file.getPath();
            s = s.substring(0, s.length() - 6);
            future = d1.getFileLen();
            aftersize.setText(future + "Bytes");
        }  else if (e.getSource() == EX) {
            System.exit(0);
        }
    }

    private static void create() {
        JFrame frame = new JFrame("File Compressor");
        driver demo = new driver();
        frame.setContentPane(demo.createContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(400, 170, 750, 350);
        frame.setVisible(true);
        JMenu fileMenu = new JMenu("File");
        JMenuBar bar = new JMenuBar();
        frame.setJMenuBar(bar);
        bar.add(fileMenu);
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        openItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        if (fileChooser.showOpenDialog(frame) == JFileChooser.OPEN_DIALOG) {
                            opened_file = fileChooser.getSelectedFile();
                            if(opened_file!=null){
                                past = opened_file.length();
//                                perc2.setText(percentage+"%");
                            }else {

                            }
                            cursize.setText(past + "Bytes");
                            aftersize.setText("NotYetCalculated");
                        }
                    }
                });

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        frame.dispose();
                    }
                });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                create();
            }
        });
    }

}
