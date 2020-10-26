package com.centit.product.oa.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="F_INNER_MSG_ANNEX")
public class InnerMsgAnnex implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ANNEX_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String annexId;//附件主键

    @Column(name = "MSG_CODE")
    private String msgCode;//消息代码

    @Column(name = "ANNEX_FILE_NAME")
    private String annexFileName;

    @Column(name = "ANNEX_FILE_ID")
    private String annexFileId;

    @OrderBy("desc")
    @Column(name = "UPLOAD_DATE")
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date uploadDate;
}
