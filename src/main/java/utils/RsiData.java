package utils;

import lombok.Data;

@Data
public class RsiData {

    //0=无 涨为1  跌为2
    private int direct;

    //收盘价
    private double closePrice;

    //涨跌数值
    private double riseFall;

    //上升平均数
    private double upAverage;

    //下降平均数
    private double downAverage;

    //RSI值
    private double rsi;

    //时间
    private long  timestamp;
}