package com.song.archives.scheduled;

import com.song.archives.utils.MySQLDatabaseBackup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Value("${backup.path}")
    private String savePath;

    @Value("${mysqldump.path}")
    private String mysqlDumpath;

    //每周星期天凌晨1点实行一次
    @Scheduled(cron = "0 0 1 ? * 1")
    private void configureTasks() {
        String backUpFileName  = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql";
        Boolean backFlag = false;
        try {
            backFlag = MySQLDatabaseBackup.exportDatabaseTool(mysqlDumpath,"localhost","root","root",savePath,backUpFileName ,"zscq");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String backResult = backFlag?"成功":"失败";
        System.out.println("定时备份："+savePath+"/"+backUpFileName+backResult);
    }
}