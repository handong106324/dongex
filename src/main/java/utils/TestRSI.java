package utils;


import lombok.extern.log4j.Log4j;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Log4j
public class TestRSI extends AlgorithmRSI{

    public int sellScore = 90;
    public int buyScore  = 20;

    public static void main(String[] args) throws SQLException {
        new TestRSI().start();
    }

    public void start() throws SQLException {

    }

}