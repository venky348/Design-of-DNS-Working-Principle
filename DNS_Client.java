import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DNS_Client
{ 
    public static String host; 
    public static int port;
	
	DNS_Client()
	{
		JFrame jfrm1=new JFrame("DNS Project");
		
		jfrm1.setSize(400,400);
		jfrm1.setLayout(null);
		jfrm1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JLabel L1=new JLabel("---- DNS Implementation ----");
		L1.setFont(new Font("Verdana", Font.BOLD, 15));
		L1.setBounds(80,20,250,40);
		
		JButton B1=new JButton("Start");
		B1.setBounds(120,200,150,40);
		jfrm1.add(B1);
		jfrm1.add(L1);
		jfrm1.setVisible(true);
		
		B1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				try
		       {
				 jfrm1.dispose();
		         DNS_LookUp();
		       }
		        catch (Exception e)
		       {
			     System.out.println(e);
	           }
			   
			}
			
			
	    });
		
	}
	
	public void DNS_LookUp() 
	{
		
		
        JFrame jfrm2=new JFrame("DNS");
		jfrm2.setLayout(null);
		jfrm2.setSize(400,400);
		jfrm2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextField jtf1=new JTextField(100);
		jtf1.setBounds(70,50,250,40);
		jfrm2.add(jtf1);
		
		JLabel jl1=new JLabel();
		jl1.setFont(new Font("Serif", Font.BOLD, 15));
		jl1.setBounds(170,150,200,40);
		jfrm2.add(jl1);
		
		JLabel jl3=new JLabel("Domain Name : ");;
		jl3.setFont(new Font("Serif", Font.BOLD, 15));
		jl3.setBounds(50,150,200,40);
		jfrm2.add(jl3);
		
		JLabel jl2=new JLabel();
		jl2.setFont(new Font("Serif", Font.BOLD, 15));
		jl2.setBounds(150,200,200,40);
		jfrm2.add(jl2);
		
		JLabel jl4=new JLabel("IP Address  : ");
		jl4.setFont(new Font("Serif", Font.BOLD, 15));
		jl4.setBounds(50,200,200,40);
		jfrm2.add(jl4);
		
		JButton jb1=new JButton("Find");
		jb1.setBounds(150,100,100,40);
		jfrm2.add(jb1);
		
		
	    jb1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
				jl1.setText(jtf1.getText());
				
				try
				{
				   String s=DNS_Implementation(jtf1.getText());
				   jl2.setText(s);
		        }
				catch(Exception e)
				{
					e.printStackTrace();
			    }
			}
        });	
		  
		  jfrm2.setVisible(true);	
				
    }
	public String DNS_Implementation(String s1) throws Exception
	{
		String s2;
		int flag;
		
		System.out.println("Reaching DNS Server at " +host+ " with port " + port + "..."); 
        
        System.out.print("Type a website: ");  
		System.out.println(s1);
        String given_hostname = s1;
        
        DatagramSocket client_socket = new DatagramSocket();
        client_socket.setSoTimeout(3000);
                
        InetAddress IP_address = InetAddress.getByName(host);
        byte[] send_data = new byte[1024];
        byte[] receive_data = new byte[1024];
         
        send_data = given_hostname.getBytes();
        
        DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length, IP_address, port);
        client_socket.send(send_packet);
        
        DatagramPacket receive_packet = new DatagramPacket(receive_data, receive_data.length);

        try
        {
            
            client_socket.receive(receive_packet);
            String server_response = new String(receive_packet.getData());
            
            String two_char_response = server_response.substring(0,2);
            s2=server_response;
            
            if(two_char_response.equals("-1"))
			{
				
                System.out.println("DNS server's response: NOT FOUND");
				s2="NOT FOUND";
				client_socket.close();
		        return s2;
				
			}
				
            else
			{
			    
                System.out.println("DNS server's response: " + server_response);
				s2=server_response;
				client_socket.close();
				return s2;
				
			}
        } 
        catch (SocketTimeoutException e) 
        {

			System.out.println("Timeout reached. " + e);
			client_socket.close();
			s2="Timeout reached.";
            return s2;
            
        }
        
	}
    
	
    public static void main(String args[]) throws Exception
    {
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					System.out.println("-----------------------------------------");
                    System.out.println("Usage: java DNS_Client <host name> <port>"); 
                    System.out.println("Default host name: 127.0.0.1");
                    System.out.println("Default port: 8888");
                    System.out.println("-----------------------------------------\n");
            
                    host = "127.0.0.1";
                    port = 8888;
					
					 DNS_Client d1=new DNS_Client();
				}
			}
		);
		
    }    
}

