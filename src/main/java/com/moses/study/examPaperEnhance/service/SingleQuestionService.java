package com.moses.study.examPaperEnhance.service;

import com.moses.study.examPaperEnhance.dao.SingleQuestionDao;
import com.moses.study.examPaperEnhance.util.MockQuestionBankGenerator;

public class SingleQuestionService {
	
	 /**
     * 对题目进行处理
     * @param questionId 题目id
     * @return 题目解析后的文本
     */
	public String makeQuestion(Integer questionId) {
		SingleQuestionDao dao = new SingleQuestionDao();
		return dao.makeQuestion(questionId, MockQuestionBankGenerator.getQuetion(questionId).getDetail());
	}
}
