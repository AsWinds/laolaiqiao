package com.codi.backgroundservice.smsengineer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.codi.backgroundservice.config.ServiceConfigUtils;
import com.codi.backgroundservice.constants.ServiceConstant;
import com.codi.bus.constant.GlobalConstant;

public class CLanSMSClient {

	private final String CONFIG_FILE = "/CLanSMSConfig.xml";
	private Map<String, String> errorMsgDic;
	private String apiUrl;
	// 行业短信
	private String account;
	private String password;
	private String action_sendsms;
	// 营销短信
	private String marketingAccount;
	private String marketingPassword;
	private String marketingApiUrl;
	
	private static final Logger logger = LoggerFactory.getLogger(CLanSMSClient.class);

	// 单例
	private CLanSMSClient() throws ParserConfigurationException, SAXException, IOException {
		// 从CLanSMSConfig.xml读取对应的配置数据
		LoadCLanConfig();
	}

	public static CLanSMSClient getInstance() throws ParserConfigurationException, SAXException, IOException {
		return SingletonHolder.getClient();
	}

	// 静态内部类
	// 只有第一次调用getInstance方法才会导致虚拟机加载SingletonHolder类，此方式能确保线程安全和单例对象的唯一性
	private static class SingletonHolder {
		private static CLanSMSClient client;

		public static CLanSMSClient getClient() throws ParserConfigurationException, SAXException, IOException {
			if (client == null)
				client = new CLanSMSClient();
			return client;
		}
	}

	private void LoadCLanConfig() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream in = ServiceConfigUtils.class.getResourceAsStream(CONFIG_FILE);
		;
		Document document = builder.parse(in);
		Element element = document.getDocumentElement();
		this.apiUrl = element.getElementsByTagName("ApiUrl").getLength() > 0
				? element.getElementsByTagName("ApiUrl").item(0).getTextContent() : "";
		// 行业短信
		this.account = element.getElementsByTagName("Account").getLength() > 0
				? element.getElementsByTagName("Account").item(0).getTextContent() : "";
		this.password = element.getElementsByTagName("Password").getLength() > 0
				? element.getElementsByTagName("Password").item(0).getTextContent() : "";
		// 营销短信
		this.marketingAccount = element.getElementsByTagName("MarketingAccount").getLength() > 0
				? element.getElementsByTagName("MarketingAccount").item(0).getTextContent() : "";
		this.marketingPassword = element.getElementsByTagName("MarketingPassword").getLength() > 0
				? element.getElementsByTagName("MarketingPassword").item(0).getTextContent() : "";
		this.marketingApiUrl = element.getElementsByTagName("MarketingApiUrl").getLength() > 0
				? element.getElementsByTagName("MarketingApiUrl").item(0).getTextContent() : "";

		this.action_sendsms = element.getElementsByTagName("SendSms").getLength() > 0
				? element.getElementsByTagName("SendSms").item(0).getTextContent() : "";
		// 读取并初始化错误标识的错误消息列表
		errorMsgDic = new HashMap<String, String>();
		NodeList errorElems = element.getElementsByTagName("Error");
		for (int i = 0; i < errorElems.getLength(); i++) {
			Element errorElem = (Element) errorElems.item(i);
			String errorCode = errorElem.getAttribute("Key");
			String errorMessage = errorElem.getAttribute("Value");
			errorMsgDic.put(errorCode, errorMessage);
		}
	}

	public CLanSendSMSResult SendSMSMessage(String mobile, String content, boolean needStatus, String extNo,
			Integer smsType) throws Exception {

		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		CLanSendSMSResult sendSMSResult = new CLanSendSMSResult();
		try {

			// 默认行业类短信
			String smsAccount = this.account;
			String smsPassword = this.password;
			String smsUrl=this.apiUrl;
			// 切换营销类短信
			if (smsType == GlobalConstant.SMS_TYPE_MARKETING) {
				smsAccount = this.marketingAccount;
				smsPassword = this.marketingPassword;
				smsUrl=this.marketingApiUrl;
			}

			URI base = new URI(smsUrl, false);
			method.setURI(new URI(base, this.action_sendsms, false));
			method.setQueryString(new NameValuePair[] { new NameValuePair("account", smsAccount),
					new NameValuePair("pswd", smsPassword), new NameValuePair("mobile", mobile),
					new NameValuePair("needstatus", String.valueOf(needStatus)), new NameValuePair("msg", content),
					new NameValuePair("extno", extNo), });
			logger.info(MessageFormat.format("url={0}, querystring={1}", smsUrl, method.getQueryString()));
			
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				String response = URLDecoder.decode(baos.toString(), "UTF-8");

				try {
					/*
					 * 结果： 20110725160412,0 1234567890100
					 */
					String[] lines = response.split("\n");
					String[] items = lines[0].split(",");
					String resultCode = items[1];
					sendSMSResult.setResultCode(resultCode);
					if (errorMsgDic.containsKey(resultCode)) {
						sendSMSResult.setResultMessage(errorMsgDic.get(resultCode));
					}

					/*
					 * // 只有发生成功，才有messageId if (resultCode.equals("0")) {
					 * sendSMSResult.setMessageId(lines[1]); }
					 */
				} catch (Exception exception) {
					sendSMSResult.setResultCode(ServiceConstant.ERROR_PARSE_INCORRECT);
					if (errorMsgDic.containsKey(ServiceConstant.ERROR_PARSE_INCORRECT)) {
						sendSMSResult.setResultMessage(
								MessageFormat.format(errorMsgDic.get(ServiceConstant.ERROR_PARSE_INCORRECT),
										exception.getMessage(), response));
					}
				}

				return sendSMSResult;

			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}

	}
}
