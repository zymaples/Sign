package com.sign;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Scanner;


public class GetPkcs implements Pkcs {
    public static void main(String[] args) throws Exception{
        //测试证书
        String issuerStr ="CN=西部CA,OU=研发部,O=gitbook有限公司,C=CN,E=gitbook@sina.com,L=银川,ST=宁夏";
        String subjectStr ="CN=西部CA,OU=研发部,O=gitbook有限公司,C=CN,E=gitbook@sina.com,L=银川,ST=宁夏";
        String certificateCRL="https://www.cwca.com.cn";
        System.out.println("请输入密码");
        Scanner pwd=new Scanner(System.in);
        String password = pwd.nextLine();
        Map<String,byte[]> result = Pkcs.createCert(password,issuerStr,subjectStr,certificateCRL);
        FileOutputStream outPutStream = new FileOutputStream("E:/CWCA/Sign/file/keystore.p12"); // ca.jks
        outPutStream.write(result.get("keyStoreData"));
        outPutStream.close();
        FileOutputStream fos = new FileOutputStream(new File("E:/CWCA/Sign/file/keystore.cer"));
        fos.write(result.get("certificateData"));
        fos.flush();
        fos.close();
    }

}
