package com.mxgraph.examples.web;

import com.mxgraph.util.mxUtils;
import org.mortbay.jetty.handler.ResourceHandler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataStoreHandler
{
    static Logger log = Logger.getLogger(DataStoreHandler.class.getName());

    static DataStoreHandler INSTANCE = new DataStoreHandler();

    public String dataStoreBase;

    private DataStoreHandler()
    {

    }

    public void setDataStoreBase(String value)
    {
        this.dataStoreBase = value;
    }

    public String readData(String key)
    {
        String filePath = getFilePath(key);
        try
        {
            String fileContents = mxUtils.readFile(filePath);
            return fileContents;
        }
        catch(IOException e)
        {
            log.log(Level.SEVERE, String.format("unable to read file %s", filePath));
        }
        return null;
    }

    public void storeData(String key, String contents)
    {
        String filePath = getFilePath(key);
        try
        {
            mxUtils.writeFile(contents, filePath);
        }
        catch(IOException e)
        {
            log.log(Level.SEVERE, String.format("unable to store file %s", filePath));
        }
    }

    protected String getFilePath(String key)
    {
        String filePath = String.format("%s%s%s.xml", this.dataStoreBase, File.separator, key);
        return filePath;
    }
}
