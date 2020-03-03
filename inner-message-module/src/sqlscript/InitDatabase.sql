/*==============================================================*/
/* Table: M_InnerMsg                                            */
/*==============================================================*/
create table M_InnerMsg  (
   MsgCode              VARCHAR(16)                    not null,
   Sender               VARCHAR(128),
   SendDate             TIMESTAMP,
   MsgTitle             VARCHAR(128),
   MsgType              CHAR(1),
   MailType             CHAR(1),
   MailUnDelType        CHAR(1),
   ReceiveName          VARCHAR(2048),
   HoldUsers            decimal(8,0),
   msgState             CHAR(1),
   msgContent           BLOB,
   EmailId              VARCHAR(8),
   OptID                VARCHAR(64)                    not null,
   OPTMethod            VARCHAR(64),
   optTag               VARCHAR(200)
);



comment on column M_InnerMsg.MsgCode is
'消息主键自定义，通过S_M_INNERMSG序列生成';

comment on column M_InnerMsg.MsgType is
'P= 个人为消息  A= 机构为公告（通知）
M=邮件';

comment on column M_InnerMsg.MailType is
'I=收件箱
O=发件箱
D=草稿箱
T=废件箱


';

comment on column M_InnerMsg.ReceiveName is
'使用部门，个人中文名，中间使用英文分号分割';

comment on column M_InnerMsg.HoldUsers is
'总数为发送人和接收人数量相加，发送和接收人删除消息时-1，当数量为0时真正删除此条记录

消息类型为邮件时不需要设置';

comment on column M_InnerMsg.msgState is
'未读/已读/删除';

comment on column M_InnerMsg.EmailId is
'用户配置多邮箱时使用';

comment on column M_InnerMsg.OptID is
'模块，或者表';

comment on column M_InnerMsg.OPTMethod is
'方法，或者字段';

comment on column M_InnerMsg.optTag is
'一般用于关联到业务主体';

alter table M_InnerMsg
   add constraint PK_M_INNERMSG primary key (MsgCode);

/*==============================================================*/
/* Table: M_InnerMsg_Recipient                                  */
/*==============================================================*/
create table M_InnerMsg_Recipient  (
   MsgCode              VARCHAR(16)                    not null,
   Receive              VARCHAR(8)                     not null,
   ReplyMsgCode         INTEGER,
   ReceiveType          CHAR(1),
   MailType             CHAR(1),
   msgState             CHAR(1),
   ID                   VARCHAR(16)                    not null
);

comment on table M_InnerMsg_Recipient is
'内部消息（邮件）与公告收件人及消息信息';

comment on column M_InnerMsg_Recipient.ReceiveType is
'P=个人为消息
A=机构为公告
M=邮件';

comment on column M_InnerMsg_Recipient.MailType is
'T=收件人
C=抄送
B=密送';

comment on column M_InnerMsg_Recipient.msgState is
'未读/已读/删除，收件人在线时弹出提示

U=未读
R=已读
D=删除';

alter table M_InnerMsg_Recipient
   add constraint PK_M_INNERMSG_RECIPIENT primary key (ID);

/*==============================================================*/
/* Table: M_MsgAnnex                                            */
/*==============================================================*/
create table M_MsgAnnex  (
   MsgCode              VARCHAR(16)                    not null,
   InfoCode             VARCHAR(16)                    not null,
   MsgAnnexId           VARCHAR(16)                    not null
);

alter table M_MsgAnnex
   add constraint PK_M_MSGANNEX primary key (MsgAnnexId);
