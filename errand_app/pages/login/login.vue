<template>
	<view style="padding:20rpx;">
		<view class="box" style="padding: 60rpx 30rpx;">
			<view style="font-size: 40rpx;">欢迎登录</view>
			<view style="margin: 70rpx 0;">
				<!-- 基础用法，不包含校验规则 -->
				<uni-forms ref="loginFormRef" :modelValue="loginFormData" :rules="rules" validateTrigger="blur">
					<uni-forms-item required name="username">
						<uni-easyinput prefix-icon="person" v-model="loginFormData.username" placeholder="请输入账号" />
					</uni-forms-item>
					<uni-forms-item required name="password">
						<uni-easyinput type="password" prefix-icon="locked-filled" v-model="loginFormData.password"
							placeholder="请输入密码" />
					</uni-forms-item>
					<uni-forms-item>
						<button @click="login" size="default" type="primary"
							style="width: 100% ;height:70rpx;line-height:70rpx; background-color:#006eff; border-color:#006eff; ">登
							录</button>
					</uni-forms-item>
				</uni-forms>
			</view>
			<view style="text-align: right;">
				没有账号？请<navigator url="/pages/register/register"
					style="display: inline; margin-left: 5rpx; color: #006eff;">注册</navigator>
			</view>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				loginFormData: {
					role: 'USER'
				},
				rules: {
					// 对username字段进行必填验证
					username: {
						// username 字段的校验规则
						rules: [
							// 校验 username 不能为空
							{
								required: true,
								errorMessage: '请填写账号',
							},
						],
					},
					password: {
						// password 字段的校验规则
						rules: [
							// 校验 password 不能为空
							{
								required: true,
								errorMessage: '请填写密码',
							},
						],
					},
				}

			}
		},

		methods: {

			login() {
				this.$refs.loginFormRef.validate().then(res => {
					this.$request.post("/login", this.loginFormData).then(res => {
						if (res.code === '200') {
							// 登录成功后将用户的信息放到缓存中
							uni.setStorageSync("xm-user", res.data);
							uni.showToast({
								icon: 'success',
								title: "登录成功"
							});

							setTimeout(() => {
								uni.switchTab({
									url: "/pages/index/index"
								})
							}, 500)
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg
							});
						}
					});

				}).catch(err => {
					console.log('err', err);
				})
			},

		}
	}
</script>

<style>

</style>