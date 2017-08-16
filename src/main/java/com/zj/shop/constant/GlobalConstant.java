package com.zj.shop.constant;

import java.util.HashMap;
import java.util.Map;

public class GlobalConstant {

    public static final String SESSION_INFO = "sessionInfo";

    public static final String DEFAULT_PASSWORD = "888888";

    public static final String OTA_AGENTID_PREFIX = "ota_agentId_prefix";//ota用户前缀

    public static final String PSS_ORDER_CREATE_OK = "1001";

    public static final Integer ENABLE = 0; // 启用
    public static final Integer TOAUDIT = 1; // 待审核
    public static final Integer DISABLE = 2; // 审核不通过

    public static final Integer CHECKCONTRACTCOMPLETE = 2;//pss审核合同通过
    public static final Integer CHECKCONTRACTFAIL = 3;//pss审核合同不通过

    public static final String PREFIX_RECHARGE = "recharge_"; //用户充值前缀
    public static final String PREFIX_WITHDRAWALS = "withdrawals_"; //用户提现前缀
    public static final String PREFIX_CONTRACT_CHECK = "contractCheck_";//合同审核
    public static final String ORDER_SEATS_MODIFY_ORDER_NUMBERS = "order_seats_modify_order_numbers";

    public static final String ORDER_UPD_ORDER_NUMBERS = "order_upd_order_numbers";

    public static final String LOG_CONTRACT_CREATE_ORDER = "合同下单";
    public static final String LOG_CONTRACT_CHECK_OK = "合同审核通过";
    public static final String LOG_CONTRACT_CHECK_FAIL = "合同审核未通过";
    public static final String LOG_CONTRACT_PAY = "合同支付";
    public static final String LOG_CONTRACT_DESPOSIT = "合同押金";
    public static final String LOG_CONTRACT_CANCEL = "合同取消";
    public static final String LOG_CONTRACT_STOP = "合同中止";
    public static final String LOG_CONTRACT_COMPLETE = "合同完成";

    public static final Integer DEFAULT = 0; // 默认
    public static final Integer NOT_DEFAULT = 1; // 非默认

    public static final Map sexlist = new HashMap() {
        {
            put("0", "男");
            put("1", "女");
        }
    };
    public static final Map statelist = new HashMap() {
        {
            put("0", "启用");
            put("1", "停用");
        }
    };

    public static final String[] payType = {"", "包机款", "补款", "税金", "票面", "升舱", "婴儿", "联运", "改期", "航空服务费",
            "退票", "抵减押金", "抵减定金", "延误补偿", "燃油"};

    public static final Integer STATUS_FORBIDDEN = 403;//没有足够的权限
    public static final Integer STATUS_SUCCESS = 200;//请求成功
    public static final Integer STATUS_ERROR = 500;//内部异常
}
