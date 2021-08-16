/**
 * Copyright (c) 2011-2012, JGraph Ltd
 */
package com.mxgraph.examples.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Servlet implementation class SaveServlet.
 *
 * The SaveDialog in Dialogs.js implements the user interface. Editor.saveFile
 * in Editor.js implements the request to the server. Note that this request
 * is carried out in a separate iframe in order to allow for the response to
 * be handled by the browser. (This is required in order to bring up a native
 * Save dialog and save the file to the local filestyem.) Finally, the code in
 * this servlet echoes the XML and sends it back to the client with the
 * required headers (see Content-Disposition in RFC 2183).
 */
public class SaveServlet extends HttpServlet
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
			String filename = request.getParameter("filename");
			String xml = request.getParameter("xml");
			String format = "xml";

			if (filename.toLowerCase().endsWith("." + format))
			{
				filename = filename.substring(0, filename.length() - ("." + format).length());
			}

			if (xml != null && xml.length() > 0)
			{
				// Decoding is optional (no plain text values allowed)
				if (xml != null && xml.startsWith("%3C"))
				{
					xml = URLDecoder.decode(xml, "UTF-8");
				}

				DataStoreHandler.INSTANCE.setEditorData(filename, xml);
			}
			else
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
		}
	}

}
