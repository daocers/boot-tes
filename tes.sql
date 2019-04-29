CREATE DATABASE IF noT EXISTS hn_tes;
USE hn_tes;


DROP TABLE IF EXISTS tes_user;
CREATE TABLE tes_user (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  username VARCHAR(50) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '用户名，员工号',
  password VARCHAR(50) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '密码',
  salt VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '密码的盐',
  id_no VARCHAR(18) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '身份证号码',
  name VARCHAR(30) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '姓名',
  branch_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '所属机构id',
  department_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '所属部门id',
  station_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '所属岗位id',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '用户状态',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';



DROP TABLE IF EXISTS tes_role;
CREATE TABLE tes_role (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '角色名称',
  code VARCHAR(50) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '角色编码',
  memo VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '备忘录',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '状态',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建人id',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新用户id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息';

/*Table structure for table tes_role_permission_x */

DROP TABLE IF EXISTS tes_role_permission_x;
CREATE TABLE tes_role_permission_x (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  role_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '角色id',
  permission_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '权限id',
  no INT(11) noT NULL DEFAULT '-1' COMMENT '序号',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL COMMENT '创建人id',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL COMMENT '更新人id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT ='角色权限关联表';


DROP TABLE IF EXISTS tes_permission;

CREATE TABLE tes_permission (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  superior_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '上级权限id',
  code VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '权限编码',
  name VARCHAR(200) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '权限名称',
  url VARCHAR(200) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '权限对应的url',
  controller VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '对应的controller名称',
  action VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '对应的action名称',
  http_method VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '接受的http请求方式',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '状态',
  no INT(11) noT NULL DEFAULT '-1' COMMENT '用于排序',
  memo VARCHAR(200) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '描述',
  type INT(11) noT NULL DEFAULT '-1' COMMENT '权限类型， 菜单权限，按钮权限',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建人id',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='权限信息';


DROP TABLE IF EXISTS tes_user_role_x;
CREATE TABLE tes_user_role_x (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '用户id',
  role_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '角色id',
  no INT(11) noT NULL DEFAULT '-1' COMMENT '用于排序',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户和角色信息关联表';


/*Table structure for table tes_branch */

DROP TABLE IF EXISTS tes_branch;

CREATE TABLE tes_branch (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '机构名称',
  code VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '机构编码',
  address VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '机构地址',
  level INT(11) noT NULL DEFAULT '-1' COMMENT '机构层级（分行级别）',
  superior_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '上级id',
  superior_code VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '上级机构编码',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '状态',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建人id',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新人id',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_branchcode (code)
) ENGINE=INnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='机构信息';


DROP TABLE IF EXISTS tes_department;

CREATE TABLE tes_department (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '部门名称',
  code VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '部门编号',
  superior_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '上级编号',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '状态',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门信息';



DROP TABLE IF EXISTS tes_station;

CREATE TABLE tes_station (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  code VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '岗位编号',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '岗位名称',
  memo VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '岗位描述',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '' COMMENT '删除标志',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '' COMMENT '状态',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id),
  UNIQUE KEY uk_name (name)
) ENGINE=INnoDB  AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位信息';


DROP TABLE IF EXISTS tes_single;
CREATE TABLE tes_single(
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  title VARCHAR(255) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '题干',
  answer VARCHAR(10) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '最佳答案',
  content VARCHAR(255) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '试题选项信息（选择试题使用）',
  extra_info VARCHAR(255) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '额外信息，比如交易操作类的交易码等',
  bank_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '题库id',

  branch_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '机构id',
  department_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '部门id',
  station_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '岗位id',
  owner_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '题目归属id',
  public_flag INT(1) noT NULL DEFAULT '-1' COMMENT '是否公用',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '1 可用',
  attr_1 INT(11) noT NULL DEFAULT -1 COMMENT '属性1 的值',
  attr_2 INT(11) noT NULL DEFAULT -1 COMMENT '属性2 的值',
  attr_3 INT(11) noT NULL DEFAULT -1 COMMENT '属性3 的值',
  attr_4 INT(11) noT NULL DEFAULT -1 COMMENT '属性4 的值',
  attr_5 INT(11) noT NULL DEFAULT -1 COMMENT '属性5 的值',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建者id',
  create_time DATETIME noT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单选题';

DROP TABLE IF EXISTS tes_multi;
CREATE TABLE tes_multi(
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  title VARCHAR(255) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '题干',
  answer VARCHAR(10) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '最佳答案',
  content VARCHAR(1000) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '试题选项信息（选择试题使用）',
  extra_info VARCHAR(255) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '额外信息，比如交易操作类的交易码等',
  bank_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '题库id',

  branch_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '机构id',
  department_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '部门id',
  station_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '岗位id',
  owner_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '题目归属id',
  public_flag INT(1) noT NULL DEFAULT '-1' COMMENT '是否公用',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '1 可用',

  attr_1 INT(11) noT NULL DEFAULT -1 COMMENT '属性1 的值',
  attr_2 INT(11) noT NULL DEFAULT -1 COMMENT '属性2 的值',
  attr_3 INT(11) noT NULL DEFAULT -1 COMMENT '属性3 的值',
  attr_4 INT(11) noT NULL DEFAULT -1 COMMENT '属性4 的值',
  attr_5 INT(11) noT NULL DEFAULT -1 COMMENT '属性5 的值',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建者id',
  create_time DATETIME noT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单选题';

DROP TABLE IF EXISTS tes_judge;
CREATE TABLE tes_judge(
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  title VARCHAR(255) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '题干',
  answer VARCHAR(10) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '最佳答案',
  content VARCHAR(1000) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '试题选项信息（选择试题使用）',
  extra_info VARCHAR(255) CHARACTER SET utf8mb4 noT NULL DEFAULT '' COMMENT '额外信息，比如交易操作类的交易码等',
  bank_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '题库id',

  branch_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '机构id',
  department_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '部门id',
  station_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '岗位id',
  owner_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '题目归属id',
  public_flag INT(1) noT NULL DEFAULT '-1' COMMENT '是否公用',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '1 可用',

  attr_1 INT(11) noT NULL DEFAULT -1 COMMENT '属性1 的值',
  attr_2 INT(11) noT NULL DEFAULT -1 COMMENT '属性2 的值',
  attr_3 INT(11) noT NULL DEFAULT -1 COMMENT '属性3 的值',
  attr_4 INT(11) noT NULL DEFAULT -1 COMMENT '属性4 的值',
  attr_5 INT(11) noT NULL DEFAULT -1 COMMENT '属性5 的值',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建者id',
  create_time DATETIME noT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单选题';



DROP TABLE IF EXISTS tes_paper;

CREATE TABLE tes_paper (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  code VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '试卷编号',
  answer_flag INT(11) noT NULL DEFAULT '-1' COMMENT '作答信息，用于判断是否缺考',
  begin_time DATETIME DEFAULT NULL COMMENT '开始作答时间',
  end_time DATETIME DEFAULT NULL  COMMENT '交卷时间',
  original_score DOUBLE(7,2) noT NULL DEFAULT '0.00' COMMENT '原始得分，按照策略计算的',
  score DOUBLE(7,2) noT NULL DEFAULT '0.00' COMMENT '得分，根据是否百分之之后的分数',
  scene_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '场次id',
  user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '考生id',

  status INT(2) noT NULL DEFAULT '-1' COMMENT '试卷状态',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_time DATETIME noT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建人id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT  '更新人id',
  PRIMARY KEY (id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷信息';


DROP TABLE IF EXISTS tes_answer;
CREATE TABLE tes_answer (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  paper_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '试卷id',
  question_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '试题信息id',
  question_type INT(11) noT NULL DEFAULT '-1' COMMENT '题型， 1 单选， 2 多选， 3 判断',
  scene_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '场次id',
  right_answer VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '最佳答案',
  answer VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '作答信息',
  time_used INT(11) noT NULL DEFAULT '-1' COMMENT '已用作答时间，单位：秒',
  time_left VARCHAR(8) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '剩余时间，单位：秒',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建人',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP ,
  update_user_id BIGINT(21) noT NULL DEFAULT '-1',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT ='作答信息表';


DROP TABLE IF EXISTS tes_question_bank;
CREATE TABLE tes_question_bank (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci  noT NULL COMMENT '题库名称',
  memo VARCHAR(200) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '题库描述',
  branch_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '所属机构id',
  department_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '所属部门id',
  station_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '所属岗位id',
  status INT(2) noT NULL DEFAULT '1' COMMENT '状态',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  update_time DATETIME  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
  update_user_id BIGINT(21)  noT NULL DEFAULT '-1'  COMMENT '更新人id',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1'  COMMENT '创建人id',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题库信息';

DROP TABLE IF EXISTS tes_scene;
CREATE TABLE tes_scene (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  code VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '编码',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '场次名称',
  branch_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '归属者id',
  department_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '归属者id',
  station_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '归属者id',
  auth_code VARCHAR(10) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '授权码',
  change_paper INT(1) noT NULL DEFAULT '-1' COMMENT '是否可以更换试卷',
  delay_minute INT(11) noT NULL DEFAULT '-1' COMMENT '考试顺延时间，单位：分钟',
  duration INT(11) noT NULL DEFAULT '-1' COMMENT '考试时间，单位：分钟',
  cancel_reason VARCHAR(300) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '取消原因，取消场次时候验证',
  single_count INT(11) noT NULL DEFAULT -1 COMMENT '单选题数量',
  single_score DOUBLE(3,1) noT NULL DEFAULT 0.00 COMMENT '单选题分值',
  multi_count INT(11) noT NULL DEFAULT -1 COMMENT '多选题数量',
  multi_score DOUBLE(3,1) noT NULL DEFAULT 0.00 COMMENT '多选题分值',
  judge_count INT(11) noT NULL DEFAULT -1 COMMENT '判断题数量',
  judge_score DOUBLE(3,1) noT NULL DEFAULT 0.00 COMMENT '判断题分值',

  receipt_count int(11) not null default 0 comment '凭条数量（页数）',
  number_length int(2) not null default 0 comment '数字的长度，用于控制难度',
  decimal_length int(1) not null default 0 comment '小数位，用于展示几位小数，库里面存储的都是整数',

  paper_policy_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '试卷策略id',
  paper_generate_type INT(11) noT NULL DEFAULT '-1' COMMENT '试卷生成方式',
  remark VARCHAR(200) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '备注信息',
  question_bank_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '使用题库id',
  user_choice_type INT(11) noT NULL DEFAULT '-1' COMMENT '本场考试用户选择方式',
  total_score DOUBLE(6,2) noT NULL DEFAULT '0.00' COMMENT '本场考试满分',
  percentable INT(1) noT NULL DEFAULT '-1' COMMENT '是否百分制',
  meta_score_info VARCHAR(200) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '题型及其对应的分值，json格式的map数据',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '场次状态',
  open_time DATETIME  DEFAULT NULL  COMMENT '开场时间',
  close_time DATETIME DEFAULT NULL  COMMENT '封场时间',
  is_del INT(1) noT NULL COMMENT '删除标志',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT= '场次信息表';
/*Table structure for table tes_paper_policy */


DROP TABLE IF EXISTS tes_property;
CREATE TABLE tes_property (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci  noT NULL COMMENT '题库名称',
  memo VARCHAR(200) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '题库描述',
  no INT(11) noT NULL DEFAULT -1 COMMENT '序号',
  question_type INT(11) noT NULL DEFAULT -1 COMMENT '题型',
  required INT(11) noT NULL DEFAULT -1  COMMENT '该属性是否必要',
  status INT(2) noT NULL DEFAULT '1' COMMENT '状态',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  update_time DATETIME  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
  update_user_id BIGINT(21)  noT NULL DEFAULT '-1'  COMMENT '更新人id',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1'  COMMENT '创建人id',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试题属性';


DROP TABLE IF EXISTS tes_property_item;
CREATE TABLE tes_property_item (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci  noT NULL COMMENT '题库名称',
  memo VARCHAR(200) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '题库描述',
  no INT(11) noT NULL DEFAULT -1 COMMENT '序号',
  property_id BIGINT(21) noT NULL DEFAULT -1 COMMENT '属性id',
  status INT(2) noT NULL DEFAULT '1' COMMENT '状态',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  update_time DATETIME  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
  update_user_id BIGINT(21)  noT NULL DEFAULT '-1'  COMMENT '更新人id',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1'  COMMENT '创建人id',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试题属性值';

drop table if exists tes_login_log;
create table tes_login_log(
  id bigint(21) not null auto_increment comment '',
  user_id bigint(21) not null default -1 comment '',
  ip varchar(15) not null default '' comment '登录ip',
  content varchar(100) not null default '' comment '其他登录信息',
  update_time DATETIME  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
  update_user_id BIGINT(21)  noT NULL DEFAULT '-1'  COMMENT '更新人id',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1'  COMMENT '创建人id',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志信息';


drop table if exists tes_manager;
CREATE TABLE tes_manager (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  user_id bigint(21) noT NULL COMMENT '管理员的用户id',
  target_id bigint(21) not null comment '被管理机构的id',
  type INT(11) noT NULL DEFAULT -1 COMMENT '管理员类型， 1 部门， 2 机构， 3 岗位',
  status INT(2) noT NULL DEFAULT '1' COMMENT '状态',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  update_time DATETIME  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
  update_user_id BIGINT(21)  noT NULL DEFAULT '-1'  COMMENT '更新人id',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1'  COMMENT '创建人id',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员信息';




drop table if exists tes_join_info;
CREATE TABLE tes_join_info (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  scene_id bigint(21) not null comment '场次id',
  target_id bigint(21) not null comment '管理id',
  target_code VARCHAR(100) COLLATE utf8mb4_unicode_ci  noT NULL COMMENT '编号',
  target_name VARCHAR(100) COLLATE utf8mb4_unicode_ci  noT NULL COMMENT '编号',
  type INT(11) noT NULL DEFAULT -1 COMMENT '管理员类型， 1 部门， 2 机构， 3 岗位',
    open_time DATETIME  DEFAULT NULL  COMMENT '开场时间',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  update_time DATETIME  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
  update_user_id BIGINT(21)  noT NULL DEFAULT '-1'  COMMENT '更新人id',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1'  COMMENT '创建人id',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参考人员';



-- -- 翻打凭条
-- drop table if exists tes_receipt;
-- CREATE TABLE tes_receipt (
--   id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
--   scene_id bigint(21) not null comment '场次id',
--   content varchar(11)  COLLATE utf8mb4_unicode_ci  noT NULL COMMENT '内容信息，一个随机数字',
--   type INT(11) noT NULL DEFAULT -1 COMMENT '管理员类型， 1 部门， 2 机构， 3 岗位',
--   is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
--   update_time DATETIME  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
--   update_user_id BIGINT(21)  noT NULL DEFAULT '-1'  COMMENT '更新人id',
--   create_user_id BIGINT(21) noT NULL DEFAULT '-1'  COMMENT '创建人id',
--   create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--   PRIMARY KEY(id)
-- ) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参考人员';



-- 凭条信息
DROP TABLE IF EXISTS tes_receipt;
CREATE TABLE tes_receipt(
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  no int(11) not null default -1 comment '序号',
  number int(11) not null default 0 comment '数字',
  scene_id bigint(21) not null default -1 comment '场次id',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '1 可用',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建者id',
  create_time DATETIME noT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='凭条信息';


-- 翻打凭条的答题记录,每个考生每张拼条一条记录
drop  table if exists tes_receipt_answer;
create table tes_receipt_answer(
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  receipt_id bigint(21) not null default -1 comment '凭条页的id',
  no int(11) not null default -1 comment '序号',
  number int(11) not null default 0 comment '数字',
  answer int(11) not null default -1 comment '答案',
  scene_id bigint(21) not null default -1 comment '场次id',
  user_id bigint(21) not null default -1 comment '答题人id',
  status INT(2) noT NULL DEFAULT '-1' COMMENT '1 可用',

  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建者id',
  create_time DATETIME noT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='凭条信息';



-- 翻打凭条 测试结果
drop table if exists tes_receipt_record;
CREATE TABLE tes_receipt_record (
  id BIGINT(21) noT NULL AUTO_INCREMENT COMMENT '主键',
  scene_id bigint(21) not null default -1 comment '场次id',
  user_id bigint(21) not null default -1 comment '考生id',
  count int(11) not null default -1 comment '测试的张数',
  seconds int(11) not null default -1 comment '使用的时间，单位：秒',
  right_count int(11) not null default -1 comment '正确数量',
  false_count int(11) not null default -1 comment '错误数量',
  rate double(5, 2) not null default 0.00 comment '正确率',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  update_time DATETIME  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
  update_user_id BIGINT(21)  noT NULL DEFAULT '-1'  COMMENT '更新人id',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1'  COMMENT '创建人id',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(id)
) ENGINE=INnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='翻打凭条记录';


/* 试卷策略 */
DROP TABLE IF EXISTS tes_paper_policy;
CREATE TABLE tes_paper_policy (
  id BIGINT(21) AUTO_INCREMENT noT NULL COMMENT '主键',
  name VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '角色名称',
  code VARCHAR(50) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '角色编码',
  memo VARCHAR(100) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '备忘录',
  single_info VARCHAR(1000) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '单选信息',
  multi_info VARCHAR(1000) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '多选信息',
  judge_info VARCHAR(1000) COLLATE utf8mb4_unicode_ci noT NULL DEFAULT '' COMMENT '判断题信息',
  single_count int(11) not null default 0 comment '单选题数量',
  multi_count int(11) not null default 0 comment '多选题数量',
  judge_count int(11) not null default 0 comment '判断题数量',
    single_score double(5, 1)  not null default 0 comment '单选题分值',
    multi_score double(5, 1)  not null default 0 comment '多选题分值',
    judge_score double(5, 1)  not null default 0 comment '判断题分值',
  receipt_count INT(3) noT NULL DEFAULT '-1' COMMENT '凭条张数',
  number_length INT(2) noT NULL DEFAULT '-1' COMMENT '凭条数字长度',
  branch_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '机构id',
  station_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '岗位id',
  department_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '部门id',


  status INT(2) noT NULL DEFAULT '-1' COMMENT '状态',
  is_del INT(1) noT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '创建人id',
  create_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) noT NULL DEFAULT '-1' COMMENT '更新用户id',
  update_time DATETIME noT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息';
