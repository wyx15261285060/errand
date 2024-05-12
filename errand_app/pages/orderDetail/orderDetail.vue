<template>
	<view style="padding: 20rpx;">
		<view class="box" v-if="order.id">
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">订单编号：</view>
				<view class="line-content">{{ order.orderNo }}</view>
			</view>
			<view class="line">
				<view class="line-title">取货地址：</view>
				<view class="line-content">{{ order.address.address + order.address.doorNo }}</view>
			</view>
			<view class="line">
				<view class="line-title">取货联系人：</view>
				<view class="line-content">{{ order.address.userName }}</view>
			</view>
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">取货电话：</view>
				<view class="line-content">{{ order.address.phone }}</view>
			</view>

			<view class="line">
				<view class="line-title">收货地址：</view>
				<view class="line-content">{{ order.targetAddress.address + order.targetAddress.doorNo }}</view>
			</view>
			<view class="line">
				<view class="line-title">收货联系人：</view>
				<view class="line-content">{{ order.targetAddress.userName }}</view>
			</view>
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">收货电话：</view>
				<view class="line-content">{{ order.targetAddress.phone }}</view>
			</view>

			<view class="line">
				<view class="line-title">物品名称：</view>
				<view class="line-content">{{ order.name }}</view>
			</view>
			<view class="line">
				<view class="line-title">物品描述：</view>
				<view class="line-content">{{ order.descr }}</view>
			</view>
			<view class="line">
				<view class="line-title">物品图片：</view>
				<view class="line-content">
					<image :src="order.img" mode="widthFix" style="width: 180rpx;"></image>
				</view>
			</view>
			<view class="line">
				<view class="line-title">物品类型：</view>
				<view class="line-content">{{ order.type }}</view>
			</view>
			<view class="line">
				<view class="line-title">物品重量：</view>
				<view class="line-content">{{ order.weight }} kg</view>
			</view>
			<view class="line">
				<view class="line-title">费用：</view>
				<view class="line-content" style="color: red;">￥{{ order.weight }}</view>
			</view>
			<view class="line">
				<view class="line-title">下单时间：</view>
				<view class="line-content">{{ order.time }}</view>
			</view>
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">备注：</view>
				<view class="line-content">{{ order.comment || '' }}</view>
			</view>
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">骑手姓名：</view>
				<view class="line-content">{{ order.certification.name }}</view>
			</view>
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">骑手照片：</view>
				<view v-if="order.certification"><image :src="order.certification.avatar" mode="widthFix" style="width: 180rpx;"></view>
				</image>
			</view>
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">骑手电话：</view>
				<view class="line-content">{{ order.certification.phone }}</view>
			</view>
			<view><button type="primary" class="button" style="margin-bottom: 20rpx;" @click="back">确认</button></view>
		</view>
	</view>


</template>

<script>
	export default {
		data() {
			return {
				order: {}

			}
		},
		// option 其他页面跳转至该页面时传过来的参数
		onLoad(option) {
			const orderId = option.orderId
			this.load(orderId)
		},
		methods: {
			back(){
				uni.navigateBack()
			},
			load(orderId) {
				this.$request.get('/order/selectById/' + orderId).then(res => {
					this.order = res.data || {}
				})
			}

		}
	}
</script>

<style>
	.line {
		display: flex;
		margin-bottom: 10rpx;
	}

	.line-title {
		width: 200rpx;
		font-weight: bold;
		text-align: right;
	}

	.line-content {
		flex: 1;
	}
</style>