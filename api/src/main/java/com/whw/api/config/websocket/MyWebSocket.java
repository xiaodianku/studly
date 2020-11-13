//package com.whw.api.config.websocket;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
////@ServerEndpoint(value = "/websocket/{uid}") //接受websocket请求路径
//@Component  //注册到spring容器中
//@Slf4j
//public class MyWebSocket {
//
//
//    //保存所有在线socket连接
//    private static Map<String,Session> uidSocketMap = new LinkedHashMap<>();
//
//    //记录当前在线数目
//    private static int count=0;
//
//    //处理连接建立
//    @OnOpen
//    public void onOpen(Session session,@PathParam("uid") String uid){
//        if(null == uid) {
//            return;
//        }
//        uidSocketMap.put(uid,session);
//        addCount();
//        log.info("新的连接加入：{}",session.getId());
//    }
//
//    //接受消息
//    @OnMessage
//    public void onMessage(String message,Session session){
//        log.info("收到客户端{}消息：{}",session.getId(),message);
//        try{
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    //处理错误
//    @OnError
//    public void onError(Throwable error,Session session){
//        log.info("发生错误{},{}",session.getId(),error.getMessage());
//    }
//
//    //处理连接关闭
//    @OnClose
//    public void onClose(Session session){
//        List<String> list = new ArrayList<>();
//        uidSocketMap.forEach((k, v)->{
//            try{
//                if(v.getId().equals(session.getId())) {
//                    list.add(k);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        });
//        list.stream().forEach(s -> {
//            uidSocketMap.remove(s);
//        });
//        reduceCount();
//        log.info("连接关闭:{}", session.getId());
//    }
//
//    //群发消息
//
//
//    //发送消息
//    public void sendMessage(String message,String uid) throws IOException {
//        log.info("发送消息："+message);
//        Session session = uidSocketMap.get(uid);
//        if(session != null){
//            session.getBasicRemote().sendText(message);
//        }
//    }
//
//    //广播消息
//    public static void broadcast(String message){
//        log.info("广播消息："+message);
//        MyWebSocket.uidSocketMap.forEach((k,v)->{
//            try{
//                v.getBasicRemote().sendText("这是一条测试广播:"+message);
//            }catch (Exception e){
//            }
//        });
//    }
//
//    //获取在线连接数目
//    public static int getCount(){
//        return count;
//    }
//
//    //操作count，使用synchronized确保线程安全
//    public static synchronized void addCount(){
//        MyWebSocket.count++;
//    }
//
//    public static synchronized void reduceCount(){
//        MyWebSocket.count--;
//    }
//}
