/**
 * Copyright (c) 2011-2012, JGraph Ltd
 */
package com.mxgraph.examples.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;

public class PublishServlet extends HttpServlet
{
	/**
	 *
	 */
	private static final long serialVersionUID = -5308353652899057537L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getContentLength() < Constants.MAX_REQUEST_SIZE)
		{
			String format = "graph";
			String filename = request.getParameter("filename");
			if (filename.toLowerCase().endsWith("." + format))
			{
				filename = filename.substring(0, filename.length() - ("." + format).length());
			}

			OutputStream out = response.getOutputStream();
			PrintWriter writer = new PrintWriter(out);
			try
			{
				DataStoreHandler.INSTANCE.publish(filename);
			}
			catch(Exception e)
			{
				error(writer, "errorPublishingFile");
			}
			finally
			{
				writer.flush();
				writer.close();
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
		}
	}

	public static void error(PrintWriter w, String key)
	{
		w.println("window.parent.openFile.error(window.parent.mxResources.get('"
				+ key + "'));");
	}

}
