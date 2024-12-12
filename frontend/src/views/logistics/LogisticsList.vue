<template>
  <div class="logistics-list">
    <div class="toolbar">
      <el-input
        v-model="search"
        placeholder="搜索订单号/物流单号"
        style="width: 200px"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>
    
    <el-table :data="logistics" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="trackingNo" label="物流单号" width="180" />
      <el-table-column prop="carrierName" label="物流公司" width="120" />
      <el-table-column prop="status" label="物流状态" width="120">
        <template #default="scope">
          <el-tag :type="getLogisticsStatusType(scope.row.status)">
            {{ getLogisticsStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="currentLocation" label="当前位置" />
      <el-table-column prop="updateTime" label="更新时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="viewTrack(scope.row)">
            查看轨迹
          </el-button>
          <el-button 
            size="small" 
            type="primary"
            @click="handleUpdate(scope.row)"
          >
            更新状态
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 物流轨迹对话框 -->
    <el-dialog title="物流轨迹" v-model="trackDialogVisible" width="60%">
      <logistics-track :tracks="tracks" />
    </el-dialog>
    
    <!-- 更新物流状态对话框 -->
    <el-dialog title="更新物流状态" v-model="updateDialogVisible">
      <el-form :model="form" label-width="100px">
        <el-form-item label="物流状态">
          <el-select v-model="form.status">
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="当前位置">
          <el-input v-model="form.currentLocation" />
        </el-form-item>
        <el-form-item label="描述信息">
          <el-input
            type="textarea"
            v-model="form.description"
            rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="updateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUpdate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import LogisticsTrack from '@/components/logistics/LogisticsTrack.vue'
import { 
  getLogistics, 
  getLogisticsTrack, 
  updateLogistics 
} from '@/api/logistics'

export default {
  name: 'LogisticsList',
  components: {
    LogisticsTrack
  },
  data() {
    return {
      search: '',
      logistics: [],
      tracks: [],
      trackDialogVisible: false,
      updateDialogVisible: false,
      form: {
        status: '',
        currentLocation: '',
        description: ''
      },
      statusOptions: [
        { value: 'PENDING', label: '待发货' },
        { value: 'SHIPPED', label: '已发货' },
        { value: 'IN_TRANSIT', label: '运输中' },
        { value: 'DELIVERED', label: '已送达' },
        { value: 'EXCEPTION', label: '异常' }
      ]
    }
  },
  created() {
    this.fetchLogistics()
  },
  methods: {
    async fetchLogistics() {
      try {
        const { data } = await getLogistics()
        this.logistics = data
      } catch (error) {
        this.$message.error('获取物流信息失败')
      }
    },
    async viewTrack(row) {
      try {
        const { data } = await getLogisticsTrack(row.orderNo)
        this.tracks = data
        this.trackDialogVisible = true
      } catch (error) {
        this.$message.error('获取物流轨迹失败')
      }
    },
    handleUpdate(row) {
      this.form = {
        orderNo: row.orderNo,
        status: row.status,
        currentLocation: row.currentLocation,
        description: ''
      }
      this.updateDialogVisible = true
    },
    async submitUpdate() {
      try {
        await updateLogistics(this.form)
        this.$message.success('更新成功')
        this.updateDialogVisible = false
        this.fetchLogistics()
      } catch (error) {
        this.$message.error('更新失败')
      }
    }
  }
}
</script> 