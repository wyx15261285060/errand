<template>
	<view style="padding: 20rpx;">
		<view class="box "style="margin-bottom: 10rpx;">
			<uni-section title="取货地址" type="line"></uni-section>
			<view style="color=#888;">请选择取货地址</view>
		</view>
		<view class="box "style="margin-bottom: 10rpx;">
			<uni-section title="收货地址" type="line"></uni-section>
			<view style="color=#888;">请选择收货地址</view>
		</view>
		<view class="box">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="180rpx" label-align="left"
				validateTrigger="blur">
				<uni-forms-item label="物品名称"  name="name"  required>
					<uni-easyinput type="text" v-model="form.name" placeholder="请输入物品名称" />
				</uni-forms-item>
				<uni-forms-item label="描述信息" name="descr">
					<uni-easyinput type="text" v-model="form.descr" placeholder="请输入描述信息" />
				</uni-forms-item>
				<uni-forms-item label="重量(kg)" name="weight">
					<uni-easyinput type="text" v-model="form.weight" placeholder="请输入物品重量" />
				</uni-forms-item>
				<!-- 计数器组件 -->
				<uni-forms-item label="费用(元)" name="price" required>
					<uni-number-box v-model="form.price" :min="1"
						style="position: relative; top: 10rpx;"></uni-number-box>
				</uni-forms-item>
				<uni-forms-item label="订单类型" name="type" required>
					<uni-data-select v-model="form.type" :localdata="orderTypes"></uni-data-select>
				</uni-forms-item>
				<!-- 小程序直接使用该组件便可实现文件上传 -->
				<uni-forms-item label="物品图片" name="img">
					<uni-file-picker limit="1" :image-styles="imageStyles" :del-icon="false" :disable-preview="true"
						fileMediatype="image" v-model="imgs" @select="handleImgUploadSuccess"></uni-file-picker>
				</uni-forms-item>
				<uni-forms-item label="备注" name="comment">
					<uni-easyinput type="text" v-model="form.comment" placeholder="请输入备注信息" />
				</uni-forms-item>
			</uni-forms>
		</view>
		<view><button class="button" type="primary" @click="addOrder">提交订单</button></view>



	</view>
</template>

<script>
	export default {
		data() {
			return {
				// 表单对象
				form:{price:1,type:''},
				rules: {
					name:{
						rules:[{
							required:true,
							errorMessage:'请填写名称',
						}]
					},
					price:{
						rules:[{
							required:true,
							errorMessage:'请给出跑腿费用',
						}]
					},
					type:{
						rules:[{
							required:true,
							errorMessage:'请选择类型',
						}]
					}
				},
				imgs: [],
				imageStyles: {
					"height": 80, // 边框高度
					"width": 80, // 边框宽度
					"border": { // 如果为 Boolean 值，可以控制边框显示与否
						"color": "#ffffff", // 边框颜色
						"width": "1px", // 边框宽度
					}
				},
				orderTypes: [{
						value: '代取快递',
						text: "代拿快递"
					},
					{
						value: '代购餐品',
						text: "代取餐品"
					},
					{
						value: '代购零食',
						text: "代买零食"
					},
					{
						value: '代购鲜花',
						text: "代送鲜花"
					},
				]
			}


		},
		onShow() {
			let orderTypeStore = uni.getStorageSync('orderStore')
			this.form.type = orderTypeStore?.type
		},
		methods: {
			
			addOrder() {
				this.$refs.formRef.validate().then(res => {
					this.$request.post('/order/addOrder',this.form).then(res => {
						if(res.code==='200'){
							uni.showToast({
								icon:'success',
								title:'下单成功'
							})
				 			setTimeout(()=>{
								uni.switchTab({
								url:'/pages/index/index'
							})
							},500)
							
						}else{
							uni.showToast({
								icon:'none',
								title:res.msg,
							})
						}
					})

				}).catch(err => {

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
						_this.form.img = url // 给表单图像属性赋值
					}
				})
			}



		}
	}
</script>

<style>

</style>