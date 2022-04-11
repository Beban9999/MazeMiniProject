import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Klijent extends JFrame {
    JLabel naslov = new JLabel("LAVIRINT",SwingConstants.CENTER);
    JLabel naslov1 = new JLabel("PUT JE RAZMAK,ZID JE #, ROBOT JE R, A IZLAZ JE E",SwingConstants.CENTER);

    JPanel tabla = new JPanel();

    JButton rob = new JButton("ROBOT");
    JButton tim = new JButton("VREME");

    JLabel odg = new JLabel("ODGOVORI SERVERA",SwingConstants.CENTER);
    JTextArea ta = new JTextArea();

    JLabel time = new JLabel("VREME",SwingConstants.CENTER);

    public Klijent() {
        setSize(600, 750);
        setLocation(10,10);
        setTitle("KLIJENT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setComponents();
        setVisible(true);
    }

    class Nit extends Thread{
        public Nit() {
            start();
        }

        public void run() {
            while(true){
                SimpleDateFormat sd = new SimpleDateFormat("hh:mm:ss");
                String stime = sd.format(new Date()).toString();

                time.setText(stime);

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



        }
    }

    public void setComponents() {
        this.setLayout(new GridBagLayout());
        tabla.setLayout(new GridLayout(9,9));

        GridBagConstraints gbc = new GridBagConstraints();
        int y = 0;

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = y++;
        naslov.setBackground(Color.blue);
        naslov.setForeground(Color.yellow);
        naslov.setOpaque(true);
        naslov.setPreferredSize(new Dimension(580, 40));
        getContentPane().add(naslov, gbc);

        gbc.gridx = 0;
        gbc.gridy = y++;
        naslov1.setBackground(Color.blue);
        naslov1.setForeground(Color.yellow);
        naslov1.setOpaque(true);
        naslov1.setPreferredSize(new Dimension(580, 40));
        getContentPane().add(naslov1, gbc);


        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = y++;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                JTextField tt = new JTextField();
                tt.setHorizontalAlignment(SwingConstants.CENTER);
                tt.setFont(new Font("Arial", Font.BOLD, 16));

                if(i == 0 || i == 8 || j == 0 || j == 8){
                    tt.setText("#");
                }
                if((j == 2 || j == 4 || j == 6) && (i > 3 && i < 7 )){
                    tt.setText("#");
                }
                if(i == 3 && j == 2) tt.setText("#");

                if((i == 1) && (j == 4 || j == 5 || j == 6)) tt.setText("#");
                tabla.add(tt, i,j);
            }
        }
        tabla.setPreferredSize(new Dimension(300,300));


        getContentPane().add(tabla, gbc);


        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = y++;
        rob.setBackground(Color.red);
        rob.setForeground(Color.yellow);
        rob.setPreferredSize(new Dimension(580, 45));
        getContentPane().add(rob, gbc);
        rob.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket sock = new Socket("localhost", 9000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())),true);

                    String req = "";
                    for(Component comp : tabla.getComponents()){
                        if(((JTextField)comp).getText().equals("")) req+= " ";
                        else req += ((JTextField)comp).getText();
                    }




                    out.println(req);
                    String odg = in.readLine();

                    ta.append("[SERVER] :\t" +odg+"\n");

                    String[] odgs = odg.split("", 50);

                    for(String i : odgs){
                        System.out.println(i);
                    }


                    sock.close();
                    in.close();
                    out.close();



                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = y++;
        tim.setPreferredSize(new Dimension(580, 45));
        tim.setBackground(Color.green);
        tim.setForeground(Color.yellow);
        getContentPane().add(tim, gbc);
        tim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Socket sock = new Socket("localhost", 9000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())),true);

                    out.println("VREME");
                    String odg = in.readLine();

                    ta.append("[SERVER] :\t" + odg+"\n");

                    sock.close();
                    in.close();
                    out.close();



                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });


        gbc.gridx = 0;
        gbc.gridy = y++;
        odg.setPreferredSize(new Dimension(580, 30));
        odg.setOpaque(true);
        odg.setBackground(Color.yellow);
        odg.setForeground(Color.blue);
        getContentPane().add(odg, gbc);


        gbc.gridx = 0;
        gbc.gridy = y++;
        ta.setPreferredSize(new Dimension(580, 150));
        getContentPane().add(ta, gbc);


        gbc.gridx = 0;
        gbc.gridy = y++;
        time.setForeground(Color.BLUE);
        time.setBackground(Color.YELLOW);
        time.setOpaque(true);
        time.setPreferredSize(new Dimension(580, 30));
        getContentPane().add(time, gbc);

        for(Component comp : getContentPane().getComponents()){
            comp.setFont(new Font("Arial", Font.BOLD, 16));
        }
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        naslov1.setFont(new Font("Arial", Font.BOLD, 20));

        new Nit();
    }

    public static void main(String[] args) {
        new Klijent();
    }


}
