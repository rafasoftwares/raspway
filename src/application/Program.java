package application;

import connect.Conexao;

public class Program {

	public static void main(String[] args) {
		Conexao conn = new Conexao();
		while (true) {
			conn.conectar("200.142.169.224", 502, 3, 8);
		}
	}
}
