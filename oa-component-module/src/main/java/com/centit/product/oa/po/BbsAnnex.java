package com.centit.product.oa.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用来描述上传的文件信息；
 */
@Data
@Entity
@Table(name="F_BBS_ANNEX")
@NoArgsConstructor
public class BbsAnnex implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ANNEX_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String annexId;//附件主键

    @Column(name = "PIECE_ID")
    private String pieceId;//评论消息id

    @Column(name = "ANNEX_FILE_NAME")
    private String annexFileName;//附件文件名称


    @Column(name = "ANNEX_FILE_ID")
    private String annexFileId;//附件文件id,这个值代表文件md5值；可以通过这个字段在文件存储位置中找到对应的文件；

    @OrderBy("desc")
    @Column(name = "UPLOAD_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date uploadDate;//附件上传时间

}
