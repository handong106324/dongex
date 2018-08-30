package utils;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class AlgorithmBase {

    @Setter @Getter
    private String symbol;

    //判断穿越
    protected boolean compareCross(double prevData,
                                   double currentData,
                                   double prevCompareData,
                                   double currentCompareData,
                                   String direct)
    {
        //从下而上穿越长天期天数线时，是一个值得注意的买入信号
        if(direct.equals("up")){
            if(currentData > currentCompareData && prevData < prevCompareData) {
                return true;
            }
        }else if(direct.equals("down")){
            //当短天期天数线从上而下穿越长天期天数线时,卖出信号
            if(currentData < currentCompareData && prevData > prevCompareData) {
                return true;
            }
        }
        return false;

    }


}