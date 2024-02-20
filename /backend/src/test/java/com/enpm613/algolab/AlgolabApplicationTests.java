package com.enpm613.algolab;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;

@SpringBootTest
class AlgolabApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Spring version : "+SpringVersion.getVersion());
	}



}
