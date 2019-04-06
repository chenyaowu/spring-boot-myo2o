$(function () {
    //从URL里获取productId参数值
    var productId = getQueryString("productId");
    //通过productId获取商品信息的URL
    var infoUrl = '/myo2o/shopadmin/getproductbyid?productId='+productId;
    //获取当前店铺设定的商品类别列表的URL
    var categoryUrl = '/myo2o/shopadmin/getproductcategorylist';
    //更新商品信息的URL
    var productPostUrl = '/myo2o/shopadmin/modifyproduct';

    if(productId){
        //若有productId则为编辑操作
        getInfo();
    }else{
        $("h1").html("新增商品");
        getCategory();
        productPostUrl='/myo2o/shopadmin/addproduct';
    }

    //获取需要编辑的商品的商品信息，并赋值给表
    function getInfo() {
        $.getJSON(infoUrl,
            function (data) {
                if(data.success){
                    //从返回的json当中获取product对象的信息，并赋值
                    var product = data.product;
                    $('#product-name').val(product.productName);
                    $('#product-desc').val(product.productDesc);
                    $('#priority').val(product.priority);
                    $('#point').val(product.point);
                    $('#normal-price').val(product.normalPrice);
                    $('#promotion_price').val(product.promotionPrice);
                    //获取原本商品类别以及该店铺的所有商品列表
                    var optionHtml ='';
                    var optionArr = data.productCategoryList;
                    var optionSelected = product.productCategory.productCategoryId;
                    //生成前端的HTML商品类别列表，并默认选择编辑前的商品类别
                    optionArr.map(function (item,index) {
                        var isSelect = optionSelected === item.productCategoryId ? 'selected' : '';
                        optionHtml +=
                            '<option data-value=" ' + item.productCategoryId + '"' + isSelect + '>' + item.productCategoryName + '</option>'
                    });
                    $('#category').html(optionHtml);
                }
            }
        );
    }
    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if(data.success){
                var productCaterotyList = data.data;
                var optionHtml ='';
                productCaterotyList.map(function (item, index) {
                    optionHtml += '<option data-value=" ' + item.productCategoryId + '">' + item.productCategoryName + '</option>'
                });
                $("#category").html(optionHtml);
            }
        });
    }

    //针对商品详情图空间组，若该控件组的最后一个元素发生变化（即上传了图片）
    //且控件数量未达到6个，则生成新的控件
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
        if($('detail-img').length < 6){
            $('#detail-img').append('<input type="file" class="detail-img"> ');
        }
    });

    //提交按钮的事件响应，分别对商品添加和编辑操作作不同响应
    $('#submit').click(function () {
        //创建商品Json对象,并从表单里面获取对应的属性值
        var product={};
        product.productName = $('#product-name').val();
        product.productDesc = $('#product-desc').val();
        product.priority = $('#priority').val();
        product.point = $('#point').val();
        product.normalPrice = $('#normal-price').val();
        product.promotionPrice=$('#promotion_price').val();
        product.productCategory = {
            productCategoryId : $('#category').find('option').not(function () {
                return !this.selected;
            }).data('value')
        };
        product.productId = productId;

        //获取缩略图文件流
        var thumbnail = $('#small-img')[0].files[0];
        //生成表单对象，用于接收参数并传递给后台
        var formData = new FormData();
        formData.append('thumbnail', thumbnail);
        //遍历商品详情图控件，获取里面的文件流
        $('.detail-img').map(function (index, item) {
            //判断控件是否已选择文件
            if($('.detail-img')[index].files.length>0){
                //将第i个文件流赋值给key为productImgi的表单键值对里
                formData.append('productImg' + index,$('.detail-img')[index].files[0]);
            }
        });
        //将product.json对象转成字符流保存至表单对象Key为productStr的键值对里
        formData.append('productStr', JSON.stringify(product));
        //获取表单的验证码
        var verifyCodeActual = $('#j_captcha').val();
        if(!verifyCodeActual){ $.toast('请输入验证码');  return; }
        formData.append('verifyCodeActual', verifyCodeActual);
        //将数据提交到后台
        $.ajax({
            url: productPostUrl, type:'POST', data:formData,
            contentType: false,  processData: false,
            success: function (data) {
                if(data.success){
                    $.toast('提交成功！');
                }
                else{
                    $.toast('提交失败！');
                }
                $('#captcha_img').click();
            }
        });
    });

})