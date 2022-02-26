
import java.io.*;
import java.net.Socket;
import java.util.Date;

public class clientWorker extends Thread {
    Socket socket;
    File file;
    OutputStream out;

    public clientWorker(Socket socket,File file,OutputStream out)
    {
        this.socket = socket;
        this.file=file;
        this.out=out;
    }

    public void run()
    {
        // buffers
        try {
            long length = file.length();
            int size = 100;
            byte[] bytes = new byte[size];
            InputStream in = new FileInputStream(file);

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            HTTPServerSkeleton.log.print("UPLOAD SUCCESSFUL "+file.getName()+"\n");
            out.flush();

           socket.close();
           out.close();
           in.close();


        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
