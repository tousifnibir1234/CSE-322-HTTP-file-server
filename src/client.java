import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws IOException {

        // true diye autoflush on na korle mara khaite hobe

        while(true) {
            Socket socket = new Socket("localhost", 6789);

            //System.out.println("Connection established");
            //BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter pr = new PrintWriter(socket.getOutputStream(),true);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Give an input file :");
            String string = scanner.next();


            File file = new File(string);
            if (!file.exists()) {
                System.out.println("UPLOAD FAILED");
                HTTPServerSkeleton.log.println("HTTP REQUEST : UPLOAD "+ file.getName());
                pr.println("UPLOAD FAILED");

                continue;
            }
            pr.println("UPLOAD " + string);
            OutputStream out = socket.getOutputStream();

            Thread  thread= new clientWorker(socket,file,out);
            thread.start();


           // pr.close();
        }




    }
}
