/* my sql */
drop table if exists M_BBS_MODULE;
drop table if exists M_BBS_PIECE;
drop table if exists M_BBS_SUBJECT;
drop table if exists M_BBS_SCORE;

/*==============================================================*/
/* Table: M_BBS_MODULE                                          */
/*==============================================================*/
create table M_BBS_MODULE
(
   MODULE_ID            		varchar(22) not null comment '主键id',
   MODULE_NAME              varchar(128) comment '版块名称',
   MODULE_DESC              varchar(2000) comment '版块描述',
   USER_CODE                varchar(32) comment '创建人',
   CREATE_TIME         			timestamp(6) comment '创建时间',
	 DATA_VALID_FLAG					char(2) DEFAULT '1' comment '数据有效性 0无效、1有效',
   primary key (MODULE_ID)
) comment = '讨论版块信息表';

/*==============================================================*/
/* Table: M_BBS_PIECE                                           */
/*==============================================================*/
CREATE TABLE M_BBS_PIECE (
  PIECE_ID 								 varchar(22)  not null comment '主键id',
	SUBJECT_ID 							 varchar(22)  not null comment '话题对象id',
	USER_CODE                varchar(32) comment '用户编码',
	CREATE_TIME							 timestamp(6) NULL DEFAULT NULL comment '评论时间',
	PIECE_SATE							 char(2) comment '评论信息操作状态',
	LAST_UPDATE_TIME         timestamp(6) NULL DEFAULT NULL comment '更新时间',
	PIECE_CONTENT						 varchar(2000) comment '评论内容',
	REPLY_ID								 varchar(22) comment '回复（引用）的消息id',
	REPLY_NAME							 varchar(32) comment '回复（引用）的消息id对应的发送人',
	DATA_VALID_FLAG					char(2) DEFAULT '1' comment '数据有效性 0无效、1有效',
  PRIMARY KEY (PIECE_ID)
) comment = '评论信息表';

/*==============================================================*/
/* Table: M_BBS_SUBJECT                                           */
/*==============================================================*/
CREATE TABLE M_BBS_SUBJECT (
  SUBJECT_ID 								 varchar(22)  not null comment '主键id',
	MODULE_ID 							 	 varchar(22)  not null comment '版块id',
	USER_CODE                  varchar(32) comment '用户编码',
	CREATE_TIME							   timestamp(6) NULL DEFAULT NULL comment '创建时间',
	SUBJECT_TYPE							 varchar(32) comment '主题类别',
	SUBJECT_SATE          		 char(2) comment '操作状态',
	LAST_UPDATE_TIME					 timestamp(6) NULL DEFAULT NULL comment '更新时间',
	REPLY_TIMES								 NUMERIC(11) comment '回复次数',
	SCORE_TIMES							   NUMERIC(11) comment '评分次数',
	SCORE_SUM							 		 NUMERIC(11) comment '评分数',
	SUBJECT_CONTENT						 varchar(2000) comment '话题内容',
	DATA_VALID_FLAG						 char(2) DEFAULT '1' comment '数据有效性 0无效、1有效',
	APPLICATION_ID 						 varchar(64)  not null comment '项目ID 类似与 OSID',
  OPT_ID 									   varchar(64)  not null comment '功能模块',
  OPT_METHOD 								 varchar(64) comment '操作方法',
  OPT_TAG 									 varchar(200) not null comment '操作业务标记',
  PRIMARY KEY (SUBJECT_ID)
) comment = '讨论话题信息表';


/*==============================================================*/
/* Table: M_BBS_SCORE                                           */
/*==============================================================*/
CREATE TABLE M_BBS_SCORE (
  SCORE_ID 								 varchar(22)  not null comment '主键id',
	SUBJECT_ID 							 	 varchar(22)  not null comment '话题id',
	BBS_SCORE							 		 NUMERIC(11) comment '评分分数',
	USER_CODE                  varchar(32) comment '用户编码',
	CREATE_TIME							   timestamp(6) NULL DEFAULT NULL comment '评分时间',
  PRIMARY KEY (SCORE_ID)
) comment = '评分信息表';

--- 清库脚本
--- DELETE FROM M_BBS_MODULE
--- DELETE FROM M_BBS_PIECE
--- DELETE FROM M_BBS_SUBJECT
--- DELETE FROM M_BBS_SCORE




