package com.codi.backgroundservice.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPoster {
	public HttpPoster(){
		this.encoding = "gb2312";
	}
	private String url;
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return this.url;
	}
	private HttpURLConnection connection;
	private URL uri;
	private String encoding;
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	public String getEncoding(){
		return this.encoding;
	}
	public String PostAndGetResponse(RequestProperty[] requestProperties,String content) throws IOException{
		uri = new URL(url);
		System.out.println("URL:"+url);
		System.out.println("Content:"+content);
		connection = (HttpURLConnection)uri.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		if(requestProperties != null){
			for(RequestProperty rp : requestProperties){
				connection.setRequestProperty(rp.getName(), rp.getValue());
			}
		}
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),encoding);
		out.write(content);
		out.flush();
		out.close();
		String outputString = "";
		InputStream is = connection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String currentLine= "";
		while((currentLine = br.readLine())!=null){
			outputString += currentLine +"\r\n";
		}
		System.out.println("\r\nResponse:" + outputString + "");
		return outputString;
	}

	public HttpURLConnection getConnection(){
		return connection;
	}
	
}
