package com.centit.product.oa.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="M_BBS_PIECE")
@NoArgsConstructor
public class BbsPiece implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="PIECE_ID")
    @ValueGenerator(strategy = GeneratorType.UUID22)
    private String pieceId;

    @Column(name="SUBJECT_ID")
    private String subjectId;

    @Column(name="DELIVERER")
    private String deliverer;

    @OrderBy("desc")
    @Column(name = "DELIVER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @ValueGenerator(strategy = GeneratorType.FUNCTION, value = "today()")
    private Date deliverDate;

    @Column(name="BBS_CONTENT")
    private String bbsContent;


}
