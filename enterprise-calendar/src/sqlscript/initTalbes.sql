/* my sql */

drop table if exists F_WORK_CLASS;
drop table if exists F_WORK_DAY;
drop table if exists F_STAT_MONTH;

/*==============================================================*/
/* Table: F_WORK_CLASS                                          */
/*==============================================================*/
create table F_WORK_CLASS
(
   CLASS_ID             varchar(22) not null,
   CLASS_NAME           varchar(50) not null,
   SHORT_NAME           varchar(10) not null,
   begin_time           varchar(6) comment '9:00',
   end_time             varchar(6) comment '+4:00 ''+''表示第二天',
   has_break            char(1),
   break_begin_time     varchar(6) comment '9:00',
   break_end_time       varchar(6) comment '+4:00 ''+''表示第二天',
   class_desc           varchar(500),
   record_date          datetime,
   recorder             varchar(8)
);

alter table F_WORK_CLASS comment '工作日工作时间段';

alter table F_WORK_CLASS
   add primary key (CLASS_ID);

/*==============================================================*/
/* Table: F_WORK_DAY                                            */
/*==============================================================*/
create table F_WORK_DAY
(
   TOP_UNIT              varchar(32) not null comment '租户代码',
   Work_Day               varchar(8) not null comment 'YYYYMMDD',
   Day_Type              char(1) not null comment 'A:工作日放假，B:周末调休成工作时间 C 正常上班 D正常休假',
   Work_Time_Type         varchar(22), -- REF CLASS_ID
   Work_Day_Desc          varchar(255)
);

alter table F_WORK_DAY comment '非正常作业时间日
A:工作日放假 B:周末调休成工作时间  C: 正常上班  D:正常休假
';

alter table F_WORK_DAY
   add primary key (Work_Day, TOP_UNIT);


/*==============================================================*/
/* Table: F_STAT_MONTH                                          */
/*==============================================================*/
create table F_STAT_MONTH
(
   YEAR_MONTH            varchar(6) not null comment 'YYYYMM',
   Begin_Day             datetime not null,
   End_Day              datetime not null,
   End_Schedule          char(1) comment '这个字段忽略',
   Begin_Schedule        char(1) comment '这个字段忽略'
);

alter table F_STAT_MONTH comment 'OA业务统计月，可以自定义统计月的起止日期';

alter table F_STAT_MONTH
   add primary key (YEAR_MONTH);
