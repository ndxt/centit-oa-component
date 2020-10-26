
CREATE TABLE f_inner_msg (
  msg_code varchar(32)  not null,
  reply_msg_code varchar(32) ,
  sender varchar(32) ,
  send_date timestamp(6),
  msg_title varchar(512) ,
  msg_type char(1) ,
  mail_type char(1) ,
  receive_name varchar(2048) ,
  carbon_copy_name varchar(2048) ,
  msg_state char(1) ,
  msg_content text ,
  opt_id varchar(64) ,
  opt_method varchar(64) ,
  opt_tag varchar(200) ,
  PRIMARY KEY (msg_code)
)
;

CREATE TABLE f_inner_msg_recipient (
  receive varchar(32)  not null,
  msg_code varchar(32)  not null,
  receive_date timestamp(6),
  reply_msg_code varchar(32) ,
  mail_type char(1) ,
  msg_state char(1) ,
  PRIMARY KEY (msg_code, receive)
)
;
CREATE TABLE m_bbs_piece (
  piece_id char(22)  not null,
  deliverer varchar(16) ,
  deliver_date timestamp(6),
  piece_content varchar(2000) ,
  reply_id char(22) ,
  application_id varchar(64) not null,
  opt_id varchar(64)  not null,
  opt_method varchar(64) ,
  opt_tag varchar(200) not null,
  reply_name varchar(16) ,
  PRIMARY KEY (piece_id)
)
;


CREATE TABLE f_inner_msg_annex (
  annex_id varchar(32)  not null,
  msg_code varchar(32)  not null,
  annex_file_name varchar(256)  not null,
  annex_file_id varchar(64)  not null,
  upload_date date,
  PRIMARY KEY (annex_id)
)
;

