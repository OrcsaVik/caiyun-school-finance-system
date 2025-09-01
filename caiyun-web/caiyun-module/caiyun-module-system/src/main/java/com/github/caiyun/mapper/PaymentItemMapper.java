package com.github.caiyun.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.caiyun.pay.model.entity.PaymentItemDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 缴费项目Mapper接口
 */
@Mapper
public interface PaymentItemMapper extends
        BaseMapper<PaymentItemDO> {

    /**
     * 分页查询缴费项目
     *
     * @param page 分页参数
     * @param name 项目名称，模糊匹配
     * @param category 项目类别
     * @param status 缴费状态
     * @return 缴费项目分页数据
     */
    IPage<PaymentItemDO> selectPage(
            Page<PaymentItemDO> page,
            @Param("name") String name,
            @Param("category") String category,
            @Param("status") String status
    );
}