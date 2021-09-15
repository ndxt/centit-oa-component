package com.centit.product.dao;

import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.po.Question;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao extends BaseDaoImpl<Question,String> {
}
