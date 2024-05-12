<template>
	<view style="padding: 20rpx;margin: 20rpx 20rpx;">
		<view class="box">

			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="160rpx" label-align="right">
				<uni-forms-item label="订单编号">
					<navigator :url="'/pages/orderDetail/orderDetail?orderId=' + order.id"
						style="padding: 24rpx 0;color: blue;">{{ order.orderNo }}</navigator>
				</uni-forms-item>
				<uni-forms-item label="内容" name="content" required>
					<uni-easyinput type="textarea" v-model="form.content" placeholder="请输入评价内容"></uni-easyinput>
				</uni-forms-item>
				<uni-forms-item label="评分">
					<view style="padding: 15rpx 0;">
						<uni-rate v-model="form.star"></uni-rate>
					</view>
				</uni-forms-item>
				<view style="margin-top: 20rpx;">
					<button type="primary" class="button" @click="save">提 交</button>
				</view>


			</uni-forms>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {},
				rules: {
					content: {
						rules: [{
							required: true,
							errorMessage: "请填写评价内容",
						}]
					},
					star: {
						rules: [{
							required: true,
							errorMessage: "请选择评价星级",
						}]
					}
				},
				order: {},
				user: uni.getStorageSync('xm-user'),


			}
		},
		onLoad(option) {
			this.load(option.orderId)
		},
		methods: {
			save() {
				this.form.userId = this.user.id,
					this.form.acceptId = this.order.acceptId,
					this.form.orderId = this.order.id,
					this.$request.post('/comment/add', this.form).then(res => {
						if (res.code === '200') {
							uni.showToast({
								icon: 'success',
								title: '评价成功'
							})
							setTimeout(() => {
								uni.navigateBack()
							}, 500)
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg,
							})
						}
					})
			},
			load(orderId) {
				this.$request.get('/order/selectById/' + orderId).then(res => {
					this.order = res.data || {}
				})
			},
		}
	}
</script>

<style>

</style>