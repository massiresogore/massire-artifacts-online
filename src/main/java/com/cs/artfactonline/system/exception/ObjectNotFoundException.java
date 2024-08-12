package com.cs.artfactonline.system.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String ObjectName, String id)
    {
        super("Could not Find "+ObjectName+" with Id:"+id+ ":(");
    }

    public ObjectNotFoundException(String ObjectName, Integer id)
    {
        super("Could not Find "+ObjectName+" with Id:"+id+ ":(");
    }
}
