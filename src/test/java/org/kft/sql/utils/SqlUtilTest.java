package org.kft.sql.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * <description>
 *
 * @author author
 * @since 2024/3/25
 **/
public class SqlUtilTest {

    @Test
    public void removeWrap() {
        String input = "avc";
        String result = SqlUtil.removeWrap(input);
        assertEquals("avc", result);

        input = "`adc`";
        result = SqlUtil.removeWrap(input);
        assertEquals("adc", result);
    }

    @Test
    public void wrapWith() {
        String input = "avc";
        String result = SqlUtil.wrapWith(input, "`");
        assertEquals("`avc`", result);

        input = "`adc`";
        result = SqlUtil.wrapWith(input, "`");
        assertEquals("`adc`", result);
    }

    @Test
    public void contains() {
    }

    @Test
    public void nextValue() {
    }

    @Test
    public void removeOptions() {
        String input = "ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账户表'";
        List<String> options = Arrays.stream(input.split(" ")).collect(Collectors.toList());
        List<String> result = SqlUtil.removeOptions(options, "COLLATE");
        assertEquals(13, result.size());
        assertFalse(result.contains("COLLATE"));
        assertFalse(result.contains("utf8mb4_0900_ai_ci"));
    }

    @Test
    public void isDdlStatement() {
    }

    @Test
    public void removeComment() {
        String sql = "ALTER TABLE `apc_auth_user`    ADD UNIQUE index `uk_user_username` (`username`, `category`) USING BTREE COMMENT '登录名唯一'";
        String result = SqlUtil.removeComment(sql);
        assertEquals("ALTER TABLE `apc_auth_user`    ADD UNIQUE index `uk_user_username` (`username`, `category`) USING BTREE", result);

        sql = "ALTER TABLE `test_user` ADD COLUMN `digest` varchar(64) NOT NULL DEFAULT '' COMMENT 'sha256 摘要', ADD COLUMN `digest_length` varchar(64) NOT NULL DEFAULT '' COMMENT 'sha256 摘要length';";
        result = SqlUtil.removeComment(sql);
        assertEquals("ALTER TABLE `test_user` ADD COLUMN `digest` varchar(64) NOT NULL DEFAULT '', ADD COLUMN `digest_length` varchar(64) NOT NULL DEFAULT '';", result);
    }
}