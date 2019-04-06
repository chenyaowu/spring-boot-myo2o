$(function () {
    var listUrl = '/myo2o/shopadmin/getproductcategorylist';
    var addUrl = '/myo2o/shopadmin/addproductcategorys';
    var deleteUrl = '/myo2o/shopadmin/removeproduccategory';
    getList();
    function getList() {
       $.getJSON(
           listUrl,
           function (data) {
               if(data.success){
                   var dataList = data.data;
                   $('.category-wrap').html('');
                   var tempHtml = '';
                   dataList.map(function (item, index) {
                       tempHtml += ''
                               + '<div class="row row-product-category now">'
                                   + '<div class="col-33 product-category-name">' + item.productCategoryName + '</div>'
                                   + '<div class="col-33">' + item.priority + '</div>'
                                   + '<div class="col-33"><a href="#" class="button delete" data-id="' + item.productCategoryId + '">删除</a></div>'
                               +'</div>';
                   });
                   $('.category-wrap').append(tempHtml);

               }
           }
       );
    }
    $('#new').click(function () {
        var  tempHtml = ''
            + '<div class="row row-product-category temp">'
                + '<div class="col-33"><input class="category-input category" type="text" placeholder="分类名"></div> '
                + '<div class="col-33"><input class="category-input priority" type="text" placeholder="优先级"></div> '
                + '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
            + '</div>';
        $('.category-wrap').append(tempHtml);
    });

    $('#submit').click(function () {
        var tempArr = $('.temp');
        var productCategoryList=[];
        tempArr.map(function (index,item) {
            var tempObject = {};
            tempObject.productCategoryName = $(item).find('.category').val();
            tempObject.priority = $(item).find('.priority').val();
            if(tempObject.productCategoryName && tempObject.priority){
                productCategoryList.push(tempObject);
            }
        });
        $.ajax({
            url:addUrl, type :'POST', contentType:'application/json',
            data : JSON.stringify(productCategoryList),
            success:function (data) {
                if(data.success){
                    $.toast('提交成功');
                    getList();
                }else{
                    $.toast('提交失败');
                }
            }
        });
    });

    function handleList(data) {
        var html = '';
        data.map(function (item,index) {
            html +=
                 '<div class="row row-shop">'
                      + '<div class= "col-40">'+ item.productCategoryName + '</div>'
                      + '<div class="col-40"> '+ item.priority + '</div>'
                      + '<div class="col-20"> <a href="#" class="button">删除</a></div>'
                +'</div>';
        });
        $('.shop-wrap').html(html);
    }

    $('.category-wrap').on('click', '.row-product-category.temp .delete', function (e) {
        $(this).parent().parent().remove();
    });

    $('.category-wrap').on('click', '.row-product-category.now .delete', function (e) {

        var target = e.currentTarget;
        console.log(target);
        console.log(target.dataset.id);
       $.confirm('确定要删除吗？', function () {
           $.ajax({
               url:deleteUrl, type:'POST', dataType:'json',
               data:{ productCategoryId:target.dataset.id },
               success:function (data) {
                   if(data.success){
                       $.toast('删除成功');
                       getList();
                   }else {
                       $.toast('删除失败');
                   }
               }
           });
       });

    });
});