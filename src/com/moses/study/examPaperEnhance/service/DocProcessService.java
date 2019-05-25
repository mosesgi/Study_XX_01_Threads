package com.moses.study.examPaperEnhance.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.moses.study.examPaperEnhance.util.MockBusiness;
import com.moses.study.examPaperEnhance.vo.SrcDocVo;
import com.moses.study.examPaperEnhance.vo.TaskResultVo;

/**
 *
 *类说明：处理文档的服务，包括文档中题目的处理和文档生成后的上传
 */
public class DocProcessService {
	/**
     * 上传文档到网络
     * @param docFileName 实际文档在本地的存储位置
     * @return 上传后的网络存储地址
     */
    public String upLoadDoc(String docFileName){
        Random r = new Random();
        MockBusiness.doBusiness(9000 + r.nextInt(400));
        return "http://www.xxxx.com/file/upload/"+docFileName;
    }

    /**
     * 将待处理文档处理为本地实际PDF文档
     * @param pendingDocVo 待处理文档
     * @return 实际文档在本地的存储位置
     */
    public String processAndGenerateLocalDoc(SrcDocVo pendingDocVo){
        System.out.println("开始处理文档："+ pendingDocVo.getDocName());
        SingleQuestionService sqService = new SingleQuestionService();
        StringBuffer sb = new StringBuffer();
        //循环处理文档中的每个题目
        for(Integer questionId: pendingDocVo.getQuestionList()){
            sb.append(sqService.makeQuestion(questionId));
        }
        return "complete_"+System.currentTimeMillis()+"_" + pendingDocVo.getDocName()+".pdf";
    }
    
    //Async using ParallelQuestionService
    public String processAndGenerateDocAsync(SrcDocVo pendingDocVo) {
    	System.out.println("开始处理文档："+ pendingDocVo.getDocName());
    	ParallelQuestionService pqService = new ParallelQuestionService();
    	
    	Map<Integer, TaskResultVo> qstResultMap = new HashMap<>();
    	for(Integer questionId: pendingDocVo.getQuestionList()) {
    		qstResultMap.put(questionId, pqService.makeQuestion(questionId));
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	for(Integer questionId: pendingDocVo.getQuestionList()) {
    		TaskResultVo result = qstResultMap.get(questionId);
    		try {
				sb.append(result.getQuestionDetail() == null ?
						result.getQuestionFuture().get().getQuestionDetail() : result.getQuestionDetail());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
    	}
    	return "Completed_" + System.currentTimeMillis() + "_" + pendingDocVo.getDocName() + ".pdf";
    }
}
