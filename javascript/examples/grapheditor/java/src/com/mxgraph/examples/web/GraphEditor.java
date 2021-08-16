package com.mxgraph.examples.web;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The save servlet is used to echo XML to the client, eg. for SVG export and saving
 * (see Dialogs.js:SaveDialog and ExportDialog). The export servlet is used to
 * implement image and PDF export (see Dialogs.js:ExportDialog). Note that the
 * CSS support is limited to the following for all HTML markup:
 * http://docs.oracle.com/javase/6/docs/api/index.html?javax/swing/text/html/CSS.html
 * The open servlet is used to open files. It does this by calling some JavaScript
 * hook in the client-side page (see open.html).
 */
public class GraphEditor
{

	public static int PORT = 8080;

	/**
	 * Uncomment this for better font size rendering in px units within labels.
	 */
	static
	{
//		mxGraphicsCanvas2D.HTML_SCALE = 0.75;
//		mxGraphicsCanvas2D.HTML_UNIT = "px";
	}

	/**
	 * Point your browser to http://localhost:8080/javascript/examples/grapheditor/www/index.html
	 */
	public static void main(String[] args) throws Exception
	{
		Server server = new Server(PORT);

		// Servlets
		Context context = new Context(server, "/");
		context.addServlet(new ServletHolder(new SaveServlet()), "/save");
		context.addServlet(new ServletHolder(new PublishServlet()), "/publish");
		context.addServlet(new ServletHolder(new ExportServlet()), "/export");
		context.addServlet(new ServletHolder(new OpenServlet()), "/open");

		StaticFileResourceHandler fileHandler = new StaticFileResourceHandler();
		fileHandler.setResourceBase(".");
		fileHandler.setServer(server);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { fileHandler, context });
		server.setHandler(handlers);

		DataStoreHandler.INSTANCE.setDataStoreBase("data_store");

		System.out.println("Go to http://localhost:" + PORT + "/javascript/examples/grapheditor/www/index.html");

		server.start();
		server.join();
	}
}

class StaticFileResourceHandler extends ResourceHandler
{

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
		super.handle(target, request, response, dispatch);

		// update the headers
		response.setHeader("Cache-Control", "no-cache");
	}
}
