package ru.ragnok123.mcpacketstealer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.novatech.jbprotocol.GameSession;
import net.novatech.jbprotocol.ProtocolClient;
import net.novatech.jbprotocol.ProtocolServer;
import net.novatech.jbprotocol.ServerConnectInfo;
import net.novatech.jbprotocol.data.Pong;
import net.novatech.jbprotocol.listener.ServerListener;
import net.novatech.jbprotocol.util.MessageConsumer;

public class Main {
	
	public ExecutorService executor = Executors.newCachedThreadPool();
	public GameSession session;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.init();
	}
	
	public void init() {
		executor.submit(() -> {
			ProtocolServer proxy = new ProtocolServer("0.0.0.0", 19132, Config.EDITION);
			proxy.setMaxConnections(1);
			proxy.setServerListener(new ServerListener() {

				@Override
				public void sessionConnected(GameSession session) {
					initHandler();
				}

				@Override
				public void sessionDisconnected(GameSession session, String cause) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void handlePong(Pong pong) {
					// TODO Auto-generated method stub
					
				}
				
			});
			proxy.bind(new MessageConsumer() {

				@Override
				public void success() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void failed(Throwable t) {
					// TODO Auto-generated method stub
					
				}
				
			});
		});
	}
	
	public void initHandler() {
		switch(Config.EDITION) {
		case JAVA:
			break;
		}
	}

}
