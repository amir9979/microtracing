package com.starsloader.logtrace;
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.HttpURLConnection;  
import java.net.URL;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.Map;  

public class TimeTest {
	private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(TimeTest.class.getName()); 	
    public static void main(String[] args) throws Exception{
        sayHello();
		httpConnect();
    }

    public static void sayHello() {
        try {
             Thread.sleep(500);
             log.info("hello world!" );
			 
        } catch (InterruptedException e) {
             e.printStackTrace();
        }
		log.info(doSomething());
   }

   public static void httpConnect() throws Exception{
        String requestUrl = "http://baidu22x2.com";  
        Map<String, Object> requestParamsMap = new HashMap<String, Object>();  
        requestParamsMap.put("areaCode", "001");  
        requestParamsMap.put("areaCode1", "�й�");  
        PrintWriter printWriter = null;  
        BufferedReader bufferedReader = null;  
        // BufferedReader bufferedReader = null;  
        StringBuffer responseResult = new StringBuffer();  
        StringBuffer params = new StringBuffer();  
        HttpURLConnection httpURLConnection = null;  
        // ��֯�������  
        Iterator it = requestParamsMap.entrySet().iterator();  
        while (it.hasNext()) {  
            Map.Entry element = (Map.Entry) it.next();  
            params.append(element.getKey());  
            params.append("=");  
            params.append(element.getValue());  
            params.append("&");  
        }  
        if (params.length() > 0) {  
            params.deleteCharAt(params.length() - 1);  
        }  	   
		try {  
            URL realUrl = new URL(requestUrl);  
            // �򿪺�URL֮�������  
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();  
            // ����ͨ�õ���������  
            httpURLConnection.setRequestProperty("accept", "*/*");  
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");  
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(params.length()));  
            // ����POST�������������������  
            httpURLConnection.setDoOutput(true);  
            httpURLConnection.setDoInput(true);  
            // ��ȡURLConnection�����Ӧ�������  
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());  
            // �����������  
            printWriter.write(params.toString());  
            // flush������Ļ���  
            printWriter.flush();  
            // ����ResponseCode�ж������Ƿ�ɹ�  
            int responseCode = httpURLConnection.getResponseCode();  
            if (responseCode != 200) {  
                log.warning(" Error===" + responseCode);  
            } else {  
                log.info("Post Success!");  
            }  
            // ����BufferedReader����������ȡURL��ResponseData  
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));  
            String line;  
            while ((line = bufferedReader.readLine()) != null) {  
                responseResult.append("/n").append(line);  
            }  
        } catch (Exception e) {  
            log.warning("send post request error!" + e);  
			throw e;
        } finally {  
            if (httpURLConnection!=null) httpURLConnection.disconnect();  
            try {  
                if (printWriter != null) {  
                    printWriter.close();  
                }  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
  
        }  
   }
   
   public static String doSomething() {
       try {
           Thread.sleep(2000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       log.info("gen uuid");
	   return java.util.UUID.randomUUID().toString();
   }	   
}
