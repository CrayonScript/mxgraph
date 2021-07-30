package com.mxgraph.model;

import java.awt.*;
import java.io.Serializable;
import java.util.StringTokenizer;

public class mxCellTextParser implements Serializable
{
    public enum ParserType
    {
        UNKNOWN,
        EXPRESSION,
        STATEMENT
    }

    private static String DELIMITER = " ";

    private String text = "";

    private ParserType parserType = ParserType.UNKNOWN;

    private ParserNode parserNode = null;

    public mxCellTextParser() { }

    public String getText()
    {
        return text;
    }

    public void setText(String value)
    {
        text = value;
    }

    public boolean canDeleteCharAtLocation(int location)
    {
        return false;
    }

    public int getNextTabFromLocation(int location)
    {
        return 5;
    }

    public Point getSelectionAtLocation(int location)
    {
        return new Point(location, location+2);
    }

    protected void parseTokenTree()
    {
        // tokenize the text
        // identify the text boundaries including whitespace
        StringTokenizer strTokenizer = new StringTokenizer(text, DELIMITER,true /* returnDelims */);
        while (strTokenizer.hasMoreTokens())
        {
            String nextToken = strTokenizer.nextToken();
        }
    }

    static class ParserTree implements Serializable
    {
        private ParserNode rootNode;
        private String text;

        ParserTree(String value)
        {
            text = value;
        }
    }

    static class ParserNode implements Serializable
    {

    }
}
