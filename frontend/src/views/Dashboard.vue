<template>
  <div class="dashboard">
    <el-container>
      <el-aside width="200px">
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          @select="handleSelect"
        >
          <el-menu-item index="1">
            <i class="el-icon-s-order"></i>
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="2">
            <i class="el-icon-box"></i>
            <span>库存管理</span>
          </el-menu-item>
          <el-menu-item index="3">
            <i class="el-icon-truck"></i>
            <span>物流管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header>
          <h2>订单管理系统</h2>
          <div class="user-info">
            <span>{{ username }}</span>
            <el-button size="small" @click="logout">退出</el-button>
          </div>
        </el-header>
        
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      activeMenu: '1',
      username: 'Admin'
    }
  },
  methods: {
    handleSelect(key) {
      switch(key) {
        case '1':
          this.$router.push('/orders');
          break;
        case '2':
          this.$router.push('/inventory');
          break;
        case '3':
          this.$router.push('/logistics');
          break;
      }
    },
    logout() {
      // 实现登出逻辑
    }
  }
}
</script>

<style scoped>
.dashboard {
  height: 100vh;
}
.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #dcdfe6;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style> 