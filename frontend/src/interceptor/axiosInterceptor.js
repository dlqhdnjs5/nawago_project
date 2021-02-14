import store from '@/store/store'
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

//요청을 보낼시 로그인했는지를 확인하고 안햇으면 로그인 화면으로 이동시키는 인터셉터
export function setInterceptorsCheckAuth(instance) {
	instance.interceptors.request.use(
			
		async function(config) {
			// Do something before request is sent
			config.headers['x-auth'] = localStorage.getItem('x-auth')
			var param = {};
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
			// Do something with request error
			return Promise.reject(error);
		},
	);

	// Add a response interceptor
	instance.interceptors.response.use(
		function(response) {
			// Any status code that lie within the range of 2xx cause this function to trigger
			// Do something with response data
			return response;
		},
		function(error) {
			// Any status codes that falls outside the range of 2xx cause this function to trigger
			// Do something with response error
			return Promise.reject(error);
		},
	);
	
	return instance;
}


export function setInterceptors(instance) {
	instance.interceptors.request.use(
			
		function(config) {
			// Do something before request is sent
			config.headers['x-auth'] = localStorage.getItem('x-auth')
			
			return config;	
		},
		function(error) {
			// Do something with request error
			return Promise.reject(error);
		},
	);

	// Add a response interceptor
	instance.interceptors.response.use(
		function(response) {
			// Any status code that lie within the range of 2xx cause this function to trigger
			// Do something with response data
			return response;
		},
		function(error) {
			// Any status codes that falls outside the range of 2xx cause this function to trigger
			// Do something with response error
			return Promise.reject(error);
		},
	);
	
	return instance;
}

