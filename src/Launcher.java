import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Yohann on 2016/10/8.
 */
public class Launcher {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        Selector selector = Selector.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 20000));
        sc.register(selector, SelectionKey.OP_READ);
        ByteBuffer buf = ByteBuffer.allocate(10);
        if (sc.finishConnect()) {
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> ite = keys.iterator();
                while (ite.hasNext()) {
                    SelectionKey key = ite.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.read(buf);
                        System.out.println(new String(buf.array()));
                        buf.clear();
                    }
                }
            }
        }
    }
}
