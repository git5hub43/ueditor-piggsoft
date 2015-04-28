package com.piggsoft.ueditor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;

public class TestFastJson {

	@Test
	public void test() {
		Message m1 = new Message();
		m1.setMessage("m1");
		List<Message> list = new ArrayList<>();
		Message m2 = new Message();
		m2.setMessage("m2");
		list.add(m2);
		m1.setList(list);
		System.out.println(JSON.toJSONString(m1));
	}

	@Test
	public void test02() throws IOException {
		String content = StreamUtils.copyToString(
				TestFastJson.class.getResourceAsStream("config.json"),
				Charset.forName("UTF-8"));
		content = filter(content);
		System.out.println(JSON.parseObject(content));
	}

	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	private String filter(String input) {
		return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
	}
}
