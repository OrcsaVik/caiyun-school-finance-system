package com.github.caiyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.caiyun.pay.model.entity.PaymentOrderDO;
import com.github.caiyun.pay.model.vo.PaymentHistoryQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 支付订单Mapper接口
 */
@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrderDO> {

    /**
     * 分页查询支付历史记录
     * TODO 幻觉输出
     * @param page 分页参数
     * @param userId 用户ID

     * @return 支付历史分页数据
     */
    IPage<PaymentOrderDO> selectHistoryPage(
            Page<PaymentOrderDO> page,
            @Param("userId") String userId,
            @Param("query") PaymentHistoryQueryVO query
    );

    /**
     * 统计用户待缴费项目数量和金额
     *
     * @param userId 用户ID
     * @return [待缴费项目数量, 待缴费总金额]
     */
    Map<String, Object> selectPendingStatistics(@Param("userId") String userId);

    /**
     * 统计用户已缴费项目数量和金额
     *
     * @param userId 用户ID
     * @return {"count": 已缴费项目数量, "amount": 已缴费总金额}
     */
    Map<String, Object> selectPaidStatistics(@Param("userId") String userId);

    /**
     * 统计用户退款项目数量和金额
     *
     * @param userId 用户ID
     * @return {"count": 退款项目数量, "amount": 退款总金额}
     */
    Map<String, Object> selectRefundStatistics(@Param("userId") String userId);

    /**
     * 统计各缴费项目类别的数据
     *
     * @param userId 用户ID
     * @return [{"category": 项目类别, "count": 项目数量, "amount": 缴费总金额, "userCount": 已缴费人数}]
     */
    List<Map<String, Object>> selectCategoryStatistics(@Param("userId") String userId);

    /**
     * 统计近6个月的缴费趋势
     *
     * @param userId 用户ID
     * @return [{"month": 月份, "count": 缴费项目数量, "amount": 缴费总金额, "userCount": 缴费人数}]
     */
    List<Map<String, Object>> selectMonthlyTrend(@Param("userId") String userId);
}