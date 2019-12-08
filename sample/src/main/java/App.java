import com.yaser.core.network.server.BioServer;
import com.yaser.core.network.server.Server;

public class App {
    public static void main(String[] args) {
        Server server = new BioServer(8080);
        server.start();
    }
}

