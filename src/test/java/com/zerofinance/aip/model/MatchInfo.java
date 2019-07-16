/*
 * 描述： 人脸对比測試Model
 * 修改人： Weng.weng
 * 修改时间： Jan 11, 2019
 * 项目： AIP
 */
package com.zerofinance.aip.model;

import com.aeasycredit.commons.lang.base.BaseModel;

/**
 * 人脸对比測試Model<br>
 * 
 * @author Weng.weng
 * @version [版本号, Jan 11, 2019]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MatchInfo extends BaseModel {
    
    private static final long serialVersionUID = 4281875445730626236L;
    
    // 判定為同一個人的最低分值
    private double scoreIfSamePerson;

    // 正类判定为正类 
    private int tpCount;
    
    // 负类判定为正类
    private int fpCount;
    
    // 正类判定为负类
    private int fnCount;
    
    // 负类判定为负类
    private int tnCount;
    
    // 正類數
    private int positivesCount;
    
    // 負類數
    private int negativesCount;
    
    /** @return 返回 scoreIfSamePerson */
    public double getScoreIfSamePerson() {
        return scoreIfSamePerson;
    }

    /** @param 对 scoreIfSamePerson 进行赋值 */
    public void setScoreIfSamePerson(double scoreIfSamePerson) {
        this.scoreIfSamePerson = scoreIfSamePerson;
    }

    /** @return 返回 tpCount */
    public int getTpCount() {
        return tpCount;
    }

    /** @param 对 tpCount 进行赋值 */
    public void setTpCount(int tpCount) {
        this.tpCount = tpCount;
    }

    /** @return 返回 fpCount */
    public int getFpCount() {
        return fpCount;
    }

    /** @param 对 fpCount 进行赋值 */
    public void setFpCount(int fpCount) {
        this.fpCount = fpCount;
    }

    /** @return 返回 fnCount */
    public int getFnCount() {
        return fnCount;
    }

    /** @param 对 fnCount 进行赋值 */
    public void setFnCount(int fnCount) {
        this.fnCount = fnCount;
    }

    /** @return 返回 tnCount */
    public int getTnCount() {
        return tnCount;
    }

    /** @param 对 tnCount 进行赋值 */
    public void setTnCount(int tnCount) {
        this.tnCount = tnCount;
    }

    /** @return 返回 positivesCount */
    public int getPositivesCount() {
        return positivesCount;
    }

    /** @param 对 positivesCount 进行赋值 */
    public void setPositivesCount(int positivesCount) {
        this.positivesCount = positivesCount;
    }

    /** @return 返回 negativesCount */
    public int getNegativesCount() {
        return negativesCount;
    }

    /** @param 对 negativesCount 进行赋值 */
    public void setNegativesCount(int negativesCount) {
        this.negativesCount = negativesCount;
    }
}
