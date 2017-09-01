package com.codi.backgroundservice.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ServiceConfigUtils {

	private static List<ServiceConfigModel> servicesConfigs;
	private static final String CONFIG_FILE_PATH = "/ServiceConfig.xml";

	private static final String getFirstChildNodeText(Element element, String childNodeName){
		if(element.getElementsByTagName(childNodeName).getLength() == 0)
			return null;
		return element.getElementsByTagName(childNodeName).item(0).getTextContent();
	}
	
	private static void initServiceConfig() throws Exception {
		// 从xml读取Service配置信息
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = factory.newDocumentBuilder();  
        InputStream in = ServiceConfigUtils.class.getResourceAsStream(CONFIG_FILE_PATH);;
        Document document = builder.parse(in);  
        Element element = document.getDocumentElement();  
        servicesConfigs = new ArrayList<ServiceConfigModel>();
        NodeList serviceNodes = element.getElementsByTagName("Service");  
        for(int i=0;i<serviceNodes.getLength();i++){  
            Element serviceElement = (Element) serviceNodes.item(i);  
            ServiceConfigModel scm = new ServiceConfigModel();
            String code = getFirstChildNodeText(serviceElement,"Code");
            String name = getFirstChildNodeText(serviceElement,"Name");
            String className = getFirstChildNodeText(serviceElement,"ClassName");
            String maxThreadNumber = getFirstChildNodeText(serviceElement,"MaxThreadNumber");
            String coreThreadNumber = getFirstChildNodeText(serviceElement,"CoreThreadNumber");
            String intervalMillionSeconds = getFirstChildNodeText(serviceElement,"IntervalMillionSeconds");
            String startType = getFirstChildNodeText(serviceElement,"StartType");
            String startTime = getFirstChildNodeText(serviceElement,"StartTime");
            String needStart = getFirstChildNodeText(serviceElement,"NeedStart");
            scm.setCode(code);
            scm.setName(name);
            scm.setClassName(className);
            scm.setMaxThreadNumber(Integer.parseInt(maxThreadNumber));
            scm.setCoreThreadNumber(Integer.parseInt(coreThreadNumber));
            scm.setIntervalMillionSeconds(Long.parseLong(intervalMillionSeconds));
            scm.setStartType(Integer.parseInt(startType));
            String[] times = startTime.split(",");
            List<String> stList = new ArrayList<String>();
            for (int l = 0; l < times.length; l++) {
            	stList.add(times[l]);
			}
            scm.setStartTime(stList);
            scm.setNeedStart("Y".equalsIgnoreCase(needStart));
            servicesConfigs.add(scm); 
        }  
	}

	static{
		try {
			initServiceConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取所有系统支持的后台服务
	 * @return
	 */
	public static List<ServiceConfigModel> getServiceConfigs(){
		if(servicesConfigs == null)
			try {
				initServiceConfig();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return servicesConfigs;
	}

}
