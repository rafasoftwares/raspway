package connect;

import java.io.IOException;
import java.net.Socket;



public class Main {

	public static void main(String[] args) {
		
		Socket cliente;
        try {
			cliente = new Socket("200.142.169.169", 502);
			System.out.print(cliente);
			System.out.print(cliente.getOutputStream());
			cliente.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	
       

	}

}
