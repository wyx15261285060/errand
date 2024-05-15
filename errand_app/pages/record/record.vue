<template>
	<view style="padding: 20rpx;">
		<view class="box" v-for="item in recordList" :key="item.id" style="margin-bottom: 10rpx;">
			<view style="display: flex; margin-bottom: 10rpx;">
				<view>
					<uni-tag type="success" style="margin-right: 20rpx;" text="充值" size="mini" v-if="item.type==='充值'"></uni-tag>
					<uni-tag type="primary" style="margin-right: 20rpx;" text="收入" size="mini" v-if="item.type==='收入'"></uni-tag>
					<uni-tag type="error"  style="margin-right: 20rpx;" text="支出" size="mini" v-if="item.type==='支出'"></uni-tag>
					<uni-tag type="warning" style="margin-right: 20rpx;" text="取消" size="mini" v-if="item.type==='取消'"></uni-tag>
					<uni-tag type="warning" style="margin-right: 20rpx;" text="超时" size="mini" v-if="item.type==='超时'"></uni-tag>
					<text style="margin-bottom: 10rpx; font-size: 30rpx; font-weight: bold;"> {{ item.content }}</text>
				</view>

				<view style="flex: 1;text-align: right;font-weight: bold;">
					<text style="color: red;font-size: 40rpx;" v-if="item.type==='充值'">+{{ item.money }}</text>
					<text style="color: dodgerblue;font-size: 40rpx;" v-if="item.type==='收入'">+{{ item.money }}</text>
					<text style="color: #888;font-size: 40rpx;" v-if="item.type==='支出'">-{{ item.money }}</text>
					<text style="color: orange;font-size: 40rpx;" v-if="item.type==='取消'">+{{ item.money }}</text>
					<text style="color: orange;font-size: 40rpx;" v-if="item.type==='超时'">+{{ item.money }}</text>
					
				</view>
			</view>
			<view style="text-align: right;color: #888;font-size: 24rpx;">{{ item.time }}</view>
		</view>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				recordList: [],
				user: uni.getStorageSync('xm-user'),

			}
		},
		onLoad() {
			this.load()
		},
		methods: {
			load() {
				this.$request.get('/record/selectAll', {
					userId: this.user.id
				}).then(res => {
					this.recordList = res.data || []
				})

			},

		}
	}
</script>

<style>

</style>