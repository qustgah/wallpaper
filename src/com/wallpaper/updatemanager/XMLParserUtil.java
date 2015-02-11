package com.wallpaper.updatemanager;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLParserUtil {

	/** 
     * ��ȡ�汾������Ϣ 
     *  
     * @param is 
     *            ��ȡ���ӷ���version.xml�ĵ��������� 
     * @return 
     */  
    public static VersionInfo getUpdateInfo(InputStream is) {  
        VersionInfo info = new VersionInfo();  
        try {  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            factory.setNamespaceAware(true);  
            XmlPullParser parser = factory.newPullParser();  
            parser.setInput(is, "UTF-8");  
            int eventType = parser.getEventType();  
            while (eventType != XmlPullParser.END_DOCUMENT) {  
                switch (eventType) {  
                case XmlPullParser.START_TAG:  
                    if ("version".equals(parser.getName())) {  
                        info.setVersion(parser.nextText());  
                    } else if ("updateTime".equals(parser.getName())) {  
                        info.setUpdateTime(parser.nextText());  
                    } else if ("updateTime".equals(parser.getName())) {  
                        info.setUpdateTime(parser.nextText());  
                    } else if ("downloadURL".equals(parser.getName())) {  
                        info.setDownloadURL(parser.nextText());  
                    } else if ("displayMessage".equals(parser.getName())) {  
                        info.setDisplayMessage(parseTxtFormat(parser.nextText(), "##"));  
                    } else if ("apkName".equals(parser.getName())) {  
                        info.setApkName(parser.nextText());  
                    } else if ("versionCode".equals(parser.getName())) {  
                        info.setVersionCode(Integer.parseInt(parser.nextText()));  
                    }  
                    break;  
                case XmlPullParser.END_TAG:  
                    break;  
                }  
                eventType = parser.next();  
            }  
        } catch (XmlPullParserException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return info;  
    }  
  
    /** 
     * ����ָ���ַ���ʽ���ַ��������У� 
     *  
     * @param data 
     *            ��Ҫ��ʽ�����ַ��� 
     * @param formatChar 
     *            ָ����ʽ���ַ� 
     * @return 
     */  
    public static String parseTxtFormat(String data, String formatChar) {  
        StringBuffer backData = new StringBuffer();  
        String[] txts = data.split(formatChar);  
        for (int i = 0; i < txts.length; i++) {  
            backData.append(txts[i]);  
            backData.append("\n");  
        }  
        return backData.toString();  
    }  
  
}
