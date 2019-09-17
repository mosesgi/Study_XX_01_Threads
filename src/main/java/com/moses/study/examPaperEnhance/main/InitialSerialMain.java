package com.moses.study.examPaperEnhance.main;

import java.util.List;

import com.moses.study.examPaperEnhance.service.DocProcessService;
import com.moses.study.examPaperEnhance.service.PreDocGenerator;
import com.moses.study.examPaperEnhance.util.MockQuestionBankGenerator;
import com.moses.study.examPaperEnhance.vo.SrcDocVo;

/**
 * 最初实现，单线程，串行
 * @author Moses
 *
 */
public class InitialSerialMain {
	public static void main(String[] args) {
		System.out.println("题库开始初始化……");
		MockQuestionBankGenerator.initBank();
		System.out.println("题库初始化完成。");
		
		//Create two PreDoc
		List<SrcDocVo> docList = new PreDocGenerator().makePendingDoc(2);
		DocProcessService dpService = new DocProcessService();
		long startTime = System.currentTimeMillis();
		for(SrcDocVo doc:docList) {
			System.out.println("开始处理文档: " + doc.getDocName() + ".....");
			long docStart = System.currentTimeMillis();
			String localName = dpService.processAndGenerateLocalDoc(doc);
			System.out.println("文档"+localName+" 生成耗时：" +(System.currentTimeMillis()-docStart)+"ms");
			docStart = System.currentTimeMillis();
			String remoteUrl = dpService.upLoadDoc(localName);
			System.out.println("已上传至[" + remoteUrl + "], 耗时: " + (System.currentTimeMillis() - docStart) + "ms");
		}
		System.out.println("-------------------共耗时: " + (System.currentTimeMillis()-startTime) + " ms---------------------");
	}
}
