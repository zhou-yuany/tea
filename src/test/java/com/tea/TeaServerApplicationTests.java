package com.tea;


import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeaServerApplicationTests {

    @Test
    void contextLoads() {
// 创建 HashMap 实例
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");

        HashMap<String, String> map2 = new HashMap<>();
        map2.put("key3", "value3");
        map2.put("key4", "value4");

        // 创建存储 HashMap 的 ArrayList
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        arrayList.add(map1);
        arrayList.add(map2);

        // // 将 ArrayList 转换为数组
        // HashMap<String, String>[] array = new HashMap[arrayList.size()];
        // arrayList.toArray(array);
        HashMap<String, Object> aaa = new HashMap<>();
        aaa.put("attrs", map1);

        // 遍历数组，打印每个 HashMap 的内容
        Gson gson = new Gson();

        String jsonParamsMap = gson.toJson(aaa);
        System.out.println(jsonParamsMap);


    }

}
