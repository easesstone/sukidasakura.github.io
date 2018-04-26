/*
package hzgc.coding.sakura;

import hzgc.coding.others.JSONHelper;
import hzgc.coding.others.LogEvent;
import hzgc.coding.others.MergeSendCallback;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

*/
/**
 * 恢复处理出错的数据，作为Ftp的一个线程。（马燊偲）
 * 整体流程：
 * 1，遍历所有error日志：data/process/p-0/error/error.log
 * data/process/p-1/error/error.log
 * data/process/p-2/error/error.log
 * 对于每个错误日志：获取错误日志的状态：
 * A、若文件处于lock状态，结束；
 * B、若文件为unlock状态：
 * 立即移动data/process/p-0/error/error.log到
 * /success/process/201802/p-0/error/error 2018-02-01-1522148-1758.log（用于备份）和
 * /merge/error/error 2018-02-01-1522148-1963.log（用于恢复处理）
 * 2，对备份目录/merge/error/ 下的所有错误文件进行扫描，
 * 将所有错误日志路径放入到一个List 里面，errLogPaths。遍历errLogPaths：
 * 1，对于errLogPaths中每一个errorN.log，遍历其中每一条数据：
 * 1，对每条数据，提取特征发送Kafka，
 * 2，同时把发送失败的数据重新写到/merge/error/下的一个新的errorN-NEW日志中，
 * 3，发送成功的数据，不进行记录。
 * 2，删除原有已处理过的错误日志errorN.log。
 * 3，结束
 *//*

public class RecoverErrProDataThread implements Runnable {

    private Logger LOG = Logger.getLogger(RecoverErrProDataThread.class);
    private static final String SUFFIX = ".log";
    private static CommonConf commonConf;
    private String feature = ProducerOverFtpProperHelper.getTopicFeature();

    //构造函数
    RecoverErrProDataThread(CommonConf commonConf) {
        RecoverErrProDataThread.commonConf = commonConf;
    }

    @Override
    public void run() {
        //初始化FileUtil工具类
        MergeUtil mergeUtil = new MergeUtil();

        //获取processLog的根目录：/opt/RealTimeFaceCompare/ftp/data/process
        String processLogDir = commonConf.getProcessLogDir();
        //获取merge/error目录：/opt/RealTimeFaceCompare/ftp/merge/error
        String mergeErrLogDir = commonConf.getMergeLogDir() + File.separator + "error";

        //列出process目录下所有error日志路径
        List<String> allErrorDir = mergeUtil.listAllErrorLogAbsPath(processLogDir);
        for (String errFile : allErrorDir) {
            //获取每个error.log需要移动到的success和merge目录下的路径
            String successErrFile = mergeUtil.getSuccessFilePath(errFile);
            String mergeErrFile = mergeUtil.getMergeFilePath(errFile);
            //移动到merge后，拷贝一份到success
            mergeUtil.lockAndMove(errFile, mergeErrFile); //其中包括判断锁是否存在
            mergeUtil.copyFile(mergeErrFile, successErrFile);
        }

        //获取merge/error下所有error日记文件的绝对路径，放入一个List中（errLogPaths）
        List<String> errFilePaths = mergeUtil.listAllFileAbsPath(mergeErrLogDir);
        //若errLogPaths这个list不为空（merge/error下有错误日志）
        if (errFilePaths != null && errFilePaths.size() != 0) { // V-1 if start
            //对于每一个error.log
            for (String errorFilePath : errFilePaths) {
                ProducerKafka kafkaProducer = ProducerKafka.getInstance();
                //获取其中每一行数据
                List<String> errorRows = mergeUtil.getAllContentFromFile(errorFilePath);
                //判断errorRows是否为空，若不为空，则需要处理出错数据
                if (errorRows != null && errorRows.size() != 0) { // V-2 if start
                    for (String row : errorRows) {
                        //用JSONHelper将某行数据转化为LogEvent格式
                        LogEvent event = JSONHelper.toObject(row, LogEvent.class);
                        String ftpUrl = event.getFtpPath();
                        //根据路径取得对应的图片，并提取特征，封装成FaceObject，发送Kafka
                        FaceObject faceObject = GetFaceObject.getFaceObject(row);
                        if (faceObject != null) { // V-3 if start
                            MergeSendCallback mergeSendCallback = new MergeSendCallback(
                                    feature,
                                    ftpUrl,
                                    event);
                            String mergeErrFileNew = errorFilePath.replace(SUFFIX, "") + "-N" + SUFFIX;
                            mergeSendCallback.setWriteErrFile(mergeErrFileNew);
                            kafkaProducer.
                                    sendKafkaMessage(feature, ftpUrl, faceObject, mergeSendCallback);
                        } // V-3 if end：faceObject不为空的判断结束
                    }
                } // V-2 if end：errorRows为空的判断结束
                //删除已处理过的error日志
                mergeUtil.deleteFile(errorFilePath);
            }
        } else { //若merge/error目录下无日志
            LOG.info("Nothing in " + mergeErrLogDir);
        } // V-1 if end：对merge/error下是否有日志的判断结束
    }
}*/
