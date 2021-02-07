import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store/store'
import {getAuthAxios} from '@/interceptor/axiosInterceptor'

Vue.use(VueRouter)

function getAuthInfo(callbackFunc) {
	
	var that = this;
	return new Promise(function(resolve,reject){
		var xAxios = getAuthAxios();
		xAxios.get('/api/validAuth')
		.then(function(resp){
			resolve(resp);
		})
		.catch(function(err){
			reject(err);
		});
	})
}

const rejectAuth = (to,from,next) => {
	
	getAuthInfo()
	.then(function(resp){
		console.log(resp)
		alert('이미 로그인을 하였습니다.');
		next("/");
	})
	.catch(function(err){
		console.log(err)
		next();
	})
}

const onlyAuth = (to,from,next) => {
	
	getAuthInfo()
	.then(function(resp){
		next();
	})
	.catch(function(err){
		console.log(err)
		alert('로그인을 해주세요.');
		//next('/loginPage');
		next('/plain/login');
	})
	
}


const PlainLogin = () => import('../components/display/PlainLogin.vue')
const Home = () => import('../components/display/Home.vue')
const ShowOffList = () => import('../components/showOff/ShowOffList.vue')
const routes = [
	{
		path:'/plain',
		component : require('../components/layout/PlainLayout').default,
		children : [
			{
				path : 'Login',
				name : 'plainLogin',
				beforeEnter : rejectAuth,
				component : PlainLogin
			},
			{
				path: '/RegisterPage',
				name : 'RegisterPage',
				beforeEnter : rejectAuth,
				component : () => import('../components/account/RegisterPage.vue')
			},
			{
				path: '/PetRegisterPage',
				name : 'PetRegisterPage',
				beforeEnter : onlyAuth,
				component : () => import('../components/account/PetRegisterPage.vue')
			}
		]
	},
	{
		path: '',
		component : require('../components/layout/DefaultLayout').default,
		children : [
			{
				 path : '/',
				 component : () => import('../components/showOff/ShowOffList.vue')
			},
			{
				 path : '/showOffList',
				 name : 'showOffList',
				 component : () => import('../components/showOff/ShowOffList.vue')
			},
			{
				 path : '/loginPage',
				 name : 'loginPage',
				 beforeEnter : rejectAuth,
				 component : () => import('../components/account/LoginPage.vue')
				
			},
			{
				path : '/myPage/:userId',
				name : 'myPage',
				beforeEnter : onlyAuth,
				component : () => import('../components/myPage/myPage.vue')
			},
			{
				path: '/example',
				name : 'Example',
				component : () => import('../components/display/example.vue')
			},
			{
				path: '/addShowOff',
				name : 'AddShowOff',
				component : () => import('../components/showOff/AddShowOff.vue')
			},
			{
				path: '/updateMember',
				name : 'updateMember',
				beforeEnter : onlyAuth,
				component : () => import('../components/myPage/UpdateMember.vue')
			},
		]
	},
	{
		path : '*',
		redirect : '/404'
	},
	{
		path : '*',
		redirect : '/401'
	},
	{
		path: '/404',
		name: 'ErrorPage404',
		component: () => import('../components/error/ErrorPage404.vue')
	},
	{
		path: '/401',
		name: 'ErrorPage404',
		component: () => import('../components/error/ErrorPage404.vue')
	}
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
