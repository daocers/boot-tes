CREATE database if NOT EXISTS ccb_tes;

USE ccb_tes;

DROP TABLE IF EXISTS tes_answer;
CREATE TABLE tes_answer (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  paper_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '试卷id',
  question_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '试题信息id',
  question_type_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '题型信息id',
  scene_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '场次id',
  answer varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '作答信息',
  time_used int(11) NOT NULL DEFAULT '-1' COMMENT '已用作答时间，单位：秒',
  time_left varchar(8) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '剩余时间，单位：秒',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  update_user_id bigint(21) NOT NULL DEFAULT '-1',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT ='作答信息表';

/*Table structure for table tes_branch */

DROP TABLE if EXISTS tes_branch;

CREATE TABLE tes_branch (
  id bigint(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  name varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '机构名称',
  code varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '机构编码',
  address varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '机构地址',
  level int(11) NOT NULL DEFAULT '-1' COMMENT '机构层级（分行级别）',
  superior_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '上级id',
  superior_code varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '上级机构编码',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '状态',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人id',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_branchCode (code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='机构信息';

/*Table structure for table tes_common_question */

DROP TABLE if EXISTS tes_common_question;
CREATE TABLE tes_common_question (
  id bigint(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  title varchar(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '题干',
  answer varchar(10) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '最佳答案',
  content varchar(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '试题选项信息（选择试题使用）',
  extra_info varchar(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '额外信息，比如交易操作类的交易码等',
  question_type_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '题型id',
  question_bank_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '题库id',
  property_item_info varchar(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '属性值的信息，属性值id，json串',
  owner_type int(11) NOT NULL DEFAULT '-1' COMMENT '题目归属类型，部门，机构，岗位等',
  owner_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '题目归属id',
  privary_type int(1) NOT NULL DEFAULT '-1' COMMENT '是否公用',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '1 可用',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建者id',
  create_time datetime NOT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='常规试题，单选多选判断';

/*Table structure for table tes_department */


DROP TABLE if EXISTS tes_department;

CREATE TABLE tes_department (
  id bigint(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  name varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  code varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门编号',
  superior_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '上级编号',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '状态',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门信息';

/*Table structure for table tes_paper */

DROP TABLE if EXISTS tes_paper;

CREATE TABLE tes_paper (
  id bigint(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  code varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '试卷编号',
  answer_flag int(11) NOT NULL DEFAULT '-1' COMMENT '作答信息，用于判断是否缺考',
  begin_time datetime DEFAULT NULL COMMENT '开始作答时间',
  end_time datetime DEFAULT NULL  COMMENT '交卷时间',
  original_score double(7,2) NOT NULL DEFAULT '0.00' COMMENT '原始得分，按照策略计算的',
  score double(7,2) NOT NULL DEFAULT '0.00' COMMENT '得分，根据是否百分之之后的分数',
  scene_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '场次id',
  user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '考生id',
  question_type_info varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '试题类型信息， json串，包含题型信息',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '试卷状态',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_time datetime NOT NULL DEFAULT  CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT  '更新人id',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷信息';

/*Table structure for table tes_paper_policy */

DROP TABLE if EXISTS tes_paper_policy;

CREATE TABLE tes_paper_policy (
  id bigint(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  code varchar(100) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '策略编号',
  name varchar(200) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '策略名称',
  question_type_info varchar(200) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '题型的集合，List<Integer>的json格式。',
  content varchar(255) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '试题策略和对应的分值，Map<Integer, Double>的json格式。',
  count int(11) NOT NULL DEFAULT '-1' COMMENT '题量',
  score double(6,2) NOT NULL DEFAULT '0.00' COMMENT '总分',
  percentable int(1) NOT NULL DEFAULT '-1' COMMENT '是否百分之，0 是， 1 否',
  owner_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '拥有者id',
  owner_type int(11) NOT NULL DEFAULT '-1' COMMENT '归属者类型',
  question_select_type int(1) NOT NULL DEFAULT '-1' COMMENT '选题方式， 0 普通， 1 策略选择',
  privary_type int(1) NOT NULL DEFAULT '-1' COMMENT '保密类型 0 公开， 1 保密',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '状态',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷策略';

/*Table structure for table tes_permission */

DROP TABLE if EXISTS tes_permission;

CREATE TABLE tes_permission (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  superior_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '上级权限id',
  code varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '权限编码',
  name varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '权限名称',
  url varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '权限对应的url',
  controller varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '对应的controller名称',
  action varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '对应的action名称',
  http_method varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '接受的http请求方式',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '状态',
  no int(11) NOT NULL DEFAULT '-1' COMMENT '用于排序',
  memo varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '描述',
  type int(11) NOT NULL DEFAULT '-1' COMMENT '权限类型， 菜单权限，按钮权限',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci  COMMENT='权限信息';

/*Table structure for table tes_profile */

DROP TABLE if EXISTS  tes_profile;

CREATE TABLE tes_profile (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '用户id',
  exam_flag int(11) NOT NULL DEFAULT '-1' COMMENT '考试状态',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建用户id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新时间id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户附加信息';

/*Table structure for table tes_property */

DROP TABLE if EXISTS tes_property;

CREATE TABLE tes_property (
  id bigint(21) NOT NULL AUTO_INCREMENT  COMMENT '主键',
  code varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '属性编码',
  memo varchar(200) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '描述',
  no int(11) NOT NULL DEFAULT '-1' COMMENT '排序索引' COMMENT '序号',
  name varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '属性名称',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '状态',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试题属性信息';


/*Table structure for table tes_property_item */

DROP TABLE if EXISTS tes_property_item;

CREATE TABLE tes_property_item (
  id bigint(21) NOT NULL AUTO_INCREMENT  COMMENT '属性条目id',
  property_id int(11) NOT NULL DEFAULT '-1' COMMENT '属性id',
  code varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '属性条目编码',
  name varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '属性条目名称',
  no int(11) NOT NULL DEFAULT '-1' COMMENT '排序索引',
  value varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '值',
  is_del int(1) NOT NULL DEFAULT '-1'  COMMENT '删除标志',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1'  COMMENT '创建人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  PRIMARY KEY (id),
  KEY FK_8qvnosuf8pth2xxwcc032bi2i (property_id)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='属性明细信息';

/*Table structure for table tes_question_bank */
DROP TABLE if EXISTS tes_question_bank;

CREATE TABLE tes_question_bank (
  id bigint(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  name varchar(100) COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '题库名称',
  memo varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '题库描述',
  branch_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '所属机构id',
  department_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '所属部门id',
  station_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '所属岗位id',
  status int(2) NOT NULL DEFAULT '1' COMMENT '状态',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  update_time datetime  DEFAULT CURRENT_TIMESTAMP  COMMENT '更新时间',
  update_user_id bigint(21)  NOT NULL DEFAULT '-1'  COMMENT '更新人id',
  create_user_id bigint(21) NOT NULL DEFAULT '-1'  COMMENT '创建人id',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题库信息';

/*Table structure for table tes_question_policy */

DROP TABLE if EXISTS tes_question_policy;

CREATE TABLE tes_question_policy (
  id bigint(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  code varchar(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '编码',
  name varchar(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '策略名称',
  content varchar(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '试题选择信息的内容，json格式',
  content_for_edit varchar(500) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '试题选择信息的内容，用于编辑时候展示',
  question_count int(11) NOT NULL DEFAULT '-1' COMMENT '试题总量',
  question_type_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '题型id',
  privary_type int(1) NOT NULL DEFAULT '-1' COMMENT '保密类型 0 公开， 1 保密',
  owner_type int(11) NOT NULL DEFAULT '-1' COMMENT '归属类型，分行，部门，岗位等信息',
  owner_id bigint(21) NOT NULL DEFAULT '-1'  COMMENT '拥有者id',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '0 禁用， 1 启用， -1 删除',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1'  COMMENT '更新人id',
  PRIMARY KEY (id),
  KEY FK_9a90gm75e65vii8j6f9q7gqsy (question_type_id)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT ='试题策略';

/*Table structure for table tes_question_property_item_x */

DROP TABLE if EXISTS tes_question_property_item_x;
CREATE TABLE tes_question_property_item_x (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  question_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '试题id',
  property_item_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '属性条目id',
  no int(11) NOT NULL DEFAULT '-1' COMMENT '序号',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY FK_rqhlieppe87biucg207ldgs8k (property_item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT ='试题和对应的属性信息关联表';

/*Table structure for table tes_question_type */

DROP TABLE if EXISTS tes_question_type;
CREATE TABLE tes_question_type (
  id bigint(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  code varchar(100) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '题型编码',
  name varchar(200) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '题型名称',
  memo varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '题型描述',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '状态',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题型信息';

/*Table structure for table tes_question_type_property_x */

DROP TABLE if EXISTS tes_question_type_property_x;
CREATE TABLE tes_question_type_property_x (
  id bigint(21) NOT NULL auto_increment  COMMENT '主键',
  question_type_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '题型id',
  property_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '属性id',
  no int(11) NOT NULL DEFAULT '-1' COMMENT '序号',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建用户id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新用户id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY FK_fc8wid3fo6utma4kxiok5kaei (property_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题型和属性关联表';

/*Table structure for table tes_role */

DROP TABLE if EXISTS tes_role;
CREATE TABLE tes_role (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  name varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  code varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '角色编码',
  memo varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备忘录',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '状态',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建人id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新用户id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_name (name),
  KEY idx_roleCode (code)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息';

/*Table structure for table tes_role_permission_x */

DROP TABLE if EXISTS tes_role_permission_x;
CREATE TABLE tes_role_permission_x (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  role_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '角色id',
  permission_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '权限id',
  no int(11) NOT NULL DEFAULT '-1' COMMENT '序号',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_user_id bigint(21) NOT NULL COMMENT '创建人id',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id bigint(21) NOT NULL COMMENT '更新人id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT ='角色权限关联表';

/*Table structure for table tes_scene */
DROP TABLE if EXISTS tes_scene;
CREATE TABLE tes_scene (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  code varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '编码',
  name varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场次名称',
  owner_type int(11) NOT NULL DEFAULT '-1' COMMENT '归属者类型',
  owner_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '归属者id',
  auth_code varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '授权码',
  change_paper int(1) NOT NULL DEFAULT '-1' COMMENT '是否可以更换试卷',
  delay_time int(11) NOT NULL DEFAULT '-1' COMMENT '考试顺延时间，单位：分钟',
  duration int(11) NOT NULL DEFAULT '-1' COMMENT '考试时间，单位：分钟',
  paper_policy_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '试卷策略id',
  cancel_reason varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '取消原因，取消场次时候验证',
  paper_generate_type int(11) NOT NULL DEFAULT '-1' COMMENT '试卷生成方式',
  remark varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '备注信息',
  question_bank_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '使用题库id',
  user_choice_type int(11) NOT NULL DEFAULT '-1' COMMENT '本场考试用户选择方式',
  total_score double(6,2) NOT NULL DEFAULT '0.00' COMMENT '本场考试满分',
  percentable int(1) NOT NULL DEFAULT '-1' COMMENT '是否百分制',
  meta_score_info varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '题型及其对应的分值，json格式的map数据',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '场次状态',
  open_time datetime  DEFAULT NULL  COMMENT '开场时间',
  close_time datetime DEFAULT NULL  COMMENT '封场时间',
  is_del int(1) NOT NULL COMMENT '删除标志',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT= '场次信息表';

DROP TABLE IF EXISTS tes_scene_user_x;
CREATE TABLE tes_scene_user_x(
  id BIGINT(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  scene_id BIGINT(21) NOT NULL DEFAULT -1 COMMENT '场次id',
  user_id BIGINT(21) NOT NULL DEFAULT -1 COMMENT '用户id',
  find_type INT(11) NOT NULL DEFAULT -1 COMMENT '查询类型，机构，部门，岗位等',
  find_id BIGINT(21) NOT NULL DEFAULT -1 COMMENT '查询条件',
  is_del INT(11) NOT NULL DEFAULT -1 COMMENT '删除标志',
  create_user_id BIGINT(21) NOT NULL DEFAULT -1 COMMENT '创建人id',
  update_user_id BIGINT(21) NOT NULL DEFAULT -1 COMMENT '更新人id',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INNODB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='试卷试题用户关系';


/*Table structure for table tes_station */

DROP TABLE if EXISTS tes_station;

CREATE TABLE tes_station (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  code varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '岗位编号',
  name varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '岗位名称',
  memo varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '岗位描述',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '' COMMENT '删除标志',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '' COMMENT '状态',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id),
  UNIQUE KEY uk_name (name)
) ENGINE=InnoDB  AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位信息';

/*Table structure for table tes_user */

DROP TABLE if EXISTS tes_user;
CREATE TABLE tes_user (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  username varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户名，员工号',
  password varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '密码',
  salt varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '密码的盐',
  id_no varchar(18) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '身份证号码',
  name varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '姓名',
  branch_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '所属机构id',
  department_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '所属部门id',
  station_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '所属岗位id',
  status int(2) NOT NULL DEFAULT '-1' COMMENT '用户状态',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username),
  UNIQUE KEY uk_idNo (id_no),
  KEY idx_branchId (branch_id,department_id),
  KEY idx_stationId (station_id),
  KEY idx_departmentId (department_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

/*Table structure for table tes_user_role_x */

DROP TABLE if EXISTS tes_user_role_x;
CREATE TABLE tes_user_role_x (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '用户id',
  role_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '角色id',
  no int(11) NOT NULL DEFAULT '-1' COMMENT '用于排序',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户和角色信息关联表';

DROP TABLE if EXISTS tes_scene_choice_x;
CREATE TABLE tes_scene_choice_x (
  id bigint(21) AUTO_INCREMENT NOT NULL COMMENT '主键',
  scene_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '场次id',
  data_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '用户所在的机构或者部门或者岗位id',
  type int(11) NOT NULL DEFAULT '-1' COMMENT '类型， 1，机构， 2 部门， 3 岗位， 4 用户',
  is_del int(1) NOT NULL DEFAULT '-1' COMMENT '删除标志',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '创建用户id',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  update_user_id bigint(21) NOT NULL DEFAULT '-1' COMMENT '更新用户id',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '场次和选择的用户分布信息关联表，即通过机构还是部门还是岗位选择的用户';

DROP TABLE IF EXISTS tes_paper_question_x;
CREATE TABLE tes_paper_question_x(
  id BIGINT(21) NOT NULL AUTO_INCREMENT COMMENT '主键',
  paper_id BIGINT(21) NOT NULL DEFAULT '-1' COMMENT '试卷id',
  question_id BIGINT(21) NOT NULL DEFAULT '-1' COMMENT '试题id',
  question_type_id bigint(21) not null DEFAULT  '-1' comment '题型id',
  scene_id BIGINT(21) NOT NULL DEFAULT  '-1' COMMENT '场次id',
  is_del INT(11) NOT NULL DEFAULT '-1' COMMENT '删除标识',
  create_user_id BIGINT(21) NOT NULL DEFAULT  '-1' COMMENT '创建人id',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_user_id BIGINT(21) NOT NULL DEFAULT '-1' COMMENT '更新人id',
  update_time DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=INNODB  AUTO_INCREMENT=1 CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷对应的试题';
