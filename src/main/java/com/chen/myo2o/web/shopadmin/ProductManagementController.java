package com.chen.myo2o.web.shopadmin;

import com.chen.myo2o.dto.ImageHolder;
import com.chen.myo2o.dto.ProductExecution;
import com.chen.myo2o.entity.Product;
import com.chen.myo2o.entity.ProductCategory;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.enums.ProductStateEnum;
import com.chen.myo2o.exception.ProductOperationException;
import com.chen.myo2o.service.ProductCategoryService;
import com.chen.myo2o.service.ProductService;
import com.chen.myo2o.util.CodeUtil;
import com.chen.myo2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    //支持上传商品详情图最大数量
    private static final int IMAXGEMAXCOUNT = 6;


    @RequestMapping(value = "/getproductlistbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getProductListByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap();
        //获取前台传入页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取前台传入的每页要求返回的商品数量
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //从session中获取店铺的信息，主要是shopId
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if((pageIndex>-1) && (pageSize>-1) &&(currentShop!=null) &&currentShop.getShopId() != null){
            //获取传入的需要检索的条件，包括是否需要从某个商品类别以及模糊查找商品名去筛选某个店铺下的商品列表
            //筛选条件可以进行排列组合
            long productCategoryId = HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName = HttpServletRequestUtil.getString(request,"productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            //传入查询条件以及分页信息,返回相应商品列表以及总数
            ProductExecution productExecution = productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("success",true);
            modelMap.put("count", productExecution.getCount());
            modelMap.put("productList", productExecution.getProductList());
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if(productCategoryId!=-1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if(productName !=null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }

    @RequestMapping(value = "/getproductbyid",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getProductById(@RequestParam Long productId){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //非空判断
        if(productId>-1){
            //获取商品信息
            Product product = productService.getProductById(productId);
            //获取店铺下的商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty_productId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/addproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProduct(HttpServletRequest request)  {
        Map<String,Object> modelMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("error","输入了错误的验证码");
            return modelMap;
        }
        //接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product ;
        String productStr = HttpServletRequestUtil.getString(request,"productStr");
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
        try {
            if(multipartResolver.isMultipart(request)){
                thumbnail = handleImage((MultipartHttpServletRequest) request, thumbnail, productImgList);

            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        //尝试获取前端传过来的表单string流并将其转成Product实体类
        try {
            product = mapper.readValue(productStr,Product.class);
        } catch (IOException e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        if(product != null && thumbnail!=null && productImgList!=null){
            try {
                //从session中获取当前店铺ID并赋值给product,减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                //执行添加操作
                ProductExecution productExecution = productService.addProduct(product,thumbnail,productImgList);
                if(productExecution.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } catch (ProductOperationException e) {
               modelMap.put("succcess",false);
               modelMap.put("errMsg",e.toString());
               return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入商品信息");
        }
        return  modelMap;
    }
    @RequestMapping(value = "/modifyproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyProduct(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //是商品编辑时候调用还是上下架操作的时候调用
        //若为前者则进行验证码验证判断，后者跳过验证码判断
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        //验证码判断
        if(!statusChange&&!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码输入有误");
            return modelMap;
        }
        //接收前端参数的变量的初始化，包括商品，缩略图，详情图列表，实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product ;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //若请求中存在文件流，则取出相关文件（包括缩略图和详情图）
        try {
            if(multipartResolver.isMultipart(request)){
                thumbnail = handleImage((MultipartHttpServletRequest) request, thumbnail, productImgList);
            }
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try {
            String productStr = HttpServletRequestUtil.getString(request,"productStr");
            //尝试获取前端传来的表单string流将其装换成Product实体类
            product = mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        //非空判断
        if(product!=null){
            try {
                //从session中获取当前店铺的Id并赋值给Product,减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                //开始进行商品信息变更
                ProductExecution productExecution = productService.modifyProduct(product,thumbnail,productImgList);
                if(productExecution.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入商品信息");
        }
        return modelMap;
    }

    private ImageHolder handleImage(MultipartHttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartRequest = request;
        //取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if(thumbnailFile != null){
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
        }
        //取出详情图列表并构建List<ImageHolder>列表对象，最多支持6张图片上传
        for (int i=0;i<IMAXGEMAXCOUNT;i++){
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg"+i);
            if(productImgFile !=null){
                //若取出的第i个详情图文件流不为空,则将其加入详情图列表
                ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
                productImgList.add(productImg);
            }else{
                break;
            }
        }
        return thumbnail;
    }
}
