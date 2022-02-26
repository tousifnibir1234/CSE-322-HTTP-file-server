import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Worker extends Thread {
    Socket s;
    String start= "<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> </head> <body> <h1> Welcome to CSE 322 Offline 1</h1> ";
    String endOfHtml =" </body> </html>";



    public Worker(Socket socket) throws IOException {
        this.s = socket;
    }

    public void run() {
            try {
                // buffers
                while (true) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    PrintWriter pr = null;

                    pr = new PrintWriter(s.getOutputStream());

                    //learn the whole process. DataOutput Stream is faster with primitive data types

                    String input = null;


                    input = in.readLine();
                    if(input== null || input.equalsIgnoreCase("GET /favicon.ico HTTP/1.1"))continue;

                    if(!input.equalsIgnoreCase("UPLOAD FAILED")) {
                        System.out.println("HTTP REQUEST : " + input);
                        HTTPServerSkeleton.log.println("HTTP Request: " + input);
                    }
                    else {
                        System.out.println("HTTP Requested File Upload Failed\n");
                        HTTPServerSkeleton.log.println("HTTP RESPONSE: UPLOAD FAILED\n");


                    }
                        StringBuilder sb = new StringBuilder();
                    sb.append(start);
                    String mimetype = "text/html";
                    // String content = "<html>Hello</html>";
                    if (input.length() > 0) {
                        //HTTPServerSkeleton.fr.write(new Date().toString()+" : \n\r Input :"+ input);
                        if (input.startsWith("GET")) {
                            //splitting input into http and other
                            String[] primary = input.split("HTTP/1.1");
                            //primary[0]=GET /
                            //split primary[0] in get and path

                            String[] secondary = primary[0].split("/", 2);
                            //secondary[1] is path....................
                            secondary[1] = secondary[1].trim();


                            secondary[1] = secondary[1].trim();
                            if (secondary[1].trim().equals("") || secondary[1].trim().equals("root")) {
                                secondary[1] = "root";
                            }
                            File file = new File(secondary[1].trim());
                            if(!file.exists())
                            {
                                sb.append("<h1> ERROR 404 FILE NOT FOUND</h1>");
                                System.out.println("Response: File Not Found\n");
                                sb.append(endOfHtml);
                                String content= sb.toString();
                                HTTPServerSkeleton.log.println("Error 404 File not found");
                                pr.write("HTTP/1.1 200 OK\r\n");
                                pr.write("Server: Java HTTP Server: 1.0\r\n");
                                pr.write("Date: " + new Date() + "\r\n");
                                pr.write("Content-Type: " + mimetype + "\r\n");
                                pr.write("Content-Length: " + content.length() + "\r\n");
                                pr.write("\r\n");
                                pr.write(content);
                                pr.flush();
                            }
                            else if (file.isDirectory()) {
                                String[] children = file.list();
                                for (int i = 0; i < children.length; i++) {
                                    File traversal = new File(secondary[1] + "/" + children[i]);
                                    if (traversal.isDirectory()) {
                                        sb.append("<a href= \"http://localhost:6789/" + secondary[1] + "/" + children[i] + "\">" +"<b>" +children[i] +"</b>"+ "</a>" + "<br>");
                                    } else if (traversal.isFile()) {
                                        sb.append("<a href= \"http://localhost:6789/" + secondary[1] + "/" + children[i] + "\">" + children[i] + "</a>" + "<br>");
                                    }

                                }

                                sb.append(endOfHtml);
                                String content = sb.toString();
                                HTTPServerSkeleton.log.println("HTTP Response: "+"HTTP/1.1 200 OK\r\n\n");
                                System.out.println("HTTP Response: "+"HTTP/1.1 200 OK\r\n");



                                pr.write("HTTP/1.1 200 OK\r\n");
                                pr.write("Server: Java HTTP Server: 1.0\r\n");
                                pr.write("Date: " + new Date() + "\r\n");
                                pr.write("Content-Type: " + mimetype + "\r\n");
                                pr.write("Content-Length: " + content.length() + "\r\n");
                                pr.write("\r\n");
                                pr.write(content);
                                pr.flush();

                            } else if (file.isFile()) {

                                String[] tokens = secondary[1].split("\\.");
                                mimetype = tokens[1];
                                if (mimetype.equalsIgnoreCase("txt"))
                                    mimetype = "text/html";
                                else if (mimetype.equalsIgnoreCase("pdf"))
                                    mimetype = "application/pdf";
                                else if (mimetype.equalsIgnoreCase("jpg"))
                                    mimetype = "image/jpg";
                                else if (mimetype.equalsIgnoreCase("jpeg"))
                                    mimetype = "image/jpeg";
                                else if(mimetype.equalsIgnoreCase("webm"))
                                    mimetype="video/webm";
                                else if(mimetype.equalsIgnoreCase("mp4"))
                                    mimetype="video/mp4";
                                else if(mimetype.equalsIgnoreCase("docx"))
                                    mimetype="application/vnd.openxmlformats-officedocument.wordprocessingml.document";

                                System.out.println("HTTP Response: File downloaded "+file.getName()+"\n");
                                HTTPServerSkeleton.log.println("HTTP RESPONSE: File Downloaded\n"
                                                +"HTTP/1.1 200 OK\r\n"
                                                +"Server: Java HTTP Server: 1.0\r\n"
                                                +"Date: " + new Date() + "\r\n"
                                                +"Content-Type: " + mimetype + "\r\n"
                                                +"Content-Length: " + file.length() + "\r\n"
                                        );

                                pr.write("HTTP/1.1 200 OK\r\n");
                                pr.write("Server: Java HTTP Server: 1.0\r\n");
                                pr.write("Date: " + new Date() + "\r\n");
                                pr.write("Content-Type: " + mimetype + "\r\n");
                                pr.write("Content-Type: application/x-force-download\r\n");
                                pr.write("Content-Length: " + file.length() + "\r\n");

                                pr.write("\r\n");
                                pr.flush();

                                int size = 100;
                                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(s.getOutputStream(), 200);
                                FileInputStream fileInputStream = new FileInputStream(file);

                                byte[] byteArr = new byte[size ];
                                for (int i = 0; i * size < file.length(); i++) {
                                    int checker = fileInputStream.read(byteArr, 0, size);
                                    if (checker >= 0) bufferedOutputStream.write(byteArr, 0, checker);
                                    bufferedOutputStream.flush();
                                }


                            }break;

                        } else if (input.startsWith("UPLOAD")) {
                            if(input.equalsIgnoreCase("UPLOAD FAILED"))
                            {
                                continue;
                            }
                            String token[] = input.split(" ", 2);
                            InputStream bufferedReader = null;
                            bufferedReader = s.getInputStream();
                            token[1] = token[1].trim();
                            File output = new File("root/" + token[1]);
                            output.createNewFile();
                            int size = 100;
                            FileOutputStream out = new FileOutputStream(output);
                            byte[] bytes = new byte[size];

                            int count;
                            while ((count = bufferedReader.read(bytes)) > 0) {
                                out.write(bytes, 0, count);
                            }
                            System.out.println("HTTP RESPONSE: UPLOAD Succesful "+ token[1]+"\n");
                            HTTPServerSkeleton.log.println("HTTP RESPONSE : UPLOAD SUCCESSFUL "+token[1]);
                            HTTPServerSkeleton.log.println("HTTP/1.1 200 OK\r\n\n");

                            out.close();
                            bufferedReader.close();
                            break;
                        }
                    }


                }
                s.close();
                HTTPServerSkeleton.log.flush();



            }catch(Exception e)
                {
                    e.printStackTrace();
                }


    }
}
