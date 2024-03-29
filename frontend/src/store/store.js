import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import router from '@/router/index.js'
import {getAuthAxios} from '@/interceptor/axiosInterceptor'

Vue.use(Vuex)

export default new Vuex.Store({
	state : {
		xToken :  localStorage.getItem('x-auth'),
		isLogin : null,
		isLoginError : true,
		mbrInfo : {
			mbrSeq   			: null,
			mbrEmail 			: null,
			mbrGrdCd 			: null,
			mbrId	 			: null,
			mbrMobileNo 		: null,
			mbrNm 				: null,
			mbrTpCd				: null,
			mbrNickNm			: null,
			mbrRpstImgUrl		: null,
			mbrRpstImgNm		: null,
		}
	},
	getters : {
		getMbrInfo : function(state){
			return state.mbrInfo;
		},
		getIsLogin : function(state){
			return state.isLogin;
		},
		getMbrId : function(state){
			return state.mbrInfo.mbrId;
		},
		getMbrSeq : function(state){
			return state.mbrInfo.mbrSeq;
		},
		getMbrImg : function(state){
			if(state.mbrInfo.mbrRpstImgUrl == null ||state.mbrInfo.mbrRpstImgNm == null ){
				return null;
			}else{
				return state.mbrInfo.mbrRpstImgUrl + '/' + state.mbrInfo.mbrRpstImgNm;
			}
		},
	},
	mutations : {
		loginSuccess : (state) => {
			state.isLogin = true;
			state.isLoginError = false;
		},
		loginError : (state) => {
			state.isLogin = false;
			state.isLoginError = true;
			state.mbrInfo = null;
			localStorage.removeItem("x-auth");
			console.log('loginError')
		},
		logout : (state) =>{
			state.isLogin = false;
			state.isLoginError = false;
			state.mbrInfo = null;
			localStorage.removeItem("x-auth");
		},
		setMbrInfo : (state,mbrInfoResult) => {
			state.mbrInfo = mbrInfoResult;
		},
		getPublicMbrInfo : function(state){
			return new Promise(function(resolve, reject) {
				var token = localStorage.getItem('x-auth');
				var mbrInfoResult = {};
				if(!!token){
					var newAxios = getAuthAxios();
					newAxios.get('/api/getMbrInfo')
					.then(resp =>{
						
						mbrInfoResult = resp.data;
						state.mbrInfo = mbrInfoResult;
						state.isLogin = true;
						state.isLoginError = false;
						resolve(state.mbrInfo);
						
					})
					.catch(err => {
						state.isLogin = false;
						state.isLoginError = true;
						state.mbrInfo = null;
						localStorage.removeItem("x-auth");
						console.log('anonymous')
						console.log(err)
					})
				}else{
					state.isLogin = false;
					state.isLoginError = true;
					state.mbrInfo = null;
					localStorage.removeItem("x-auth");
					console.log('anonymous')
				}
			})
		}
	},
	actions : {
		setLoginSuccess : ({commit}) => {
			commit('loginSuccess');
		},
		setLoginError : ({commit}) => {
			commit('loginError');
		},
		login : ({dispatch,commit},param) => {
			axios.post('/api/loginProcesse', param )
			.then(function(resp){
				
				var token = resp.data;
				localStorage.setItem('x-auth', token); //토큰
				
				commit('loginSuccess');
				router.push('/');
			})
			.catch(function(error){
				alert(error.response.data);
				commit('loginError');
			});
			
		},
		logout : ({commit}) => {
			commit('logout');
			router.push('/plain/login');
		}, 
		getMbrInfoLogin : ({commit}) => {
			const token = localStorage.getItem('x-auth');
			if(!!token){
				const newAxios = getAuthAxios();
				newAxios.get('/api/getMbrInfo')
				.then(resp =>{
					const mbrInfoResult = resp.data;
					const mbrInfoDto = {
						mbrEmail 		: mbrInfoResult.mbrEmail,
						mbrGrdCd 		: mbrInfoResult.mbrGrdCd,
						mbrId 			: mbrInfoResult.mbrId,
						mbrMobileNo 	: mbrInfoResult.mbrMobileNo,
						mbrNm 			: mbrInfoResult.mbrNm,
						mbrTpCd 		: mbrInfoResult.mbrTpCd
					}
					commit('setMbrInfo',mbrInfoDto)
					commit('loginSuccess');
				})
				.catch(err => {
					alert('로그인중 시스템 오류가 발생하였습니다.\n 로그인 페이지로 이동합니다.');
					commit('loginError');
					router.push('/plain/login');
				});
			}
			
		},
		getMbrInfo : function({commit}) {
			return new Promise(function(resolve, reject) {
				const token = localStorage.getItem('x-auth');
				let mbrInfoResult = {};
				if(!!token){
					const newAxios = getAuthAxios();
					newAxios.get('/api/getMbrInfo')
					.then(resp =>{
						
						mbrInfoResult = resp.data;
						resolve(mbrInfoResult);
					})
					.catch(err => {
						alert('로그인중 시스템 오류가 발생하였습니다\n로그인 페이지로 이동합니다.');
						commit('loginError');
						router.push('/plain/login');
					})
				}else{
					alert('로그인이 필요한 화면입니다.\n로그인 페이지로 이동합니다.');
					commit('loginError');
					router.push('/plain/login');
				}
			})
		},
		getValidAuth : function(commit) {
			return new Promise(function(resolve,reject){
				const xAxios = getAuthAxios();
				xAxios.get('/api/validAuth')
				.then(function(resp){
					resolve(resp);
				  })
				  .catch(function(err){
					  reject(err);
				  });
			})
		},
		getCommonCd :   function({commit},upperCd) {
			return new Promise(function(resolve,reject){
				axios.get('/api/common/getCommonCd?upperCd='+upperCd)
				.then(function(resp){
					  resolve(resp.data);
				  })
				  .catch(function(err){
					  reject(err);
				  })
			});
		},
		PageGetter : function({commit},payload) {
			const xAxios = getAuthAxios();
			return new Promise(function(resolve,reject){
				xAxios.get(payload.url,
					{params : {
						page : payload.page,
						size : payload.size,
						params : payload.param
					}}
				)
				.then(function(resp){
					  resolve(resp.data);
				  })
				  .catch(function(err){
					  reject(err);
				  })
			});
		}
		
	}
	
});
