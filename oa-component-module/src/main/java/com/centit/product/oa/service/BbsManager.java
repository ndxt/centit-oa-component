package com.centit.product.oa.service;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.oa.po.BbsPiece;
import com.centit.support.database.utils.PageDesc;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface BbsManager {


    /**
     * 写入bbsPiece信息
     * @param bbsPiece
     */
    void createBbsPiece(BbsPiece bbsPiece);
    //用来添加BbsPiece字段中replyId的值
    void updateBbsPiece(BbsPiece bbsPiece);

    /**
     * 查询根据条件分页查询信息,并根据时间进行排序(先发生的时间靠前);
     * @param filterMap 需要过滤的字段;以key-value的类型保存到map集合中
     * @param pageDesc 分页参数设置
     * @return 符合条件的BbsPiece的集合
     */
    List<BbsPiece> listBbsPieces(Map<String, Object> filterMap, PageDesc pageDesc);

    /**
     * 根据pieceId获取一条BbsPiece中的pieceContent记录
     * @param pieceId
     * @return
     */
    JSONObject getBbsPieces(String pieceId);

    /**
     * 通过id删除消息记录
     * @param pieceId
     */
    boolean deleteBbsPieceByID(String pieceId, HttpServletResponse httpResponse);

}
