<template>
	<view style="padding: 20rpx;">
		<view class="box" style="padding: 40rpx 0;">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="140rpx" label-align="right">
				<uni-forms-item label="头像" name="avatar">
					<uni-file-picker limit="1" :image-styles="imageStyles" :del-icon="false" :disable-preview="true"
						fileMediatype="image" v-model="imgs" @select="handleImgUploadSuccess"></uni-file-picker>
				</uni-forms-item>
				<uni-forms-item label="账号" name="username">
					<uni-easyinput type="text" v-model="form.username" disabled />
				</uni-forms-item>
				<uni-forms-item label="密码" name="password" required>
					<uni-easyinput type="password" v-model="form.password" placeholder="请输入密码" />
				</uni-forms-item>
				<uni-forms-item label="姓名" name="name" required>
					<uni-easyinput type="text" v-model="form.name" placeholder="请输入姓名" />
				</uni-forms-item>
				<uni-forms-item label="性别" name="sex">
					<uni-data-checkbox style="position: relative; top: 10rpx;" v-model="form.sex"
						:localdata="sexList"></uni-data-checkbox>
				</uni-forms-item>
				<uni-forms-item label="电话" name="phone">
					<uni-easyinput type="text" v-model="form.phone" placeholder="请输入电话" />
				</uni-forms-item>
				<uni-forms-item label="邮箱" name="email">
					<uni-easyinput type="text" v-model="form.email" placeholder="请输入邮箱" />
				</uni-forms-item>
				<view style="margin-bottom: 40rpx;">
					<button type="primary" @click="save" class="button">保 存</button>
				</view>
			</uni-forms>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//  user:uni.getStorageSync('xm-user'),
				form: {},
				rules: {
					password: {
						rules: [{
							required: true,
							errorMessage: '请填写密码',
						}]
					},
					name: {
						rules: [{
							required: true,
							errorMessage: '请填写名称',
						}]
					},
				},
				imgs: [],
				imageStyles: {
					"height": 80, // 边框高度
					"width": 80, // 边框宽度
					"border": { // 如果为 Boolean 值，可以控制边框显示与否
						"color": "#eee", // 边框颜色
						"width": "1px", // 边框宽度
						"style": "solid", // 边框样式
						"radius": "50%" // 边框圆角，支持百分比
					}
				},
				sexList: [{
						text: '男',
						value: '男'
					},
					{
						text: '女',
						value: '女'
					},
				]

			}
		},
		onShow() {
			this.form = uni.getStorageSync('xm-user')
			if (this.form.avatar) {
				this.imgs[0] = {
					url: this.form.avatar
				}
			}

		},
		methods: {
			save() {
				// 邮箱校验正则表达式
				let email_reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
				if (!email_reg.test(this.form.email)) {
					uni.showToast({
						icon: 'none',
						title: '请输入正确的邮箱'
					})
					return
				}
				this.$request.put('/user/update', this.form).then(res => {
					if (res.code === '200') {
						uni.showToast({
							icon: 'success',
							title: '操作成功'
						})
						uni.setStorageSync('xm-user', this.form)
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/personal/personal'
							})
						}, 1000)
						// uni.navigateBack()
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
				})
			},
			// 回调函数 e中携带图片信息
			handleImgUploadSuccess(e) {
				let _this = this
				// 获取文件路径
				const filePath = e.tempFilePaths[0]
				uni.uploadFile({
					// 后端接口（默认发送post请求） 注意 _this.$baseUrl需要在全局变量定义
					url: _this.$baseUrl + '/files/upload',
					filePath: filePath,
					//后端文件形参的名字
					name: "file",
					success(res) {
						let url = JSON.parse(res.data || '{}').data // 获取返回的图像链接
						_this.form.avatar = url // 头像属性赋值
					}
				})
			},


		}
	}
</script>

<style>

</style>