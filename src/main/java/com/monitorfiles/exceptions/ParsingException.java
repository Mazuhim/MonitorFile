/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles.exceptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

/**
 *
 * @author thiago.costa
 */
public class ParsingException extends Exception
{
    private Date occurrenceTime;

    public ParsingException(String message)
    {
        this(message, null);
    }

    public ParsingException(String message, Throwable cause)
    {
        super(message, cause);
        this.occurrenceTime = new Date();
    }

    public Date getOccurrenceTime()
    {
        return occurrenceTime;
    }

    public void setOccurrenceTime(Date occurrenceTime)
    {
        this.occurrenceTime = occurrenceTime;
    }

    public String getStackTraceString()
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream stream = new PrintStream(outputStream))
        {
            this.printStackTrace(stream);
            return outputStream.toString();
        } catch (IOException ex)
        {
            return null;
        }
    }
}
