package com.hs.analyser;

import java.io.IOException;

import com.hs.analyser.service.MonitorService;

public class Application {

	public static void main(String[] args) throws IOException {
		// for debugging we can commentout below line and set path
		//args = new String[] { "D:\\projects\\sample\\hs" };
		if (args.length != 1) {
			throw new RuntimeException("Only one args is required");
		}
		MonitorService application = new MonitorService();
		application.startMonitoring(args[0]);
	}

}
