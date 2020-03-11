/*==============================================================*/
/* Table: F_INNER_MSG   针对发件人                                      */
/*==============================================================*/
create table F_INNER_MSG  (
   Msg_Code              VARCHAR(32)                    not null,
   Reply_Msg_Code        VARCHAR(32),
   Sender                VARCHAR(32),
   Send_Date             TIMESTAMP,
   Msg_Title             VARCHAR(512),
   Msg_Type              CHAR(1),
   Mail_Type             CHAR(1),
   Receive_Name          VARCHAR(2048),
   msg_State             CHAR(1),
   msg_Content           CLOB,
   Opt_ID                VARCHAR(64)                    not null,
   OPT_Method            VARCHAR(64),
   opt_Tag               VARCHAR(200)
);



comment on column F_INNER_MSG.Msg_Code is
'消息主键自定义，通过S_M_INNERMSG序列生成';

comment on column F_INNER_MSG.Msg_Type is
'P= 个人为消息  A= 机构为公告（通知）
M=邮件';
-- 仅仅针对发件人
comment on column F_INNER_MSG.Mail_Type is
'I=收件箱
O=发件箱
D=草稿箱
T=废件箱
';

comment on column F_INNER_MSG.Receive_Name is
'使用部门，个人中文名，中间使用英文分号分割';


comment on column F_INNER_MSG.Opt_ID is
'模块，或者表';

comment on column F_INNER_MSG.OPT_Method is
'方法，或者字段';

comment on column F_INNER_MSG.opt_Tag is
'一般用于关联到业务主体';

alter table F_INNER_MSG
   add constraint PK_M_INNERMSG primary key (Msg_Code);

/*==============================================================*/
/* Table: F_INNER_MSG_RECIPIENT                                  */
/*==============================================================*/
create table F_INNER_MSG_RECIPIENT  (
   Receive              VARCHAR(32)                  not null,
   Msg_Code             VARCHAR(32)                  not null,
   RECEIVE_Date         TIMESTAMP,
   Reply_Msg_Code       VARCHAR(32),
   Mail_Type             CHAR(1),
   msg_State             CHAR(1),
);

alter table F_INNER_MSG_RECIPIENT
   add constraint PK_INNER_MSG_RECIPIENT primary key (Msg_Code, Receive);


comment on table F_INNER_MSG_RECIPIENT is
'内部消息（邮件）与公告收件人及消息信息';


comment on column F_INNER_MSG_RECIPIENT.Mail_Type is
'T=收件人
C=抄送
B=密送';

comment on column F_INNER_MSG_RECIPIENT.msg_State is
'未读/已读/删除，收件人在线时弹出提示
U=未读
R=已读
D=删除';

/*==============================================================*/
/* Table: M_MsgAnnex                                            */
/*==============================================================*/
create table F_INNER_MSG_ANNEX  (
   Annex_ID             VARCHAR(32)                    not null,
   Msg_Code             VARCHAR(32)                    not null,
   ANNEX_file_name            VARCHAR(256)                   not null,
   Annex_FILE_Id        VARCHAR(64)                    not null,
   upload_date          date
);

alter table F_INNER_MSG_ANNEX
   add constraint PK_MSG_ANNEX primary key (Annex_ID);


/*==============================================================*/
/* Table: F_BBS_SUBJECT  主题                                */
/*==============================================================*/
create table F_BBS_SUBJECT  (
   SUBJECT_ID            VARCHAR(32)                    not null,
   deliverer             VARCHAR(32),
   deliver_Date          TIMESTAMP,
   BBS_Title             VARCHAR(512),
   BBS_Content           CLOB,
   Opt_ID                VARCHAR(64)                    not null,
   OPT_Method            VARCHAR(64),
   opt_Tag               VARCHAR(200)
);

alter table F_BBS_SUBJECT
   add constraint PK_F_BBS_SUBJECT primary key (SUBJECT_ID);

/*==============================================================*/
/* Table: F_BBS_PIECE  讨论版回复信息                                  */
/*==============================================================*/
create table F_BBS_PIECE  (
   PIECE_ID            VARCHAR(32)                    not null,
   SUBJECT_ID            VARCHAR(32)                    not null,
   deliverer             VARCHAR(32),
   deliver_Date          TIMESTAMP,
   BBS_Content           CLOB
);

alter table F_BBS_PIECE
   add constraint PK_F_BBS_PIECE primary key (PIECE_ID);


/*==============================================================*/
/* Table: M_MsgAnnex                                            */
/*==============================================================*/
create table F_BBS_Annex (
   Annex_ID             VARCHAR(32)                    not null,
   SUBJECT_ID           VARCHAR(32),
   PIECE_ID             VARCHAR(32),
   ANNEX_file_name      VARCHAR(256)                   not null,
   Annex_FILE_Id        VARCHAR(64)                    not null,
   upload_date          date
);

alter table F_BBS_Annex
   add constraint PK_F_BBS_Annex primary key (Annex_ID);
