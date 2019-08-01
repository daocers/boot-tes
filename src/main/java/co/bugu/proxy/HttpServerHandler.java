package co.bugu.proxy;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

/**
 * @Author daocers
 * @Date 2019/7/30:15:47
 * @Description:
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private String oriHost = "http://47.93.189.30";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
//            todo  读取请求channel中的数据，转发
            DefaultHttpRequest request = (DefaultHttpRequest) msg;
            String uri = request.uri();

            String contentType = getResponseContentType(uri);
            String method = request.getMethod().name();
            if ("/favicon.ico".equals(uri)) {
                return;
            }
            logger.info("begin::{}", System.currentTimeMillis());

            String res = "";
            try {
                URL url = new URL(oriHost + uri);
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection connection = (HttpURLConnection) urlConnection;
                connection.setRequestMethod(method);

                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder builder = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    res = builder.toString();
                    logger.info("获取到的网页url: {}信息为：{}", new String[]{uri, res});
                }
                connection.disconnect();
            } catch (Exception e) {
                logger.error("代理服务器报错", e);
            }
            if (res.contains("<html>")) {
                contentType = "text/html";
            }

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(res != null ? res.getBytes() : new byte[0]));
            response.headers()
                    .set(CONTENT_TYPE, contentType)
                    .set(CONTENT_LENGTH, response.content().readableBytes())
                    .set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(response);


        } else {
//            抛出异常，防止请求卡住
            throw new Exception();
        }
    }

    private String getResponseContentType(String uri) {
        if (uri.endsWith(".css")) {
            return "text/css";
        } else if (uri.endsWith(".js")) {
            return "application/x-javascript";
        } else if (uri.equals(".jpg")) {
            return "application/x-jpg";
        } else if (uri.equals(".png")) {
            return "application/x-png";
        } else {
            return "application/octet-stream";
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
