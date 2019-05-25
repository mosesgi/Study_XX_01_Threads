package com.moses.study.examPaperEnhance.util;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.moses.study.examPaperEnhance.vo.QuestionInDBVo;

public class MockQuestionBankGenerator {
	//存储题库数据,key就是题目的id，value就是题目的详情
    private static ConcurrentHashMap<Integer,QuestionInDBVo> questionBankMap
            = new ConcurrentHashMap<>();
    //定时任务池，负责定时更新题库数据
    private static ScheduledExecutorService updateQuestionBank
            = new ScheduledThreadPoolExecutor(1);

    //初始化题库
    public static void initBank(){
        for(int i=0;i<Constants.QUESTION_BANK_SIZE;i++){
            String questionContent = makeQuestionDetail(Constants.QUESTION_LENGTH);
            questionBankMap.put(i,new QuestionInDBVo(i,
            		questionContent,EncryptUtils.EncryptBySHA1(questionContent)));
        }
        updateQuestionTimer();//启动定期更新题库数据任务
    }

    //生成随机字符串，代表题目的实际内容,length表示题目的长度
    private static String makeQuestionDetail(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //获得题目，我们假设一次数据库的读耗时在一般在20ms左右，所以休眠20ms
    public static QuestionInDBVo getQuetion(int i) {
        MockBusiness.doBusiness(20);
        return questionBankMap.get(i);
    }
    
    //获得题目的摘要，我们假设一次数据库的读耗时在一般在10ms左右，所以休眠10ms
    public static String getSha(int i) {
    	MockBusiness.doBusiness(10);
        return questionBankMap.get(i).getSha();
    }

    //更新题库的定时任务
    private static class UpdateBank implements Runnable{

        @Override
        public void run() {
        	//随机数
            Random random = new Random();
            int questionId = random.nextInt(Constants.QUESTION_BANK_SIZE);
            String questionContent = makeQuestionDetail(Constants.QUESTION_LENGTH);
            questionBankMap.put(questionId,new QuestionInDBVo(questionId,
                    questionContent,EncryptUtils.EncryptBySHA1(questionContent)));
            System.out.println("题目【"+questionId+"】被更新！！");
        }
    }

    //定期更新题库数据
    private static void updateQuestionTimer(){
        System.out.println("开始定时更新题库..........................");
        updateQuestionBank.scheduleAtFixedRate(new UpdateBank(),
                15,5, TimeUnit.SECONDS);
    }
}
