package org.kft.sql.converter;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.kft.sql.utils.SqlUtil;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * <description>
 *
 * @author author
 * @since 2024/1/25
 **/
public class OpenGaussConverterTest {

    @Test
    public void convert() {
        String sql = "CREATE TABLE IF NOT EXISTS `engine_job` (\n" +
                "  `id` bigint(13) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',\n" +
                "  `job_id` varchar(64) NOT NULL COMMENT '作业ID',\n" +
                "  `dag` varchar(4096) NOT NULL DEFAULT '' COMMENT 'DAG',\n" +
                "  `config` varchar(4096) NOT NULL DEFAULT '' COMMENT 'CONFIG',\n" +
                "  `node_id` varchar(64) NOT NULL DEFAULT '' COMMENT '当前节点ID',\n" +
                "  `initiator` varchar(64) NOT NULL DEFAULT '' COMMENT '发起方节点ID',\n" +
                "  `parties` varchar(256) NOT NULL DEFAULT '' COMMENT '参与方ID列表',\n" +
                "  `storage_info` varchar(512) NOT NULL DEFAULT '' COMMENT '存储信息',\n" +
                "  `address` varchar(64) NOT NULL DEFAULT '' COMMENT 'SecretFlow通信地址',\n" +
                "  `start_time` datetime(3) COMMENT '开始时间',\n" +
                "  `finish_time` datetime(3) COMMENT '结束时间',\n" +
                "  `status` varchar(8) NOT NULL DEFAULT 'INIT' COMMENT '状态[INIT、RUNNING、SUCCESS、FAILED、STOPPED]',\n" +
                "  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',\n" +
                "  `modified_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  UNIQUE KEY `unq_job_id` (`job_id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='作业表';";
        SqlConverter converter = new OpenGaussConverter();
        String newSql = converter.convert(sql);
        System.out.println(newSql);
        assertEquals(newSql, "");
    }

    @Test
    public void sssss() {
        ArrayList<String> strings = Lists.newArrayList("not", "a", "null", "default");
        System.out.println(SqlUtil.contains(strings, "not", "a"));
    }

    @Test
    public void testConvert() {
        String sql = "CREATE TABLE `apc_component_invisible` (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `component_code` varchar(50) DEFAULT '' COMMENT '组件code',\n" +
                "  `component_version` varchar(30) DEFAULT '' COMMENT '组件version',\n" +
                "  `project_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '项目类型',\n" +
                "  `engine_code` varchar(64) DEFAULT '' COMMENT '引擎code',\n" +
                "  `engine_version` varchar(64) DEFAULT '' COMMENT '引擎version',\n" +
                "  `create_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',\n" +
                "  `modified_time` datetime(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  UNIQUE KEY `idx_code_version` (`component_code`,`component_version`) USING BTREE,\n" +
                "  KEY `idx_engine` (`engine_code`,`engine_version`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPRESSED COMMENT='组件'";
        SqlConverter converter = new OpenGaussConverter();
        String newSql = converter.convert(sql);
        System.out.println(newSql);
    }
}