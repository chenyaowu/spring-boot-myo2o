package com.chen.myo2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//提供两类路径
@Configuration
public class PathUtil {

    private static String winPath;
    private static String linuxPath;
    private static String shopPath;

    @Value("${win.base.path}")
    public static void setWinPath(String winPath) {
        PathUtil.winPath = winPath;
    }
    @Value("${linux.base.path}")
    public static void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }

    @Value("${shop.relevant.path}")
    public static void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    private static String seperator = System.getProperty("file.separator");

    //返回项目图片根路径
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath;
        if(os.toLowerCase().startsWith("win")){
            basePath = winPath;
        }else{
            basePath = linuxPath;
        }
        basePath = basePath.replace("/",seperator);
        return  basePath;
    }
    //依据不同业务需求，返回相应图片的子路径
    public static String getShopImagePath(long shopId){
        String imagePath = shopPath + shopId+seperator;
        return imagePath.replace("/",seperator);
    }
}
