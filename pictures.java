import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.InputStream;
import java.awt.Desktop;

public class pictures {
    public static boolean valid = true;
    public static boolean next = false;
    public static boolean real = true;
    public static JFrame myJFrame = new JFrame();
    public static Desktop desktop = Desktop.getDesktop();
    public static File file = new File("");
    public static File[] files = new File[0];

    public static void main(String[] args) throws Exception {
        String data = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(chooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            data = chooser.getSelectedFile().getAbsolutePath();
        }
        File dir = new File(data);
        files = dir.listFiles();
        myJFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_RIGHT) {
                    next = false;
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    next = false;
                    valid = false;
                } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
                    file.delete();
                    files = dir.listFiles();
                }
            }
        });
        myJFrame.setVisible(true);

        ActionListener scroll = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!next) {
                    try {
                        next = true;
                        real = true;
                        Runtime.getRuntime().exec("taskkill /IM Photos.exe /F");
                        while (real) {
                            ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
                            Process process = processBuilder.start();
                            String tasksList = pictures.toString(process.getInputStream());
                            if (!tasksList.contains("Photos.exe"))
                                real = false;
                        }
                        file = files[(int) (files.length * Math.random())];
                        desktop.open(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (!valid) {
                    try {
                        Runtime.getRuntime().exec("taskkill /IM Photos.exe /F");
                        System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    myJFrame.setVisible(true);
                }
            }
        };

        Timer timer = new Timer(100, scroll);
        timer.start();
    }

    public static String toString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }

}
