package com.github.caiyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.caiyun.pay.model.entity.RefundRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 退款记录Mapper接口
 */
@Mapper
public interface RefundRecordMapper extends BaseMapper<RefundRecordDO> {

    /**
     * 根据订单ID查询最新的退款记录
     *
     * @param orderId 订单ID
     * @return 退款记录
     */
    RefundRecordDO selectLatestByOrderId(@Param("orderId") String orderId);

    /**
     * 根据订单ID查询退款总金额
     *
     * @param orderId 订单ID
     * @return 退款总金额
     */
    Long selectTotalAmountByOrderId(@Param("orderId") String orderId);
}