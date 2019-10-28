package com.tesis.microservice.pisco.comun;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyProxySelector extends ProxySelector{
	@Override
	public List<Proxy> select(URI uri) {
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9002));
	    ArrayList list = new ArrayList();
        list.add(proxy);
        return list;
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		System.err.println("Connection to " + uri + " failed.");
		
	}
}
