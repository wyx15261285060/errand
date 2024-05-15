<template>
	<view style="padding: 20rpx;">
		<view style="margin-bottom: 10rpx;">
			<!-- current当前选中的菜单(索引)， styleType分段器样式-->
			<uni-segmented-control :current="current" :values="items" @clickItem="onClickItem" styleType="text"
				activeColor="#006eff"></uni-segmented-control>
		</view>
		<view>
			<view v-for="item in orderList" :key="item.id" class="box" style="margin-bottom: 10rpx;"
				@click="goOrderDetail(item.id)">
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
						<!-- 待接单、待送达、待收货三种状态是需要显示下单时间，其余状态不需要显示 -->
						<text style="margin-right: 20rpx;"
							v-if="item.status === '待接单' || item.status === '待送达' || item.status === '待收货'">已下单{{ item.minutes }}分钟</text>
						<text style="color: #888;" v-if="item.status === '已取消'">{{ item.status }}</text>
						<text style="color: orange;" v-if="item.status === '待接单'">{{ item.status }}</text>
						<text style="color: dodgerblue" v-if="item.status === '待送达'">{{ item.status }}</text>
						<text style="color: mediumpurple;" v-if="item.status === '待收货'">{{ item.status }}</text>
						<text style="color: indianred;" v-if="item.status === '待评价'">{{ item.status }}</text>
						<text style="color: #18bc37;" v-if="item.status === '已完成'">{{ item.status }}</text>
						<text style="color: #ff007f;" v-if="item.status === '已超时'">{{ item.status }}</text>
					</view>
					<view style="flex: 1; text-align: right;">
						<!-- <uni-tag text="接单" type="primary" size="mini" v-if="item.status==='待接单'"></uni-tag> -->
						<view style="display: inline-block;" v-if="item.status==='已取消'||item.status==='已完成'||item.status==='已超时'">
							<uni-icons type="trash" size="16" color="#888"
								style="position: relative;top: 4rpx;"></uni-icons>
							<text style="color: #888;" @click.native.stop="handleDel(item.id)">删除</text>
						</view>

						<uni-tag text="确认收货" type="primary" size="mini" v-if="item.status==='待收货'"
							@click.native.stop="changeStatus(item,'待评价')"></uni-tag>
							<uni-tag text="取消订单" type="default" size="small" v-if="item.status === '待接单'"
							@click.native.stop="changeStatus(item, '已取消')"></uni-tag>
							<uni-tag text="待评价" type="success" size="small" v-if="item.status === '待评价'"
							@click.native.stop="goComment(item.id)"></uni-tag>
					</view>

				</view>
			</view>
		</view>
		<view>
			<!-- 二次确认弹窗 -->
			<uni-popup ref="alertDialog" type="dialog">
				<uni-popup-dialog :type="msgType" cancelText="取消" confirmText="确认" title="删除确认" content="您确认删除订单吗？"
					@confirm="del"></uni-popup-dialog>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				items: ['全部订单', '待接单', '待送达', '待收货', '待评价'],
				orderList: [],
				current: '全部订单',
				user: uni.getStorageSync('xm-user'),
				orderId: 0

			}
		},
		onShow() {
			this.load()
		},
		methods: {
			// 跳转至评价页面
			goComment(orderId){
				uni.navigateTo({
					url:'/pages/comment/comment?orderId='+orderId
				})
			},
			// 查看订单详情
			goOrderDetail(orderId) {
				uni.navigateTo({
					url: '/pages/orderDetail/orderDetail?orderId=' + orderId
				})
			},
			// e是一个点击事件对象
			onClickItem(e) {
				this.current = this.items[e.currentIndex]
				this.load()
			},
			// 确认送达，将状态修改为待评价 
			changeStatus(order, status) {
				order.status = status
				this.$request.put('/order/update', order).then(res => {
					if (res.code === '200') {
						uni.showToast({
							icon: 'success',
							title: '操作成功'
						})
						this.load()
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg,
						})
					}

				})
			},
			load() {
				//如果不是‘全部订单’就不把status参数拼接到paramsJSON对象中
				let params = {
					userId: this.user.id,
				}
				let status = this.current
				if (this.current !== '全部订单') {
					params.status = this.current
				}
				this.$request.get('/order/selectAll', params).then(res => {
					this.orderList = res.data || []
				})
			},
			// 假删除，打开弹窗
			handleDel(orderId) {
				this.orderId = orderId
				this.$refs.alertDialog.open()
				del()
			},
			// 确定时间
			del() {
				this.$request.del('/order/delete/' + this.orderId).then(res => {
					if (res.code === '200') {
						uni.showToast({
							icon: 'success',
							title: '操作成功'
						})
						this.load()
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
				})
			}


		}
	}
</script>

<style>

</style>