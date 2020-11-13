//package com.whw.api.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.whw.api.config.websocket.MyWebSocket;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/socket")
//public class SocketController {
//
//    @Autowired
//    private MyWebSocket myWebSocket;
//
//    @GetMapping("/testSend/{uid}")
//    public String broadcast(@PathVariable String uid) throws IOException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type", "1");
//        jsonObject.put("data", "1");
//        myWebSocket.sendMessage(jsonObject.toJSONString(), uid);
//        return "ok";
//    }
//
//}
//
