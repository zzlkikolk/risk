package com.api.zhangzl.riskDemo.Service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class CheckPointServiceTest {

    @Resource
    CheckPointService checkPointService;
    @Test
    void checkPoint() {
        checkPointService.checkPoint();
    }
}