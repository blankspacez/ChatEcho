package com.seu.ai.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回结果
 * ok = 1 表示成功，0 表示失败
 */
@Data
@NoArgsConstructor
public class ResultVO {
    private Integer ok;
    private String msg;

    public ResultVO(Integer ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }

    public static ResultVO ok() {
        return new ResultVO(1, "ok");
    }

    public static ResultVO fail(String msg) {
        return new ResultVO(0, msg);
    }
}
