/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles;

/**
 *
 * @author thiagomazuhimcosta
 */
public enum DataType {
    SELLER("001", "Vendedor"),
    CLIENT("002", "Cliente"),
    SALE("003", "Venda");

    private String value;
    private String description;

    private DataType(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public String getValue()
    {
        return value;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return this.description;
    }

    public static DataType forValue(String value)
    {
        if (value != null)
        {
            for (DataType dataType : DataType.values())
            {
                if (dataType.value.equals(value))
                {
                    return dataType;
                }
            }
        }

        return null;
    }
}
