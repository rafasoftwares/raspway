package connect;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Conexao {
	private static int contador = 1;

	public void conectar(String enderecoIp, int porta, int registoIni, int quantRegistros) {
		try {
			int unit = 255;
			System.out.println("   ");
			System.out.println("#Requisição: " + getDateTime());

			// set up socket
			Socket es = new Socket(enderecoIp, porta);
			OutputStream os = es.getOutputStream();
			FilterInputStream is = new BufferedInputStream(es.getInputStream());
			byte obuf[] = new byte[261];
			byte ibuf[] = new byte[261];
			int i;

			// Construindo o pacote de Requisição [0 0 0 0 0 6 ui 3 rr rr nn nn]
			for (i = 0; i < 5; i++)
				obuf[i] = 0;
			obuf[5] = 6;
			obuf[6] = (byte) unit;
			obuf[7] = 3;
			obuf[8] = (byte) (registoIni >> 8);
			obuf[9] = (byte) (registoIni & 0xff);
			obuf[10] = (byte) (quantRegistros >> 8);
			obuf[11] = (byte) (quantRegistros & 0xff);

			// send request
			os.write(obuf, 0, 12);

			// read response
			i = is.read(ibuf, 0, 261);
			if (i < 9) {
				if (i == 0) {
					System.out.println("unexpected close of connection at remote end");
				} else {
					System.out.println("response was too short - " + i + " chars");
				}
			} else if (0 != (ibuf[7] & 0x80)) {
				System.out.println("MODBUS exception response - type " + ibuf[8]);
			} else if (i != (9 + 2 * quantRegistros)) {
				System.out.println("incorrect response size is " + i + " expected" + (9 + 2 * quantRegistros));
			} else {
				for (i = 0; i < quantRegistros; i++) {
					int w = (ibuf[9 + i + i] << 8) + (ibuf[10 + i + i] & 0xff);
					System.out.println("Registro#" + i + " = " + w);
				}
			}
			// close down
			es.close();
			System.out.println("Contador = " + contador);
			contador++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("exception :" + e);
			contador++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
