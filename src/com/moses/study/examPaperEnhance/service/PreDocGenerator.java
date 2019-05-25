package com.moses.study.examPaperEnhance.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.moses.study.examPaperEnhance.util.Constants;
import com.moses.study.examPaperEnhance.vo.SrcDocVo;

public class PreDocGenerator {
	/**
    * 生成一批待处理文档
    * @param docCount 生成的文档数量
    * @return 待处理文档列表
    */
   public List<SrcDocVo> makePendingDoc(int count){
       Random r = new Random();
       //文档列表
       List<SrcDocVo> docList = new LinkedList<>();
       for(int i=0;i<count;i++){
           List<Integer> questionList = new LinkedList<Integer>();
           //每个文档中包含的题目列表
           for(int j=0;j<Constants.QUESTION_COUNT_IN_DOC;j++){
               int questionId = r.nextInt(Constants.QUESTION_BANK_SIZE);
               questionList.add(questionId);
           }
           SrcDocVo pendingDocVo = new SrcDocVo("PreDoc_"+i, questionList);
           docList.add(pendingDocVo);
       }
       return docList;
   }
}
