package com.song.archives;

import com.song.archives.utils.DateUtil;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

public class ArchivesApplicationTests {

    @Test
    public void test1(){
        String url = "/ship/index";
        System.out.println(url.contains("/index"));
    }

}
