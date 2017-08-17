import java.awt.*;
import javax.swing.Timer; //for timer

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.io.IOException;


public class Display extends JPanel implements ActionListener {

    JPanel panel;     //declaring needed labels,panel and text area
    Timer clk;
    JLabel label[];
    JTextArea textArea;
    AuctionServer auctionServer;

    public Display(AuctionServer server) {

        super(new BorderLayout());
		textArea = new JTextArea(1300, 100); 
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.auctionServer=server;

        label = new JLabel[27];
        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 3));

		
		//set the texts to the single array.These will added to the labels
        String []array1={"Symbol","Security Name","Price","FB","Facebook","100","VRTU","Virtusa Corporation - common stock","9.42","MSFT","Microsoft Corporation - Common Stock","3.24","GOOGL","Google Inc. - Class A Common Stock","3.95","YHOO","Yahoo! Inc. - Common Stock","2.46","XLNX","Xilinx","100","TSLA","Tesla Motors","100","TXN","Texas Instruments Incorporated - Common Stock","4.22"};
        int size;

        for(int counter=0;counter<27;counter++) {     //setting labels

            if(counter<3)
                size=23;
            else
                size=15;

            label[counter] = new JLabel(array1[counter]);
            label[counter].setFont(new Font("Serif", Font.BOLD, size));
            label[counter].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label[counter].setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(label[counter]);
        }
        clk = new Timer(500, this);     //set the timmer.Here the actionperformed is updating by Display call by every 500ms
        clk.start();

        add(panel, BorderLayout.NORTH);    //set the panel and textarea
        add(textArea,BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        label[5].setText(auctionServer.data.findName_price("FB")[1]);      //updating the prices of the needed symbols by every 500ms
        label[8].setText(auctionServer.data.findName_price("VRTU")[1]);
        label[11].setText(auctionServer.data.findName_price("MSFT")[1]);
        label[14].setText(auctionServer.data.findName_price("GOOGL")[1]);
        label[17].setText(auctionServer.data.findName_price("YHOO")[1]);
        label[20].setText(auctionServer.data.findName_price("XLNX")[1]);
        label[23].setText(auctionServer.data.findName_price("TSLA")[1]);
        label[26].setText(auctionServer.data.findName_price("TXN")[1]);

		
		//tracking the the changes done to the stock item, how the offers varied with time and who made the offers
       if(AuctionServer.key!=null){
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(Calendar.getInstance().getTime());
            textArea.append(timeStamp+" : "+auctionServer.key+" is bided by "+auctionServer.name+" to "+auctionServer.data.findName_price(auctionServer.key)[1]+"\n");
			AuctionServer.key=null;
       }

    }



}
	
	
	
