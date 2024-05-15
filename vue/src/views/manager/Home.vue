<template>
  <div>
    <div class="card" style="padding: 15px">
      您好，{{ user?.username }}！欢迎使用本系统
    </div>

    <div style="display: flex; margin: 10px 0">
      <div style="width: 50%;" class="card">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">公告列表</div>
        <el-timeline>
          <el-timeline-item v-for="item in notices" :timestamp="item.time" placement="top">
            <el-card>
              <h4>{{ item.title }}</h4>
              <p>{{ item.content }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>

      </div>
      <div style="width: 50%;" class="card">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">评价列表</div>
        <el-timeline>
          <el-timeline-item v-for="item in commentList" :timestamp="item.time" placement="top">
            <el-card>
              <el-rate  v-model="item.star" disabled show-score text-color="#ff9900"></el-rate>
              <p>{{ item.content }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>


  </div>
</template>

<script>

export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: [],
      commentList: [],
    }
  },

  created() {
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })
    this.$request.get('/comment/selectAll').then(res => {
      this.commentList = res.data || []
    })
  },




}
</script>
