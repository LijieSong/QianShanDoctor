package com.example.bjlz.qianshandoctor.application;

/**
 * 项目名称：QianShanDoctor
 * 类描述：Address 所用网址
 * 创建人：slj
 * 创建时间：2016-7-1 10:11
 * 修改人：slj
 * 修改时间：2016-7-1 10:11
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class Address {
   //baseurl
//   private static String BASE_URL = "http://192.168.1.11:8080/qianshan/";
    //    public static String BASE_URL = "http://192.168.0.118:8080/qianshan/";//地铁的
    private static final String BASE_URL = "http://192.168.1.149:8080/IAS/";
//    private static final String BASE_URL = "http://192.168.1.11:8080/IAS/";
    //login网址
    public static final String Login_Url = BASE_URL +"login/login.do";
    //患者列表获取的网址  1 医生列表  2 患者列表
    public static final String Obsessive_Url = BASE_URL +"doctor/getDoctorList.do";
   //获取血压列表的网址
    public static final String GetXueYaList_Url = BASE_URL +"blueTooth/getXueYaList.do";

   //测试网址
   public static final String TEXT_URL1 = "http://www.chinasun.com.cn";
    public static final String TEXT_URL2 = "http://25065034.pe168.com";
    public static final String TEXT_URL3= "http://www.chinasunhealth.com";
    public static final String TEXT_URL4 = "https://www.baidu.com";

    public static final String BMobKey ="5fffefc9d3fdc2a9f5410bc22c0e4312";
}
