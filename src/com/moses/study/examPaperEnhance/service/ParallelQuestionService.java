package com.moses.study.examPaperEnhance.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.moses.study.examPaperEnhance.dao.SingleQuestionDao;
import com.moses.study.examPaperEnhance.util.Constants;
import com.moses.study.examPaperEnhance.util.MockQuestionBankGenerator;
import com.moses.study.examPaperEnhance.vo.QuestionInCacheVo;
import com.moses.study.examPaperEnhance.vo.QuestionInDBVo;
import com.moses.study.examPaperEnhance.vo.TaskResultVo;

/**
 *
 * 类说明：并发处理题目的服务
 */
public class ParallelQuestionService {
	// 已处理题目的缓存
	private static ConcurrentHashMap<Integer, QuestionInCacheVo> questionCache = new ConcurrentHashMap<>();
	// 正在处理题目的缓存
	private static ConcurrentHashMap<Integer, Future<QuestionInCacheVo>> questionProcessingCache = new ConcurrentHashMap<>();
	private static ExecutorService makeQuestionService = Executors.newFixedThreadPool(Constants.CPU_COUNT * 2);
	
	public TaskResultVo makeQuestion(Integer questionId) {
		QuestionInCacheVo qstCacheVo = questionCache.get(questionId);
		if(qstCacheVo == null) {
			System.out.println("题目[" + questionId + "]在缓存中不存在，准备启动任务...");
			return new TaskResultVo(submitAndGetQuestionCacheFuture(questionId));
		}
		String questionSha = MockQuestionBankGenerator.getSha(questionId);
		if(questionSha.equals(qstCacheVo.getQuestionSha())) {
			System.out.println("题目[" + questionId + "]在缓存中已存在，且未变化.");
			return new TaskResultVo(qstCacheVo.getQuestionDetail());
		} else {
			System.out.println("题目[" + questionId + "]在缓存中已存在，但是发生了变化，更新缓存.");
			return new TaskResultVo(submitAndGetQuestionCacheFuture(questionId));
		}
	}
	
	private Future<QuestionInCacheVo> submitAndGetQuestionCacheFuture(Integer questionId){
		Future<QuestionInCacheVo> questionFuture = questionProcessingCache.get(questionId);
		try {
			if(questionFuture == null) {
				QuestionInDBVo qstDbVo = MockQuestionBankGenerator.getQuetion(questionId);
				QuestionTask questionTask = new QuestionTask(qstDbVo, questionId);
				/*不靠谱的，无法避免两个线程处理同一个题目
				questionFuture = makeQuestionService.submit(questionTask);
				processingQuestionCache.putIfAbsent(questionId, questionFuture);
				改成
				processingQuestionCache.putIfAbsent(questionId, questionFuture);
				questionFuture = makeQuestionService.submit(questionTask);
				也不行
				*/
				
				FutureTask<QuestionInCacheVo> ft = new FutureTask<QuestionInCacheVo>(questionTask);
				//先在Map中占位，以防止过程中其他线程处理相同question
				questionFuture = questionProcessingCache.putIfAbsent(questionId, ft);
				if(questionFuture == null) {
					questionFuture = ft;
					makeQuestionService.execute(ft);
					System.out.println("成功启动了题目[" + questionId + "]的计算任务，请等待完成...");
				} else {
					System.out.println("..........有其他线程刚刚启动了题目[" + questionId +"]的计算任务，本任务无需开启.");
				}
			} else {
				System.out.println("题目[" + questionId + "]已在处理中，无需再次处理.");
			}
		} catch(Exception e) {
			questionProcessingCache.remove(questionId);
			e.printStackTrace();
			throw e;
		}
		return questionFuture;
	}
	
	//解析题目的任务类
	private class QuestionTask implements Callable<QuestionInCacheVo>{
		private QuestionInDBVo qstDbVo;
		private Integer questionId;
		public QuestionTask(QuestionInDBVo qstDbVo, Integer questionId) {
			super();
			this.qstDbVo = qstDbVo;
			this.questionId = questionId;
		}
		
		public QuestionInCacheVo call() throws Exception{
			try {
				SingleQuestionDao dao = new SingleQuestionDao();
				String qstDetail = dao.makeQuestion(questionId, MockQuestionBankGenerator.getQuetion(questionId).getDetail());
				String questionSha = qstDbVo.getSha();
				QuestionInCacheVo qstCache = new QuestionInCacheVo(qstDetail, questionSha);
				questionCache.put(questionId, qstCache);
				return qstCache;
			} finally {
				questionProcessingCache.remove(questionId);
			}
		}
	}
}
