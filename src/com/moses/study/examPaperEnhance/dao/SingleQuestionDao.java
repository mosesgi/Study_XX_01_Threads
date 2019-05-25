package com.moses.study.examPaperEnhance.dao;

import java.util.Random;

import com.moses.study.examPaperEnhance.util.MockBusiness;

/**
 * 类说明：单个题目的处理器，可以看成DAO层
 * @author Moses
 *
 */
public class SingleQuestionDao {
    /**
     * 对题目进行处理，如解析文本，下载图片等等工作
     * @param questionId 题目id
     * @return 题目解析后的文本
     */
    public String makeQuestion(Integer questionId,String questionSrc){
        Random r = new Random();
        MockBusiness.doBusiness(450+r.nextInt(100));
        return "CompleteQuestion[id="+questionId +" content=:"+ questionSrc+"]";
    }
}
