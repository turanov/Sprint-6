import ru.sber.filesystem.VFilesystem
import ru.sber.filesystem.VPath
import java.io.IOException
import java.net.ServerSocket

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
class FileServer {
    private val notFound = "HTTP/1.0 404 Not Found\r\n\nServer: FileServer\r\n\n\r\n"
    private val success = "HTTP/1.0 200 OK\r\nServer: FileServer\r\n\r\n"
    private val goodResponseEnd = "\r\n"

    /**
     * Main entrypoint for the basic file server.
     *
     * @param socket Provided socket to accept connections on.
     * @param fs     A proxy filesystem to serve files from. See the VFilesystem
     *               class for more detailed documentation of its usage.
     * @throws IOException If an I/O error is detected on the server. This
     *                     should be a fatal error, your file server
     *                     implementation is not expected to ever throw
     *                     IOExceptions during normal operation.
     */
    @Throws(IOException::class)
    fun run(socket: ServerSocket, fs: VFilesystem) {

        /**
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */
        while (true) {
            socket.use {
                while (true) {
                    it.accept().use { socket ->
                        val reader = socket.getInputStream().bufferedReader()
                        val request = reader.readLine().split(" ")
                        val writer = socket.getOutputStream().bufferedWriter()
                        val path = VPath(request[1])
                        val info = fs.readFile(path)

                        if (info.isNullOrEmpty()) {
                            writer.write(HTTP.NOT_FOUND.answer)
                        } else {
                            writer.write(HTTP.OK.answer + "$info\r\n")
                        }
                        writer.flush()
                        writer.close()
                    }
                }
            }
        }
    }

    enum class HTTP(val answer: String) {
        NOT_FOUND(
            "HTTP/1.0 404 Not Found\r\n" +
                    "Server: FileServer\r\n" +
                    "\r\n"
        ),
        OK(
            "HTTP/1.0 200 OK\r\n" +
                    "Server: FileServer\r\n" +
                    "\r\n"
        )
    }
}