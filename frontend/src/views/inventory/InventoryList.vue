<template>
  <div class="inventory-list">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">添加商品</el-button>
      <el-input
        v-model="search"
        placeholder="搜索商品"
        style="width: 200px"
      />
    </div>
    
    <el-table :data="inventory" border style="width: 100%">
      <el-table-column prop="productId" label="商品ID" width="120" />
      <el-table-column prop="productName" label="商品名称" width="200" />
      <el-table-column prop="quantity" label="库存数量" width="120" />
      <el-table-column prop="lockQuantity" label="锁定数量" width="120" />
      <el-table-column label="库存状态" width="120">
        <template #default="scope">
          <el-tag :type="getStockStatusType(scope.row)">
            {{ getStockStatusText(scope.row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button 
            size="small" 
            type="warning"
            @click="handleUpdateStock(scope.row)"
          >
            更新库存
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 编辑库存对话框 -->
    <el-dialog 
      :title="dialogType === 'add' ? '添加商品' : '编辑商品'"
      v-model="dialogVisible"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="form.productName" />
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input-number v-model="form.quantity" :min="0" />
        </el-form-item>
        <el-form-item label="预警阈值">
          <el-input-number v-model="form.alertThreshold" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { getInventory, updateInventory } from '@/api/inventory'

export default {
  name: 'InventoryList',
  data() {
    return {
      search: '',
      inventory: [],
      dialogVisible: false,
      dialogType: 'add',
      form: {
        productName: '',
        quantity: 0,
        alertThreshold: 0
      }
    }
  },
  created() {
    this.fetchInventory()
  },
  methods: {
    async fetchInventory() {
      try {
        const { data } = await getInventory()
        this.inventory = data
      } catch (error) {
        this.$message.error('获取库存信息失败')
      }
    },
    getStockStatusType(row) {
      if (row.quantity <= row.alertThreshold) {
        return 'danger'
      }
      return 'success'
    },
    getStockStatusText(row) {
      if (row.quantity <= row.alertThreshold) {
        return '库存不足'
      }
      return '库存充足'
    },
    handleAdd() {
      this.dialogType = 'add'
      this.form = {
        productName: '',
        quantity: 0,
        alertThreshold: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await updateInventory(this.form)
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.fetchInventory()
      } catch (error) {
        this.$message.error('保存失败')
      }
    }
  }
}
</script> 