<template>
	<view style="padding: 20rpx;">
		<!-- 首页轮播图 -->
		<view style="margin-bottom: 20rpx;">
			<swiper circular autoplay :interval="1000" :duration="500" indicator-dots style="height: 320rpx;"
				indicator-color="rgba(255, 255, 255, 0.6)" indicator-active-color="#ff5500">
				<swiper-item v-for="item in imgs" :key="item">
					<image :src="item" alt="" style="width: 100%; height: 350rpx;" />
				</swiper-item>
			</swiper>
		</view>
		<!-- 公告栏 -->
		<view style="margin-bottom: 20rpx;">
			<uni-notice-bar v-if="content" show-icon single="true" :text="content" />
		</view>
		<!-- 跑腿分类 -->
		<view style="display: flex; margin-bottom: 10rpx;" class="box">
			<view class="category-item" @click="goPlaceOrder('代取快递')">
				<image src="../../static/imgs/快递.png" style="width: 50%;" mode="widthFix"></image>
				<view style="flex: 1;">代取快递</view>
			</view>
			<view class="category-item"  @click="goPlaceOrder('代购鲜花')">
				<image src="../../static/imgs/花.png" style="width: 50%;" mode="widthFix"></image>
				<view style="flex: 1;">代购鲜花</view>
			</view>
			<view class="category-item" @click="goPlaceOrder('代购零食')">
				<image src="../../static/imgs/零食.png" style="width: 50%;" mode="widthFix"></image>
				<view style="flex: 1;">代购零食</view>
			</view>
			<view class="category-item" @click="goPlaceOrder('代购餐品')">
				<image src="../../static/imgs/取餐.png" style="width:50%;" mode="widthFix"></image>
				<view style="flex: 1;">代购餐品</view>
			</view>
		</view>
		<!-- 跑腿订单列表 -->
		<view class="box" style="color: #006eff; font-weight: bold; margin-bottom: 10rpx;">
			跑腿订单
		</view>
		<view>
			<view v-for="item in orderList" :key="item.id" class="box" style="margin-bottom: 10rpx;" @click="goOrderDetail(item.id)">
				<view style="display: flex;align-items: center; margin-bottom: 20rpx;">
					<view style="flex:1">
						<uni-tag text="餐品" size="small" type="success" v-if="item.type === '代购餐品'"></uni-tag>
						<uni-tag text="快递" size="small" type="primary" v-if="item.type === '代取快递'"></uni-tag>
						<uni-tag text="鲜花" size="small" type="warning" v-if="item.type === '代购鲜花'"></uni-tag>
						<uni-tag text="零食" size="small" type="error" v-if="item.type === '代购零食'"></uni-tag>
						<text style="margin-left: 10rpx;">{{item.name}}</text>
					</view>
					<view style="flex: 1; text-align: right;">
						<text style="color:chocolate;">跑腿费</text>
						<text style="color: red; font-size: 30rpx;">￥{{item.price}}</text>
					</view>
					
				</view>
				<view style="display: flex; align-items: center;">
					<view style="flex: 1;">
						<text style="margin-right: 10rpx;">已下单{{item.minutes}}分钟</text>
						<text style="color: aqua;">待接单</text>
					</view>
					<view style="flex: 1; text-align: right;">
						<uni-tag text="接单" type="primary" size="mini"></uni-tag>
					</view>
					
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import placeOrderVue from '../placeOrder/placeOrder.vue';
	export default {
		data() {
			return {
				imgs: [
					require('../../static/imgs/1.jpg'),
					require('../../static/imgs/2.jpg'),
					require('../../static/imgs/3.jpg'),
					require('../../static/imgs/4.jpg'),
					require('../../static/imgs/5.jpg'),
				],
				content: '',
				noticeList: [],
				interval: null,
				orderList: [],
			}
		},
		onShow() {
			this.load();
		},
		// onLoad() {// 只加载一次
		// 	this.load();
		// },
		onHide() { // 清理定时器
			clearInterval(this.interval);
			this.interval = null;
		},
		methods: {
			// 公告页面加载 
			load() {
				this.$request.get("/order/selectAll", {
					status: '待接单'
				}).then(res => {
					this.orderList = res.data || [];
				})
				this.$request.get("/notice/selectAll").then(res => {
					this.noticeList = res.data || [];
					let i = 0;
					this.content = this.noticeList.length ? this.noticeList[i].content : "";
					if (this.noticeList.length > 0) {
						this.interval = setInterval(() => {
							i++;
							if (i === this.noticeList.length) {
								i = 0;
							}
							this.content = this.noticeList[i].content;
						}, 2000)
					}
				})
			},
			
			goPlaceOrder(type){
				// 先获取缓存内容，再设置值
				let orderStore = uni.getStorageSync('orderStore') || {}
				orderStore.type = type 
				// 设置缓存
				uni.setStorageSync('orderStore',orderStore)
				uni.navigateTo({
					url:'/pages/placeOrder/placeOrder'
				})
			},
			goOrderDetail(orderId){
				uni.navigateTo({
					url:'/pages/orderDetail/orderDetail?orderId='+orderId
				})
			}
			

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
		grid-gap: 20rpx;
	}
</style>