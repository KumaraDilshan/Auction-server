import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.*;
import javax.swing.*;


public class AuctionServer implements Runnable {

    public static final int BASE_PORT = 1250;      //defining the base port and needed fields
    public static String key=null;
    public static String name;
    private static ServerSocket serverSocket;
    private static int socketNumber;
    public StockList data;
    private Socket connectionSocket;

    public AuctionServer(int socket,StockList data1) throws IOException {     //in this constructor the object data1 is defined to the connection
        serverSocket = new ServerSocket(socket);
        socketNumber = socket;
        data =data1;
    }

    public AuctionServer(Socket socket,StockList data1) {    //the next constructor
        this.connectionSocket = socket;
        data =data1;
    }

    public void server_loop() throws IOException {
        while(true) {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new AuctionServer(socket,data));
            worker.start();
        }
    }

    public void run() {
        try {
            String symbol;    //string to keep the read line symbol from the user
            double bid;		//string to keep the highest bid from the hashmap
            double bid_current;    //string to keep the read line bid from the user
			String name_temp;		//string to keep the read line name of the user
			
            BufferedReader in = new BufferedReader(new InputStreamReader(this.connectionSocket.getInputStream()));    //taking the lines from the user
            PrintWriter out = new PrintWriter(new OutputStreamWriter(this.connectionSocket.getOutputStream()));
            String line;
            line = in.readLine();
            name=line;
			name_temp=name;
            line = in.readLine();
            symbol=line;
			
            boolean available=data.findkey(symbol);     //find if the entered key is valid in the hashmap
            if (available==false)
                System.exit(-1);
            else {
                bid=Double.parseDouble(data.findName_price(symbol)[1]);        //taking the bid to corresponding symbol from database
                for (line = in.readLine(); (line != null && !line.equals("quit")) || line.isEmpty(); line = in.readLine()) {    //taking new bids and updating the system
                    bid=Double.parseDouble(data.findName_price(symbol)[1]);		//taking the bid (the highest bid ever recorded) to corresponding symbol
                    if (line.isEmpty())
                        continue;
                    bid_current=Double.parseDouble(line);
                    if(bid_current>bid) {     //comparing the bid with the current highest
                        bid = bid_current;
                        data.setstockList1value(symbol,line);      //updating the hashmap
						key=symbol;     //set the string to symbol for displaying it in the track
                    }
						
				    name=name_temp;	//keep the name unchanged	
                } // for loop
            }
        }// try
        catch (IOException e) {
            System.out.println(e);
        }
        try {
            this.connectionSocket.close();
        } catch(IOException e) {}

    }

    public static void main(String [] args) throws IOException {
        StockList data = new StockList("stocks.csv","Symbol","Security Name","Price");     //call the file and headings
        AuctionServer server = new AuctionServer(BASE_PORT,data);

        JFrame frame = new JFrame("Auction Server");       //set the properties of the GUI
        frame.setPreferredSize(new Dimension(1300, 700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new Display(server));
        frame.pack();
        frame.setVisible(true);
        server.server_loop();      //keep run the loop 

    }
}

