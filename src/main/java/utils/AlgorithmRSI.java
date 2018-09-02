package utils;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

public class AlgorithmRSI extends AlgorithmBase {

    //根据收盘价，计算当天与前一天的收盘价涨跌。

    //M1的两条最近的历史记录
    private List<RsiData> historyData = new LinkedList<>();

    @Setter
    @Getter
    private int operationMinutes = 14;//一般14天作为参数则成为默定值。但在实际操作中，分析者常觉得14天太长了一点，才有5天和9天之方法

    @Setter
    @Getter
    private int klineType = 1;  //K线类型，1就是1分钟K线

    protected  RsiData getRsiData(double closePrice,long timestamp){

        int needInitMinutes   = this.getOperationMinutes(); //需要初始化的最小分钟数

        //需要初始化的最小条数
        int needInitCount = needInitMinutes/this.getKlineType();

        RsiData rsiData = new RsiData();

        rsiData.setClosePrice(closePrice);
        rsiData.setTimestamp(timestamp);

        int rsiDataListCount = this.getHistoryData().size();

        //如果是第一条数据，则没有涨跌
        if(rsiDataListCount<1){
            rsiData.setDirect(0);
        }else{
            int prevIndex = rsiDataListCount-1;
            double prevRsiPrice = this.getHistoryData().get(prevIndex).getClosePrice();

            //计算是涨还是跌
            if( prevRsiPrice > closePrice){
                //设置涨跌类型
                rsiData.setDirect(2);

                //计算涨跌具体数值
                double riseFall = prevRsiPrice - closePrice;
                rsiData.setRiseFall(riseFall);

            }else if(prevRsiPrice < closePrice){
                //设置涨跌类型
                rsiData.setDirect(1);

                //计算涨跌具体数值
                double riseFall = closePrice - prevRsiPrice;
                rsiData.setRiseFall(riseFall);

            }else{
                rsiData.setDirect(0);
                rsiData.setRiseFall(0);
            }

            if( rsiDataListCount < needInitCount ){
                rsiData.setRsi(0);
            }else{
                List<RsiData> rsiCurrentList =  this.getHistoryData();
                List<RsiData> rsiSublist = rsiCurrentList.subList( rsiDataListCount-needInitCount,rsiDataListCount);

                double upAverage = 0;

                double downAverage = 0;

                for (RsiData currentData : rsiSublist) {
                    if( currentData.getDirect() ==1 ){
                        upAverage = upAverage + currentData.getRiseFall();
                    }else if( currentData.getDirect() ==2 ){
                        downAverage = downAverage + currentData.getRiseFall();
                    }
                }
                //计算上升平均数
                upAverage = upAverage/needInitCount;
                rsiData.setUpAverage(upAverage);

                //计算下降平均数
                downAverage = downAverage/needInitCount;
                rsiData.setDownAverage(downAverage);

                //计算RSI值
                double rsiVaule =  upAverage / ( upAverage + downAverage) * 100;
                rsiData.setRsi(rsiVaule);
            }
        }
        return rsiData;

    }

    protected  void addHistoryData(RsiData rsiData){
        historyData.add(rsiData);
    }

    protected List<RsiData> getHistoryData(){
        return historyData;
    }


}