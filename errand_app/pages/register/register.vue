<template>
	<view style="padding:20rpx;">
		<view class="box" style="padding: 60rpx 30rpx;">
			<view style="font-size: 40rpx;">欢迎注册</view>
			<view style="margin: 70rpx 0;">
				<!-- 基础用法，不包含校验规则 -->
				<uni-forms ref="formRef" :modelValue="formData" :rules="rules" validateTrigger="blur">
					<uni-forms-item required name="phone">
						<uni-easyinput prefix-icon="phone" v-model="formData.phone" placeholder="请输入手机号" />
					</uni-forms-item>
					<uni-forms-item required name="username">
						<uni-easyinput prefix-icon="person" v-model="formData.username" placeholder="请输入账号" />
					</uni-forms-item>
					<uni-forms-item required name="password">
						<uni-easyinput type="password" prefix-icon="locked-filled" v-model="formData.password"
							placeholder="请输入密码" />
					</uni-forms-item>
					<uni-forms-item required name="newPassword">
						<uni-easyinput type="password" prefix-icon="locked-filled" v-model="formData.newPassword"
							placeholder="请再次输入密码" />
					</uni-forms-item>
					<uni-forms-item>
						<button @click="register" size="default" type="primary"
							style="width: 100% ;height:70rpx;line-height:70rpx; background-color:#006eff; border-color:#006eff; ">注
							册</button>
					</uni-forms-item>
				</uni-forms>
			</view>
			<view style="text-align: right;">
				已有账号？请<navigator url="/pages/login/login" style="display: inline; margin-left: 5rpx; color: #006eff;">登录
				</navigator>
			</view>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				formData: {
					role: 'USER'
				},
				rules: {
					// 对手机号字段进行必填验证
					phone: {
						// username 字段的校验规则
						rules: [
							// 校验 username 不能为空
							{
								required: true,
								errorMessage: '请填写手机号',
							},
						],
					},
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
					newPassword: {
						rules: [{
							required: true,
							errorMessage: '请再次输入密码',
						}]

					}
				},


			}
		},

		methods: {
			register() {
				let flag = true;
				let reg = /^[1][3,4,5,7,8,9][0-9]{9}$/
				if (!reg.test(this.formData.phone)) { //判断手机号格式是否正确
					uni.showToast({
						title: '请输入正确的手机号',
						icon: 'none'
					})

				} else {
					// 判断手机号是否唯一
					this.$refs.formRef.validate().then(res => {
						this.$request.post("/register", this.formData).then(res => {
							if (res.code === '200') {
								uni.showToast({
									icon: ' success',
									title: "注册成功"
								});
								setTimeout(() => {
									uni.navigateTo({ // navigateTo 页面跳转 ，switchTag 菜单切换
										url: "/pages/login/login"
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

				}
			},

		}
	}
</script>

<style>

</style>