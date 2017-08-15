import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

public class Response implements ServletResponse {

    private static final int BUFFER_SIZE = 2048;
    private Request request;
    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    /* 提供静态页面服务 */
    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fileInputStream = null;
        try {
            File file = new File(Constants.WEB_ROOT, request.getUri());
            fileInputStream = new FileInputStream(file);
            int ch = fileInputStream.read(bytes, 0, BUFFER_SIZE);
            while (ch != -1) {
                outputStream.write(bytes, 0, ch);
                ch = fileInputStream.read(bytes, 0, BUFFER_SIZE);
            }

        } catch (FileNotFoundException e) {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            outputStream.write(errorMessage.getBytes());
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
    }


    /* implementation of ServletResponse */

    public String getCharacterEncoding() {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public PrintWriter getWriter() throws IOException {
        PrintWriter writer = new PrintWriter(outputStream, true);
        return writer;
    }

    public void setCharacterEncoding(String s) {

    }

    public void setContentLength(int i) {

    }

    public void setContentLengthLong(long l) {

    }

    public void setContentType(String s) {

    }

    public void setBufferSize(int i) {

    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {

    }

    public void resetBuffer() {

    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {

    }

    public void setLocale(Locale locale) {

    }

    public Locale getLocale() {
        return null;
    }
}
