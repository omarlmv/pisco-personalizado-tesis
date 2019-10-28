package com.tesis.microservice.pisco.comun;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyProxySelector2 extends ProxySelector{
	@Override
	public List<Proxy> select(URI uri) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("fwproxy-nlb.grupoib.local", 80));
		
		System.setProperty("http.proxyUser", "IB_LIMA_MASTER\\xt5831");
		System.setProperty("http.proxyPassword", "junio2016$");
		
	    ArrayList list = new ArrayList();
        list.add(proxy);
        return list;
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		System.err.println("Connection to " + uri + " failed.");
		
	}
}
