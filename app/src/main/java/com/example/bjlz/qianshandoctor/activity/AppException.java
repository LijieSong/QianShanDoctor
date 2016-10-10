/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；

package com.example.bjlz.qianshandoctor.activity;

import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名称：QianShanDoctor
 * 类描述：
 * 创建人：slj
 * 创建时间：2016-9-22 11:17
 * 修改人：slj
 * 修改时间：2016-9-22 11:17
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class AppException extends Exception {
    private String times;//时间
    private String exception;//异常信息
    private String PhoneInfo;//收集信息
    private  SaveListener<String> saveListener;

    public String getTimes() {
        return times;
    }

    public void setDate(String times) {
        this.times = times;
    }

    public String getException() {
        return exception;
    }

    public void setExceptionDetail(String exception) {
        this.exception = exception;
    }

    public String getPhoneInfo() {
        return PhoneInfo;
    }

    public void setPhoneInfo(String phoneInfo) {
        PhoneInfo = phoneInfo;
    }

    public void save(SaveListener<String> saveListener) {
    }
}
