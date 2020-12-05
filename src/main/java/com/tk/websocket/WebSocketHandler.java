package com.tk.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tk.sz.service.UserService;
import com.tk.sz.template.KmhProductDto;
import com.tk.sz.template.KmhVo;
import com.tk.sz.template.RespTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author zhusg02
 * @date 2020/12/4 8:47
 */
@ServerEndpoint(value = "/kmh/getData/{voteCode}")
@Component
public class WebSocketHandler {
    public static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketHandler> webSocketSet = new CopyOnWriteArraySet<WebSocketHandler>();
    private String uid = "";
    private Session session;
    boolean flag = true;
    //ExecutorService threadPool = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
    @Autowired
    private UserService userServiceMapper;
    private static UserService userService;

    @PostConstruct
    public void init(){
        WebSocketHandler.userService = this.userServiceMapper;
    }

    @OnOpen
    public void onOpen(Session session,@PathParam("voteCode") String voteCode) throws IOException {
        this.session = session;
        webSocketSet.add(this);

        RespTemplate resp = new RespTemplate();

        logger.info("开门红押注websocket后端接收的...................json:   " + voteCode);
        KmhVo res = new KmhVo();

        new Thread(()->{
            Integer num = null;
            while (flag){
                try {
                    List<KmhProductDto> productList = userService.getProductData(voteCode);
                    Integer totalNum = userService.getTotalNum(voteCode);
                    res.setProductList(productList);
                    res.setTotalNum(totalNum);

                    if (!res.getTotalNum().equals(num)) {
                        num = res.getTotalNum();
                        resp.setCode("0");
                        resp.setData(JSON.toJSONString(res));
                        resp.setMessage("成功");
                        this.sendMessage(JSON.toJSONString(resp));
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("参数信息：{},uid:{}",message,uid);
        //群发消息
        for (WebSocketHandler item : webSocketSet) {
            try {
                item.sendMessage(JSON.toJSONString(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(){
        flag = false;
        webSocketSet.remove(this);
        logger.info("连接断开");
        if (session != null){
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义消息推送、可群发、单发
     * */
    public static void sendInfo(String message,@PathParam("id") String id) throws IOException {
        logger.info("推送消息到前端:{}，推送信息:{}",id,message);
        for (WebSocketHandler item : webSocketSet) {
            try {
                //这里可以设定只推送给这个id的，为null则全部推送
                if(id==null) {
                    item.sendMessage(message);
                }else if(item.uid.equals(id)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("连接异常！");
        error.printStackTrace();
    }

    // 发送信息
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
