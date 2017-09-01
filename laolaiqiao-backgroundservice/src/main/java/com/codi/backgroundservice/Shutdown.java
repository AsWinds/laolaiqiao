package com.codi.backgroundservice;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shutdown {
	
	private static final Logger logger = LoggerFactory.getLogger(Shutdown.class);
	
	public static void main(String[] args) {
		String msg = "Closing background service ....";
		logger.info(msg);
		System.out.println(msg);
		try(Socket socket = new Socket()) {
			socket.connect(Startup.SHUT_DOWN_ADDRESS, 10 * 1000);;
		} catch (IOException e) {
			msg = "Connect to BackGroundService fail ....";
			logger.error(msg, e);
			System.out.println(msg);
			return;
		}
		msg = "Have send close command to background service, will exit ....";
		logger.info(msg);
		System.err.println(msg);
	}
}
