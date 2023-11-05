import java.io.*;
import java.net.*;
import java.util.Hashtable;

public class DNS_Server 
{
    public static void main(String args[]) throws Exception
    {
        int port_number;

        if(args.length == 0)
        {
            System.out.println("-----------------------------");
            System.out.println("Usage: java DNS_Server <port>");
            System.out.println("Default port: 8888");
            System.out.println("-----------------------------\n");
            port_number = 8888;
        }
        else
            port_number = Integer.parseInt(args[0]); 

        Hashtable<String, String> addresses = new Hashtable<String, String>(); 
        
        File file = new File("hosts.txt");
        FileReader file_reader = new FileReader(file);
        BufferedReader reader = new BufferedReader(file_reader);

        String line_of_file;
        
        while((line_of_file=reader.readLine())!=null)
        {
            String[] columns = line_of_file.split(" ");
            addresses.put(columns[0], columns[1]); 
        }
        
        reader.close();
        
        System.out.println("DNS Server is running at port " + port_number + "..."); 
        
        DatagramSocket server_socket = new DatagramSocket(port_number);
        byte[] receive_data = new byte[1024];
        byte[] send_data = new byte[1024];
           
        while(true)    
        {
 
            DatagramPacket receive_packet = new DatagramPacket(receive_data, receive_data.length);
            server_socket.receive(receive_packet);
            String given_hostname = new String(receive_packet.getData(), 0, receive_packet.getLength());
            
            String found_address = "-1";

            if(addresses.get(given_hostname) != null)
            {
                System.out.println("Client's request: " + given_hostname + " | IP address: " + addresses.get(given_hostname)); 
                found_address = addresses.get(given_hostname);
            }
            else
            {
                try
                {

                    InetAddress address_search = java.net.InetAddress.getByName(given_hostname); 
                    found_address = address_search.getHostAddress();
                    
                    System.out.println("Client's request: " + given_hostname + " | IP address: " + found_address);
                    
                    addresses.put(given_hostname, found_address);   
                    
                    PrintWriter writer = new PrintWriter(new FileOutputStream(new File("hosts.txt"),true),true);
                    writer.append(given_hostname + " " + found_address + "\n");
                    writer.close();
                }
                catch(UnknownHostException ex)
                {
                    System.out.println("Client's domain request: " + given_hostname + " | IP address: Not found");
                }      
            }
            
            InetAddress client_address = receive_packet.getAddress();
            int client_port = receive_packet.getPort();
            
            send_data = found_address.getBytes();
            DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length, client_address, client_port);
            server_socket.send(send_packet);
        }
    }
} 

