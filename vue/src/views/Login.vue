<template>
  <div class="container">
    <div style="width: 350px; padding: 50px 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #006eff;font-weight: bold">欢迎使用
      </div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password
                    v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input placeholder="请输入验证码" v-model="form.code">
          </el-input>
        </el-form-item>
        <div v-if="timeLeft > 0">
          <img :src="captchaUrl" @click="refreshCaptcha" />
        </div>
        <el-form-item>
          <el-button style="width: 100%; background-color: #006eff; border-color: #006eff; color: white"
                     @click="login">登 录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

export default {
  name: "Login",
  data() {
    return {
      form: {role: 'ADMIN'},
      timeLeft: 5,
      captchaUrl: "",
      rules: {
        username: [
          {required: true, message: '请输入账号', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        code: [
          {required: true, trigger: "change", message: "请输入验证码"},
        ]
      },
    }
  },
  created() {
    this.refreshCaptcha();
  },
  methods: {
    refreshCaptcha() {
      this.$request.get('/auth/captcha').then(res => {
        this.captchaUrl = res.data.code || ''
        // 将key获取出来
        this.form.uuid = res.data.uuid
        console.log(this.form.uuid)
      });
    },



    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$router.push('/')  // 跳转主页
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

a {
  color: #2a60c9;
}

.refresh-icon {
  font-size: 16px; /* 设置图标大小 */
  color: #333; /* 设置图标颜色 */
}
</style>