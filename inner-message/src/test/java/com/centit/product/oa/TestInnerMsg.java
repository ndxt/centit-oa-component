package com.centit.product.oa;

import com.alibaba.fastjson.JSONArray;
import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.product.oa.dao.InnerMsgDao;
import com.centit.product.oa.dao.InnerMsgRecipientDao;
import com.centit.product.oa.po.InnerMsg;
import com.centit.product.oa.po.InnerMsgAnnex;
import com.centit.product.oa.po.InnerMsgRecipient;
import com.centit.product.oa.service.impl.InnerMessageManagerImpl;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@PropertySource(value = "classpath:system.properties")
@ComponentScan(basePackages = {"com.centit"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import(value = {JdbcConfig.class})
@ContextConfiguration(classes = TestInnerMsg.class)
@RunWith(SpringRunner.class)
public class TestInnerMsg {

    @Autowired
    private InnerMsgDao innerMsgDao;
    @Autowired
    private InnerMsgRecipientDao innerMsgRecipientDao;
    @Autowired
    private InnerMessageManagerImpl innerMessageManager;


    @Test
    public void creatInnerMsgDaoTest(){
        InnerMsg innerMsg = new InnerMsg();
        innerMsg.setOptId("122");

        InnerMsgRecipient innerMsgRecipient = new InnerMsgRecipient();
        innerMsgRecipient.setReceive("u0000");
        ArrayList<InnerMsgRecipient> objects = new ArrayList<>();
        objects.add(innerMsgRecipient);
        innerMsg.setRecipients(objects);
        innerMsgDao.saveNewObject(innerMsg);
        innerMsgDao.saveObjectReferences(innerMsg);
        System.out.println(innerMsg.getMsgCode());
    }

    @Test
    public void creatInnerMsgRecipientDaoTest(){
        InnerMsgRecipient innerMsgRecipient = new InnerMsgRecipient();
        innerMsgRecipient.setMsgCode("1111");
        innerMsgRecipient.setReceive("45");
        innerMsgRecipientDao.saveNewObject(innerMsgRecipient);
        System.out.println("success");
    }

    @Test
    public void creatInnerMsgTest(){
        InnerMsg innerMsg = new InnerMsg();
        //innerMsg.setSender("12322");
        innerMsg.setMsgTitle("标题1");
        innerMsg.setMsgContent("内容1");
        innerMsg.setMailType("U");
        innerMsg.setMsgState("d");
        innerMsg.setReceiveName("张三,李四");
        innerMsg.setOptId("111");
        //ArrayList<InnerMsgRecipient>
        ArrayList<InnerMsgRecipient> innerMsgRecipients = new ArrayList<>();
        InnerMsgRecipient innerMsgRecipient = new InnerMsgRecipient();
        innerMsgRecipient.setReceive("张三");
        innerMsgRecipients.add(innerMsgRecipient);
        innerMsg.setRecipients(innerMsgRecipients);
        //ArrayList<InnerMsgAnnex>
        ArrayList<InnerMsgAnnex> innerMsgAnnexes = new ArrayList<>();
        InnerMsgAnnex innerMsgAnnex = new InnerMsgAnnex();
        innerMsgAnnex.setAnnexFileId("md511134");
        innerMsgAnnex.setAnnexFileName("测试文件1");
        innerMsgAnnexes.add(innerMsgAnnex);
        innerMsg.setInnerMsgAnnexs(innerMsgAnnexes);
       /* ArrayList<String> arrayList = new  ArrayList<String>();
        arrayList.add(innerMsg.getMsgTitle());
        arrayList.add(innerMsg.getMsgContent());
        arrayList.add(innerMsg.getMailType());
        arrayList.add(innerMsg.getMsgState());
        arrayList.add(innerMsg.getReceiveName());
        arrayList.add(innerMsg.getOptId());
        arrayList.add(innerMsg.getRecipients().get(0).getReceive());*/
        String sysUserCode = "1111111";
        innerMessageManager.sendInnerMsg(innerMsg,sysUserCode);
        System.out.println("success");
    }
    @Test
    public void InnerMsgGetReferencesTest(){
        InnerMsg inn = innerMsgDao.getObjectWithReferences("brG69TeSTPqmUFWqy0Tzgg");
        System.out.println(inn);
    }

    @Test
    public void orderTest(){
        InnerMsg inn = innerMsgDao.getObjectWithReferences("brG69TeSTPqmUFWqy0Tzgg");
        System.out.println(inn);
    }

    @Test
    public void randomTest(){
        Random random = new Random();
        String sysUserCode ="u666"+random.nextInt(3);
        System.out.println(sysUserCode);

    }
    //listMsgRecipientsCascade
    @Test
    public void listMsgRecipientsCascadeTest(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("optTag","123");
        hashMap.put("receive","u1234");
        hashMap.put("sort","msgCode");
        hashMap.put("order","desc");
        hashMap.put("ORDER_BY","qq");
        List<InnerMsg> innerMsgs = innerMsgDao.listObjects(hashMap);
        PageDesc pageDesc = new PageDesc(1, 10);
        System.out.println("success");

    }

    @Test
    public void deleteMsgTest(){
        //innerMsgDao.getObjectWithReferences("VWf54rCJQ9iK1YYxAsgMFQ");
        HashMap<String, Object> HashMap = new HashMap<>();
        HashMap.put("optTag","123");
        HashMap.put("receive","u1234");
        innerMsgDao.listObjects(HashMap);
        System.out.println("success");

    }

    @Test
    public void listMsgRecipientsCascadePlusTest(){
      /*  HashMap<String, Object> map = new HashMap<>();
        map.put("optTag","123");
        map.put("receive","u1234");
        map.put("msgState","U");
        PageDesc pageDesc = new PageDesc(1, 10);
        //innerMessageManager.listMsgRecipientsCascadePlus(map,pageDesc);
        System.out.println("success");

        */
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("punitCode", "null");
        paramsMap.put("createDate", "2010-12-01");
        paramsMap.put("mathName", "江苏 先腾");
        paramsMap.put("array", "1,2,3,4,5");
        paramsMap.put("sort", "uu.unitType asc");

        String queryStatement =
            "select uu.unitCode,uu.unitName,uu.unitType,uu.isValid,uu.unitTag"
                +"  from projectTable uu  "
                +" where 1=1 [:(SPLITFORIN,LONG,CREEPFORIN)array| and uu.unitType in (:array)]"
                + "[:(date)createTime | and uu.createTime >= :createTime ]"
                + "[:(like)mathName | and uu.unitName like :mathName ]"
                +"[:(inplace)sort | order by :sort  ]";
        String query = QueryUtils.translateQuery(
            queryStatement, null,
            paramsMap, true).getQuery();
        QueryAndNamedParams queryAndNamedParams = new QueryAndNamedParams(query,paramsMap);
        System.out.println(queryAndNamedParams);


    }

    @Test
    public void listInboxTest(){
        //innerMsgDao.getObjectWithReferences("VWf54rCJQ9iK1YYxAsgMFQ");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("optId","123");
        hashMap.put("receive","806");
        PageDesc pageDesc = new PageDesc(1, 10);
        innerMessageManager.listMsgRecipientsCascade(hashMap,pageDesc);
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
}
