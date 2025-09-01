<template>
  <div class="payment-records">
    <!-- 顶部搜索区域 -->
    <div class="filters">
      <el-input
        v-model="keyword"
        placeholder="搜索缴费标题或描述"
        prefix-icon="el-icon-search"
        class="search-box"
        clearable
      />

      <!-- 缴费类别筛选 -->
      <el-select v-model="categoryFilter" placeholder="选择类别" clearable>
        <el-option
          v-for="item in categories"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>

      <!-- 学期筛选 -->
      <el-select v-model="termFilter" placeholder="选择学期" clearable>
        <el-option
          v-for="item in terms"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>

      <!-- 日期范围筛选 -->
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        class="date-range"
      />
    </div>

  <!-- 缴费记录表格 -->
<el-table :data="filteredRecords" stripe style="width: 100%" :empty-text="'暂无缴费记录'">
  <!-- 1. 缴费项目：对应映射后的title -->
  <el-table-column prop="title" label="缴费项目" width="200" />
  
  <!-- 2. 金额：对应映射后的amount（显示¥符号） -->
  <el-table-column label="金额" width="120">
    <template #default="{ row }">
      ¥{{ row.amount }}
    </template>
  </el-table-column>
  
  <!-- 3. 类别：对应映射后的category -->
  <el-table-column prop="category" label="类别" width="120" />
  
  <!-- 4. 学期：对应映射后的term -->
  <el-table-column prop="term" label="学期" width="120" />
  
  <!-- 5. 缴费时间：对应映射后的paidAt（已处理null） -->
  <el-table-column label="缴费时间" width="180">
    <template #default="{ row }">
      {{ row.paidAt }}
    </template>
  </el-table-column>
  
  <!-- 6. 状态：使用映射后的status和statusTagType渲染标签 -->
  <el-table-column label="状态" width="120">
    <template #default="{ row }">
      <el-tag :type="row.statusTagType">
        {{ row.status }}
      </el-tag>
    </template>
  </el-table-column>
  
  <!-- 显示订单号 -->
  <el-table-column label="订单号" width="280">
    <template #default="{ row }">
      {{ row.orderId }}
    </template>
  </el-table-column>
</el-table>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'

// 定义支付项目的类型接口
interface PaymentItemVO {
  title: string
  amount: number
  category: string
  term: string
  paidAt?: string
  status: 'PAID' | 'UNPAID' | 'PENDING'
  description?: string
}

const payments = ref<PaymentItemVO[]>([])
const keyword = ref('')
const categoryFilter = ref('')
const termFilter = ref('')
const dateRange = ref<[string, string] | null>(null)

// 类别选项
const categories = [
  { value: '学费', label: '学费' },
  { value: '住宿费', label: '住宿费' },
  { value: '杂费', label: '杂费' }
]

// 学期选项
const terms = [
  { value: '2025春', label: '2025春' },
  { value: '2025秋', label: '2025秋' },
  { value: '2026春', label: '2026春' }
]

// 存储静态数据
const rawRecords = ref([]);

// 生成静态模拟数据
const generateStaticData = () => {
  // 模拟后端返回的原始数据结构
  return [
    {
      itemId: 1,
      itemName: '学费',
      amount: 5000,
      category: '学费',
      payTime: '2025-09-01T08:30:00',
      status: 'PAID',
      tradeNo: 'TRADE202509010001'
    },
    {
      itemId: 2,
      itemName: '住宿费',
      amount: 1200,
      category: '住宿费',
      payTime: null,
      status: 'PENDING',
      tradeNo: 'TRADE202509010002'
    },
    {
      itemId: 3,
      itemName: null,
      amount: 300,
      category: '杂费',
      payTime: '2025-08-28T14:20:00',
      status: 'PAID',
      tradeNo: 'TRADE202508280003'
    },
    {
      itemId: 4,
      itemName: '教材费',
      amount: 450,
      category: '杂费',
      payTime: null,
      status: 'PENDING',
      tradeNo: 'TRADE202509020004'
    },
    {
      itemId: 5,
      itemName: '学费',
      amount: 5200,
      category: '学费',
      payTime: '2025-07-15T10:15:00',
      status: 'PAID',
      tradeNo: 'TRADE202507150005'
    }
  ];
};

// 数据映射：后端字段 → 前端表格字段
const filteredRecords = computed(() => {
  // 先进行关键词筛选
  let filtered = rawRecords.value.filter(record => {
    // 关键词筛选逻辑
    const matchesKeyword = keyword.value
      ? (record.itemName && record.itemName.includes(keyword.value)) ||
        (record.description && record.description.includes(keyword.value))
      : true;
      
    // 类别筛选逻辑
    const matchesCategory = categoryFilter.value
      ? record.category === categoryFilter.value
      : true;
      
    // 学期筛选逻辑
    const matchesTerm = termFilter.value
      ? record.term === termFilter.value
      : true;
      
    return matchesKeyword && matchesCategory && matchesTerm;
  });
  
  // 然后进行数据映射
  return filtered.map(record => {
    // 处理状态映射（后端status → 前端显示文本）
    const getStatusText = () => {
      switch (record.status) {
        case 'PAID': return '已缴费';
        case 'PENDING': return '待缴费';
        default: return '未知状态';
      }
    };

    // 处理状态标签类型（对应Element Plus Tag的type）
    const getStatusTagType = () => {
      switch (record.status) {
        case 'PAID': return 'success'; // 绿色
        case 'PENDING': return 'warning'; // 橙色
        default: return 'info'; // 蓝色
      }
    };

    return {
      // 1. 前端表格需要的字段：title（缴费项目）
      title: record.itemName || `缴费项目-${record.itemId}`,
      
      // 2. 金额：保留两位小数
      amount: Number(record.amount).toFixed(2),
      
      // 3. 类别：给默认值
      category: record.category || '未分类',
      
      // 4. 学期：根据itemId动态分配不同学期
      term: record.itemId % 3 === 0 ? '2025春' : record.itemId % 3 === 1 ? '2025秋' : '2026春',
      
      // 5. 缴费时间：格式化时间
      paidAt: record.payTime ? new Date(record.payTime).toLocaleString() : '-',
      
      // 6. 状态相关
      status: getStatusText(),
      statusTagType: getStatusTagType(),
      
      // 7. 订单号
      orderId: `ORDER-FIXED-${record.itemId}-${record.tradeNo}`,
      
      // 8. 保留后端原始字段
      raw: record
    };
  });
});

// 初始化加载静态数据
onMounted(() => {
  rawRecords.value = generateStaticData();
})
</script>

<style scoped>
.payment-records {
  padding: 16px;
}

.filters {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  align-items: center;
}

.search-box {
  width: 260px;
}

.date-range {
  width: 300px;
}
</style>
