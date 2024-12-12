<template>
  <div class="order-list">
    <div class="toolbar">
      <el-input
        v-model="search"
        placeholder="搜索订单号"
        style="width: 200px"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>
    
    <el-table :data="orders" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="totalAmount" label="订单金额" width="120" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="viewOrder(scope.row)">查看</el-button>
          <el-button 
            size="small" 
            type="primary" 
            v-if="scope.row.status === 'PENDING_PAYMENT'"
            @click="handlePay(scope.row)"
          >
            支付
          </el-button>
          <el-button 
            size="small" 
            type="danger"
            v-if="scope.row.status === 'PENDING_PAYMENT'"
            @click="handleCancel(scope.row)"
          >
            取消
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    />
    
    <!-- 订单详情对话框 -->
    <el-dialog title="订单详情" v-model="dialogVisible" width="50%">
      <order-detail :order="selectedOrder" />
    </el-dialog>
  </div>
</template>

<script>
import OrderDetail from '@/components/order/OrderDetail.vue'
import { getOrders, cancelOrder } from '@/api/order'

export default {
  name: 'OrderList',
  components: {
    OrderDetail
  },
  data() {
    return {
      search: '',
      orders: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      selectedOrder: null
    }
  },
  created() {
    this.fetchOrders()
  },
  methods: {
    async fetchOrders() {
      try {
        const { data } = await getOrders({
          page: this.currentPage,
          size: this.pageSize,
          search: this.search
        })
        this.orders = data.content
        this.total = data.total
      } catch (error) {
        this.$message.error('获取订单列表失败')
      }
    },
    getStatusType(status) {
      const types = {
        PENDING_PAYMENT: 'warning',
        PAID: 'success',
        SHIPPED: 'primary',
        COMPLETED: 'success',
        CANCELLED: 'info'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        PENDING_PAYMENT: '待支付',
        PAID: '已支付',
        SHIPPED: '已发货',
        COMPLETED: '已完成',
        CANCELLED: '已取消'
      }
      return texts[status] || status
    },
    viewOrder(order) {
      this.selectedOrder = order
      this.dialogVisible = true
    },
    async handleCancel(order) {
      try {
        await this.$confirm('确认取消该订单吗？')
        await cancelOrder(order.id)
        this.$message.success('订单已取消')
        this.fetchOrders()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('取消��单失败')
        }
      }
    },
    handleSearch() {
      this.currentPage = 1
      this.fetchOrders()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchOrders()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchOrders()
    }
  }
}
</script>

<style scoped>
.order-list {
  padding: 20px;
}
.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}
</style> 