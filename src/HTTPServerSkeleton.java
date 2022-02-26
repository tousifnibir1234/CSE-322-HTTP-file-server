import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class HTTPServerSkeleton {
    static final int PORT = 6789;
    static   File logfile=new File("log.txt");
    static PrintWriter log;

    static {
        try {
            log = new PrintWriter(new FileOutputStream(logfile,true),true);
            log.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static byte[] readFileData(File file) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[(int) file.length()];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }
            return fileData;
       // return String.valueOf(fileData);
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverConnect = new ServerSocket(PORT);
        System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
        if(!logfile.exists())
            logfile.createNewFile();
        // fileWriter.flush();


        // commenting index.html and outh
        // File file = new File("index.html");
        //FileInputStream fis = new FileInputStream(file);
        //BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        //String line;
        /*while(( line = br.readLine()) != null ) {
            sb.append( line );
            sb.append( '\n' );
        }*/
        String start= "<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> </head> <body> <h1> Welcome to CSE 322 Offline 1</h1> ";
        String endOfHtml =" </body> </html>";

        while(true)
        {
            Socket s = serverConnect.accept();
            Thread worker= new Worker(s);
            worker.start();


        }

    }

}
