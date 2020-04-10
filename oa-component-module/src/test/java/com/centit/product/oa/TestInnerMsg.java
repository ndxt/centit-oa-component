package com.centit.product.oa;

import com.centit.framework.jdbc.config.JdbcConfig;
import com.centit.product.oa.dao.InnerMsgDao;
import com.centit.product.oa.dao.InnerMsgRecipientDao;
import com.centit.product.oa.po.InnerMsg;
import com.centit.product.oa.po.InnerMsgAnnex;
import com.centit.product.oa.po.InnerMsgRecipient;
import com.centit.product.oa.service.impl.BbsManagerImpl;
import com.centit.product.oa.service.impl.InnerMessageManagerImpl;
import com.centit.support.database.utils.PageDesc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@PropertySource(value = "classpath:system.properties")
@ComponentScan(basePackages = {"com.centit"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@Import(value = {JdbcConfig.class})
@ContextConfiguration(classes = TestPiece.class)
@RunWith(SpringRunner.class)
public class TestInnerMsg {

    @Autowired
    private InnerMsgDao innerMsgDao;
    @Autowired
    private InnerMsgRecipientDao innerMsgRecipientDao;
    @Autowired
    InnerMessageManagerImpl innerMessageManager;


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
    public void randomTest(){
        Random random = new Random();
        String sysUserCode ="u666"+random.nextInt(3);
        System.out.println(sysUserCode);

    }
    //listMsgRecipientsCascade
    @Test
    public void listMsgRecipientsCascadeTest(){
        HashMap<String, Object> HashMap = new HashMap<String,Object>();
        HashMap.put("optTag","123");
        HashMap.put("receive","u1234");
        PageDesc pageDesc = new PageDesc(1, 10);
        //List<java.util.HashMap<String, Object>> hashMaps = innerMessageManager.listMsgRecipientsCascade(HashMap, pageDesc);
        System.out.println("success");

    }

    @Test
    public void deleteMsgTest(){
        innerMsgDao.getObjectWithReferences("VWf54rCJQ9iK1YYxAsgMFQ");
        System.out.println("success");

    }

    @Test
    public void listMsgRecipientsCascadePlusTest(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("optTag","123");
        map.put("receive","u1234");
        map.put("msgState","U");
        PageDesc pageDesc = new PageDesc(1, 10);
        //innerMessageManager.listMsgRecipientsCascadePlus(map,pageDesc);
        System.out.println("success");

    }

    @Test
    public void updateRecipientTest(){
        InnerMsgRecipient recipient = new InnerMsgRecipient();
        recipient.setMsgState("R");
        recipient.setMsgCode("0nTDk1y_TaCOv1WSvbr1QA");
        recipient.setReceive("u12345");
        innerMessageManager.updateRecipient(recipient);

        System.out.println("success");

    }

}
