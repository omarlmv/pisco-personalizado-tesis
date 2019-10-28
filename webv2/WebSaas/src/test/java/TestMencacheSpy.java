import java.io.IOException;
import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;

public class TestMencacheSpy {
	private String memcachedHost = "localhost";
	private int memcachedPort = 11211; // by default - 11211
	private MemcachedClient memcachedClient;

	TestMencacheSpy() throws IOException {
		memcachedClient = new MemcachedClient(new InetSocketAddress(
				memcachedHost, memcachedPort));
	}

	public void putContentInMemCache(String key, String value) {
		memcachedClient.set(key, 60*60*16, value); // (3600 - expiry time in
												// seconds)
	}

	public Object getContentInMemCache(String key) {
		return memcachedClient.get(key);
	}

	public void deleteContentFromMemCache(String key) {
		memcachedClient.delete(key);
		
	}

	public static void main(String[] args) throws IOException {
		TestMencacheSpy test = new TestMencacheSpy();
		System.out.println(test.getContentInMemCache("key16"));
	//	test.putContentInMemCache("key16", "abc");
//		System.out.println(test.getContentInMemCache("key"));
		
//		test.deleteContentFromMemCache("key");
//		System.out.println(test.getContentInMemCache("key"));
		
//		 // To insert multiple records into memcache 
//		 for (int i = 0; i< 10; i++){
//			 test.putContentInMemCache("key" + "_" + i , "value" + "_" + i);
//		 }
//		 // To retrieve multiple records from memcache 
//		 for (int i = 0; i < 10; i++){
//			 System.out.println("key _" +i + " - " +test.getContentInMemCache("key" + "_" + i));
//		}
//		 
		
		 
	}
}
