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
	public void contextLoads() {
		String tStrDN = "CN=宋洋洋,T=232324198904261835,L=jirburong,NAME=";
		String[] dnArr = tStrDN.split(",");
		JSONObject object = new JSONObject();
		for (String dn:dnArr) {
			String[] key = dn.split("=");
			object.put(key[0],key[1]);
		}

		System.out.println(object.getString("T"));
	}

	@Test
	public void testDate(){
		Date overDate = DateUtil.parseStrToDate("2019-06-04", "yyyy-MM-dd", null);
		Date sysDate = new Date();

		System.out.println(overDate.after(sysDate));
	}

}
