package com.centit.product.oa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.product.oa.dao.BbsPieceDao;
import com.centit.product.oa.dao.InnerMsgDao;
import com.centit.product.oa.po.BbsPiece;
import com.centit.product.oa.po.InnerMsg;
import com.centit.product.oa.service.impl.BbsManagerImpl;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import sun.java2d.pipe.SpanIterator;

import java.util.*;

@PropertySource(value = "classpath:system.properties")
@ComponentScan(basePackages = {"com.centit"},
        excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import(value = {JdbcConfig.class})
@ContextConfiguration(classes = TestPiece.class)
@RunWith(SpringRunner.class)
public class TestPiece {


    @Autowired
    private BbsManagerImpl bbsManager;
    @Autowired
    InnerMsgDao innerMsgDao;

    @Autowired
    BbsPieceDao  bbsPieceDao;

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
        map.put("applicationId","123");
        map.put("optId","123");
        List<BbsPiece> bbsPieces = bbsManager.listBbsPieces(map, new PageDesc(1,200));
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

    @Test
    public void ContentTypeBySqlTest(){
        /*BaseDaoImpl<BbsPiece, String> bbsPieceStringBaseDao = new BaseDaoImpl<BbsPiece, String>() {
            @Override
            public Map<String, String> getFilterField() {
                return null;
            }
        };*/
        //JdbcTemplate jdbcTemplate = bbsPieceDao.getJdbcTemplate();
        String sql = "SELECT * FROM `m_bbs_piece` where OPT_ID = :OPT_ID and OPT_TAG = :OPT_TAG and APPLICATION_ID = :APPLICATION_ID and PIECE_CONTENT LIKE '%\"contentType\":\"file\"%' "; //and PIECE_CONTENT LIKE '%"contentType":"file"%'
        HashMap<String, Object> map = new HashMap<>();//'%\"contentType\":\"file\"%'"
        map.put("OPT_ID",123);
        map.put("APPLICATION_ID",123);
        map.put("OPT_TAG",123);
        map.put("PIECE_CONTENT","%\"contentType\":\"file\"%");
        map.put("ps",0);
        map.put("pz",10);
      //  bbsPieceDao.listObjectsBySql(sql,map);
        String countsql= "SELECT count(1) FROM `m_bbs_piece` where OPT_ID = :OPT_ID and OPT_TAG = :OPT_TAG and APPLICATION_ID = :APPLICATION_ID and PIECE_CONTENT LIKE '%\"contentType\":\"file\"%' ";
        System.out.println("success");
    }
    @Test
    public void ContentTypeBySqlPlusTest(){
        /*BaseDaoImpl<BbsPiece, String> bbsPieceStringBaseDao = new BaseDaoImpl<BbsPiece, String>() {
            @Override
            public Map<String, String> getFilterField() {
                return null;
            }
        };*/
        //JdbcTemplate jdbcTemplate = bbsPieceDao.getJdbcTemplate();
        String sql = "SELECT * FROM `m_bbs_piece` where OPT_ID = :OPT_ID and OPT_TAG = :OPT_TAG and APPLICATION_ID = :APPLICATION_ID and PIECE_CONTENT LIKE '%\"contentType\":\"file\"%' "; //and PIECE_CONTENT LIKE '%"contentType":"file"%'
        HashMap<String, Object> map = new HashMap<>();//'%\"contentType\":\"file\"%'"
        map.put("OPT_ID",123);
        map.put("APPLICATION_ID",123);
        map.put("OPT_TAG",123);
        map.put("PIECE_CONTENT","%\"contentType\":\"file\"%");
        map.put("ps",0);
        map.put("pz",10);
        //bbsPieceDao.listObjectsBySql(sql,map);
        String countsql= "SELECT count(1) FROM `m_bbs_piece` where OPT_ID = :OPT_ID and OPT_TAG = :OPT_TAG and APPLICATION_ID = :APPLICATION_ID and PIECE_CONTENT LIKE '%\"contentType\":\"file\"%' ";
        System.out.println("success");
    }

    @Test
    public void DemoTest(){
        HashMap<String, Object> filterMap = new HashMap<>();
        filterMap.put("optId","123");
        filterMap.put("receive","u1234");
       // filterMap.put("order","desc");

        PageDesc pageDesc = new PageDesc(1, 10);
        String sql = " where OPT_ID = :optId and MSG_CODE in (SELECT Msg_Code FROM `f_inner_msg_recipient` WHERE Receive = :receive ";
        if (null != filterMap.get("msgState")){
            sql = sql + " and msg_State = :msgState ) ORDER BY SEND_DATE desc ";
        }else {
            sql = sql + " ) ORDER BY SEND_DATE desc ";
        }
        JSONArray objects = innerMsgDao.listObjectsByFilterAsJson(sql, filterMap, pageDesc);
        List<InnerMsg> innerMsgs = objects.toJavaList(InnerMsg.class);
       /* innerMsgs.sort(new Comparator<InnerMsg>() {
            @Override
            public int compare(InnerMsg o1, InnerMsg o2) {
                return o1.getSendDate().compareTo(o2.getSendDate());
            }
        });*/
        for (InnerMsg innerMsg : innerMsgs) {
            System.out.println(innerMsg);
        }

    }

    @Test
    public void  listBbsPieces() {
        HashMap<String, Object> filterMap = new HashMap<>();
        filterMap.put("optId","123");
        filterMap.put("applicationId","123");
        filterMap.put("optTag","123");
        filterMap.put("order_by","DELIVER_DATE");
        filterMap.put("sort","desc");
        PageDesc pageDesc = new PageDesc(1, 20);
        List<BbsPiece> bbsPiecesList = bbsPieceDao.listObjects(filterMap, pageDesc);
        for (int i = 0; i < bbsPiecesList.size(); i++) {
            System.out.println(bbsPiecesList.get(i));
        }
    }



    @Test
    public void  contentPieces() {
        HashMap<String, Object> filterMap = new HashMap<>();
        filterMap.put("optId","123");
        filterMap.put("applicationId","123");
        filterMap.put("optTag","123");
        filterMap.put("contentType","file");

        PageDesc pageDesc = new PageDesc(1, 20);
        String sql ="where OPT_ID = :optId and OPT_TAG = :optTag and APPLICATION_ID = :applicationId and PIECE_CONTENT LIKE :contentType ORDER BY DELIVER_DATE desc ";//'%"contentType":"file"%'
        String contentType = (String) filterMap.get("contentType");
        if (StringUtils.isNotBlank(contentType)){
            if (contentType.equals("file")){
                filterMap.put("contentType", "%\"contentType\":\"file\"%" );
            }
        }
        JSONArray objects = bbsPieceDao.listObjectsByFilterAsJson(sql, filterMap, pageDesc);
        List<BbsPiece> bbsPieces = objects.toJavaList(BbsPiece.class);
        for (BbsPiece bbsPiece : bbsPieces) {
            System.out.println(bbsPiece);
        }

    }


}
