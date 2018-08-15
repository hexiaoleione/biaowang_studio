package com.hex.express.iwant.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
/**
 * http请求工具类
 * @author SCHT-40
 *
 */
public class HttpUtil {
	/**
	 * 发送get请求
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		//定义结果
		String result = null;
		//创建连接
		try {
			URL url2 = new URL(url);
			//打开连接
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
			//设置请求方式和请求时长
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			//获取返回码
			int responseCode = conn.getResponseCode();
			//判断code结果
			if (responseCode == 200) {
				//连接成功就获取数据 文件输入流
				InputStream in = conn.getInputStream();
				//定义一个内存流
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//定义一个缓冲区
				byte[] buffer = new byte[1024];
				int lens = 0;
				while ((lens = in.read(buffer)) !=-1) {
					//把数据写到内存中去
					baos.write(buffer, 0, lens);
				}
				//刷新
				baos.flush();
				//把流装换成字节数组
				byte[] bs = baos.toByteArray();
				//关闭流
				baos.close();
				//把字节数组转换成字符串
				result = new String(bs);
				//关闭流
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回结果
		return result;
	}
	  /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    } 

     /**
      * 通过get请求往服务器提交数据(登录)
      * @param path  请求路径
      * @param username  用户名
      * @param password  密码
      * @return
      * @throws Exception
      */
     public boolean loginByGet(String path,String username,String password) throws Exception{
      ///http://192.168.1.101:8080/web/LoginServlet?name=%E7%BE%8E%E5%A5%B3&password=123456
      StringBuilder sb = new StringBuilder(path);
      sb.append("?");
      sb.append("name=").append(URLEncoder.encode(username, "utf-8"));
      sb.append("&");
      sb.append("password=").append(password);
      
      URL url = new URL(sb.toString());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(5000);
      conn.setRequestMethod("GET");
      if(conn.getResponseCode() == 200){
       return true;
      }
      return false;
     }
     
     /**通过post请求向服务器提交数据(登录)
      * post   请求首先是先把数据写入到缓存。一定要向服务器去获取数据
      * @param path
      * @param username
      * @param password
      * @return
      * @throws Exception
      */
     public boolean loginByPost(String path,String username,String password) throws Exception{
      
      URL url = new URL(path);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      
      conn.setConnectTimeout(5000);
      conn.setRequestMethod("POST");
      
      //name=%E5%8F%B0%E6%B9%BE%E5%AF%8C%E5%B0%91&password=123456
      StringBuilder sb = new StringBuilder();
      sb.append("name=").append(URLEncoder.encode(username, "utf-8")).append("&");
      sb.append("password=").append(password);
      byte[] entity = sb.toString().getBytes();
      
      //设置请求参数
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//实体参数的类型
      conn.setRequestProperty("Content-Length", entity.length+"");//实体参数的长度
      //允许对外输出
      conn.setDoOutput(true);
      OutputStream os = conn.getOutputStream();
      os.write(entity);
      
      if(conn.getResponseCode() == 200){
       return true;
      }
      return false;
     }
     
     /**
      * 通过HttpClient  以get请求向服务器提交数据(登录)
      * @param path
      * @param username
      * @param password
      * @return
      * @throws Exception
      */
     public boolean loginByHttpClientGet(String path,String username,String password) throws Exception{
      
      StringBuilder sb = new StringBuilder(path);
      sb.append("?");
      sb.append("name=").append(URLEncoder.encode(username, "utf-8"));
      sb.append("&");
      sb.append("password=").append(password);
      
      //1 得到浏览器
      HttpClient httpClient = new DefaultHttpClient();//浏览器
      
      //2 指定请求方式
      HttpGet httpGet = new HttpGet(sb.toString());
      
      //3执行请求
      HttpResponse httpResponse = httpClient.execute(httpGet);
      
      //4判断请求是否成功
      int statusCode = httpResponse.getStatusLine().getStatusCode();
      if(statusCode == 200){
       return true;
      }
      return false;
     }
     
     /**
      * 通过httpClient  以post请求向服务器发送数据
      * @param path
      * @param username
      * @param password
      * @return
      * @throws Exception
      */
     public boolean loginHttpClientByPost(String path,String username,String password) throws Exception{
      
      //1 得到浏览器
      HttpClient httpClient = new DefaultHttpClient();
      
      //2 指定请求方式
      HttpPost httpPost = new HttpPost(path);
      
      //3构建请求实体的数据
      List<NameValuePair> parameters = new ArrayList<NameValuePair>();
      parameters.add(new BasicNameValuePair("name", username));
      parameters.add(new BasicNameValuePair("password", password));
      
      //4 构建实体
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
      
      //5 把实体数据设置到请求对象
      httpPost.setEntity(entity);
      
      //6 执行请求
      HttpResponse httpResponse = httpClient.execute(httpPost);
      
      //7 判断请求是否成功
      if(httpResponse.getStatusLine().getStatusCode() == 200){
       return true;
      }
      
      return false;
     }
     /**
      * 读取数据
      * @param urlpath
      * @return
      */
 	public static byte[] request(String urlpath){ //alt+shift+z
		ByteArrayOutputStream baos=null;
		try {
			baos=new ByteArrayOutputStream();
			//1 创建URL
			URL url = new URL(urlpath);
			//2创建HttpUrlConnection
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			//3设置请求参数
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			//4建立连接
			connection.connect();
			//5处理响应
			if(connection.getResponseCode()==200){
				InputStream is=connection.getInputStream();
				byte[] buf=new byte[1024*4];
				int len=0;
				while((len=is.read(buf))!=-1){
					baos.write(buf,0,len);
				}

				return baos.toByteArray();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
