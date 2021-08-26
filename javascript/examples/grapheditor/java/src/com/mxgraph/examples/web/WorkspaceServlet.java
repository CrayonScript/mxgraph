/**
 * Copyright (c) 2011-2012, JGraph Ltd
 */
package com.mxgraph.examples.web;

import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * CrayonScript Inc.
 *
 */
public class WorkspaceServlet extends HttpServlet
{

	private static String WORKSPACE_PATH = "../CrayonScriptPluginUnityProject/Assets/";

	/**
	 *
	 */
	private static final long serialVersionUID = -4442397463551836919L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		OutputStream out = response.getOutputStream();
		PrintWriter writer = new PrintWriter(out);

		ObjectMapper mapper = new ObjectMapper();

		// TODO: FIXME fix upfront iteration with incremental

		Iterator<File> iterator = FileUtils.iterateFilesAndDirs(
				new File(WORKSPACE_PATH),
				TrueFileFilter.INSTANCE,
				TrueFileFilter.INSTANCE);

		ArrayList<File> builderList = new ArrayList<>();
		while (iterator.hasNext())
		{
			File file = iterator.next();
			if ("meta".equalsIgnoreCase(FilenameUtils.getExtension(file.getName())))
			{
				continue;
			}
			builderList.add(file);
		}

		WorkspaceNode rootNode = new WorkspaceNode();
		HashMap<File, WorkspaceNode> referenceMap = new HashMap<>();

		for (File file : builderList)
		{
		    WorkspaceNode thisNode = new WorkspaceNode();
			String name = file.getName();
			thisNode.name = name;
			referenceMap.put(file, thisNode);
		}

		for (File file : builderList)
		{
		    WorkspaceNode thisNode = referenceMap.get(file);
			WorkspaceNode parentNode = referenceMap.get(file.getParentFile());
			if (parentNode != null)
			{
				parentNode.children.add(thisNode);
			}
			else
			{
				rootNode.children.add(thisNode);
			}
		}

		String plainText = mapper.writeValueAsString(rootNode);
		String encodedText = OpenServlet.encodeURIComponent(plainText);

		writer.println(encodedText);

		writer.flush();
		writer.close();
	}

	private static class WorkspaceNode
	{
		public ArrayList<WorkspaceNode> children = new ArrayList<>();

		public String name;

		public String path;
	}
}
