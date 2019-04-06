package com.chen.myo2o.service.impl;

import com.chen.myo2o.dao.ProductDao;
import com.chen.myo2o.dao.ProductImgDao;
import com.chen.myo2o.dto.ImageHolder;
import com.chen.myo2o.dto.ProductExecution;
import com.chen.myo2o.entity.Product;
import com.chen.myo2o.entity.ProductImg;
import com.chen.myo2o.enums.ProductStateEnum;
import com.chen.myo2o.exception.ProductOperationException;
import com.chen.myo2o.service.ProductService;
import com.chen.myo2o.util.ImageUtil;
import com.chen.myo2o.util.PageCalculator;
import com.chen.myo2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException {
       /*
       @Param imageHolder 缩略图
       @Pram imageHolderList 详情图
       1.处理缩略图，获取缩略图相对路径并赋值给product
       2.往tb_product写入商品信息，获取productId
       3.结合productId批量处理商品详情图
       4.将商品详情图列表批量插入tb_product_img中
        */
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() > 0) {
            //给商品设置上默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //默认上架状态
            product.setEnableStatus(1);
            //如果商品缩略图不为空则添加
            if (imageHolder != null) {
                addThumbnail(product, imageHolder);
            }
            try {
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("商品创建失败");
                }
            } catch (ProductOperationException e) {
                throw new ProductOperationException("商品创建失败" + e.toString());
            }
            //若商品详情图不为空则添加
            if (imageHolderList != null && imageHolderList.size() > 0) {
                addProductImgList(product, imageHolderList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY_LIST);
        }
    }

    @Override
    public Product getProductById(long id) {
        return productDao.queryProductById(id);
    }

    @Override
    @Transactional
    /*
    1.若缩略图参数有值，则先处理缩略图
     .若原先存在缩略图，则先删除再添加新图，之后获取缩略图相对路径并赋值给product
    2.若商品详情图列表数值有值，对商品详情图列表进行做同样操作
    3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
    4.更新tb_product的信息
     */
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> imageHolderList) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            //给商品设置默认值
            product.setLastEditTime(new Date());
            //若商品缩略图不为空且原有缩略图不为空，则删除原有的缩略图并添加
            if (thumbnail != null) {
                Product tempProduct = productDao.queryProductById(product.getProductId());
                if (tempProduct.getImgAddr() != null) {
                    ImageUtil.deleteFile(tempProduct.getImgAddr());
                }
                addThumbnail(product, thumbnail);
            }
            //如果有新存的商品详情图，则原先的删除，并添加新的图片
            if (imageHolderList != null && imageHolderList.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgList(product, imageHolderList);
            }
            try {
                int effectNum = productDao.updateProduct(product);
                if (effectNum <= 0) {
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("更新商品信息失败:" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        //页码转换成数据库行代码，并代用dao层取回指定页码的商品列表
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        //基于同样的查询条件返回该查询条件下的商品总数
        int count = productDao.queryProductCount(productCondition);
        ProductExecution productExecution = new ProductExecution();
        productExecution.setCount(count);
        productExecution.setProductList(productList);
        return productExecution;
    }

    /*
    删除某个商品下的所有详情图
     */
    private void deleteProductImgList(Long productId) {
        //根据ProductId获取原来的图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //删掉原来的图片
        for (ProductImg productImg : productImgList) {
            ImageUtil.deleteFile(productImg.getImgAddr());
        }
        //删除数据库里的图片
        productImgDao.deleteProductImgByProductId(productId);
    }

    //批量添加详情图
    private void addProductImgList(Product product, List<ImageHolder> imageHolderList) {
        String desc = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        for (ImageHolder productImgHolder : imageHolderList) {
            String imgAddr = ImageUtil.generateNormalImg(productImgHolder, desc);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        //如果确实有图片需要添加的，就执行添加操作
        if (productImgList.size() > 0) {
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            } catch (ProductOperationException e) {
                throw new ProductOperationException("创建商品详情图片失败" + e.toString());
            }
        }
    }


    //添加缩略图
    private void addThumbnail(Product product, ImageHolder imageHolder) {
        String desc = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(imageHolder, desc);
        product.setImgAddr(thumbnailAddr);
    }
}
