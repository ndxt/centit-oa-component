package com.centit.framework.system.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="M_MSGANNEX")
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

}
