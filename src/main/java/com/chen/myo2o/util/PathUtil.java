package com.chen.myo2o.util;

//提供两类路径
public class PathUtil {
    private static String seperator = System.getProperty("file.separator");
    //返回项目图片根路径
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "E:/projectdev/image/";
        }else{
            basePath = "/home/xiangce/image/";
        }
        basePath = basePath.replace("/",seperator);
        return  basePath;
    }
    //依据不同业务需求，返回相应图片的子路径
    public static String getShopImagePath(long shopId){
        String imagePath = "/upload/item/shop/"+shopId+"/";
        return imagePath.replace("/",seperator);
    }
}
