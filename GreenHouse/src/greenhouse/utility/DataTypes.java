/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.utility;

/**
 *
 * @author Hasitha Lakmal
 */
public enum DataTypes {
    TEMPERATURE("temperature"),
    PRESSURE("pressure"),
    LIGHT("light"),
    PH("ph"),
    HUMIDITY("humidity"),
    SOIL_MOISTURE("soil_moisture"),
    ALL("*");
    
    private final String columnName;

    DataTypes(String columnName) {
        this.columnName = columnName;
    }

    public String columnName() {
        return columnName;
    }
}
