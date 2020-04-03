package com.centit.product.oa;

import com.alibaba.fastjson.JSONObject;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.service.impl.BbsManagerImpl;
import com.centit.support.database.utils.PageDesc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource(value = "classpath:system.properties")
@ComponentScan(basePackages = {"com.centit"},
        excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import(value = {JdbcConfig.class})
@ContextConfiguration(classes = TestPiece.class)
@RunWith(SpringRunner.class)
public class TestPiece {


    @Autowired
    private BbsManagerImpl bbsManager;

    /*@Autowired
    private BbsController bbsController;*/
    @Test
    public void batchcreateBbsPieceTest(){
        /*BbsPiece bbsPiece = new BbsPiece();
        bbsPiece.setApplicationId("asjfioadfa");
        bbsPiece.setOptId("sfs");
        bbsPiece.setOptTag("你好");
        bbsManager.createBbsPiece(bbsPiece);
        System.out.println("successful");*/
        for (int i=0;i<10;i++){
            createBbsPieceTest();
        }
    }
   // @Before
    @Test
    public void  createBbsPieceTest(){
        BbsPiece bbsPiece = new BbsPiece();
        bbsPiece.setApplicationId("asjfioadfa");
        bbsPiece.setOptId("sfs");
        bbsPiece.setOptTag("你好啊");
        bbsManager.createBbsPiece(bbsPiece);
        System.out.println("successful");
    }

    @Test
    public void updateBbsPieceTest(){
        BbsPiece bbsPiece = new BbsPiece();
        bbsPiece.setApplicationId("asjfioadfa");
        bbsPiece.setOptId("sfs");
        bbsPiece.setOptTag("你好啊");
        bbsManager.createBbsPiece(bbsPiece);
        System.out.println("successful");
    }

    //listBbsPieces
    @Test
    public void listBbsPiecesTest(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("applicationId","asjfioadfa");
        map.put("optId","sfs");
        List<BbsPiece> bbsPieces = bbsManager.listBbsPieces(map, new PageDesc(1,20));
        for (BbsPiece bbsPiece : bbsPieces) {
            System.out.println(bbsPiece.getDeliverDate());
        }
        System.out.println("successful");
    }

    //deleteBbsPieceByID
    @Test
    public void deleteBbsPieceByID(){

        //bbsManager.deleteBbsPieceByID("iy0R2Hx0TV6bYDDfGaXrDA");

        System.out.println("successful");
    }

    //根据提供的pieceId获取对应的pieceContent
    @Test
    public void getBbsPieceByID(){

        //String pieceContent = bbsManager.getBbsPieces("fCFBAFOcSXKuTDlcbC3MCA");

        //System.out.println(pieceContent);
    }

    //对Bbscontroller中的方法进行测试
    @Test
    public void createBbsPieceControllerTest(){
        BbsPiece bbsPiece = new BbsPiece();
        bbsPiece.setApplicationId("1234648");
        bbsPiece.setOptId("query");
        bbsPiece.setOptTag("欢迎回来");
      //  bbsController.createBbsPiece(bbsPiece);
        System.out.println("hahah");
    }

    @Test
    public void jsonTest(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","zhansan");
        Object o = JSONObject.toJSON(map);
        System.out.println(o.toString());
        Object parse = JSONObject.parse(o.toString());
        System.out.println(parse);
    }


}
