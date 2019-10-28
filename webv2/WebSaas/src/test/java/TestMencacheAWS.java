import java.io.IOException;
import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;


public class TestMencacheAWS {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String configEndpoint = "localhost";
        Integer clusterPort = 11214;
        

        Integer clusterPort2 = 11213;
       

        MemcachedClient client = new MemcachedClient(
                                 new InetSocketAddress(configEndpoint, clusterPort),
                                 new InetSocketAddress(configEndpoint, clusterPort2));       
        // The client will connect to the other cache nodes automatically.

        // Store a data item for an hour.  
        // The client will decide which cache host will store this item. 
        client.set("theKey", 3600, "This is the data value refresh");
	}

}
