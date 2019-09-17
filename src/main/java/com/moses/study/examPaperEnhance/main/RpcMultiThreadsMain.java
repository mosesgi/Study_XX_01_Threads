package com.moses.study.examPaperEnhance.main;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.moses.study.examPaperEnhance.service.DocProcessService;
import com.moses.study.examPaperEnhance.service.PreDocGenerator;
import com.moses.study.examPaperEnhance.util.Constants;
import com.moses.study.examPaperEnhance.util.MockQuestionBankGenerator;
import com.moses.study.examPaperEnhance.vo.SrcDocVo;

/**
 * 类说明：rpc服务端，采用生产者消费者模式，生产者消费者还会级联
 * 
 * @author Moses
 *
 */
public class RpcMultiThreadsMain {

	// 负责生成文档
	private static ExecutorService docMakeService = Executors.newFixedThreadPool(Constants.CPU_COUNT * 2);
	// 负责上传文档
	private static ExecutorService docUploadService = Executors.newFixedThreadPool(Constants.CPU_COUNT * 2);

	private static CompletionService<String> docMakeCs = new ExecutorCompletionService<>(docMakeService);

	private static CompletionService<String> docUploadCs = new ExecutorCompletionService<>(docUploadService);
	
	//生成文档的任务
	private class MakeDocTask implements Callable<String>{
		private SrcDocVo pendingDocVo;
		
		public MakeDocTask(SrcDocVo pendingDocVo) {
			super();
			this.pendingDocVo = pendingDocVo;
		}

		@Override
		public String call() throws Exception {
			DocProcessService dpService = new DocProcessService();
			long start = System.currentTimeMillis();
//			String localFileName = dpService.processAndGenerateLocalDoc(pendingDocVo);
			String localFileName = dpService.processAndGenerateDocAsync(pendingDocVo);
			System.out.println("文档" + localFileName + "生成耗时：" + (System.currentTimeMillis() - start) + " ms.");
			return localFileName;
		}
	}
	
	//上传文档的任务
	private class UploadDocTask implements Callable<String>{
		private String filePath;

		public UploadDocTask(String filePath) {
			super();
			this.filePath = filePath;
		}
		
		public String call() throws Exception {
			DocProcessService dpService = new DocProcessService();
			long start = System.currentTimeMillis();
			String remoteUrl = dpService.upLoadDoc(filePath);
			System.out.println("已上传至[" + remoteUrl + "], 耗时: " + (System.currentTimeMillis() - start) + " ms.");
			return remoteUrl;
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("题库开始初始化……");
		MockQuestionBankGenerator.initBank();
		System.out.println("题库初始化完成。");
		
		//Create two PreDoc
		List<SrcDocVo> docList = new PreDocGenerator().makePendingDoc(60);
		DocProcessService dpService = new DocProcessService();
		RpcMultiThreadsMain rmtm = new RpcMultiThreadsMain();
		long start = System.currentTimeMillis();
		
		for(SrcDocVo doc: docList) {
			docMakeCs.submit(rmtm.new MakeDocTask(doc));
		}
		
		for(SrcDocVo doc: docList) {
			Future<String> future = docMakeCs.take();		//???take()会阻塞，用completionService有何用？
			docUploadCs.submit(rmtm.new UploadDocTask(future.get()));
		}
		
		//实际业务中可不要，主要为了取得总时间
		for(SrcDocVo doc: docList) {
			docUploadCs.take().get();
		}
		
		System.out.println("-------------------共耗时: " + (System.currentTimeMillis()-start) + " ms---------------------");
	}
}
