<template>
	<view style="padding: 20rpx;">
		<view class="box" style="margin-bottom: 10rpx;">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="180rpx" label-align="right"
				validateTrigger="blur">
				<uni-forms-item label="地址" name="address" required>
					<uni-easyinput type="text" v-model="form.address" placeholder="请输入地址" />
				</uni-forms-item>
				<uni-forms-item label="门牌号" name="doorNo" required>
					<uni-easyinput type="text" v-model="form.doorNo" placeholder="请输入门牌号" />
				</uni-forms-item>
				<uni-forms-item label="联系人" name="userName" required>
					<uni-easyinput type="text" v-model="form.userName" placeholder="请输入联系人" />
				</uni-forms-item>
				<uni-forms-item label="联系电话" name="phone" required>
					<uni-easyinput type="text" v-model="form.phone" placeholder="请输入联系电话" />
				</uni-forms-item>
				<view>
					<button type="primary" class="button" @click="save(type)">保存并使用</button>
				</view>
			</uni-forms>
		</view>

		<view class="box">
			<view v-for="item in addressList" :key="item.id">
				<view style="padding: 10rpx 0; border-bottom: 1px solid #eee;" @click="selectAddress(item)">
					<view style="font-weight: bold; font-size: 32rpx; margin-bottom: 10rpx;">
						<!-- 详细地址 -->
						{{ item.address + item.doorNo }}
					</view>
					<view style="color: #888; margin-bottom: 10rpx;">
						<text style="margin-right: 20rpx;">{{ item.userName }}</text>
						<text>{{ item.phone }}</text>
					</view>
					<view style="text-align: right;">
						<!-- native.stop停止事件传播 -->
						<uni-icons type="compose" size="18" color="#888" style="margin-right: 10rpx;"
							@click.native.stop="handleEdit(item)"></uni-icons>
						<uni-icons type="trash" size="18" color="#888" @click.native.stop="del(item.id)"></uni-icons>
					</view>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: uni.getStorageSync('xm-user'),
				form: {},
				rules: {
					address: {
						rules: [{
							required: true,
							errorMessage: '请填写地址',
						}]
					},
					doorNo: {
						rules: [{
							required: true,
							errorMessage: '请填写门牌号',
						}]
					},
					userName: {
						rules: [{
							required: true,
							errorMessage: '请填写联系人',
						}]
					},
					phone: {
						rules: [{
							required: true,
							errorMessage: '请填写联系电话',
						}]
					}
				},
				addressList: [],
				type:'新增',
				addressType:'',

			}
		},
		onLoad(option) {
			this.addressType = option.addressType // 地址类型
			this.load();
		},
		methods: {
			// 点击地址列表后 查看详细地址信息
			selectAddress(address){
				// 先获取缓存数据
				let orderStore = uni.getStorageSync('orderStore') || {}
				if(this.addressType==='取货'){
					orderStore.pickAddress = address
				}else{// 收货地址
					orderStore.recieveAddress = address
				}
				// 将地址信息放到订单信息缓存中
				uni.setStorageSync('orderStore',orderStore)	
				uni.redirectTo({
					url:'/pages/placeOrder/placeOrder'
				})
			},
			
			del(id) {
				this.$request.del('/address/delete/' + id).then(res => {
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
			},
			
			handleEdit(address) {
				// 拷贝当前选中的地址信息 到表单中
				this.form = JSON.parse(JSON.stringify(address))
				this.type = '修改'
			},
			
			save(type) {
				if(type === '新增'){
					this.$refs.formRef.validate().then(res => {
						this.$request.post('/address/add', this.form).then(res => {
							if (res.code === '200') {
								uni.showToast({
									icon: 'success',
									title: '操作成功'
								})
								// 设置地址信息到缓存中
								this.selectAddress(res.data)
								this.form = {}
								this.load()
								// setTimeout(()=>{
								// 	uni.switchTab({
								// 	url:'/pages/index/index'
								// })
								// },500)
							} else {
								uni.showToast({
									icon: 'none',
									title: res.msg,
								})
							}
						})
					}).catch(error => {
						console.error(error)
					})
					
				}else if(type === '修改'){
					// 拷贝当前选中的地址信息 到表单中
					this.$request.put('/address/update', this.form).then(res => {
						if (res.code === '200') {
							uni.showToast({
								icon: 'success',
								title: '操作成功'
							})
							this.form = {}
							this.load()
							// setTimeout(()=>{
							// 	uni.switchTab({
							// 	url:'/pages/index/index'
							// })
							// },500)
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg,
							})
						}
					})
				}
			},
			load() {
				this.$request.get('/address/selectAll', {
					user: this.user.id
				}).then(res => {
					this.addressList = res.data || [];
				})
			},

		}
	}
</script>

<style>

</style>