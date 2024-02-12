import axios from 'axios'
import router from '@/router/index.js'

export function getAuthAxios() {
	const instance = axios.create();
	return setInterceptors(instance);
}

export function getAuthCheckedAxios() {
	const instance = axios.create();
	return setInterceptorsCheckAuth(instance);
}

export function setInterceptorsCheckAuth(instance) {
	instance.interceptors.request.use(
			
		async function(config) {
			config.headers['x-auth'] = localStorage.getItem('x-auth')
			await axios.get('/api/validAuth',config)
		    .then( response => {
		    	console.log('valid auth');
		    	return config;	
	          })
	          .catch(function () {
	            console.log('auth error!!');
	            alert('로그인 후 이용해주시기 바랍니다.');
	            router.push({
	   				name : 'plainLogin'
	   			})
	            return Promise.reject('401 by interceptor')
	          });
			 return config
			
		},
		function(error) {
			return Promise.reject(error);
		},
	);

	instance.interceptors.response.use(
		function(response) {
			return response;
		},
		function(error) {
			return Promise.reject(error);
		},
	);
	
	return instance;
}


export function setInterceptors(instance) {
	instance.interceptors.request.use(
			
		function(config) {
			config.headers['x-auth'] = localStorage.getItem('x-auth')
			
			return config;	
		},
		function(error) {
			return Promise.reject(error);
		},
	);

	instance.interceptors.response.use(
		function(response) {
			return response;
		},
		function(error) {
			return Promise.reject(error);
		},
	);
	
	return instance;
}

