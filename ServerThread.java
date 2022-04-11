import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServerThread extends Thread{
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    public ServerThread(Socket sock){
        this.sock = sock;

        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())),true);


        } catch (IOException e) {
            e.printStackTrace();
        }
        start();

    }
    public void run() {

        try {
            String req = in.readLine();
            String odg = "";

            if(req.equals("VREME")){
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                odg = sdf.format(new Date()).toString();

                Server.tas.append("Klijent prihacen\n"+req+"\n");

            }else{

                Server.tas.append("Klijent prihvacen\nROBOT\n");

                String[][] table = new String[9][9];    int br = 0;
                for(int i = 0; i < 9; i++){
                    for(int j = 0; j < 9; j++){
                        table[i][j] = new String(""+req.charAt(br++));
                    }
                }


                int[] Rcord = new int[2];
                int[] Ecord = new int[2];
                ArrayList<Integer>Eovi = new ArrayList<Integer>();

                for(int i = 0; i < 9; i++){
                    for(int j = 0; j < 9; j++){
                        if(table[i][j].equals("R")){
                            Rcord[0] = i; Rcord[1] = j;
                            System.out.println(Rcord[0]+" "+ Rcord[1]);
                        }
                        if(table[i][j].equals("E")){
                            Ecord[0] = i; Ecord[1] = j;
                            System.out.println(Ecord[0]+" "+ Ecord[1]);
                            Eovi.add(i);
                            Eovi.add(j);
                        }
                        Server.tas.append(table[i][j]);
                    }
                    Server.tas.append("\n");
                }

                for(int i = 0; i < Eovi.size(); i++){

                    System.out.println(Eovi.get(i)+ " "+ Eovi.get(i+1));
                    i++;
                }

                br = 0;

                while(br != Eovi.size()){
                    Ecord[0] = Eovi.get(br); Ecord[1] = Eovi.get(++br);

                    while((Rcord[0] != Ecord[0]) || (Rcord[1] != Ecord[1])){
                        if((Rcord[1] < Ecord[1]) && (!table[Rcord[0]][Rcord[1]+1].equals("#"))){
                            Rcord[1]++;
                            odg += "R";
                            System.out.println(odg);
                            if((Ecord[0] == Rcord[0]-1) && !(table[Rcord[0]-1][Rcord[1]].equals("#"))) {
                                Rcord[0]--;
                                odg += "U";
                                System.out.println(odg);
                            }
                        }
                        else{
                            if(table[Rcord[0]+1][Rcord[1]].equals("#")){
                                Rcord[1]++;
                                odg += "R";
                                System.out.println(odg);
                            }
                            if(Rcord[0] != Ecord[0]){



                                Rcord[0]++;
                                odg+= "D";
                                System.out.println(odg);

                                if((Ecord[1] == Rcord[1]-1) && !(table[Rcord[0]][Rcord[1]-1].equals("#"))){
                                    Rcord[1]--;
                                    odg += "L";
                                    System.out.println(odg);
                                }



                            }
                        }
                    }

                    odg += "  ";
                    Rcord[0] = 1; Rcord[1] = 1; br++;
                }





            }

            out.println(odg);
            out.close();
            in.close();
            sock.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
