import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer1 {
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer1 server1 = new HttpServer1();
        server1.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8081;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
            System.out.println("server is running on port:" + serverSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // loop waiting for a request
        while (!shutdown) {
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                // create Request object and parse
                Request request = new Request(inputStream);
                request.parse();

                // create Response object
                Response response = new Response(outputStream);
                response.setRequest(request);

                String uri = request.getUri();
                if (uri == null) {
                    socket.close();
                    continue;
                }
                System.out.println("uri:" + uri);
                if (uri.startsWith("/servlet/")) {
                    ServletProcessor1 processor1 = new ServletProcessor1();
                    processor1.process(request, response);
                } else {
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }


                socket.close();

                shutdown = uri.equals(SHUTDOWN_COMMAND);

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
