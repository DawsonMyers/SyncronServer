package syncron.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ByteCountTest {
	public static boolean[] digiInput = new boolean[10];
	public static byte[] digiBytes;    //= new byte[10];
	public static int    byteCounts;    //= new byte[10];

	static String serverIP = "192.168.1.109";

	public static void main(String[] args) throws UnknownHostException {
		String data = "<475_583_659_674_683_672_692_701_683_723_695_316>";

		StringBuffer sb = new StringBuffer();
		for (boolean b : digiInput) sb.append(b == true ? "1" : "0");
		String strCmd = sb.toString();

		int byteCount = strCmd.getBytes().length;
		System.out.println("Byte count = " + byteCount + " string: " + strCmd);

		InetAddress receiverAddress = InetAddress.getByName(serverIP);
		System.out.println("Byte count = " + receiverAddress.getAddress());


	}

}
