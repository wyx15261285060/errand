<template>
	<view style="padding: 20rpx;">
		<view style="text-align: center;">
			<image :src="user.avatar" style="width: 200rpx; height: 200rpx; border-radius:50%;"></image>
			<!-- margin 参数 10rpx表示上下间距 0表示左右间距 -->
			<view style="margin: 10rpx 0; font-weight: bold;">{{ user.name }}</view>
			<view v-if="user.rider">
				<uni-icons color="#006eff" type="vip-filled" style="position: relative;top: 2rpx;"></uni-icons>
				<text style="color: #006eff; font-weight: bold;">已认证</text>
			</view>
		</view>
		<view style="margin: 20rpx,0" class="box">
			<view class="title">用户服务</view>
			<view style="display: flex;">
				<view class="category-item" @click="goPage('/pages/address/address')">
					<image src="../../static/imgs/地址.png" style="width: 50%;" mode="widthFix"></image>
					<view style="flex: 1;font-weight: bold;">我的地址</view>
				</view>
				<view class="category-item" @click="goPage('代购鲜花')">
					<image src="../../static/imgs/收支.png" style="width: 50%;" mode="widthFix"></image>
					<view style="flex: 1;font-weight: bold;">收支明细</view>
				</view>
				<view class="category-item" @click="goPage('代购零食')">
					<image src="../../static/imgs/评价.png" style="width: 50%;" mode="widthFix"></image>
					<view style="flex: 1;font-weight: bold;">评价中心</view>
				</view>
				<view class="category-item" @click="goPage('代购餐品')">
					<image src="../../static/imgs/钱包.png" style="width:50%;" mode="widthFix"></image>
					<view style="flex: 1;font-weight: bold;">我的钱包</view>
				</view>
			</view>
		</view>

		<view style="margin: 20rpx,0" class="box">
			<view class="title">骑手服务</view>
			<view style="display: flex;">
				<view class="category-item" @click="goPage('/pages/certification/certification')">
					<image src="../../static/imgs/认证.png" style="width: 30%;" mode="widthFix"></image>
					<view style="flex: 1; font-weight: bold;">骑手认证</view>
				</view>
				<view class="category-item" @click="goPage('/pages/acceptOrder/acceptOrder')">
					<image src="../../static/imgs/跑腿.png" style="width: 30%;" mode="widthFix"></image>
					<view style="flex: 1; font-weight: bold;">骑手订单</view>
				</view>
			</view>
		</view>
		<view style="margin: 20rpx 0;" class="box">
			<view class="info-item" @click="goPage('/pages/personInfo/personInfo')">
				<uni-icons type="person" size="16"></uni-icons>
				<text style="margin-left: 10rpx; font-weight: bold;">个人信息</text>
			</view>
			<view class="info-item" @click="goPage('/pages/introduce/introduce')">
				<uni-icons type="medal" size="16"></uni-icons>
				<text style="margin-left: 10rpx; font-weight: bold;">平台介绍</text>
			</view>
			<view class="info-item" @click="goPage('/pages/userProtocol/userProtocol')">
				<uni-icons type="paperclip" size="16"></uni-icons>
				<text style="margin-left: 10rpx; font-weight: bold;">用户协议</text>
			</view>
			<view class="info-item" @click="logout">
				<uni-icons type="undo" size="16"></uni-icons>
				<text style="margin-left: 10rpx; font-weight: bold;">退出登录</text>
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: uni.getStorageSync('xm-user')

			}
		},
		methods: {
			goPage(url) {
				uni.navigateTo({
					url: url
				})
			},
			logout() {
				// 清理缓存
				uni.removeStorageSync('xm-user')
				uni.removeStorageSync('orderStore')
				uni.reLaunch({
					url: '/pages/login/login'
				})
			},

		}
	}
</script>

<style>
	.category-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		/* 上下对齐 */
		justify-items: space-between;
		align-items: center;
		/* 文字与图片的间距 */
		grid-gap: 10rpx;
	}

	.title {
		font-weight: bold;
		font-size: 36rpx;
		margin-bottom: 30rpx;
	}

	.info-item {
		padding: 15rpx;
		border-bottom: 2px solid #eee;

	}

	.info-item .uni-icons {
		position: relative;
		top: 2rpx;

	}
</style>