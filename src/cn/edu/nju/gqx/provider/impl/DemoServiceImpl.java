package cn.edu.nju.gqx.provider.impl;

import cn.edu.nju.gqx.provider.DemoService;

public class DemoServiceImpl implements DemoService {

	public String sayHello(String name) {
		return "Hello " + name;
	}

}
