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
    public boolean isValid(int x, int y, String[][] grid, boolean[][] visited){
        if ((x >= 0 && y >=0) && (x < 9 && y < 9) && (!grid[x][y].equals("#") && (!visited[x][y]))){
            return true;
        }
        return false;
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
                            //System.out.println(Rcord[0]+" "+ Rcord[1]);
                        }
                        if(table[i][j].equals("E")){
                            Ecord[0] = i; Ecord[1] = j;
                            //System.out.println(Ecord[0]+" "+ Ecord[1]);
                            Eovi.add(i);
                            Eovi.add(j);
                        }
                        Server.tas.append(table[i][j]);
                    }
                    Server.tas.append("\n");
                }



                while(Eovi.size() > 0){
                    boolean [][] visited = new boolean[9][9];
                    for(int i = 0; i < 9; i++){
                        for(int j = 0; j < 9; j++){
                            visited[i][j] = false;
                        }
                    }
                    Ecord[0] = Eovi.get(0);
                    Eovi.remove(0);
                    Ecord[1] = Eovi.get(0);
                    Eovi.remove(0);
                    System.out.println(Ecord[0] + " "+ Ecord[1]);
                    ArrayList<Cell> q = new ArrayList<>();
                    q.add(new Cell(Rcord[0], Rcord[1], ""));
                    visited[Rcord[0]][Rcord[1]] = true;
                    while(q.size() > 0){
                        Cell source = q.get(0);
                        q.remove(source);
                        //System.out.println(source.row +" "+source.col +" "+ source.path);
                        if (source.row == Ecord[0] && source.col == Ecord[1]){
                            odg += source.path+" ";
                            break;
                        }

                        if (isValid(source.row-1, source.col, table, visited)){
                            q.add(new Cell(source.row-1, source.col, source.path+"U"));
                            visited[source.row-1][source.col] = true;
                        }
                        if (isValid(source.row + 1, source.col, table, visited)){
                            q.add(new Cell(source.row+1, source.col, source.path+"D"));
                            visited[source.row+1][source.col] = true;
                        }
                        if (isValid(source.row, source.col-1, table, visited)){
                            q.add(new Cell(source.row, source.col-1, source.path+"L"));
                            visited[source.row][source.col-1] = true;
                        }
                        if (isValid(source.row, source.col+1, table, visited)){
                            q.add(new Cell(source.row, source.col+1, source.path+"R"));
                            visited[source.row][source.col+1] = true;
                        }


                    }
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
