package main.java;

import genertor.MySqlScriptGenerater;
import genertor.SqlScriptGenerator;

public class SqlScriptGen {
    public static void main( String[] args ) throws Exception {
        SqlScriptGenerator generator = new SqlScriptGenerator( new MySqlScriptGenerater(), "excel.xml" );
        generator.generate();
    }
}
