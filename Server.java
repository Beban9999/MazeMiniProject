import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame {
    public static JTextArea tas = new JTextArea();

    public Server(){
        setTitle("SERVER");
        setLocation(600, 10);
        setSize(400,750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(tas);
        tas.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        tas.append("Server pokrenut\n");
        tas.setBackground(Color.yellow);
        tas.setForeground(Color.blue);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Server();

        try {
            ServerSocket ss = new ServerSocket(9000);
            while(true){
                Socket sock = ss.accept();
                ServerThread st = new ServerThread(sock);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
