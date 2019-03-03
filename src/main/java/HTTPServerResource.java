import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.*;

public class HTTPServerResource {

    /*
    public static void main(String[] args) {

        try {
            initializeServer(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    */

    public static void initializeServer(int port, Observer obs) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        GetHandler ghl = new GetHandler();
        PostHandler phl = new PostHandler();

        ghl.attach(obs);
       // phl.attach(obs);

        server.createContext("/", ghl);
        server.createContext("/post", phl);

        server.start();

        System.out.println("Server up");

    }

    static class GetHandler extends Subject implements HttpHandler  {

        public void handle(HttpExchange he) throws IOException {

            System.out.println("GetHandler fired");

            String response = "default";

            if (he.getRequestMethod().equalsIgnoreCase("GET")) {

                System.out.println("RequestMethod = GET");

                OutputStream os = he.getResponseBody();

                //getPath returns the "name" of the file in the request - this can be sent to the fileserver
                System.out.println(he.getRequestURI().getPath());

                response = getFileServerResponse(he.getRequestURI().getPath());

                he.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());

                he.close();

            }

        }

        public String getFileServerResponse(String filename) {

            if (filename != "/favicon.ico" || filename != "\\favicon.ico") {
                notifyAllObservers(filename);
            }
            else {
                notifyAllObservers("/");
            }

            return responseData;

        }

    }

    static class PostHandler extends Subject implements HttpHandler {


        public void handle(HttpExchange he) throws IOException {

            System.out.println("PostHandler fired");

            for (Map.Entry<String, List<String>> header : he.getRequestHeaders().entrySet()) {

                System.out.println(header.getKey() + ": " + header.getValue().get(0));

            }

            /*

            DiskFileItemFactory dfif = new DiskFileItemFactory();

            try {

                ServletFileUpload up = new ServletFileUpload(dfif);

                final HttpExchange innerHE = he;

                List<FileItem> result = up.parseRequest(new RequestContext() {

                    public String getCharacterEncoding() {
                        return "UTF-8";
                    }


                    public String getContentType() {
                        return innerHE.getRequestHeaders().getFirst("Content-type");
                    }


                    public int getContentLength() {
                        return 0;
                    }


                    public InputStream getInputStream() throws IOException {
                        return innerHE.getRequestBody();
                    }
                });

                he.getResponseHeaders().add("Content-type", "text/plain");
                he.sendResponseHeaders(200, 0);

                OutputStream os = he.getResponseBody();
                for(FileItem fi : result) {
                    os.write(fi.getName().getBytes());
                    os.write("\r\n".getBytes());
                    System.out.println("File-Item: " + fi.getFieldName() + " = " + fi.getName());
                }
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (FileUploadException e) {
                e.printStackTrace();
            }

        */

        }

    }

}