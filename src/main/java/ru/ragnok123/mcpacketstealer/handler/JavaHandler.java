package ru.ragnok123.mcpacketstealer.handler;

import java.net.InetSocketAddress;

import net.novatech.jbprotocol.GameSession;
import net.novatech.jbprotocol.ProtocolClient;
import net.novatech.jbprotocol.ServerConnectInfo;
import net.novatech.jbprotocol.java.JavaGameState;
import net.novatech.jbprotocol.java.JavaProtocol;
import net.novatech.jbprotocol.java.JavaSession;
import net.novatech.jbprotocol.listener.ClientListener;
import net.novatech.jbprotocol.listener.GameListener;
import net.novatech.jbprotocol.listener.LoginServerListener;
import net.novatech.jbprotocol.packet.AbstractPacket;
import net.novatech.jbprotocol.util.SessionData;
import ru.ragnok123.mcpacketstealer.Config;
import ru.ragnok123.mcpacketstealer.Main;

public class JavaHandler {

	private Main main;
	private JavaSession session;
	private JavaSession gameSession;

	public JavaHandler(Main main) {
		this.main = main;
		this.session = (JavaSession) main.session;
	}

	public void sendPacket(AbstractPacket pk) {
		gameSession.sendPacket(pk);
	}

	public void initClient() {
		session.requireAuthentication(true);

		session.setGameListener(new GameListener() {

			@Override
			public void receivePacket(AbstractPacket packet) {
				JavaProtocol protocol = (JavaProtocol) session.getProtocol();
				if (protocol.getGameState() != JavaGameState.GAME) {
					if (gameSession == null) {
						ProtocolClient client = new ProtocolClient("0.0.0.0", 19132, Config.EDITION);
						client.setClientListener(new ClientListener() {
							@Override
							public void sessionConnected(GameSession session) {
								gameSession = (JavaSession) session;
								gameSession.setGameListener(new GameListener() {
									@Override
									public void receivePacket(AbstractPacket packet) {
										System.out.println(packet.toString());
									}
								});
							}

							@Override
							public void sessionDisconnected(GameSession session, String cause) {
								gameSession = null;
								System.out.println("Disconnected: " + cause);
							}

							@Override
							public void sessionFailed(GameSession session, String cause) {
								gameSession = null;
								System.out.println("Failed to connect: " + cause);
							}

						});
						client.connectTo(new ServerConnectInfo(new InetSocketAddress(Config.IP, Config.PORT)));
					}
					gameSession.sendPacket(packet);
				}
			}

		});
	}

}