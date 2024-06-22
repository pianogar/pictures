import java.awt.event.*;
import javax.swing.*;
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
    public static int type = Integer
            .parseInt(JOptionPane.showInputDialog(myJFrame, "Press 1 for auto, Press 2 for manual"));
    public static int num = Integer.parseInt(JOptionPane.showInputDialog(myJFrame, "Enter number of folders"));
    public static File[][] files = new File[num][0];
    public static File[] dir = new File[num];

    public static void main(String[] args) throws Exception {
        String[] data = new String[num];
        for (int i = 0; i < num; i++) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                data[i] = chooser.getSelectedFile().getAbsolutePath();
            } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                System.exit(0);
            }
            dir[i] = new File(data[i]);
        }
        for (int i = 0; i < num; i++) {
            files[i] = dir[i].listFiles();
        }
        if (type == 1) {
            auto();
        } else if (type == 2) {
            manual();
        }
    }

    public static String toString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }

    public static void auto() throws Exception {
        int sec = Integer.parseInt(JOptionPane.showInputDialog(myJFrame, "How many seconds per picture"));
        myJFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    next = true;
                    valid = false;
                } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
                    file.delete();
                    for (int i = 0; i < num; i++) {
                        files[i] = dir[i].listFiles();
                    }
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    next = true;
                } else if (keyCode == KeyEvent.VK_UP) {
                    next = false;
                }
            }
        });
        myJFrame.setVisible(true);

        ActionListener scroll = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!next) {
                    try {
                        real = true;
                        Runtime.getRuntime().exec("taskkill /IM Photos.exe /F");
                        while (real) {
                            ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
                            Process process = processBuilder.start();
                            String tasksList = pictures.toString(process.getInputStream());
                            if (!tasksList.contains("Photos.exe"))
                                real = false;
                        }
                        int rand = (int) (num * Math.random());
                        file = files[rand][(int) (files[rand].length * Math.random())];
                        desktop.open(file);
                        Thread.sleep(sec * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Timer timer1 = new Timer(100, scroll);
        timer1.start();
        while (1 == 1) {
            myJFrame.setVisible(true);
            if (!valid) {
                try {
                    Runtime.getRuntime().exec("taskkill /IM Photos.exe /F");
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void manual() throws Exception {
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
                    for (int i = 0; i < num; i++) {
                        files[i] = dir[i].listFiles();
                    }
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
                        int rand = (int) (num * Math.random());
                        file = files[rand][(int) (files[rand].length * Math.random())];
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

}
